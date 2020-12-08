package me.kench.game;

import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.api.GuildsAPI;
import me.glaremasters.guilds.guild.Guild;
import me.kench.RotMC;
import me.kench.config.PlayerDataConfig;
import me.kench.player.PlayerClass;
import me.kench.player.PlayerData;
import me.kench.player.Stats;
import me.kench.utils.JsonParser;
import me.kench.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

public class DataManager {

    private PlayerDataConfig dataconfig;
    public FileConfiguration config;

    public DataManager(PlayerDataConfig dataconfig) {
        this.dataconfig = dataconfig;
        this.config = dataconfig.config;
    }

    public PlayerData getPlayerData(Player p) {
        String uuid = "";

        for(String u : config.getKeys(false)) {
            if(p.getUniqueId().toString().equalsIgnoreCase(u)) {
                uuid = u;
            }
        }

        if(uuid.equals("")) return new PlayerData(p);

        int maxSlots = 2;

        maxSlots = config.getInt(p.getUniqueId().toString() + ".maxSlots");

        ArrayList<PlayerClass> classes = new ArrayList<>();
        PlayerClass currentClass = null;

        for(String classuuid : config.getConfigurationSection(uuid + ".classes").getKeys(false)) {

            String classtype = config.getString(p.getUniqueId().toString() + ".classes." + classuuid + ".classtype");
            boolean selected = config.getBoolean(p.getUniqueId().toString() + ".classes." + classuuid + ".selected");
            int level = config.getInt(p.getUniqueId().toString() + ".classes." + classuuid + ".level");
            int xp = config.getInt(p.getUniqueId().toString() + ".classes." + classuuid + ".xp");

            double health = config.getDouble(p.getUniqueId().toString() + ".classes." + classuuid + ".stats.health");
            double attack = config.getDouble(p.getUniqueId().toString() + ".classes." + classuuid + ".stats.attack");
            double defense = config.getDouble(p.getUniqueId().toString() + ".classes." + classuuid + ".stats.defense");
            double speed = config.getDouble(p.getUniqueId().toString() + ".classes." + classuuid + ".stats.speed");
            double dodge = config.getDouble(p.getUniqueId().toString() + ".classes." + classuuid + ".stats.dodge");

            Stats stats = new Stats();
            stats.health = (float) health;
            stats.attack = (float) attack;
            stats.defense = (float) defense;
            stats.speed = (float) speed;
            stats.dodge = (float) dodge;

            PlayerClass pc = new PlayerClass(UUID.fromString(classuuid), p, new GameClass(classtype), xp, level, stats);
            pc.selected = selected;

            if(selected) {
                currentClass = pc;
            }

            classes.add(pc);
        }

        return new PlayerData(p, maxSlots, classes, currentClass);
    }

    public ArrayList<PlayerClass> getPlayerClasses(Player p) {
        ArrayList<PlayerClass> classes = new ArrayList<>();

        if(config == null) return classes;
        if(config.getConfigurationSection(p.getUniqueId() + ".classes") == null) return classes;
        if(config.getConfigurationSection(p.getUniqueId() + ".classes").getKeys(false) == null) return classes;

        for(String classuuid : config.getConfigurationSection(p.getUniqueId() + ".classes").getKeys(false)) {

            String classtype = config.getString(p.getUniqueId().toString() + ".classes." + classuuid + ".classtype");
            boolean selected = config.getBoolean(p.getUniqueId().toString() + ".classes." + classuuid + ".selected");
            int level = config.getInt(p.getUniqueId().toString() + ".classes." + classuuid + ".level");
            int xp = config.getInt(p.getUniqueId().toString() + ".classes." + classuuid + ".xp");

            double health = config.getDouble(p.getUniqueId().toString() + ".classes." + classuuid + ".stats.health");
            double attack = config.getDouble(p.getUniqueId().toString() + ".classes." + classuuid + ".stats.attack");
            double defense = config.getDouble(p.getUniqueId().toString() + ".classes." + classuuid + ".stats.defense");
            double speed = config.getDouble(p.getUniqueId().toString() + ".classes." + classuuid + ".stats.speed");
            double dodge = config.getDouble(p.getUniqueId().toString() + ".classes." + classuuid + ".stats.dodge");

            Stats stats = new Stats();
            stats.health = (float) health;
            stats.attack = (float) attack;
            stats.defense = (float) defense;
            stats.speed = (float) speed;
            stats.dodge = (float) dodge;

            PlayerClass pc = new PlayerClass(UUID.fromString(classuuid), p, new GameClass(classtype), xp, level, stats);
            pc.selected = selected;

            classes.add(pc);
        }

        return classes;
    }

    public String getInventory(UUID uuid, UUID pc_uuid) {
        return config.getString(uuid.toString() + ".classes." + pc_uuid.toString() + ".inv");
    }


    public HashMap<Integer, List<String>> getTopGuilds() {
        HashMap<Integer, List<String>> top10Classes = new HashMap<>();
        ArrayList<List<String>> all = new ArrayList<>();

        GuildsAPI guildsAPI = Guilds.getApi();

        for(String u : config.getKeys(false)) {

            OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(u));
            Guild g = guildsAPI.getGuild(op);

            int xp = 0;

            if(config == null) return top10Classes;
            if(config.getConfigurationSection(u + ".classes") == null) continue;
            if(config.getConfigurationSection(u + ".classes").getKeys(false) == null) continue;

            for (String classuuid : config.getConfigurationSection(u + ".classes").getKeys(false)) {
                xp += config.getInt(u + ".classes." + classuuid + ".xp");
            }

            if(g == null || g.getName() == null) continue;

            int size = all.size();
            for(int i=0; i<size; i++) {
                List<String> data = all.get(i);
                if(data.get(0).equals(g.getName())) {
                    xp += Integer.parseInt(data.get(1));
                    all.remove(i);
                    break;
                }
            }

            all.add(Arrays.asList(g.getName(), xp + ""));
        }

        /** Sorting **/
        for(int i=0;i<all.size()-1;i++) {
            for(int ii=0;ii<all.size()-1;ii++) {
                List<String> data = all.get(ii);
                int exp = Integer.parseInt(data.get(1));

                List<String> nextData = all.get(ii+1);
                int nextExp = Integer.parseInt(nextData.get(1));

                if(exp < nextExp) {
                    all.set(ii, nextData);
                    all.set(ii+1, data);
                }
            }
        }

        for(int i=0;i<10;i++) {

            if(i+1 > all.size()) break;

            List<String> data = all.get(i);
            top10Classes.put(i+1, data);
        }

        return top10Classes;
    }

    public HashMap<Integer, List<String>> getTopClasses() {
        HashMap<Integer, List<String>> top10Classes = new HashMap<>();
        ArrayList<List<String>> all = new ArrayList<>();

        if(config == null) return top10Classes;

        for(String u : config.getKeys(false)) {

            if(config.getConfigurationSection(u + ".classes") == null) continue;
            if(config.getConfigurationSection(u + ".classes").getKeys(false) == null) continue;

            for (String classuuid : config.getConfigurationSection(u + ".classes").getKeys(false)) {
                int xp = config.getInt(u + ".classes." + classuuid + ".xp");
                String gameClass = config.getString(u + ".classes." + classuuid + ".classtype");
                all.add(Arrays.asList(Bukkit.getOfflinePlayer(UUID.fromString(u)).getName(), gameClass, xp + ""));
            }
        }

        /** Sorting **/
        for(int i=0;i<all.size()-1;i++) {
            for(int ii=0;ii<all.size()-1;ii++) {
                List<String> data = all.get(ii);
                int exp = Integer.parseInt(data.get(2));

                List<String> nextData = all.get(ii+1);
                int nextExp = Integer.parseInt(nextData.get(2));

                if(exp < nextExp) {
                    all.set(ii, nextData);
                    all.set(ii+1, data);
                }
            }
        }

        for(int i=0;i<10;i++) {

            if(i+1 > all.size()) break;

            List<String> data = all.get(i);
            top10Classes.put(i+1, data);
        }

        return top10Classes;
    }

    public void update(Player p, PlayerClass old) {

        PlayerData pd = RotMC.getPlayerData(p);

        config.set(p.getUniqueId().toString() + ".maxSlots", pd.maxSlots);

        config.set(p.getUniqueId().toString() + ".rankHuntress", RankUtils.getCharacterRank(p, "Huntress"));
        config.set(p.getUniqueId().toString() + ".rankKnight", RankUtils.getCharacterRank(p, "Knight"));
        config.set(p.getUniqueId().toString() + ".rankWarrior", RankUtils.getCharacterRank(p, "Warrior"));
        config.set(p.getUniqueId().toString() + ".rankNecromancer", RankUtils.getCharacterRank(p, "Necromancer"));
        config.set(p.getUniqueId().toString() + ".rankAssassin", RankUtils.getCharacterRank(p, "Assassin"));
        config.set(p.getUniqueId().toString() + ".rankRogue", RankUtils.getCharacterRank(p, "Rogue"));

        if(pd.classes.isEmpty() == false) {
            ArrayList<UUID> classuuids = new ArrayList<>();
            for (PlayerClass pc : pd.classes) {
                classuuids.add(pc.getUuid());

                config.set(p.getUniqueId().toString() + ".classes." + pc.getUuid() + ".classtype", pc.getData().getName());
                config.set(p.getUniqueId().toString() + ".classes." + pc.getUuid() + ".selected", false);
                config.set(p.getUniqueId().toString() + ".classes." + pc.getUuid() + ".level", pc.getLevel());
                config.set(p.getUniqueId().toString() + ".classes." + pc.getUuid() + ".xp", pc.getXp());

                config.set(p.getUniqueId().toString() + ".classes." + pc.getUuid() + ".stats.health", pc.getStats().health);
                config.set(p.getUniqueId().toString() + ".classes." + pc.getUuid() + ".stats.attack", pc.getStats().attack);
                config.set(p.getUniqueId().toString() + ".classes." + pc.getUuid() + ".stats.defense", pc.getStats().defense);
                config.set(p.getUniqueId().toString() + ".classes." + pc.getUuid() + ".stats.speed", pc.getStats().speed);
                config.set(p.getUniqueId().toString() + ".classes." + pc.getUuid() + ".stats.dodge", pc.getStats().dodge);

                if (old != null && pc.getUuid().toString().equalsIgnoreCase(old.getUuid().toString())) {
                    config.set(p.getUniqueId().toString() + ".classes." + pc.getUuid() + ".inv", JsonParser.toBase64(p));
                }
            }

            for(PlayerClass pc : getPlayerClasses(p)) {
                if(!classuuids.contains(pc.getUuid())) {
                    config.set(p.getUniqueId().toString() + ".classes." + pc.getUuid(), null);
                }
            }

            config.set(p.getUniqueId().toString() + ".classes." + pd.getMainClass().getUuid() + ".selected", true);
        }

        dataconfig.save();


    }

}
