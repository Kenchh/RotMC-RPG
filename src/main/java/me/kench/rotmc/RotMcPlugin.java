package me.kench.rotmc;

import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import me.kench.rotmc.commands.*;
import me.kench.rotmc.commands.subcommand.SubcommandManager;
import me.kench.rotmc.database.DataManager;
import me.kench.rotmc.listener.*;
import me.kench.rotmc.utils.external.papi.GlowPlaceholder;
import me.kench.rotmc.utils.external.papi.StarPlaceholder;
import me.kench.rotmc.player.LevelProgression;
import me.kench.rotmc.session.SessionManager;
import me.kench.rotmc.utils.armor.ArmorListener;
import me.kench.rotmc.utils.armor.DispenserArmorListener;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class RotMcPlugin extends JavaPlugin {
    private static RotMcPlugin instance;
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
        pluginManager.registerEvents(new VariousGameItemInteractListener(), this);
        pluginManager.registerEvents(new PlayerJoinLeaveListener(), this);
        pluginManager.registerEvents(new PlayerChatListener(), this);
        pluginManager.registerEvents(new DamageListener(), this);
        pluginManager.registerEvents(new PlayerNativeXpLevelListener(), this);
        pluginManager.registerEvents(new VariousGameItemInventoryListener(), this);
        pluginManager.registerEvents(new PlayerDeathListener(), this);
        pluginManager.registerEvents(new DispenserArmorListener(), this);
        pluginManager.registerEvents(new ArmorListener(Collections.emptyList()), this);

        if (pluginManager.getPlugin("PlaceholderAPI") != null) {
            new GlowPlaceholder().register();
            new StarPlaceholder().register();
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

    public static RotMcPlugin getInstance() {
        return instance;
    }

    public static <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }

    public static <T> TaskChain<T> newSharedChain(String name) {
        return taskChainFactory.newSharedChain(name);
    }
}
