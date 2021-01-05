package me.kench.utils;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JsonParser {

    public static String toBase64(final Player player) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream((OutputStream) byteArrayOutputStream);
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

    public static Inventory fromBase64(final String s) {

        final BukkitObjectInputStream bukkitObjectInputStream;
        try {
            bukkitObjectInputStream = new BukkitObjectInputStream((InputStream) new ByteArrayInputStream(Base64Coder.decodeLines(s)));
            final Inventory inventory = Bukkit.getServer().createInventory((InventoryHolder) null, 54);
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
