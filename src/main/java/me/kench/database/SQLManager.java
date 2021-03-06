package me.kench.database;

import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.api.GuildsAPI;
import me.glaremasters.guilds.guild.Guild;
import me.kench.RotMC;
import me.kench.game.GameClass;
import me.kench.player.PlayerClass;
import me.kench.player.PlayerData;
import me.kench.player.Stats;
import me.kench.utils.JsonParser;
import me.kench.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SQLManager {

    private final ConnectionPoolManager pool;
    private final String playerDataTable;

    public SQLManager(ConnectionPoolManager pool, String infoTable) {
        this.pool = pool;
        this.playerDataTable = infoTable;
        makeTable();

    }

    /*
       Creates initial player data table in the database
     */
    private void makeTable() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            String statement = "CREATE TABLE IF NOT EXISTS `" + playerDataTable + "` " +
                    "(" +
                    "uuid TEXT, data LONGTEXT, maxslots INT, rankHuntress INT, rankKnight INT, rankWarrior INT, rankNecromancer INT, rankAssassin INT, rankRogue INT" +
                    ")";

            conn = pool.getConnection();
            ps = conn.prepareStatement(statement);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, rs);
        }

    }

    /*
       Obtain a single object from desired type from the database
     */
    public <T> T getValueOrDefaultFromDatabase(OfflinePlayer p, String column, Class<T> type, T defaultV) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            if (p == null) return defaultV;

            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT * FROM " + playerDataTable + " WHERE uuid=?");
            ps.setString(1, p.getUniqueId().toString());

            rs = ps.executeQuery();

            if (rs.next()) return type.cast(rs.getObject(column));
            return defaultV;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, rs);
        }

        return defaultV;
    }

    /*
       Set data for a single column within th e database
     */
    public void setPlayerData(UUID uuid, String column, Object data) {
        new BukkitRunnable() {
            @Override
            public void run() {

                Connection conn = null;
                PreparedStatement ps = null;

                try {

                    conn = pool.getConnection();

                    String stmt = "UPDATE " + playerDataTable + " SET " + column + "=?  WHERE uuid=?";
                    ps = conn.prepareStatement(stmt);

                    ps.setObject(1, data);
                    ps.setString(2, uuid.toString());
                    ps.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    pool.close(conn, ps, null);
                }

            }
        }.runTaskAsynchronously(RotMC.getInstance());
    }

    /*
        Creates initial player data table in the database
     */
    public boolean playerExists(UUID uuid) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            conn = pool.getConnection();

            String stmt = "SELECT * FROM " + playerDataTable + " WHERE uuid=?";
            ps = conn.prepareStatement(stmt);

            ps.setString(1, uuid.toString());
            rs = ps.executeQuery();

            if(rs.next()) return true;
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, rs);
        }

        return false;
    }

    public int getMaxSlots(UUID playeruuid) {

        int s = 2;

        createPlayerData(playeruuid);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            conn = pool.getConnection();

            String stmt = "SELECT * FROM " + playerDataTable + " WHERE uuid=?";
            ps = conn.prepareStatement(stmt);

            ps.setString(1, playeruuid.toString());
            rs = ps.executeQuery();

            while(rs.next()) s = rs.getInt("maxslots");

        } catch (NullPointerException e) {
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, rs);
        }

        return s;
    }

    public ArrayList<PlayerClass> getPlayerClasses(Player p) {

        ArrayList<PlayerClass> playerclasses = new ArrayList<PlayerClass>();
        createPlayerData(p.getUniqueId());

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            conn = pool.getConnection();

            String stmt = "SELECT * FROM " + playerDataTable + " WHERE uuid=?";
            ps = conn.prepareStatement(stmt);

            ps.setString(1, p.getUniqueId().toString());
            rs = ps.executeQuery();

            while(rs.next()) {

                if(rs.getString("data") == null) return playerclasses;

                JSONArray array = new JSONArray(rs.getString("data"));

                for(int i = 0; i < array.length(); i++) {

                    JSONObject input = array.getJSONObject(i);

                    UUID classuuid = UUID.fromString(input.getString("uuid"));
                    GameClass GameClass = new GameClass(input.getString("GameClass"));
                    int xp = input.getInt("xp");
                    int level = input.getInt("level");
                    boolean selected = input.getBoolean("selected");

                    Stats stats = new Stats();

                    JSONObject stat = input.getJSONObject("stats");

                    stats.health = (float) stat.getDouble("health");
                    stats.attack = (float) stat.getDouble("attack");
                    stats.defense = (float) stat.getDouble("defense");
                    stats.speed = (float) stat.getDouble("speed");
                    stats.dodge = (float) stat.getDouble("dodge");
                    stats.vitality = (float) stat.getDouble("vitality");

                    if (classuuid != null && GameClass != null && xp != -1 && level != -1) {
                        PlayerClass pc = new PlayerClass(classuuid, Bukkit.getPlayer(p.getUniqueId()), GameClass, xp, level, stats);
                        if(selected) pc.selected = true;
                        playerclasses.add(pc);
                    }

                }

                return playerclasses;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, rs);
        }

        return playerclasses;
    }

    public PlayerClass getCurrentClass(UUID playeruuid) {

        createPlayerData(playeruuid);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            conn = pool.getConnection();

            String stmt = "SELECT * FROM " + playerDataTable + " WHERE uuid=?";
            ps = conn.prepareStatement(stmt);

            ps.setString(1, playeruuid.toString());
            rs = ps.executeQuery();

            while(rs.next()) {

                UUID classuuid = null;
                GameClass GameClass = null;
                int xp = -1;
                int level = -1;
                Stats stats = new Stats();

                if(rs.getString("data") == null) return null;

                JSONArray array = new JSONArray(rs.getString("data"));

                for(int i = 0; i < array.length(); i++) {

                    JSONObject input = array.getJSONObject(i);

                    if(!input.getBoolean("selected")) continue;

                    classuuid = UUID.fromString(input.getString("uuid"));
                    GameClass = new GameClass(input.getString("GameClass"));
                    xp = input.getInt("xp");
                    level = input.getInt("level");

                    JSONObject stat = input.getJSONObject("stats");

                    stats.health = (float) stat.getDouble("health");
                    stats.attack = (float) stat.getDouble("attack");
                    stats.defense = (float) stat.getDouble("defense");
                    stats.speed = (float) stat.getDouble("speed");
                    stats.dodge = (float) stat.getDouble("dodge");
                    stats.vitality = (float) stat.getDouble("vitality");

                }

                if (classuuid != null && GameClass != null && xp != -1 && level != -1) {
                    PlayerClass pc = new PlayerClass(classuuid, Bukkit.getPlayer(playeruuid), GameClass, xp, level, stats);
                    pc.selected = true;
                    return pc;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, rs);
        }

        return null;
    }

    private void createPlayerData(UUID uuid) {
        if (playerExists(uuid)) return;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = pool.getConnection();

            String stmt = "INSERT INTO " + playerDataTable + "(uuid, data, maxslots, rankHuntress, rankKnight, rankWarrior, rankNecromancer, rankAssassin, rankRogue) VALUE(?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(stmt);
            ps.setString(1, uuid.toString());
            ps.setString(2, new JSONArray().toString());
            ps.setString(2, new JSONArray().toString());
            ps.setInt(3, 2);

            ps.setInt(4, 0);
            ps.setInt(5, 0);
            ps.setInt(6, 0);
            ps.setInt(7, 0);
            ps.setInt(8, 0);
            ps.setInt(9, 0);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, rs);
        }
    }

    public PlayerData getPlayerData(Player p) {
        return new PlayerData(p, getMaxSlots(p.getUniqueId()), getPlayerClasses(p), getCurrentClass(p.getUniqueId()));
    }

    public String getInventory(UUID playeruuid, UUID classuuid) {

        createPlayerData(playeruuid);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            conn = pool.getConnection();

            String stmt = "SELECT * FROM " + playerDataTable + " WHERE uuid=?";
            ps = conn.prepareStatement(stmt);

            ps.setString(1, playeruuid.toString());
            rs = ps.executeQuery();

            while(rs.next()) {

                if(rs.getString("data") == null) return null;

                JSONArray array = new JSONArray(rs.getString("data"));

                for(int i = 0; i < array.length(); i++) {
                    JSONObject input = array.getJSONObject(i);

                    if(!classuuid.toString().equalsIgnoreCase(input.getString("uuid"))) continue;

                    return input.getString("inv");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, rs);
        }

        return "";
    }

    public HashMap<Integer, List<String>> getTopClasses() {

        HashMap<Integer, List<String>> top10Classes = new HashMap<>();
        ArrayList<List<String>> all = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            conn = pool.getConnection();

            String stmt = "SELECT * FROM " + playerDataTable + "";
            ps = conn.prepareStatement(stmt);

            rs = ps.executeQuery();

            while(rs.next()) {

                String uuid = rs.getString("uuid");
                JSONArray array = new JSONArray(rs.getString("data"));

                String name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();

                for(int i = 0; i < array.length(); i++) {
                    JSONObject input = array.getJSONObject(i);

                    String gameclass = input.getString("GameClass");
                    int xp = input.getInt("xp");

                    all.add(Arrays.asList(name, gameclass, xp + ""));
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, rs);
        }

        return top10Classes;
    }

    public HashMap<Integer, List<String>> getTopGuilds() {

        HashMap<Integer, List<String>> top10Classes = new HashMap<>();
        ArrayList<List<String>> all = new ArrayList<>();

        GuildsAPI guildsAPI = Guilds.getApi();
        List<Guild> guilds = guildsAPI.getGuildHandler().getGuilds();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            conn = pool.getConnection();

            String stmt = "SELECT * FROM " + playerDataTable + "";
            ps = conn.prepareStatement(stmt);

            rs = ps.executeQuery();

            while (rs.next()) {

                String uuid = rs.getString("uuid");
                Guild g = null;
                for (Guild z : guilds) {
                    if (z.getMember(UUID.fromString(uuid)) == null) continue;
                    g = z;
                }

                if (g == null || g.getName() == null) continue;
                int xp = 0;

                JSONArray array = new JSONArray(rs.getString("data"));

                for(int i = 0; i < array.length(); i++) {
                    JSONObject input = array.getJSONObject(i);

                    xp += input.getInt("xp");
                }



                int size = all.size();
                for (int i = 0; i < size; i++) {
                    List<String> data = all.get(i);
                    if (data.get(0).equals(g.getName())) {
                        xp += Integer.parseInt(data.get(1));
                        all.remove(i);
                        break;
                    }
                }

                all.add(Arrays.asList(g.getName(), xp + ""));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, rs);
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

    public void update(Player p, PlayerClass old) {

        PlayerData pd = RotMC.getPlayerData(p);

        JSONArray toUpdate = new JSONArray();

        ArrayList<PlayerClass> updated = new ArrayList<>();

        for (PlayerClass pc : pd.classes) {
            if(pc.getUuid().toString().equals(pd.getMainClass().getUuid().toString())) {
                updated.add(pd.getMainClass());
            } else {
                updated.add(pc);
            }
        }

        pd.classes = updated;

        for (PlayerClass pc : pd.classes) {

            if(old != null) {
                if (pc.getUuid().toString().equalsIgnoreCase(old.getUuid().toString())) {
                    toUpdate.put(
                            new JSONObject().
                                    put("uuid", pc.getUuid()).
                                    put("GameClass", pc.getData().getName()).
                                    put("selected", pc.selected).
                                    put("xp", pc.getXp()).
                                    put("level", pc.getLevel()).
                                    put("stats", new JSONObject().
                                            put("health", pc.getStats().health).
                                            put("attack", pc.getStats().attack).
                                            put("defense", pc.getStats().defense).
                                            put("speed", pc.getStats().speed).
                                            put("dodge", pc.getStats().dodge).
                                            put("vitality", pc.getStats().vitality)

                                    ).
                                    put("inv", JsonParser.toBase64(p))
                    );

                } else {
                    toUpdate.put(
                            new JSONObject().
                                    put("uuid", pc.getUuid()).
                                    put("GameClass", pc.getData().getName()).
                                    put("selected", pc.selected).
                                    put("xp", pc.getXp()).
                                    put("level", pc.getLevel()).
                                    put("stats", new JSONObject().
                                            put("health", pc.getStats().health).
                                            put("attack", pc.getStats().attack).
                                            put("defense", pc.getStats().defense).
                                            put("speed", pc.getStats().speed).
                                            put("dodge", pc.getStats().dodge).
                                            put("vitality", pc.getStats().vitality)

                                    ).
                                    put("inv", getInventory(p.getUniqueId(), pc.getUuid()))
                    );
                }
            } else {
                toUpdate.put(
                        new JSONObject().
                                put("uuid", pc.getUuid()).
                                put("GameClass", pc.getData().getName()).
                                put("selected", pc.selected).
                                put("xp", pc.getXp()).
                                put("level", pc.getLevel()).
                                put("stats", new JSONObject().
                                        put("health", pc.getStats().health).
                                        put("attack", pc.getStats().attack).
                                        put("defense", pc.getStats().defense).
                                        put("speed", pc.getStats().speed).
                                        put("dodge", pc.getStats().dodge).
                                        put("vitality", pc.getStats().vitality)

                                ).
                                put("inv", getInventory(p.getUniqueId(), pc.getUuid()))
                );
            }
        }

        int huntress = RankUtils.getCharacterRank(p, "Huntress");
        int knight = RankUtils.getCharacterRank(p, "Knight");
        int warrior = RankUtils.getCharacterRank(p, "Warrior");
        int necromancer = RankUtils.getCharacterRank(p, "Necromancer");
        int assassin = RankUtils.getCharacterRank(p, "Assassin");
        int rogue = RankUtils.getCharacterRank(p, "Rogue");

        new BukkitRunnable() {
            @Override
            public void run() {
                setPlayerData(p.getUniqueId(), "data", toUpdate.toString());
                setPlayerData(p.getUniqueId(), "maxslots", pd.maxSlots);
                setPlayerData(p.getUniqueId(), "rankHuntress", huntress);
                setPlayerData(p.getUniqueId(), "rankKnight", knight);
                setPlayerData(p.getUniqueId(), "rankWarrior", warrior);
                setPlayerData(p.getUniqueId(), "rankNecromancer", necromancer);
                setPlayerData(p.getUniqueId(), "rankAssassin", assassin);
                setPlayerData(p.getUniqueId(), "rankRogue", rogue);
            }
        }.runTaskAsynchronously(RotMC.getInstance());

    }


}
