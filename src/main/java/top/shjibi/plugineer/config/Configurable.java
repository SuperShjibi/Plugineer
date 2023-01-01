package top.shjibi.plugineer.config;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * 可以保存、读写的数据
 */
public abstract class Configurable<T> {

    /**
     * 保存内存中所有的数据
     */
    public abstract void save();

    /**
     * 从文件中加载数据
     *
     * @param file 需要加载的文件
     * @return 加载的数据
     */
    protected abstract @NotNull T load(@NotNull File file);

    /**
     * @return 该数据的名字
     */
    public abstract @NotNull String getName();

    /**
     * @return 存储该数据的文件夹
     */
    public abstract @NotNull File getFolder();

    /**
     * 获取存储数据的所有文件
     */
    public abstract @NotNull File[] getFiles();

    /**
     * 获取使用该数据的文件夹
     */
    public abstract @NotNull Plugin getPlugin();

    /**
     * @return 内存中所有的数据
     */
    public abstract @NotNull T getData();

    /**
     * 创建该插件的数据文件夹和存储数据的文件夹
     *
     * @param plugin     使用该文件夹的插件
     * @param folderName 存储数据文件夹的名字
     * @return 创建的存储数据的文件夹
     */
    protected File mkdirs(@NotNull Plugin plugin, @Nullable String folderName) {
        File dataFolder = plugin.getDataFolder();
        File folder = folderName == null ? dataFolder : new File(dataFolder.getAbsolutePath() + "\\" + folderName);

        if (!dataFolder.exists()) {
            boolean result = dataFolder.mkdir();
            if (!result) throw new RuntimeException("无法创建插件数据文件夹!");
        }
        if (!folder.exists()) {
            boolean result = folder.mkdir();
            if (!result) throw new RuntimeException("无法创建" + folder.getName() + "文件夹!");
        }
        return folder;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{name: " + getName() + ", data: " + getData() + "}";
    }

}
