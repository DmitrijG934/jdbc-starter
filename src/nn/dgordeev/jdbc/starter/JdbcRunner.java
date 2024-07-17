package nn.dgordeev.jdbc.starter;

import nn.dgordeev.jdbc.starter.util.ConnectionManager;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public class JdbcRunner {

    public static void main(String[] args) throws SQLException {
        checkMetaData();
    }

    private static void checkMetaData() throws SQLException {
        try (var connection = ConnectionManager.open()) {
            var metaData = connection.getMetaData();
            var catalogs = metaData.getCatalogs();
            while (catalogs.next()) {
                var catalog = catalogs.getString(1);
                var schemas = metaData.getSchemas();
                while (schemas.next()) {
                    var schema = schemas.getString("TABLE_SCHEM");
                    var tables = metaData.getTables(catalog, schema, "%", new String[]{"TABLE"});
                    if (schema.equals("bookings")) {
                        while (tables.next()) {
                            System.out.println(tables.getString("TABLE_NAME"));
                        }
                    }
                }
            }
        }
    }

    private static Collection<String> getAircraftCodesByRange(int startRange, int endRange) {
        var sql = """
                SELECT DISTINCT aircraft_code
                FROM aircrafts_data
                WHERE range BETWEEN ? AND ?
                """;
        try (
                var connection = ConnectionManager.open();
                var preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(3);
            var aircraftCodes = new HashSet<String>();
            preparedStatement.setInt(1, startRange);
            preparedStatement.setInt(2, endRange);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                aircraftCodes.add(resultSet.getString("aircraft_code"));
            }
            return aircraftCodes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
