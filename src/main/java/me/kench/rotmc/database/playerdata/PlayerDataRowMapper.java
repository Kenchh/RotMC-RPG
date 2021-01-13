package me.kench.rotmc.database.playerdata;

import me.kench.rotmc.player.PlayerClass;
import me.kench.rotmc.player.RpgClass;
import me.kench.rotmc.player.stat.Stat;
import me.kench.rotmc.player.stat.Stats;
import me.kench.rotmc.utils.InventorySerializer;
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
                stats.setStat(Stat.HEALTH, (float) statsObject.getDouble("health"));
                stats.setStat(Stat.ATTACK, (float) statsObject.getDouble("attack"));
                stats.setStat(Stat.DEFENSE, (float) statsObject.getDouble("defense"));
                stats.setStat(Stat.SPEED, (float) statsObject.getDouble("speed"));
                stats.setStat(Stat.EVASION, (float) statsObject.getDouble("dodge"));
                stats.setStat(Stat.VITALITY, (float) statsObject.getDouble("vitality"));

                classes.add(new PlayerClass(
                        playerUniqueId,
                        UUID.fromString(object.getString("uuid")),
                        RpgClass.getByName(object.getString("GameClass")),
                        stats,
                        object.getInt("xp"),
                        object.getInt("level"),
                        object.getBoolean("selected"),
                        InventorySerializer.fromBase64(object.getString("inv"))
                ));
            }
        }

        return classes;
    }
}
