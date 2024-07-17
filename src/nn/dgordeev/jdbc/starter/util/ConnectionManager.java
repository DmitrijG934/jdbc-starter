package nn.dgordeev.jdbc.starter.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static nn.dgordeev.jdbc.starter.util.ConnectionManager.PropertyKey.DB_URL;
import static nn.dgordeev.jdbc.starter.util.ConnectionManager.PropertyKey.PASSWORD;
import static nn.dgordeev.jdbc.starter.util.ConnectionManager.PropertyKey.USERNAME;

public final class ConnectionManager {

    static {
        loadDriver();
    }

    private ConnectionManager() {
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(DB_URL),
                    PropertiesUtil.get(USERNAME),
                    PropertiesUtil.get(PASSWORD)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Applicable for older Java versions (lower than Java 8).
    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public enum PropertyKey {

        DB_URL("db.url"),
        USERNAME("db.username"),
        PASSWORD("db.password");

        private final String value;

        PropertyKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}