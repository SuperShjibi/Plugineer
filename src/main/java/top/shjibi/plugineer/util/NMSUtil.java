package top.shjibi.plugineer.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

/**
 * 与NMS相关的工具类
 */
public final class NMSUtil {

    private NMSUtil() {
    }

    /**
     * 获取当前服务器运行的MC的版本
     */
    @NotNull
    public static String getVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf(".") + 1);
    }

    /**
     * 获取NM类
     */
    @Nullable
    public static Class<?> getNMClass(@NotNull String name) {
        try {
            return Class.forName("net.minecraft." + name);
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().log(Level.WARNING, "无法找到类! (" + name + ")");
            return null;
        }
    }

    /**
     * 获取NMS类
     */
    @Nullable
    public static Class<?> getNMSClass(@NotNull String name) {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().log(Level.WARNING, "无法找到类! (" + name + ")");
            return null;
        }
    }

    /**
     * 获取CraftBukkit类
     */
    @Nullable
    public static Class<?> getCraftBukkitClass(@NotNull String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().log(Level.WARNING, "无法找到类! (" + name + ")");
            return null;
        }
    }

    /**
     * 给玩家发包
     */
    public static boolean sendPacket(@NotNull Player p, @NotNull Object packet) {
        try {
            Class<?> packetClass = Class.forName("net.minecraft.network.protocol.Packet");
            if (!packetClass.isAssignableFrom(packet.getClass())) return false;
            Object entityPlayer = p.getClass().getMethod("getHandle").invoke(p);
            Object connection = entityPlayer.getClass().getField("b").get(entityPlayer);
            connection.getClass().getMethod("a", packetClass).invoke(connection, packet);
            return true;
        } catch (ReflectiveOperationException e) {
            Bukkit.getLogger().log(Level.WARNING, "无法发送包: " + packet.getClass().getSimpleName());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取NMS的ItemStack
     */
    @Nullable
    public static Object asNMSCopy(@NotNull ItemStack item) {
        try {
            Class<?> itemClass = getCraftBukkitClass("inventory.CraftItemStack");
            if (itemClass == null) throw new ReflectiveOperationException();
            return itemClass.getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
        } catch (ReflectiveOperationException e) {
            Bukkit.getLogger().log(Level.WARNING, "无法获取NMS物品");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取物品NBT(字符串)
     */
    @Nullable
    public static String getItemNBT(@NotNull ItemStack item) {
        try {
            Object nmsItem = asNMSCopy(item);
            if (nmsItem == null) return null;
            Object nbt = nmsItem.getClass().getMethod("u").invoke(nmsItem);
            return (String) nbt.getClass().getMethod("toString").invoke(nbt);
        } catch (ReflectiveOperationException | NullPointerException e) {
            Bukkit.getLogger().log(Level.WARNING, "无法获取物品NBT");
            e.printStackTrace();
            return null;
        }
    }
}
