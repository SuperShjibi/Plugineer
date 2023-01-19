package top.shjibi.plugineer.config;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Configurable data of a specific plugin that can be saved and read.
 */
public abstract class Configurable<T> {

    /**
     * Saves data in the memory
     */
    public abstract void save();

    /**
     * Loads data from a file
     *
     * @param file The file which stored data
     * @return The loaded data
     */
    @NotNull
    protected abstract T load(@NotNull File file);

    /**
     * @return Name of this specific configurable
     */
    @NotNull
    public abstract String getName();

    /**
     * @return The folder where all the files are saved
     */
    @NotNull
    public abstract File getFolder();

    /**
     * @return All the files which stored data
     */
    @NotNull
    public abstract File[] getFiles();

    /**
     * @return The plugin which owns and controls all the data
     */
    @NotNull
    public abstract Plugin getPlugin();

    /**
     * @return All the data in the memory
     */
    @NotNull
    public abstract T getData();

    /**
     * Creates the plugin folder and the folder which stores data
     *
     * @param plugin     The plugin which owns and controls all the data
     * @param folderName Name of the folder which stores data
     * @return The folder which stores data
     */
    @NotNull
    protected static File mkdirs(@NotNull Plugin plugin, @Nullable String folderName) {
        File dataFolder = plugin.getDataFolder();
        File folder = folderName == null ? dataFolder : new File(dataFolder.getAbsolutePath() + "\\" + folderName);

        if (!dataFolder.exists()) {
            boolean result = dataFolder.mkdir();
            if (!result) throw new RuntimeException("Cannot create the plugin folder!");
        }
        if (!folder.exists()) {
            boolean result = folder.mkdir();
            if (!result) throw new RuntimeException("Cannot create '" + folder.getName() + "' folder!");
        }
        return folder;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{name: " + getName() + ", data: " + getData() + "}";
    }

}
