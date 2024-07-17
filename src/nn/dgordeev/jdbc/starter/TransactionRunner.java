package nn.dgordeev.jdbc.starter;

import nn.dgordeev.jdbc.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionRunner {

    public static void main(String[] args) throws SQLException {
        long identifier = 3;
        var deleteFromTest2 = "DELETE FROM test2 WHERE test1_id = ?";
        var deleteFromTest1 = "DELETE FROM test1 WHERE test_id = ?";

        Connection connection = null;
        PreparedStatement deleteTest2Statement = null;
        PreparedStatement deleteTestStatement = null;

        try {

            connection = ConnectionManager.open();
            deleteTest2Statement = connection.prepareStatement(deleteFromTest2);
            deleteTestStatement = connection.prepareStatement(deleteFromTest1);
            connection.setAutoCommit(false);

            connection.setSchema("jdbc_starter");
            deleteTest2Statement.setLong(1, identifier);
            deleteTestStatement.setLong(1, identifier);
            deleteTest2Statement.executeUpdate();
      /*      if (true) {
                throw new RuntimeException("Boom!");
            }*/
            deleteTestStatement.executeUpdate();
            connection.commit();

        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (deleteTestStatement != null) {
                deleteTestStatement.close();
            }
            if (deleteTest2Statement != null) {
                deleteTest2Statement.close();
            }
        }
    }
}
