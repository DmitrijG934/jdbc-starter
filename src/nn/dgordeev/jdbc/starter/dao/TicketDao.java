package nn.dgordeev.jdbc.starter.dao;

import nn.dgordeev.jdbc.starter.entity.Ticket;
import nn.dgordeev.jdbc.starter.exception.DaoException;
import nn.dgordeev.jdbc.starter.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
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
    private static final String FIND_BY_ID_SQL = """
            SELECT
                id, passenger_no, passenger_name, flight_id, seat_no, cost
            FROM ticket
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT
                id, passenger_no, passenger_name, flight_id, seat_no, cost
            FROM ticket
            LIMIT ? OFFSET ?
            """;

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
            saveStatement.setLong(3, ticket.getFlightId());
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
            updateStatement.setLong(3, ticket.getFlightId());
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

    public Collection<Ticket> findAll() {
        return findAll(10, 0);
    }

    public Collection<Ticket> findAll(long limit, long offset) {
        try (
                var connection = ConnectionManager.get();
                var findStatement = connection.prepareStatement(FIND_ALL_SQL)
        ) {
            findStatement.setLong(1, limit);
            findStatement.setLong(2, offset);
            var result = findStatement.executeQuery();
            var tickets = new ArrayList<Ticket>();
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
                .flightId(result.getObject("flight_id", Long.class))
                .cost(result.getBigDecimal("cost"))
                .build();
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
