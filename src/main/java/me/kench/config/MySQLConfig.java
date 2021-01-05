package me.kench.config;

import me.kench.RotMC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MySQLConfig {

    public String hostname, database, username, password;
    public int port;

    private File file;
    private FileConfiguration config;

    public MySQLConfig() {

        if (!RotMC.getInstance().getDataFolder().exists()) {
            RotMC.getInstance().getDataFolder().mkdirs();
        }

        file = new File(RotMC.getInstance().getDataFolder(), "mysql.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            config = YamlConfiguration.loadConfiguration(file);

            config.set("hostname", "127.0.0.1");
            config.set("database", "rotmc");
            config.set("username", "admin");
            config.set("password", "123");
            config.set("port", 3306);

        } else {
            config = YamlConfiguration.loadConfiguration(file);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        hostname = config.getString("hostname");
        database = config.getString("database");
        username = config.getString("username");
        password = config.getString("password");
        port = config.getInt("port");

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
