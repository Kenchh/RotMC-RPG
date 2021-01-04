package me.kench.database.playerdata;

import me.kench.game.GameClass;
import me.kench.player.PlayerClass;
import me.kench.player.Stats;
import me.kench.utils.JsonParser;
import org.bukkit.Bukkit;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDataRowMapper implements RowMapper<PlayerData> {
    @Override
    public PlayerData map(ResultSet rs, StatementContext ctx) throws SQLException {
        UUID playerUniqueId = UUID.fromString(rs.getString("uuid"));
        List<PlayerClass> classes = getPlayerClasses(rs, playerUniqueId);

        return new PlayerData(
                playerUniqueId,
                classes,
                rs.getInt("maxslots"),
                rs.getInt("rankHuntress"),
                rs.getInt("rankKnight"),
                rs.getInt("rankWarrior"),
                rs.getInt("rankNecromancer"),
                rs.getInt("rankAssassin"),
                rs.getInt("rankRogue")
        );
    }

    private List<PlayerClass> getPlayerClasses(ResultSet rs, UUID playerUniqueId) throws SQLException {
        List<PlayerClass> classes = new ArrayList<>();

        String dataJson = rs.getString("data");
        if (dataJson != null) {
            JSONArray array = new JSONArray(dataJson);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                JSONObject statsObject = object.getJSONObject("stats");

                Stats stats = new Stats();
                stats.health = (float) statsObject.getDouble("health");
                stats.attack = (float) statsObject.getDouble("attack");
                stats.defense = (float) statsObject.getDouble("defense");
                stats.speed = (float) statsObject.getDouble("speed");
                stats.dodge = (float) statsObject.getDouble("dodge");
                stats.vitality = (float) statsObject.getDouble("vitality");

                PlayerClass clazz = new PlayerClass(
                        UUID.fromString(object.getString("uuid")),
                        Bukkit.getPlayer(playerUniqueId),
                        new GameClass(object.getString("GameClass")),
                        object.getInt("xp"),
                        object.getInt("level"),
                        stats
                );

                if (object.getBoolean("selected")) {
                    clazz.selected = true;
                }

                clazz.inventory = JsonParser.fromBase64(object.getString("inv"));

                classes.add(clazz);
            }
        }

        return classes;
    }
}
