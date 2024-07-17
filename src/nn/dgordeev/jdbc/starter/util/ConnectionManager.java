package nn.dgordeev.jdbc.starter.util;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.util.Optional.ofNullable;
import static nn.dgordeev.jdbc.starter.util.ConnectionManager.PropertyKey.DB_URL;
import static nn.dgordeev.jdbc.starter.util.ConnectionManager.PropertyKey.PASSWORD;
import static nn.dgordeev.jdbc.starter.util.ConnectionManager.PropertyKey.POOL_SIZE;
import static nn.dgordeev.jdbc.starter.util.ConnectionManager.PropertyKey.USERNAME;

public final class ConnectionManager {

    private static final Integer DEFAULT_POOL_SIZE = 10;
    private static BlockingQueue<ConnectionPoolObject> pool;

    static {
        loadDriver();
        initializeConnectionPool();
    }

    private ConnectionManager() {
    }

    private static void initializeConnectionPool() {

        int poolSize = ofNullable(PropertiesUtil.get(POOL_SIZE))
                .map(Integer::parseInt)
                .orElse(DEFAULT_POOL_SIZE);

        pool = new ArrayBlockingQueue<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            var connection = open();
            var proxyConnection = (Connection) Proxy.newProxyInstance(
                    ConnectionManager.class.getClassLoader(),
                    new Class[]{Connection.class},
                    (proxy, method, args) -> "close".equals(method.getName()) ?
                            pool.add(new ConnectionPoolObject(connection, (Connection) proxy)) :
                            method.invoke(connection, args));
            pool.add(new ConnectionPoolObject(connection, proxyConnection));
        }
    }

    public static void closeConnectionPool() throws SQLException {
        for (ConnectionPoolObject connectionPoolObject : pool) {
            var sourceConnection = connectionPoolObject.sourceConnection();
            sourceConnection.close();
        }
    }

    public static Connection get() {
        try {
            return pool.take().proxyConnection();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection open() {
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
        PASSWORD("db.password"),
        FETCH_SIZE("db.fetch.size"),
        POOL_SIZE("db.pool.size");

        private final String value;

        PropertyKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private record ConnectionPoolObject(Connection sourceConnection, Connection proxyConnection) {

    }
}