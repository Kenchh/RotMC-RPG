package me.kench.rotmc.database.playerdata;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.UUID;

/*
    For future:
    - uuid should not be TEXT, it should be CHAR(36).
    - data should not be stored as LONGTEXT json, it needs to be normalized for best performance
    - ranks need to be normalized
 */
public interface PlayerDataDao {
    @SqlUpdate("create table if not exists `playerdata` (`uuid` TEXT, `data` LONGTEXT, `maxslots` INT, `rankHuntress` INT, `rankKnight` INT, `rankWarrior` INT, `rankNecromancer` INT, `rankAssassin` INT, `rankRogue` INT)")
    void createContainer();

    @SqlQuery("select exists(select 1 from `playerdata` where `uuid` = :uuid)")
    boolean exists(@Bind("uuid") UUID uniqueId);

    @SqlUpdate("insert into `playerdata` (`uuid`, `data`, `maxslots`, `rankHuntress`, `rankKnight`, `rankWarrior`, `rankNecromancer`, `rankAssassin`, `rankRogue`) values (:uuid, [], 2, 0, 0, 0, 0, 0, 0)")
    void create(@Bind("uuid") UUID uniqueId);

    @SqlQuery("select * from `playerdata` where `uuid` = :uuid limit 1")
    @RegisterRowMapper(PlayerDataRowMapper.class)
    PlayerData load(@Bind("uuid") UUID uniqueId);

    @SqlQuery("select * from `playerdata`")
    List<PlayerData> loadAll();

    @SqlUpdate("update `playerdata` set `data` = :data, `maxslots` = :maxslots, `rankHuntress` = :rankHuntress, `rankKnight` = :rankKnight, `rankWarrior` = :rankWarrior, `rankNecromancer` = :rankNecromancer, `rankAssassin` = :rankAssassin, `rankRogue` = :rankRogue where `uuid` = :uuid")
    void save(@Bind("uuid") UUID uniqueId, @Bind("data") String data, @Bind("maxslots") int maxSlots, @Bind("rankHuntress") int rankHuntress, @Bind("rankKnight") int rankKnight, @Bind("rankWarrior") int rankWarrior, @Bind("rankNecromancer") int rankNecromancer, @Bind("rankAssassin") int rankAssassin, @Bind("rankRogue") int rankRogue);

    // TODO: top classes/top guilds
}
