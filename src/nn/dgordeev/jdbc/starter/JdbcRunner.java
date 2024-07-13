package nn.dgordeev.jdbc.starter;

import nn.dgordeev.jdbc.starter.util.ConnectionManager;

import java.sql.SQLException;

public class JdbcRunner {

    public static void main(String[] args) {
        try (var connection = ConnectionManager.open()) {
            System.out.println(connection.getTransactionIsolation());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
