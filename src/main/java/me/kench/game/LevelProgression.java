package me.kench.game;

import me.kench.RotMC;
import me.kench.player.PlayerClass;
import me.kench.player.PlayerData;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class LevelProgression {

    /* Key -> Level; Value -> XP */
    public HashMap<Integer, Integer> levelxp = new HashMap<Integer, Integer>();

    public LevelProgression() {

        int x = 10;
        int y = 8;
        int z = 16;

        levelxp.put(1, x + y + z);
        System.out.println("----------->>>> " + 1 + " | " + (x+y+z) + " | " + (x+y+z));

        /* i = level */
        for(int i = 2; i <= 19; i++) {
            levelxp.put(i, x*i*i + y*i + z + levelxp.get(i-1));
            System.out.println("----------->>>> " + i + " | " + (x*i*i + y*i + z) + " | " + levelxp.get(i));
        }

    }

    public boolean hasLeveledUp(Player p, int leveltocomparewith) {
        PlayerData pd = RotMC.getPlayerData(p);

        if(pd.getMainClass().getLevel() < leveltocomparewith) {
            return true;
        }

        return false;

    }

    public int getXPByLevel(int level) {
        if(level > 1) {
            return levelxp.get(level - 1);
        } else {
            return 0;
        }
    }

    public void displayLevelProgression(Player p) {

        PlayerClass pc = RotMC.getPlayerData(p).getMainClass();

        /*
        for(ItemStack item : p.getInventory().getContents()) {
            if(item == null || item.getType() == Material.AIR || !item.hasItemMeta() || !item.getItemMeta().hasLore()) continue;

            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();

            int i = 0;
            for(String s : item.getItemMeta().getLore()) {

                if(s.contains("Player Class:")) {
                    String gc = TextUtils.getLastWord(s, 0);

                    String tick = pc.getData().getName().equalsIgnoreCase(gc) ? ChatColor.GREEN + ChatColor.BOLD.toString() + "✔" + ChatColor.RESET + ChatColor.GREEN
                            : ChatColor.RED + ChatColor.BOLD.toString() + "✖" + ChatColor.RESET + ChatColor.RED;
                    lore.set(i, ChatColor.RESET + tick + " Player Class: " + gc);
                }

                if(s.contains("Player Level:")) {
                    String level = TextUtils.getLastWord(s, 1);

                    String tick = pc.getLevel() >= Integer.parseInt(level) ? ChatColor.GREEN + ChatColor.BOLD.toString() + "✔" + ChatColor.RESET + ChatColor.GREEN
                            : ChatColor.RED + ChatColor.BOLD.toString() + "✖" + ChatColor.RESET + ChatColor.RED;
                    lore.set(i, ChatColor.RESET + tick + " Player Level: " + level + "+");
                }

                i++;
            }

            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        */

        p.setLevel(pc.getLevel());

        if(pc.getLevel() == 20) {
            p.setExp(1);
        } else {
            p.setExp(getLevelProgressPercentage(pc));
        }

    }

    public float getLevelProgressPercentage(PlayerClass pc) {

        if(pc.getLevel() == 20) {
            return 0F;
        }

        int range = getMaxXP(pc.getLevel()) - getMinXP(pc.getLevel());
        int xpInLevelRange = pc.getXp() - getMinXP(pc.getLevel());

        float percentage = ((100 / ((float) range)) * xpInLevelRange);

        return (percentage / 100);
    }

    public int getMinXP(int level) {
        if(level > 1) {
            return levelxp.get(level - 1);
        } else {
            return 0;
        }
    }

    public int getMaxXP(int level) {
        if(level >= 1 && level < 20) {
            return levelxp.get(level);
        }
        return -1;
    }

    public int getLevelByXP(int xp) {
        for(int level : levelxp.keySet()) {

            int lastcum = 0;
            int newcum = 0;

            if(level == 1) {
                lastcum = 0;
            } else {
                lastcum = levelxp.get(level - 1);
            }

            newcum = levelxp.get(level);

            if(xp >= lastcum && xp < newcum) {
                return level;
            } else if(xp >= 26524){
                return 20;
            }

        }
        return -1;
    }


}
