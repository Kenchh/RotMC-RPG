package me.kench.database;

import me.kench.RotMC;
import me.kench.game.GameClass;
import me.kench.player.PlayerClass;
import me.kench.player.PlayerData;
import me.kench.player.Stats;
import me.kench.utils.JsonParser;
import org.bukkit.Bukkit;
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

    private void makeTable() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            /*
             * CREATING WHOLE TABLE
             */
            String statement = "CREATE TABLE IF NOT EXISTS `" + playerDataTable + "` " +
                    "(" +
                    "uuid TEXT, data TEXT, maxslots INT" +
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
                                            put("dodge", pc.getStats().dodge)

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
                                            put("dodge", pc.getStats().dodge)

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
                                        put("dodge", pc.getStats().dodge)

                                ).
                                put("inv", getInventory(p.getUniqueId(), pc.getUuid()))
                );
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                setPlayerData(p.getUniqueId(), "data", toUpdate.toString());
                setPlayerData(p.getUniqueId(), "maxslots", pd.maxSlots);
            }
        }.runTaskAsynchronously(RotMC.getInstance());

    }

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

            while(rs.next()) {
                s = rs.getInt("maxslots");
            }

        } catch (NullPointerException e) {
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, rs);
        }

        return s;
    }

    public ArrayList<PlayerClass> getPlayerClasses(UUID playeruuid) {
        ArrayList<PlayerClass> playerclasses = new ArrayList<PlayerClass>();

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

                /* TODO Go through all player classes a player has and putting them into the arraylist */

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

                    stats.health = stat.getInt("health");
                    stats.attack = stat.getInt("attack");
                    stats.defense = stat.getInt("defense");
                    stats.speed = stat.getInt("speed");
                    stats.dodge = stat.getInt("dodge");

                    Inventory inventory = JsonParser.fromBase64(input.getString("inv"));

                    if (classuuid != null && GameClass != null && xp != -1 && level != -1) {
                        PlayerClass pc = new PlayerClass(classuuid, Bukkit.getPlayer(playeruuid), GameClass, xp, level, stats);
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

                /* TODO Go through all player classes a player has and putting them into the arraylist */
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

                    stats.health = stat.getInt("health");
                    stats.attack = stat.getInt("attack");
                    stats.defense = stat.getInt("defense");
                    stats.speed = stat.getInt("speed");
                    stats.dodge = stat.getInt("dodge");

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

            String stmt = "INSERT INTO " + playerDataTable + "(uuid, data, maxslots) VALUE(?,?,?)";
            ps = conn.prepareStatement(stmt);
            ps.setString(1, uuid.toString());
            ps.setString(2, new JSONArray().toString());
            ps.setInt(3, 2);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, rs);
        }
    }

    /* UUID, UUID & String-Inv */
    public HashMap<UUID, HashMap<UUID, String>> inventories;

    public void getInventories() {

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

    public HashMap<Integer, List<String>> getTopProfiles() {
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

                int xp = 0;

                for(int i = 0; i < array.length(); i++) {
                    JSONObject input = array.getJSONObject(i);

                    xp += input.getInt("xp");
                }

                all.add(Arrays.asList(name, xp + ""));
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, rs);
        }

        return top10Classes;
    }

}
