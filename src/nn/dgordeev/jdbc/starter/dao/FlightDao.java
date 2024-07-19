package nn.dgordeev.jdbc.starter.dao;

import nn.dgordeev.jdbc.starter.entity.Flight;
import nn.dgordeev.jdbc.starter.exception.DaoException;
import nn.dgordeev.jdbc.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class FlightDao implements Dao<Long, Flight> {

    private static volatile FlightDao INSTANCE;

    private static final String FIND_ALL_SQL = """
            SELECT
                id, flight_no, departure_date, departure_airport_code, arrival_date,
                arrival_airport_code, aircraft_id, status
            FROM flight
            """;

    private FlightDao() {
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    public Flight findByIdRequired(Long id, Connection connection) {
        var sql = FIND_ALL_SQL + " WHERE id = ?";
        try (
                var findByIdStatement = connection.prepareStatement(sql)
        ) {
            findByIdStatement.setObject(1, id);
            var result = findByIdStatement.executeQuery();
            if (result.next()) {
                return buildFlight(result);
            }
            throw new DaoException("Unable to find flight by id " + id);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Flight findByIdRequired(Long id) {
        try (var connection = ConnectionManager.get()) {
            return findByIdRequired(id, connection);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Collection<Flight> findAll() {
        return List.of();
    }

    @Override
    public Optional<Flight> findById(Long id) {
        return ofNullable(findByIdRequired(id));
    }

    public Optional<Flight> findById(Long id, Connection connection) {
        return ofNullable(findByIdRequired(id, connection));
    }

    @Override
    public Flight update(Flight ticket) {
        return null;
    }

    @Override
    public Flight save(Flight ticket) {
        return null;
    }

    public static FlightDao getInstance() {
        if (INSTANCE == null) {
            synchronized (FlightDao.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FlightDao();
                }
            }
        }
        return INSTANCE;
    }

    private Flight buildFlight(ResultSet result) throws SQLException {
        return new Flight(
                result.getLong("id"),
                result.getString("flight_no"),
                result.getTimestamp("departure_date").toLocalDateTime(),
                result.getString("departure_airport_code"),
                result.getTimestamp("arrival_date").toLocalDateTime(),
                result.getString("arrival_airport_code"),
                result.getInt("aircraft_id"),
                result.getString("status")
        );
    }
}
