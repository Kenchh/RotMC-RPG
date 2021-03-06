package me.kench;

import me.kench.commands.*;
import me.kench.commands.subcommand.SubCommandManager;
import me.kench.config.MySQLConfig;
import me.kench.database.ConnectionPoolManager;
import me.kench.events.*;
import me.kench.gui.*;
import me.kench.papi.GlowPlaceHolder;
import me.kench.papi.StarPlaceHolder;
import me.kench.player.PlayerClass;
import me.kench.player.PlayerData;
import me.kench.database.SQLManager;
import me.kench.game.LevelProgression;
import me.kench.game.PlayerDataManager;
import me.kench.utils.armor.ArmorListener;
import me.kench.utils.armor.DispenserArmorListener;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class RotMC extends JavaPlugin {

    private static RotMC instance;

    public static RotMC getInstance() {
        return instance;
    }

    private static SQLManager sqlManager;
    private static PlayerDataManager playerDataManager;
    private static LevelProgression levelProgression;
    private static SubCommandManager subCommandManager;

    RegisteredServiceProvider<LuckPerms> provider;

    @Override
    public void onEnable() {
        instance = this;

        provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);

        MySQLConfig sqlConfig = new MySQLConfig();
        ConnectionPoolManager pool = new ConnectionPoolManager(sqlConfig.hostname, sqlConfig.port, sqlConfig.username, sqlConfig.password, sqlConfig.database);
        sqlManager = new SQLManager(pool, "playerdata");

        playerDataManager = new PlayerDataManager();
        levelProgression = new LevelProgression();
        subCommandManager = new SubCommandManager();

        getServer().getPluginCommand("rotmc").setExecutor(new RotMCCMD());
        getServer().getPluginCommand("class").setExecutor(new ClassCMD());
        getServer().getPluginCommand("stats").setExecutor(new SkillsCMD());
        getServer().getPluginCommand("ftop").setExecutor(new FameCMD());
        getServer().getPluginCommand("gtop").setExecutor(new GuildCMD());
        getServer().getPluginCommand("glow").setExecutor(new GlowCMD());

        getServer().getPluginManager().registerEvents(new CreateClassGUI(), this);
        getServer().getPluginManager().registerEvents(new ClassesGUI(), this);
        getServer().getPluginManager().registerEvents(new ConfirmationGUI(), this);
        getServer().getPluginManager().registerEvents(new GuiEvents(), this);
        getServer().getPluginManager().registerEvents(new SkillsGUI(), this);
        getServer().getPluginManager().registerEvents(new ExtractorGUI(), this);
        getServer().getPluginManager().registerEvents(new InteractEvent(), this);
        getServer().getPluginManager().registerEvents(new GlowGUI(), this);

        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        getServer().getPluginManager().registerEvents(new DamageEvent(), this);
        getServer().getPluginManager().registerEvents(new XPEvent(), this);
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

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new GlowPlaceHolder().register();
            new StarPlaceHolder().register();
        }


        getServer().getPluginManager().registerEvents(new ArmorListener(Collections.emptyList()), this);
        try{
            //Better way to check for this? Only in 1.13.1+?
            Class.forName("org.bukkit.event.block.BlockDispenseArmorEvent");
            getServer().getPluginManager().registerEvents(new DispenserArmorListener(), this);
        }catch(Exception ignored){}

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

    public LuckPerms getApi() {
        return provider.getProvider();
    }

    public static PlayerData getPlayerData(Player offlinePlayer) {
        if (offlinePlayer == null) return null;

        return playerDataManager.getPlayerData(offlinePlayer);
    }

}
