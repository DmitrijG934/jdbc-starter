package nn.dgordeev.jdbc.starter.dao;

import nn.dgordeev.jdbc.starter.dto.TicketFilter;
import nn.dgordeev.jdbc.starter.entity.Flight;
import nn.dgordeev.jdbc.starter.entity.Ticket;
import nn.dgordeev.jdbc.starter.exception.DaoException;
import nn.dgordeev.jdbc.starter.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class TicketDao {

    private volatile static TicketDao INSTANCE;

    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM ticket WHERE id = ?
            """;
    private static final String INSERT_SQL = """
            INSERT INTO ticket
            (passenger_no, passenger_name, flight_id, seat_no, cost)
            VALUES
            (?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_BY_ID_SQL = """
            UPDATE ticket
            SET passenger_no = ?,
                passenger_name = ?,
                flight_id = ?,
                seat_no = ?,
                cost = ?
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT
                t.id, t.passenger_no, t.passenger_name, t.flight_id, t.seat_no, t.cost,
                f.id as fid, f.flight_no, f.departure_date, f.departure_airport_code, f.arrival_date,
                f.arrival_airport_code, f.aircraft_id, f.status
            FROM ticket t
            LEFT JOIN flight f ON t.flight_id = f.id
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + " WHERE ticket.id = ?";

    private TicketDao() {
    }

    public Ticket save(Ticket ticket) {
        try (
                var connection = ConnectionManager.get();
                var saveStatement =
                        connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            saveStatement.setString(1, ticket.getPassengerNo());
            saveStatement.setString(2, ticket.getPassengerName());
            saveStatement.setLong(3, ticket.getFlight().id());
            saveStatement.setString(4, ticket.getSeatNo());
            saveStatement.setBigDecimal(5, ticket.getCost());
            saveStatement.executeUpdate();

            var generatedKeys = saveStatement.getGeneratedKeys();
            generatedKeys.next();

            ticket.setId(generatedKeys.getLong("id"));

            return ticket;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Ticket update(Ticket ticket) {
        if (ticket.getId() == null) {
            throw new DaoException("Ticked id should not be empty");
        }
        try (
                var connection = ConnectionManager.get();
                var updateStatement = connection.prepareStatement(UPDATE_BY_ID_SQL)
        ) {
            updateStatement.setString(1, ticket.getPassengerNo());
            updateStatement.setString(2, ticket.getPassengerName());
            updateStatement.setLong(3, ticket.getFlight().id());
            updateStatement.setString(4, ticket.getSeatNo());
            updateStatement.setBigDecimal(5, ticket.getCost());
            updateStatement.setLong(6, ticket.getId());

            updateStatement.executeUpdate();
            return ticket;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<Ticket> findById(Long id) {
        return ofNullable(findByIdRequired(id));
    }

    public Collection<Ticket> findAll(TicketFilter filter) {
        var queryParams = filter.getQueryParameters();
        var sql = FIND_ALL_SQL + queryParams.sql();
        if (filter.distinctFilter()) {
            sql = sql.replace("SELECT", "SELECT DISTINCT");
        }
        try (
                var connection = ConnectionManager.get();
                var findStatement = connection.prepareStatement(sql)
        ) {
            var prepareParams = queryParams.prepareParams();
            for (int i = 0; i < prepareParams.size(); i++) {
                findStatement.setObject(i + 1, prepareParams.get(i));
            }
            System.out.println(findStatement);
            var result = findStatement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (result.next()) {
                tickets.add(buildTicket(result));
            }
            return tickets;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private Ticket buildTicket(ResultSet result) throws SQLException {
        return Ticket.builder()
                .id(result.getLong("id"))
                .passengerName(result.getString("passenger_name"))
                .passengerNo(result.getString("passenger_no"))
                .seatNo(result.getString("seat_no"))
                .flight(buildFlight(result))
                .cost(result.getBigDecimal("cost"))
                .build();
    }

    private Flight buildFlight(ResultSet result) throws SQLException {
        return new Flight(
                result.getLong("fid"),
                result.getString("flight_no"),
                result.getTimestamp("departure_date").toLocalDateTime(),
                result.getString("departure_airport_code"),
                result.getTimestamp("arrival_date").toLocalDateTime(),
                result.getString("arrival_airport_code"),
                result.getInt("aircraft_id"),
                result.getString("status")
        );
    }

    public Ticket findByIdRequired(Long id) {
        try (
                var connection = ConnectionManager.get();
                var findStatement = connection.prepareStatement(FIND_BY_ID_SQL)
        ) {
            findStatement.setLong(1, id);
            var result = findStatement.executeQuery();
            if (result.next()) {
                return buildTicket(result);
            } else {
                throw new DaoException("Unable to find ticket with id " + id);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(Long id) {
        try (
                var connection = ConnectionManager.get();
                var deleteStatement = connection.prepareStatement(DELETE_BY_ID_SQL)
        ) {
            deleteStatement.setLong(1, id);
            return deleteStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public static TicketDao getInstance() {
        if (INSTANCE == null) {
            synchronized (TicketDao.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TicketDao();
                }
            }
        }
        return INSTANCE;
    }
}
