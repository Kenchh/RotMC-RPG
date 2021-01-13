package me.kench.rotmc.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.config.MySqlConfig;
import me.kench.rotmc.database.playerdata.PlayerDataDam;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class DataManager implements AutoCloseable {
    private final HikariDataSource hikari;
    private final PlayerDataDam playerData;

    public DataManager(RotMcPlugin plugin) {
        MySqlConfig config = new MySqlConfig();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setUsername(config.username);
        hikariConfig.setPassword(config.password);
        hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s", config.hostname, config.port, config.database));

        hikari = new HikariDataSource(hikariConfig);
        Jdbi jdbi = Jdbi.create(hikari);
        jdbi.installPlugin(new SqlObjectPlugin());

        playerData = new PlayerDataDam(jdbi);
    }

    public PlayerDataDam getPlayerData() {
        return playerData;
    }

    @Override
    public void close() throws Exception {
        if (hikari.isRunning()) {
            hikari.close();
        }
    }
}
