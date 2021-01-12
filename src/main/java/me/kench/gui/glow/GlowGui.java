package me.kench.gui.glow;

import com.andrebreves.tuple.Tuple2;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.glaremasters.guilds.guild.Guild;
import me.kench.RotMC;
import me.kench.database.playerdata.PlayerDataDam;
import me.kench.gui.glow.item.GlowGuiCancelButton;
import me.kench.gui.glow.item.GlowGuiGlowButton;
import me.kench.items.ItemBuilder;
import me.kench.player.PlayerClass;
import me.kench.utils.GlowType;
import me.kench.utils.GlowUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Set;

public class GlowGui {
    private final StaticPane background;
    private final StaticPane cancelButton;

    public GlowGui() {
        StaticPane background = new StaticPane(0, 0, 9, 6);

        GuiItem blackBg = new GuiItem(ItemBuilder.create(Material.BLACK_STAINED_GLASS_PANE).name(" ").build(), event -> event.setCancelled(true));
        GuiItem pinkBg = new GuiItem(ItemBuilder.create(Material.PINK_STAINED_GLASS_PANE).name(" ").build(), event -> event.setCancelled(true));

        int bgY = 0;
        for (int i = 0; i < 9; i++) {
            if (bgY % 2 == 0) {
                background.addItem(blackBg, i, bgY);
            } else {
                background.addItem(pinkBg, 0, bgY);
                background.addItem(pinkBg, 8, bgY);
                bgY++;
                continue;
            }

            if (i == 8) {
                if (bgY == 4) {
                    break;
                } else {
                    i = 0;
                    bgY++;
                }
            }
        }

        this.background = background;

        StaticPane cancelButton = new StaticPane(0, 5, 1, 1);
        cancelButton.addItem(new GlowGuiCancelButton(), 0, 0);
        this.cancelButton = cancelButton;
    }

    public void display(Player player) {
        RotMC.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(player.getUniqueId())
                .async(data -> {
                    ChestGui gui = new ChestGui(6, "Select your glow color");

                    PlayerDataDam dam = RotMC.getInstance().getDataManager().getPlayerData();
                    Set<PlayerClass> topClasses = dam.loadTop10Classes().keySet();
                    Set<Guild> topGuilds = dam.loadTop10Guilds().keySet();

                    StaticPane glowPane = new StaticPane(2, 1, 5, 5);
                    for (GlowType glowType : GlowType.values()) {
                        Tuple2<Integer, Integer> relativeGuiPosition = glowType.getRelativeGuiPosition();

                        glowPane.addItem(
                                new GlowGuiGlowButton(
                                        data,
                                        glowType,
                                        GlowUtils.isPermitted(
                                                data,
                                                glowType,
                                                topClasses,
                                                topGuilds
                                        )
                                ),
                                relativeGuiPosition.v1(),
                                relativeGuiPosition.v2()
                        );
                    }

                    gui.addPane(background);
                    gui.addPane(glowPane);
                    gui.addPane(cancelButton);

                    return gui;
                })
                .syncLast(gui -> gui.show(player))
                .execute();
    }
}
