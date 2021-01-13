package me.kench.rotmc.database.playerdata;

import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.player.PlayerClass;
import me.kench.rotmc.player.RpgClass;
import me.kench.rotmc.player.stat.Stat;
import me.kench.rotmc.player.stat.Stats;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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

    public Player getPlayer() {
        return Bukkit.getPlayer(getUniqueId());
    }

    public List<PlayerClass> getClasses() {
        return classes;
    }

    public String getClassesAsJson() {
        JSONArray array = new JSONArray();

        for (PlayerClass clazz : getClasses()) {
            Stats potionStats = clazz.getStats().getPotionStats();

            // If the class we are currently looking at is currently in use, we need to ensure
            // that the inventory data is fresh.
            if (clazz.isSelected()) {
                Player player = Bukkit.getPlayer(getUniqueId());
                if (player != null) {
                    clazz.setInventory(player.getInventory());
                }
            }

            array.put(new JSONObject()
                    .put("uuid", clazz.getUniqueId())
                    .put("GameClass", clazz.getRpgClass().getName())
                    .put("selected", clazz.isSelected())
                    .put("xp", clazz.getFame())
                    .put("level", clazz.getLevel())
                    .put("stats", new JSONObject()
                            .put("health", potionStats.getStat(Stat.HEALTH))
                            .put("attack", potionStats.getStat(Stat.ATTACK))
                            .put("defense", potionStats.getStat(Stat.DEFENSE))
                            .put("speed", potionStats.getStat(Stat.SPEED))
                            .put("dodge", potionStats.getStat(Stat.EVASION))
                            .put("vitality", potionStats.getStat(Stat.VITALITY))
                    )
                    .put("inv", clazz.getInventory())
            );
        }

        return array.toString();
    }

    public PlayerClass getSelectedClass() {
        return classes.stream().filter(PlayerClass::isSelected).findFirst().orElse(null);
    }

    public boolean hasSelectedClass() {
        return getSelectedClass() != null;
    }

    public boolean changeSelectedClass(UUID classUniqueId, boolean isNew) {
        Player player = getPlayer();
        if (player == null) return false;

        PlayerClass newClass = classes.stream().filter(it -> it.getUniqueId().equals(classUniqueId)).findFirst().orElse(null);
        if (newClass == null) return false;

        // Save current inventory before switching classes.
        PlayerClass currentClass = getSelectedClass();
        if (currentClass != null) {
            currentClass.setInventory(player.getInventory());
        }

        classes.forEach(it -> it.setSelected(false));
        newClass.setSelected(true);

        // TODO: this should probably be moved to the call context later
        RotMcPlugin.getInstance().getLevelProgression().displayLevelProgression(player);

        ensureClassPermissions();

        if (currentClass != null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + getPlayer().getName());
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sudo " + getPlayer().getName() + " rp");
        }

        // Restore saved inventory of new class
        PlayerInventory playerInventory = player.getInventory();
        Inventory newClassInventory = newClass.getInventory();

        if (newClassInventory != null) {
            for (int i = 0; i < 36; i++) {
                ItemStack item = newClassInventory.getContents()[i];
                if (item != null) {
                    getPlayer().getInventory().setItem(i, item);
                }
            }

            ItemStack offhand = newClassInventory.getContents()[40];
            if (offhand != null && offhand.getType() != Material.AIR) {
                playerInventory.setItemInOffHand(offhand);
            }

            ItemStack helmet = newClassInventory.getContents()[39];
            if (helmet != null && helmet.getType() != Material.AIR) {
                playerInventory.setHelmet(helmet);
            }

            ItemStack chest = newClassInventory.getContents()[38];
            if (chest != null && chest.getType() != Material.AIR) {
                playerInventory.setChestplate(chest);
            }

            ItemStack legs = newClassInventory.getContents()[37];
            if (legs != null && legs.getType() != Material.AIR) {
                playerInventory.setLeggings(legs);
            }

            ItemStack boots = newClassInventory.getContents()[36];
            if (boots != null && boots.getType() != Material.AIR) {
                getPlayer().getInventory().setBoots(boots);
            }
        }

        newClass.applyStats();

        if (isNew) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ultimatekits:kit " + newClass.getRpgClass().getName() + " " + player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give -s " + player.getName() + " starterhealth");
        }

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

    public int getOverallRank() {
        return getRankHuntress() + getRankKnight() + getRankWarrior() + getRankNecromancer() + getRankAssassin() + getRankRogue();
    }

    public me.kench.rotmc.session.PlayerSession getSession() {
        return RotMcPlugin.getInstance().getSessionManager().getSession(getUniqueId());
    }

    public void ensureClassPermissions() throws IllegalStateException {
        if (getSelectedClass() == null) return;

        RpgClass rpgClass = getSelectedClass().getRpgClass();

        UserManager lpUserManager = RotMcPlugin.getInstance().getLuckPerms().getUserManager();
        User lpUser = lpUserManager.getUser(getUniqueId());
        if (lpUser == null) {
            throw new IllegalStateException(String.format("Could not find LuckPerms profile for player with unique ID %s!", getUniqueId()));
        }

        // Remove class level permissions
        for (int i = 1; i <= 20; i++) {
            lpUser.data().remove(Node.builder(String.format("rotmc.level.{1-%d}", i)).build());
        }

        // Remove all possible class permissions
        for (String permission : RpgClass.getAllPermissions()) {
            lpUser.data().remove(Node.builder(permission).build());
        }

        // Add class permissions
        for (String permission : rpgClass.getPermissions()) {
            lpUser.data().add(Node.builder(permission).build());
        }

        lpUserManager.saveUser(lpUser);
    }
}
