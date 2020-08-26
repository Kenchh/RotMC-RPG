package me.kench.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionPoolManager {

    private HikariDataSource dataSource;

    private String hostname, database, username, password;
    private int port;

    private int minimumConnections, maximumConnections;
    private long connectionTimeout;
    private String testQuery;

    public ConnectionPoolManager(String hostname, int port, String username, String password, String database) {

        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        this.maximumConnections = 5;
        this.minimumConnections = 1;
        this.connectionTimeout = 0;
        this.testQuery = "SELECT 1";

        setupPool();
    }

    public void setupPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(

                "jdbc:mysql://" +
                        hostname +
                        ":" +
                        port +
                        "/" +
                        database

        );

        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setUsername(username);
        config.setPassword(password);
        config.setMinimumIdle(minimumConnections);
        config.setMaximumPoolSize(maximumConnections);
        config.setConnectionTimeout(connectionTimeout);
        config.setConnectionTestQuery(testQuery);
        dataSource = new HikariDataSource(config);

    }


    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close(Connection conn, PreparedStatement ps, ResultSet res) {
        if(conn != null) try { conn.close(); } catch (SQLException ignored) {}
        if(ps != null) try { ps.close(); } catch (SQLException ignored) {}
        if(res != null) try { res.close(); } catch (SQLException ignored) {}
    }

    public void closePool() {
        if(dataSource !=null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

}