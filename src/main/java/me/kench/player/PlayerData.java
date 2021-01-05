package me.kench.player;

import me.kench.RotMC;
import me.kench.items.GameItem;
import me.kench.items.stats.EssenceType;
import me.kench.items.stats.essenceanimations.EssenceAnimation;
import me.kench.utils.JsonParser;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerData {

    Player p;
    public ArrayList<PlayerClass> classes = new ArrayList<>();
    public int maxSlots = 2;
    public PlayerClass currentClass;

    public PlayerClass clickedClass;

    public GameItem extractGameItem;
    public ItemStack extractor;

    public HashMap<EssenceType, EssenceAnimation> activeEssences = new HashMap<>();

    public String lastKiller = "";
    public String lastDamage = "";

    public ArrayList<Block> goldblocks = new ArrayList<>();
    public ArrayList<Block> iceblocks = new ArrayList<>();
    public ArrayList<Block> obbyblocks = new ArrayList<>();

    public BukkitTask task;

    public GameItem gameItem;

    public PlayerData(Player player) {
        this.p = player;
    }

    public PlayerData(Player player, int maxSlots, ArrayList<PlayerClass> classes, PlayerClass currentClass) {
        this.p = player;
        this.maxSlots = maxSlots;
        this.classes = classes;
        this.currentClass = currentClass;
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

    public ArrayList<PlayerClass> getClasses() {
        return classes;
    }

    public void assignPerms() {

        if (getMainClass() == null) return;

        PlayerClass pc = getMainClass();
        String classname = getMainClass().getData().getName();

        ArrayList<String> allPerms = new ArrayList<>();
        ArrayList<String> newPerms = new ArrayList<>();

        /* Delete old perms */
        allPerms.add("rotmc.weapon.sword");
        allPerms.add("rotmc.weapon.dagger");
        allPerms.add("rotmc.weapon.staff");
        allPerms.add("rotmc.weapon.bow");

        allPerms.add("rotmc.knight");
        allPerms.add("rotmc.warrior");
        allPerms.add("rotmc.huntress");
        allPerms.add("rotmc.necromancer");
        allPerms.add("rotmc.assassin");
        allPerms.add("rotmc.rogue");

        /*
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
        */

        /* Add new Perms */
        if (classname.equalsIgnoreCase("Knight") || classname.equalsIgnoreCase("Warrior")) {
            newPerms.add("rotmc.weapon.sword");
            newPerms.add("rotmc." + classname.toLowerCase());
        }

        if (classname.equalsIgnoreCase("Rogue") || classname.equalsIgnoreCase("Assassin")) {
            newPerms.add("rotmc.weapon.dagger");
            newPerms.add("rotmc." + classname.toLowerCase());
        }

        if (classname.equalsIgnoreCase("Huntress")) {
            newPerms.add("rotmc.weapon.bow");
            newPerms.add("rotmc." + classname.toLowerCase());
        }

        if (classname.equalsIgnoreCase("Necromancer")) {
            newPerms.add("rotmc.weapon.staff");
            newPerms.add("rotmc." + classname.toLowerCase());
        }

        /*
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
        */

        new BukkitRunnable() {
            @Override
            public void run() {

                User user = RotMC.getInstance().getApi().getUserManager().loadUser(p.getUniqueId()).join();

                for (int i = 1; i <= 20; i++)
                    user.data().remove(Node.builder("rotmc.level.{1-" + i + "}").build());
                for (String delperm : allPerms) {
                    user.data().remove(Node.builder(delperm).build());
                }

                user.data().add(Node.builder("rotmc.level.{1-" + pc.getLevel() + "}").build());
                for (String addperm : newPerms) {
                    user.data().add(Node.builder(addperm).build());
                }

                RotMC.getInstance().getApi().getUserManager().saveUser(user);

            }
        }.runTaskLater(RotMC.getInstance(), 2L);

    }

    public void selectClass(PlayerClass pclass, boolean newclass) {

        for (PlayerClass pc : classes) {
            pc.selected = false;
        }

        PlayerClass old = currentClass;

        currentClass = pclass;
        RotMC.getInstance().getLevelProgression().displayLevelProgression(p);
        pclass.selected = true;

        assignPerms();

        if (old != null && old.getPlayer() != null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + getPlayer().getName());
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sudo " + getPlayer().getName() + " rp");
        }

        RotMC.getInstance().getSqlManager().update(p, old);

        getPlayer().getInventory().clear();

        if (old != null && old.getPlayer() != null) {
            String inv64 = RotMC.getInstance().getSqlManager().getInventory(p.getUniqueId(), pclass.getUuid());

            if (!(inv64 == null || inv64.equalsIgnoreCase(""))) {

                Inventory inv = JsonParser.fromBase64(inv64);

                if (inv != null) {
                    for (int i = 0; i < 36; i++) {
                        ItemStack item = inv.getContents()[i];

                        if (item != null && item.getType() != Material.AIR)
                            getPlayer().getInventory().setItem(i, item);
                    }

                    ItemStack offhand = inv.getContents()[40];
                    if (offhand != null && offhand.getType() != Material.AIR) {
                        getPlayer().getInventory().setItemInOffHand(offhand);
                    }

                    ItemStack helmet = inv.getContents()[39];
                    if (helmet != null && helmet.getType() != Material.AIR) {
                        getPlayer().getInventory().setHelmet(helmet);
                    }

                    ItemStack chest = inv.getContents()[38];
                    if (chest != null && chest.getType() != Material.AIR) {
                        getPlayer().getInventory().setChestplate(chest);
                    }

                    ItemStack legs = inv.getContents()[37];
                    if (legs != null && legs.getType() != Material.AIR) {
                        getPlayer().getInventory().setLeggings(legs);
                    }

                    ItemStack boots = inv.getContents()[36];
                    if (boots != null && boots.getType() != Material.AIR) {
                        getPlayer().getInventory().setBoots(boots);
                    }

                }
            }
        }

        pclass.applyStats();

        if (newclass) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ultimatekits:kit " + pclass.getData().getName() + " " + p.getName());
            if (old == null || old.getPlayer() == null)
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give -s " + getPlayer().getName() + " starterhealth");
        }

    }

}
