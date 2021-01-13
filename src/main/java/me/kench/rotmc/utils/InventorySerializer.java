package me.kench.rotmc.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InventorySerializer {
    public static String toBase64(final Player player) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            final BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
            bukkitObjectOutputStream.writeInt(player.getInventory().getSize() + 4);

            for (int i = 0; i < player.getInventory().getSize(); ++i) {
                bukkitObjectOutputStream.writeObject((Object) player.getInventory().getItem(i));
            }

            for (int j = 0; j < 4; ++j) {
                bukkitObjectOutputStream.writeObject((Object) player.getInventory().getArmorContents()[j]);
            }

            bukkitObjectOutputStream.close();

            return Base64Coder.encodeLines(byteArrayOutputStream.toByteArray());
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to save item stacks.", ex);
        }
    }

    public static Inventory fromBase64(final String inventoryJson) {
        final BukkitObjectInputStream bukkitObjectInputStream;

        try {
            bukkitObjectInputStream = new BukkitObjectInputStream(new ByteArrayInputStream(Base64Coder.decodeLines(inventoryJson)));

            final Inventory inventory = Bukkit.getServer().createInventory(null, 54);

            for (int int1 = bukkitObjectInputStream.readInt(), i = 0; i < int1; ++i) {
                inventory.setItem(i, (ItemStack) bukkitObjectInputStream.readObject());
            }

            bukkitObjectInputStream.close();

            return inventory;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
