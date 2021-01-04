package me.kench.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.kench.RotMC;
import me.kench.config.MySQLConfig;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class DataManager implements AutoCloseable {
    private final HikariDataSource hikari;
    private final DataAccessor accessor;

    public DataManager(RotMC plugin) {
        MySQLConfig config = new MySQLConfig();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setUsername(config.username);
        hikariConfig.setPassword(config.password);
        hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s", config.hostname, config.port, config.database));

        hikari = new HikariDataSource(hikariConfig);
        Jdbi jdbi = Jdbi.create(hikari);
        jdbi.installPlugin(new SqlObjectPlugin());

        accessor = new DataAccessor(jdbi);
    }

    public DataAccessor getAccessor() {
        return accessor;
    }

    @Override
    public void close() throws Exception {
        if (hikari.isRunning()) {
            hikari.close();
        }
    }
}
