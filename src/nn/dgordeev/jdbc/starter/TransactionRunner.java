package nn.dgordeev.jdbc.starter;

import nn.dgordeev.jdbc.starter.util.ConnectionManager;
import nn.dgordeev.jdbc.starter.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class TransactionRunner {

    public static void main(String[] args) {
        // Won't work with current "postgres" database, only with "demo" database
        var jdbcUrl = PropertiesUtil.get(ConnectionManager.PropertyKey.DB_URL);
        if (jdbcUrl.contains("demo")) {
            try {
                executeExample();
                ConnectionManager.closeConnectionPool();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void executeExample() throws SQLException {
        long identifier = 2;
        var deleteFromTest2 = "DELETE FROM test2 WHERE test1_id = " + identifier;
        var deleteFromTest1 = "DELETE FROM test1 WHERE test_id = " + identifier;

        Connection connection = null;
        Statement statement = null;

        try {

            connection = ConnectionManager.get();
            statement = connection.createStatement();

            connection.setAutoCommit(false);
            connection.setSchema("jdbc_starter");

            statement.addBatch(deleteFromTest2);
            statement.addBatch(deleteFromTest1);

            var executeBatchResult = statement.executeBatch();
            System.out.println(Arrays.toString(executeBatchResult));

            connection.commit();

        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }
}
