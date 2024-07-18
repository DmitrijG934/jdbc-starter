package nn.dgordeev.jdbc.starter.dao;

import nn.dgordeev.jdbc.starter.entity.Ticket;
import nn.dgordeev.jdbc.starter.exception.DaoException;
import nn.dgordeev.jdbc.starter.util.ConnectionManager;

import java.sql.SQLException;
import java.sql.Statement;

public class TicketDao {

    private volatile static TicketDao INSTANCE;

    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM ticket WHERE id = ?
            """;
    private static final String INSERT_SQL = """
            INSERT INTO ticket (passenger_no, passenger_name, flight_id, seat_no, cost) VALUES
            (?, ?, ?, ?, ?)
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
                    return new TicketDao();
                }
            }
        }
        return INSTANCE;
    }
}
