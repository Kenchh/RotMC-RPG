package me.kench.game;

import me.kench.RotMC;
import me.kench.player.PlayerClass;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class LevelProgression {
    /* Key -> Level; Value -> Fame */
    public Map<Integer, Integer> levelToFameMap;

    public LevelProgression() {
        // Level progression formula:
        // x = 10, y = 8, z = 16
        // Level 2: (x + y + z) = 34
        // Level 3-20: ((x * currentLevel^2) + (y * currentLevel) + z) + previousLevelFame

        levelToFameMap = new HashMap<>();
        levelToFameMap.put(1, 0);
        levelToFameMap.put(2, 34);
        levelToFameMap.put(3, 106);
        levelToFameMap.put(4, 236);
        levelToFameMap.put(5, 444);
        levelToFameMap.put(6, 750);
        levelToFameMap.put(7, 1174);
        levelToFameMap.put(8, 1736);
        levelToFameMap.put(9, 2456);
        levelToFameMap.put(10, 3354);
        levelToFameMap.put(11, 4450);
        levelToFameMap.put(12, 5764);
        levelToFameMap.put(13, 7316);
        levelToFameMap.put(14, 9126);
        levelToFameMap.put(15, 11214);
        levelToFameMap.put(16, 13600);
        levelToFameMap.put(17, 16304);
        levelToFameMap.put(18, 19346);
        levelToFameMap.put(19, 22746);
        levelToFameMap.put(20, 26524);
    }

    public int getLevelByFame(int xp) {
        int level = 1;

        for (Map.Entry<Integer, Integer> entry : levelToFameMap.entrySet()) {
            if (xp >= entry.getValue()) {
                level = entry.getKey();
            }
        }

        return level;
    }

    public int getFameByLevel(int level) {
        if (level <= 1 || level >= 20) {
            throw new IllegalArgumentException("Level is clamped between 1 and 20.");
        }

        return levelToFameMap.get(level);
    }

    public void displayLevelProgression(Player player) {
        RotMC.getInstance().getDataManager().getAccessor().getPlayerData()
                .loadSafe(player.getUniqueId())
                .syncLast(data -> {
                    PlayerClass selectedClass = data.getSelectedClass();
                    player.setLevel(selectedClass.getLevel());
                    player.setExp(getLevelProgressPercentage(selectedClass));
                })
                .execute();
    }

    public float getLevelProgressPercentage(PlayerClass playerClass) {
        int fame = playerClass.getFame(), level = playerClass.getLevel();

        if (level == 20) {
            return 0F;
        }

        float elapsed = playerClass.getFame() - getMinimumFame(playerClass.getLevel());
        float range = getMaximumFame(level) - getMinimumFame(level);

        return Math.min(elapsed / range, 1.0F);
    }

    public int getMinimumFame(int level) {
        if (level <= 1 || level >= 20) {
            throw new IllegalArgumentException("Level is clamped between 1 and 20.");
        }

        return levelToFameMap.get(level);
    }

    public int getMaximumFame(int level) {
        if (level <= 1 || level >= 20) {
            throw new IllegalArgumentException("Level is clamped between 1 and 20.");
        }

        return levelToFameMap.get(level + 1) - 1;
    }
}
