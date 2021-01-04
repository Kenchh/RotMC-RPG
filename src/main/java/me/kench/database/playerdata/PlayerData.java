package me.kench.database.playerdata;

import me.kench.player.PlayerClass;
import me.kench.player.Stats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class PlayerData {
    private final UUID uniqueId;
    private final List<PlayerClass> classes;
    private int maxSlots;
    private int rankHuntress;
    private int rankKnight;
    private int rankWarrior;
    private int rankNecromancer;
    private int rankAssassin;
    private int rankRogue;

    public PlayerData(UUID uniqueId, List<PlayerClass> classes, int maxSlots, int rankHuntress, int rankKnight, int rankWarrior, int rankNecromancer, int rankAssassin, int rankRogue) {
        this.uniqueId = uniqueId;
        this.classes = classes;
        this.maxSlots = maxSlots;
        this.rankHuntress = rankHuntress;
        this.rankKnight = rankKnight;
        this.rankWarrior = rankWarrior;
        this.rankNecromancer = rankNecromancer;
        this.rankAssassin = rankAssassin;
        this.rankRogue = rankRogue;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public List<PlayerClass> getClasses() {
        return classes;
    }

    public String getClassesAsJson() {
        JSONArray array = new JSONArray();

        for (PlayerClass clazz : getClasses()) {
            Stats stats = clazz.getStats();

            // If the class we are currently looking at is currently in use, we need to ensure
            // that the inventory data is fresh.
            if (clazz.selected) {
                Player player = Bukkit.getPlayer(getUniqueId());
                if (player != null) {
                    clazz.inventory = player.getInventory();
                }
            }

            array.put(new JSONObject()
                    .put("uuid", clazz.getUuid())
                    .put("GameClass", clazz.getData().getName())
                    .put("selected", clazz.selected)
                    .put("xp", clazz.getXp())
                    .put("level", clazz.getLevel())
                    .put("stats", new JSONObject()
                            .put("health", stats.health)
                            .put("attack", stats.attack)
                            .put("defense", stats.defense)
                            .put("speed", stats.speed)
                            .put("dodge", stats.dodge)
                            .put("vitality", stats.vitality)
                    )
                    .put("inv", clazz.inventory)
            );
        }

        return array.toString();
    }

    public PlayerClass getSelectedClass() {
        return classes.stream().filter(it -> it.selected).findFirst().orElse(null);
    }

    public boolean changeSelectedClass(UUID classUniqueId) {
        PlayerClass newClass = classes.stream().filter(it -> it.getUuid().equals(classUniqueId)).findFirst().orElse(null);
        if (newClass == null) return false;

        // Save current inventory before switching classes.
        Player player = Bukkit.getPlayer(getUniqueId());
        if (player != null) {
            PlayerClass currentlySelected = getSelectedClass();
            currentlySelected.inventory = player.getInventory();
        }

        classes.forEach(it -> it.selected = false);
        newClass.selected = true;
        return true;
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public void setMaxSlots(int maxSlots) {
        this.maxSlots = maxSlots;
    }

    public int getRankHuntress() {
        return rankHuntress;
    }

    public void setRankHuntress(int rankHuntress) {
        this.rankHuntress = rankHuntress;
    }

    public int getRankKnight() {
        return rankKnight;
    }

    public void setRankKnight(int rankKnight) {
        this.rankKnight = rankKnight;
    }

    public int getRankWarrior() {
        return rankWarrior;
    }

    public void setRankWarrior(int rankWarrior) {
        this.rankWarrior = rankWarrior;
    }

    public int getRankNecromancer() {
        return rankNecromancer;
    }

    public void setRankNecromancer(int rankNecromancer) {
        this.rankNecromancer = rankNecromancer;
    }

    public int getRankAssassin() {
        return rankAssassin;
    }

    public void setRankAssassin(int rankAssassin) {
        this.rankAssassin = rankAssassin;
    }

    public int getRankRogue() {
        return rankRogue;
    }

    public void setRankRogue(int rankRogue) {
        this.rankRogue = rankRogue;
    }
}
