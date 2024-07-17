package nn.dgordeev.jdbc.starter;

import nn.dgordeev.jdbc.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class TransactionRunner {

    public static void main(String[] args) throws SQLException {

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
