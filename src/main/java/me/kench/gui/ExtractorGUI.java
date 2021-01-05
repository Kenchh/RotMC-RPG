package me.kench.gui;

import me.kench.RotMC;
import me.kench.items.EssenceItem;
import me.kench.items.GameItem;
import me.kench.items.GemItem;
import me.kench.items.RuneItem;
import me.kench.items.stats.Essence;
import me.kench.items.stats.Gem;
import me.kench.items.stats.Rune;
import me.kench.player.PlayerData;
import me.kench.utils.ItemUtils;
import me.kench.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ExtractorGUI implements Listener {

    private Inventory inv;

    public ExtractorGUI() {

    }

    public ExtractorGUI(Player p, GameItem gameItem, ItemStack extractor) {
        inv = Bukkit.createInventory(null, 9 * 3, "Choose an item to extract");

        PlayerData pd = RotMC.getPlayerData(p);
        pd.extractGameItem = gameItem;
        pd.extractor = extractor;

        ItemStack red = new ItemStack(Material.BARRIER);
        ItemMeta redmeta = red.getItemMeta();
        redmeta.setDisplayName(ChatColor.RED + "Cancel");
        red.setItemMeta(redmeta);

        inv.setItem(0 + 9 * 2, red);

        ItemStack black = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta blackmeta = black.getItemMeta();
        blackmeta.setDisplayName(" ");
        black.setItemMeta(blackmeta);

        ItemStack green = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta greenmeta = green.getItemMeta();
        greenmeta.setDisplayName(" ");
        green.setItemMeta(greenmeta);

        ItemStack blue = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta bluemeta = blue.getItemMeta();
        bluemeta.setDisplayName(" ");
        blue.setItemMeta(bluemeta);

        ItemStack pink = new ItemStack(Material.PINK_STAINED_GLASS_PANE);
        ItemMeta pinkmeta = pink.getItemMeta();
        pinkmeta.setDisplayName(" ");
        pink.setItemMeta(pinkmeta);

        for (int i = 0; i < gameItem.getStats().gems.size(); i++) {
            Gem g = gameItem.getStats().gems.get(i);

            ItemStack gem = new ItemStack(Material.CARROT_ON_A_STICK);
            ItemMeta meta = gem.getItemMeta();
            meta.setCustomModelData(g.getType().getModeldata() + g.getLevel());
            meta.setDisplayName(g.getType().getPrefix() + g.getType().getName() + " " + TextUtils.getRomanFromNumber(g.getLevel()));
            gem.setItemMeta(meta);

            inv.setItem(0 + 9 + 1 + i, gem);
        }

        if (gameItem.getStats().getRune() != null) {
            Rune r = gameItem.getStats().getRune();

            ItemStack rune = new ItemStack(Material.CARROT_ON_A_STICK);
            ItemMeta meta = rune.getItemMeta();
            meta.setCustomModelData(r.getType().getModeldata());
            meta.setDisplayName(r.getType().getPrefix() + r.getType().getName());
            rune.setItemMeta(meta);

            inv.setItem(0 + 9 + 1 + 4, rune);
        }


        if (gameItem.getStats().getEssence() != null) {
            Essence e = gameItem.getStats().getEssence();

            ItemStack essence = new ItemStack(Material.CARROT_ON_A_STICK);
            ItemMeta meta = essence.getItemMeta();
            meta.setCustomModelData(e.getType().getModeldata());
            meta.setDisplayName(e.getType().getPrefix() + e.getType().getName());
            essence.setItemMeta(meta);

            inv.setItem(0 + 9 + 1 + 6, essence);
        }


    }

    public Inventory getInv() {
        return inv;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView() != null && e.getView().getTitle() != "Choose an item to extract") {
            return;
        }

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;

        if (e.getCurrentItem().getType() == Material.BARRIER) {
            e.getWhoClicked().closeInventory();
            return;
        }

        ItemStack item = e.getCurrentItem();

        if (!e.getCurrentItem().hasItemMeta() || !e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        boolean isGem = ItemUtils.isGem(e.getCurrentItem().getItemMeta().getDisplayName());
        boolean isRune = ItemUtils.isRune(e.getCurrentItem().getItemMeta().getDisplayName());
        boolean isEssence = ItemUtils.isEssence(e.getCurrentItem().getItemMeta().getDisplayName());

        if (!isGem && !isRune && !isEssence) return;

        Player p = (Player) e.getWhoClicked();
        PlayerData pd = RotMC.getPlayerData(p);

        if (isGem) {
            ItemMeta meta = item.getItemMeta();

            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Success Chance:" + ChatColor.GOLD + " 50%",
                    ChatColor.GRAY + "▸ YYY:" + ChatColor.GOLD + " ZZZ"
            ));

            item.setItemMeta(meta);

            GemItem gem = new GemItem(item);
            gem.successChance = 50;
            gem.update();

            pd.extractGameItem.getStats().gems.remove(e.getSlot() - 10);
            pd.extractGameItem.getStats().gemsockets++;
            pd.extractGameItem.update();

            for (ItemStack it : p.getInventory().getContents()) {
                if (!it.hasItemMeta() || !it.getItemMeta().hasDisplayName()) continue;
                if (it.getItemMeta().getDisplayName().contains("Extractor")) {
                    it.setAmount(it.getAmount() - 1);
                    break;
                }
            }

            p.sendMessage(ChatColor.GREEN + "You have successfully extracted " + gem.getGem().getType().getPrefix() + gem.getGem().getType().getName() + " " + TextUtils.getRomanFromNumber(gem.getGem().getLevel()) + "!");
            p.playSound(p.getLocation(), Sound.ENTITY_PUFFER_FISH_BLOW_UP, 1F, 0.8F);
            p.getInventory().addItem(gem.getItem());
            p.closeInventory();
            return;
        }

        if (isRune) {
            ItemMeta meta = item.getItemMeta();

            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Success Chance:" + ChatColor.GOLD + " 50%",
                    ChatColor.GRAY + "▸ Permanent Effect:" + ChatColor.GOLD + " ZZZ"
            ));

            item.setItemMeta(meta);

            RuneItem rune = new RuneItem(item);
            rune.successChance = 50;
            rune.update();

            pd.extractGameItem.getStats().setRune(null);
            pd.extractGameItem.getStats().hasRuneSocket = true;
            pd.extractGameItem.update();

            for (ItemStack it : p.getInventory().getContents()) {
                if (!it.hasItemMeta() || !it.getItemMeta().hasDisplayName()) continue;
                if (it.getItemMeta().getDisplayName().contains("Extractor")) {
                    it.setAmount(it.getAmount() - 1);
                    break;
                }
            }

            p.sendMessage(ChatColor.GREEN + "You have successfully extracted " + rune.getRune().getType().getPrefix() + rune.getRune().getType().getName() + "!");
            p.playSound(p.getLocation(), Sound.ENTITY_PUFFER_FISH_BLOW_UP, 1F, 0.8F);
            p.getInventory().addItem(rune.getItem());
            p.closeInventory();
        }

        if (isEssence) {

            ItemMeta meta = item.getItemMeta();

            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Success Chance:" + ChatColor.GOLD + " 100%",
                    ChatColor.GRAY + "▸ Vanity Effect:" + ChatColor.GOLD + " ZZZ"
            ));

            item.setItemMeta(meta);

            EssenceItem essenceItem = new EssenceItem(item);
            essenceItem.update();

            pd.extractGameItem.getStats().setEssence(null);
            pd.extractGameItem.getStats().hasEssenceSocket = true;
            pd.extractGameItem.update();

            for (ItemStack it : p.getInventory().getContents()) {
                if (!it.hasItemMeta() || !it.getItemMeta().hasDisplayName()) continue;
                if (it.getItemMeta().getDisplayName().contains("Extractor")) {
                    it.setAmount(it.getAmount() - 1);
                    break;
                }
            }

            p.sendMessage(ChatColor.GREEN + "You have successfully extracted " + essenceItem.getEssence().getType().getPrefix() + essenceItem.getEssence().getType().getName() + "!");
            p.playSound(p.getLocation(), Sound.ENTITY_PUFFER_FISH_BLOW_UP, 1F, 0.8F);
            p.getInventory().addItem(essenceItem.getItem());
            p.closeInventory();
        }

    }

}
