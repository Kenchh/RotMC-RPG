package me.kench;

import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import me.kench.commands.*;
import me.kench.commands.subcommand.SubCommandManager;
import me.kench.database.DataManager;
import me.kench.listener.*;
import me.kench.player.LevelProgression;
import me.kench.gui.createclass.CreateClassGui;
import me.kench.gui.extractor.ExtractorGui;
import me.kench.gui.glow.GlowGui;
import me.kench.gui.skills.SkillsGui;
import me.kench.papi.GlowPlaceHolder;
import me.kench.papi.StarPlaceHolder;
import me.kench.session.SessionManager;
import me.kench.utils.armor.ArmorListener;
import me.kench.utils.armor.DispenserArmorListener;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class RotMC extends JavaPlugin {
    private static RotMC instance;
    private static TaskChainFactory taskChainFactory;
    // TODO: why are most of these (below) static?
    private static LevelProgression levelProgression;
    private static SubCommandManager subCommandManager;
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
        subCommandManager = new SubCommandManager();

        getServer().getPluginCommand("rotmc").setExecutor(new RotMCCMD());
        getServer().getPluginCommand("class").setExecutor(new ClassCMD());
        getServer().getPluginCommand("stats").setExecutor(new SkillsCMD());
        getServer().getPluginCommand("ftop").setExecutor(new FameCMD());
        getServer().getPluginCommand("gtop").setExecutor(new GuildCMD());
        getServer().getPluginCommand("glow").setExecutor(new GlowCMD());

        getServer().getPluginManager().registerEvents(new GuiEvents(), this);
        getServer().getPluginManager().registerEvents(new SkillsGui(), this);
        getServer().getPluginManager().registerEvents(new ExtractorGui(), this);
        getServer().getPluginManager().registerEvents(new InteractEvent(), this);
        getServer().getPluginManager().registerEvents(new GlowGui(), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        getServer().getPluginManager().registerEvents(new DamageEvent(), this);
        getServer().getPluginManager().registerEvents(new XPEvent(), this);
        getServer().getPluginManager().registerEvents(new InvEvents(), this);
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);
        
// TODO: remove this; not needed as Players should NOT be online when plugin is enabling.
//        for (Player p : Bukkit.getOnlinePlayers()) {
//            RotMC.getInstance().getPlayerDataManager().registerPlayerData(p);
//
//            if (RotMC.getPlayerData(p).getMainClass() != null) {
//                PlayerClass pc = RotMC.getPlayerData(p).getMainClass();
//                RotMC.getInstance().getLevelProgression().displayLevelProgression(p);
//                p.sendMessage(ChatColor.GREEN + "Your current profile: " + ChatColor.YELLOW + pc.getData().getName() + " " + ChatColor.GOLD + pc.getLevel());
//                pc.applyStats();
//            } else {
//                p.openInventory(new CreateClassGUI(RotMC.getPlayerData(p)).getInv());
//            }
//        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new GlowPlaceHolder().register();
            new StarPlaceHolder().register();
        }

        getServer().getPluginManager().registerEvents(new ArmorListener(Collections.emptyList()), this);

        try {
            //Better way to check for this? Only in 1.13.1+?
            Class.forName("org.bukkit.event.block.BlockDispenseArmorEvent");
            getServer().getPluginManager().registerEvents(new DispenserArmorListener(), this);
        } catch (Exception ignored) {
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

    public SubCommandManager getSubCommandManager() {
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
