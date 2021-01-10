package me.kench.gui.extractor;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.kench.RotMC;
import me.kench.gui.extractor.item.ExtractorGuiCancelButton;
import me.kench.items.*;
import me.kench.items.stats.Essence;
import me.kench.utils.ItemUtils;
import me.kench.utils.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.function.Consumer;

public class ExtractorGui {
    private final StaticPane cancelButton;
    private final Consumer<InventoryClickEvent> eventHandler;
    private Inventory inv;

    public ExtractorGui() {
        StaticPane cancelButton = new StaticPane(0, 2, 1, 1);
        cancelButton.addItem(new ExtractorGuiCancelButton(), 0, 0);
        this.cancelButton = cancelButton;

        eventHandler = event -> {

        };
    }

    public void display(Player player, GameItem item, ItemStack extractor) {
        RotMC.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(player.getUniqueId())
                .async(data -> {
                    ChestGui gui = new ChestGui(3, "Choose an item to extract");

                    OutlinePane itemPane = new OutlinePane(1, 1, 7, 1);
                    GameItemStats gameItemStats = item.getStats();

                    gameItemStats.gems.forEach(gem -> itemPane.addItem(gem.getGuiItem(eventHandler)));

                    if (gameItemStats.getRune() != null) {
                        itemPane.addItem(gameItemStats.getRune().getGuiItem(eventHandler));
                    }

                    if (gameItemStats.getEssence() != null) {
                        itemPane.addItem(gameItemStats.getEssence().getGuiItem(eventHandler));
                    }

                    gui.addPane(cancelButton);

                    return gui;
                })
                .syncLast(gui -> gui.show(player))
                .execute();
    }

    public ExtractorGui(Player p, GameItem gameItem, ItemStack extractor) {
        if (gameItem.getStats().getEssence() != null) {
            Essence e = gameItem.getStats().getEssence();

            ItemStack essence = new ItemStack(Material.CARROT_ON_A_STICK);
            ItemMeta meta = essence.getItemMeta();
            meta.setCustomModelData(e.getType().getModelData());
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
