package me.kench.database;

import me.kench.database.playerdata.PlayerDataDam;
import org.jdbi.v3.core.Jdbi;

public class DataAccessor {
    private final PlayerDataDam playerData;

    public DataAccessor(Jdbi jdbi) {
        playerData = new PlayerDataDam(jdbi);
    }

    public PlayerDataDam getPlayerData() {
        return playerData;
    }
}
