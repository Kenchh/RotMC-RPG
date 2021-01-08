package me.kench.game;

import me.kench.RotMC;
import me.kench.player.PlayerClass;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class LevelProgression {
    /* Key -> Level; Value -> Fame */
    public Map<Integer, Long> levelToFameMap;

    public LevelProgression() {
        // Level progression formula:
        // x = 10, y = 8, z = 16
        // Level 2: (x + y + z) = 34
        // Level 3-20: ((x * currentLevel^2) + (y * currentLevel) + z) + previousLevelFame

        levelToFameMap = new HashMap<>();
        levelToFameMap.put(1, 0L);
        levelToFameMap.put(2, 34L);
        levelToFameMap.put(3, 106L);
        levelToFameMap.put(4, 236L);
        levelToFameMap.put(5, 444L);
        levelToFameMap.put(6, 750L);
        levelToFameMap.put(7, 1174L);
        levelToFameMap.put(8, 1736L);
        levelToFameMap.put(9, 2456L);
        levelToFameMap.put(10, 3354L);
        levelToFameMap.put(11, 4450L);
        levelToFameMap.put(12, 5764L);
        levelToFameMap.put(13, 7316L);
        levelToFameMap.put(14, 9126L);
        levelToFameMap.put(15, 11214L);
        levelToFameMap.put(16, 13600L);
        levelToFameMap.put(17, 16304L);
        levelToFameMap.put(18, 19346L);
        levelToFameMap.put(19, 22746L);
        levelToFameMap.put(20, 26524L);
    }

    public int getLevelByFame(long fame) {
        int level = 1;

        for (Map.Entry<Integer, Long> entry : levelToFameMap.entrySet()) {
            if (fame >= entry.getValue()) {
                level = entry.getKey();
            }
        }

        return level;
    }

    public long getFameByLevel(int level) {
        if (level <= 1 || level >= 20) {
            throw new IllegalArgumentException("Level is clamped between 1 and 20.");
        }

        return levelToFameMap.get(level);
    }

    public void displayLevelProgression(Player player) {
        RotMC.getInstance().getDataManager().getPlayerData()
                .loadSafe(player.getUniqueId())
                .syncLast(data -> {
                    PlayerClass selectedClass = data.getSelectedClass();
                    player.setLevel(selectedClass.getLevel());
                    player.setExp(getLevelProgressPercentage(selectedClass));
                })
                .execute();
    }

    public float getLevelProgressPercentage(PlayerClass playerClass) {
        long fame = playerClass.getFame();
        int level = playerClass.getLevel();

        if (level == 20) {
            return 0F;
        }

        float elapsed = playerClass.getFame() - getMinimumFame(playerClass.getLevel());
        float range = getMaximumFame(level) - getMinimumFame(level);

        return Math.min(elapsed / range, 1.0F);
    }

    public long getMinimumFame(int level) {
        if (level <= 1 || level >= 20) {
            throw new IllegalArgumentException("Level is clamped between 1 and 20.");
        }

        return levelToFameMap.get(level);
    }

    public long getMaximumFame(int level) {
        if (level <= 1 || level >= 20) {
            throw new IllegalArgumentException("Level is clamped between 1 and 20.");
        }

        return levelToFameMap.get(level + 1) - 1;
    }
}
