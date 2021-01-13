package me.kench;

import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import me.kench.commands.*;
import me.kench.commands.subcommand.SubcommandManager;
import me.kench.database.DataManager;
import me.kench.listener.*;
import me.kench.papi.GlowPlaceHolder;
import me.kench.papi.StarPlaceHolder;
import me.kench.player.LevelProgression;
import me.kench.session.SessionManager;
import me.kench.utils.armor.ArmorListener;
import me.kench.utils.armor.DispenserArmorListener;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class RotMC extends JavaPlugin {
    private static RotMC instance;
    private static TaskChainFactory taskChainFactory;
    // TODO: why are most of these (below) static?
    private static LevelProgression levelProgression;
    private static SubcommandManager subCommandManager;
    private BukkitAudiences adventure;
    private DataManager dataManager;
    private SessionManager sessionManager;

    RegisteredServiceProvider<LuckPerms> provider;

    @Override
    public void onEnable() {
        instance = this;
        provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        adventure = BukkitAudiences.create(this);
        dataManager = new DataManager(this);
        sessionManager = new SessionManager();
        levelProgression = new LevelProgression();
        subCommandManager = new SubcommandManager();

        Server server = getServer();
        server.getPluginCommand("rotmc").setExecutor(new RotMcCommand());
        server.getPluginCommand("class").setExecutor(new ClassCommand());
        server.getPluginCommand("stats").setExecutor(new SkillsCommand());
        server.getPluginCommand("ftop").setExecutor(new FameCommand());
        server.getPluginCommand("gtop").setExecutor(new GuildCommand());
        server.getPluginCommand("glow").setExecutor(new GlowCommand());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new GuiEvents(), this);
        pluginManager.registerEvents(new InteractEvent(), this);
        pluginManager.registerEvents(new JoinLeaveEvent(), this);
        pluginManager.registerEvents(new ChatEvent(), this);
        pluginManager.registerEvents(new DamageEvent(), this);
        pluginManager.registerEvents(new XPEvent(), this);
        pluginManager.registerEvents(new InventoryEvents(), this);
        pluginManager.registerEvents(new DeathEvent(), this);
        pluginManager.registerEvents(new DispenserArmorListener(), this);
        pluginManager.registerEvents(new ArmorListener(Collections.emptyList()), this);

        if (pluginManager.getPlugin("PlaceholderAPI") != null) {
            new GlowPlaceHolder().register();
            new StarPlaceHolder().register();
        }
    }

    public BukkitAudiences adventure() {
        return adventure;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public LevelProgression getLevelProgression() {
        return levelProgression;
    }

    public SubcommandManager getSubCommandManager() {
        return subCommandManager;
    }

    public LuckPerms getLuckPerms() {
        return provider.getProvider();
    }

    public static RotMC getInstance() {
        return instance;
    }

    public static <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }

    public static <T> TaskChain<T> newSharedChain(String name) {
        return taskChainFactory.newSharedChain(name);
    }
}
