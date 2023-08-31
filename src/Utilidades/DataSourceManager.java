package utilidades;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceManager {

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        Properties prop = ConfigLoader.getProperties(); // Obtén las propiedades cargadas

        config.setJdbcUrl(prop.getProperty("jdbcUrl"));
        config.setUsername(prop.getProperty("username"));
        config.setPassword(prop.getProperty("password"));

        config.setMaximumPoolSize(Integer.parseInt(prop.getProperty("poolSize")));
        config.setAutoCommit(true);
        config.setConnectionTimeout(Long.parseLong(prop.getProperty("connectionTimeout")));

        ds = new HikariDataSource(config);
    }

    private DataSourceManager() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void closePool() {
        ds.close();
    }
}
