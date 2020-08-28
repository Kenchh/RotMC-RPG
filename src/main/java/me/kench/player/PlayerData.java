package me.kench.player;

import me.kench.RotMC;
import me.kench.items.GameItem;
import me.kench.items.stats.EssenceType;
import me.kench.items.stats.essenceanimations.EssenceAnimation;
import me.kench.utils.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerData {

    Player p;
    public ArrayList<PlayerClass> classes;
    public int maxSlots;
    public PlayerClass currentClass;

    public PlayerClass clickedClass;

    public GameItem extractGameItem;
    public ItemStack extractor;

    public HashMap<EssenceType, EssenceAnimation> activeEssences = new HashMap<>();

    public PlayerData(Player player) {
        this.p = player;
        this.maxSlots = RotMC.getInstance().getDatabase().getMaxSlots(player.getUniqueId());
        this.classes = RotMC.getInstance().getDatabase().getPlayerClasses(player.getUniqueId());
        this.currentClass = RotMC.getInstance().getDatabase().getCurrentClass(player.getUniqueId());
    }

    public Player getPlayer() {
        return p;
    }

    public boolean hasClass() {
        return currentClass != null;
    }

    public PlayerClass getMainClass() {
        return currentClass;
    }

    public void assignPerms() {

        if(getMainClass() == null) return;

        PlayerClass pc = getMainClass();
        String classname = getMainClass().getData().getName();

        ArrayList<String> allPerms = new ArrayList<>();
        ArrayList<String> newPerms = new ArrayList<>();

        /* Delete old perms */
        allPerms.add("rotmc.weapon.sword rotmc.weapon.dagger rotmc.weapon.staff rotmc.weapon.bow");
        allPerms.add("rotmc.knight rotmc.warrior rotmc.huntress rotmc.necromancer rotmc.assassin rotmc.rogue");
        allPerms.add("rotmc.robe rotmc.leather rotmc.heavy");

        String lvl1 = "";
        for(int i=1;i<=5;i++) {
            lvl1 += ("rotmc.level." + i + " ");
        }
        allPerms.add(lvl1);

        String lvl2 = "";
        for(int i=6;i<=10;i++) {
            lvl2 += ("rotmc.level." + i + " ");
        }
        allPerms.add(lvl2);

        String lvl3 = "";
        for(int i=11;i<=15;i++) {
            lvl3 += ("rotmc.level." + i + " ");
        }
        allPerms.add(lvl3);

        String lvl4 = "";
        for(int i=16;i<=20;i++) {
            lvl4 += ("rotmc.level." + i + " ");
        }
        allPerms.add(lvl4);

        /* Add new Perms */
        if(classname.equalsIgnoreCase("Knight") || classname.equalsIgnoreCase("Warrior")) {
            newPerms.add("rotmc.weapon.sword" + " rotmc." + classname.toLowerCase() + " rotmc.heavy");
        }

        if(classname.equalsIgnoreCase("Rogue") || classname.equalsIgnoreCase("Assassin")) {
            newPerms.add("rotmc.weapon.dagger" + " rotmc." + classname.toLowerCase() + " rotmc.leather");
        }

        if(classname.equalsIgnoreCase("Huntress")) {
            newPerms.add("rotmc.weapon.bow" + " rotmc." + classname.toLowerCase() + " rotmc.leather");
        }

        if(classname.equalsIgnoreCase("Necromancer")) {
            newPerms.add("rotmc.weapon.staff" + " rotmc." + classname.toLowerCase() + " rotmc.robe");
        }

        String nlvl1 = "";
        for(int i=1;i<=pc.getLevel()/4;i++) {
            nlvl1 += ("rotmc.level." + i + " ");
        }
        if(nlvl1 != "")
            newPerms.add(nlvl1);

        String nlvl2 = "";
        for(int i=(pc.getLevel()/4) + 1;i<=(pc.getLevel()/2);i++) {
            nlvl2 += ("rotmc.level." + i + " ");
        }
        if(nlvl2 != "")
            newPerms.add(nlvl2);

        String nlvl3 = "";
        for(int i=(pc.getLevel()/2) + 1;i<=((pc.getLevel()/4) * 3);i++) {
            nlvl3 += ("rotmc.level." + i + " ");
        }
        if(nlvl3 != "")
            newPerms.add(nlvl3);

        String nlvl4 = "";
        for(int i=((pc.getLevel()/4) * 3) + 1;i<=pc.getLevel();i++) {
            nlvl4 += ("rotmc.level." + i + " ");
        }
        if(nlvl4 != "")
            newPerms.add(nlvl4);

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                i++;
                if(i > allPerms.size()) {
                    this.cancel();

                    new BukkitRunnable() {
                        int ii = 0;
                        @Override
                        public void run() {
                            ii++;
                            if(ii > newPerms.size()) {
                                this.cancel();
                                return;
                            }
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "manuaddp " + p.getName() + " " + newPerms.get(ii - 1));
                        }
                    }.runTaskTimer(RotMC.getInstance(), 1L, 2L);

                    return;
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "manudelp " + p.getName() + " " + allPerms.get(i - 1));
            }
        }.runTaskTimer(RotMC.getInstance(), 1L, 2L);

    }

    public void selectClass(PlayerClass pclass) {

        for(PlayerClass pc : classes) {
            pc.selected = false;
        }

        PlayerClass old = currentClass;

        currentClass = pclass;
        RotMC.getInstance().getLevelProgression().displayLevelProgression(p);
        pclass.selected = true;

        pclass.applyStats();
        assignPerms();

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + getPlayer().getName());

        RotMC.getInstance().getDatabase().update(p, old);

        getPlayer().getInventory().clear();

        if(old != null) {
            String invjson = RotMC.getInstance().getDatabase().getInventory(p.getUniqueId(), pclass.getUuid());

            if(invjson.equalsIgnoreCase("") || invjson == null) return;

            Inventory inv = JsonParser.fromBase64(invjson);

            if(inv != null) {
                for (int i = 0; i < 36; i++) {
                    ItemStack item = inv.getContents()[i];

                    if (item != null && item.getType() != Material.AIR)
                        getPlayer().getInventory().setItem(i, item);
                }

                ItemStack offhand = inv.getContents()[40];
                if(offhand != null && offhand.getType() != Material.AIR) {
                    getPlayer().getInventory().setItemInOffHand(offhand);
                }

                ItemStack helmet = inv.getContents()[39];
                if(helmet != null && helmet.getType() != Material.AIR) {
                    getPlayer().getInventory().setHelmet(helmet);
                }

                ItemStack chest = inv.getContents()[38];
                if(chest != null && chest.getType() != Material.AIR) {
                    getPlayer().getInventory().setChestplate(chest);
                }

                ItemStack legs = inv.getContents()[37];
                if(legs != null && legs.getType() != Material.AIR) {
                    getPlayer().getInventory().setLeggings(legs);
                }

                ItemStack boots = inv.getContents()[36];
                if(boots != null && boots.getType() != Material.AIR) {
                    getPlayer().getInventory().setBoots(boots);
                }

            }

        }
    }

}
