package me.kench;

import me.kench.commands.*;
import me.kench.commands.subcommand.SubCommandManager;
import me.kench.events.*;
import me.kench.gui.*;
import me.kench.player.PlayerClass;
import me.kench.player.PlayerData;
import me.kench.config.MySQLConfig;
import me.kench.database.ConnectionPoolManager;
import me.kench.database.SQLManager;
import me.kench.game.LevelProgression;
import me.kench.game.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class RotMC extends JavaPlugin {

    private static RotMC instance;

    public static RotMC getInstance() {
        return instance;
    }

    private static SQLManager sqlManager;
    private static PlayerDataManager playerDataManager;
    private static LevelProgression levelProgression;
    private static SubCommandManager subCommandManager;

    @Override
    public void onEnable() {
        instance = this;

        MySQLConfig sqlConfig = new MySQLConfig();
        ConnectionPoolManager pool = new ConnectionPoolManager(sqlConfig.hostname, sqlConfig.port, sqlConfig.username, sqlConfig.password, sqlConfig.database);
        sqlManager = new SQLManager(pool, "playerdata");
        playerDataManager = new PlayerDataManager();
        levelProgression = new LevelProgression();
        subCommandManager = new SubCommandManager();

        getServer().getPluginCommand("rotmc").setExecutor(new RotMCCMD());
        getServer().getPluginCommand("class").setExecutor(new ClassCMD());
        getServer().getPluginCommand("skills").setExecutor(new SkillsCMD());
        getServer().getPluginCommand("fame").setExecutor(new FameCMD());
        getServer().getPluginCommand("account").setExecutor(new AccountCMD());

        getServer().getPluginManager().registerEvents(new CreateClassGUI(), this);
        getServer().getPluginManager().registerEvents(new ClassesGUI(), this);
        getServer().getPluginManager().registerEvents(new ConfirmationGUI(), this);
        getServer().getPluginManager().registerEvents(new GuiEvents(), this);
        getServer().getPluginManager().registerEvents(new SkillsGUI(), this);
        getServer().getPluginManager().registerEvents(new ExtractorGUI(), this);

        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        getServer().getPluginManager().registerEvents(new DamageEvent(), this);
        getServer().getPluginManager().registerEvents(new XPEvent(), this);
        getServer().getPluginManager().registerEvents(new PickUpEvent(), this);
        getServer().getPluginManager().registerEvents(new InvEvents(), this);
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);

        for(Player p : Bukkit.getOnlinePlayers()) {

            RotMC.getInstance().getPlayerDataManager().registerPlayerData(p);

            if(RotMC.getPlayerData(p).getMainClass() != null) {

                PlayerClass pc = RotMC.getPlayerData(p).getMainClass();
                RotMC.getInstance().getLevelProgression().displayLevelProgression(p);
                p.sendMessage(ChatColor.GREEN + "Your current profile: " + ChatColor.YELLOW + pc.getData().getName() + " " + ChatColor.GOLD + pc.getLevel());
                pc.applyStats();

            } else {
                p.openInventory(new CreateClassGUI(RotMC.getPlayerData(p)).getInv());
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    if (RotMC.getPlayerData(p) != null) {
                        if (RotMC.getPlayerData(p).getMainClass() != null) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    RotMC.getPlayerData(p).getMainClass().applyStats();
                                }
                            }.runTaskLater(RotMC.getInstance(), 1L);
                        }
                    }
                }
            }
        }.runTaskTimer(this, 1L, 20L);

    }

    public SQLManager getDatabase() {
        return sqlManager;
    }
    public SQLManager getSqlManager() {
        return sqlManager;
    }
    public LevelProgression getLevelProgression() {
        return levelProgression;
    }
    public SubCommandManager getSubCommandManager() {
        return subCommandManager;
    }
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public static PlayerData getPlayerData(final OfflinePlayer offlinePlayer) {
        if (offlinePlayer == null) {
            return null;
        }
        return playerDataManager.getPlayerData(offlinePlayer);
    }

}
