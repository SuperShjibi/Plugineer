package top.shjibi.plugineer.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/** 与NMS相关的工具类 */
public final class NMSUtil {

    private NMSUtil() {}

    /** 获取当前服务器运行的MC的版本 */
    public static String getVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf(".") + 1);
    }

    /** 获取NM类 */
    public static Class<?> getNMClass(String name) {
        try {
            return Class.forName("net.minecraft." + name);
        } catch (ClassNotFoundException e) {
            System.out.println("无法找到类! (" + name + ")");
            return null;
        }
    }

    /** 获取NMS类 */
    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            System.out.println("无法找到类! (" + name + ")");
            return null;
        }
    }

    /** 获取CraftBukkit类 */
    public static Class<?> getCraftBukkitClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            System.out.println("无法找到类! (" + name + ")");
            return null;
        }
    }

    /** 给玩家发包 */
    public static boolean sendPacket(Player p, Object packet) {
        try {
            Class<?> packetClass = Class.forName("net.minecraft.network.protocol.Packet");
            if (!packetClass.isAssignableFrom(packet.getClass())) return false;
            Object entityPlayer = p.getClass().getMethod("getHandle").invoke(p);
            Object connection = entityPlayer.getClass().getField("b").get(entityPlayer);
            connection.getClass().getMethod("a", packetClass).invoke(connection, packet);
            return true;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 获取NMS的ItemStack */
    public static Object asNMSCopy(ItemStack item) {
        try {
            Class<?> itemClass = getCraftBukkitClass("inventory.CraftItemStack");
            if (itemClass == null) throw new ReflectiveOperationException();
            return itemClass.getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("无法获取NMSCopy");
        }
    }

    /** 获取物品NBT(字符串) */
    public static String getItemNBT(ItemStack item) {
        try {
            Object nmsItem = asNMSCopy(item);
            Object nbt = nmsItem.getClass().getMethod("u").invoke(nmsItem);
            return (String) nbt.getClass().getMethod("toString").invoke(nbt);
        } catch (ReflectiveOperationException | NullPointerException e) {
            return null;
        }
    }
}
