package me.kench.config;

import me.kench.RotMC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class PlayerDataConfig {

    public File file;
    public FileConfiguration config;

    public PlayerDataConfig() {

        if (!RotMC.getInstance().getDataFolder().exists()) {
            RotMC.getInstance().getDataFolder().mkdirs();
        }

        file = new File(RotMC.getInstance().getDataFolder(), "playerdata.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            config = YamlConfiguration.loadConfiguration(file);

        } else {
            config = YamlConfiguration.loadConfiguration(file);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void save() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.runTaskAsynchronously(RotMC.getInstance());
    }

}
