package top.shjibi.plugineer.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Map;
import java.util.logging.Level;

/**
 * A configuration of a plugin
 */
public class Config extends Configurable<YamlConfiguration> {

    protected final JavaPlugin plugin;
    protected final File folder;
    protected final File[] files;
    protected final YamlConfiguration config;
    protected final YamlConfiguration defaults;
    protected final String name;

    /**
     * Creates a {@link Config} with the specified name
     *
     * @param plugin {@link Plugin} that the {@link Config} belongs to.
     * @param name   Name of the config, will be used to create the config file
     */
    public Config(@NotNull JavaPlugin plugin, @NotNull String name) {
        this(plugin, name, null);
    }


    /**
     * Creates a {@link Config} with the specified name
     *
     * @param plugin {@link Plugin} that the {@link Config} belongs to.
     * @param name   Name of the config, will be used to create the config file
     * @param folderPath Folder which stores the config file.
     */
    public Config(@NotNull JavaPlugin plugin, @NotNull String name, @Nullable String folderPath) {
        this.plugin = plugin;
        this.name = name;
        this.folder = mkdirs(plugin, folderPath);
        this.files = new File[]{new File(folder.getAbsolutePath() + "\\" + name + ".yml")};
        this.defaults = loadDefault();
        this.config = load(files[0]);
    }

    /**
     * Creates an empty {@link ConfigurationSection} at the specified path with specified data.
     *
     * @param path The specified path
     * @param map  The data to put
     * @return Newly created section
     */
    @NotNull
    public ConfigurationSection createSection(@NotNull String path, @NotNull Map<?, ?> map) {
        return config.createSection(path, map);
    }

    /**
     * Creates an empty ConfigurationSection at the specified path, any value that was previously
     * set at this path will be overwritten.
     *
     * @param path The specified path
     * @return Newly created section
     */
    @NotNull
    public ConfigurationSection createSection(@NotNull String path) {
        return config.createSection(path);
    }

    /**
     * Sets the provided path to a specified value
     *
     * @param path  The provided path
     * @param value The value to set
     */
    public void setConfig(@NotNull String path, @Nullable Object value) {
        config.set(path, value);
    }

    /**
     * Gets the value of a provided path as a {@link String}
     *
     * @param path The provided path
     * @return Value as a {@link String}, or null if the provided path doesn't exist
     */
    @Nullable
    public String getConfig(@NotNull String path) {
        Object o = config.get(path);
        if (o == null) return null;
        return o.toString();
    }

    /**
     * Gets the value of a provided path cast to the given class.
     *
     * @param clazz The given class.
     * @param path  The provided path
     * @return Value cast to the given class, or null if the value can't be cast to the given class.
     */
    @Nullable
    public <T> T getConfig(@NotNull Class<T> clazz, @NotNull String path) {
        return config.getObject(path, clazz, null);
    }

    /**
     * Gets the default value of a provided path as a {@link String}
     *
     * @param path The provided path
     * @return Default value as a {@link String}, or null if the provided path doesn't exist
     */
    @Nullable
    public String getDefaultConfig(@NotNull String path) {
        Object o = defaults.get(path);
        if (o == null) return null;
        return o.toString();
    }

    /**
     * Gets the default value of a provided path cast to the given class.
     *
     * @param clazz The given class.
     * @param path  The provided path
     * @return Default value cast to the given class, or null if the value can't be cast to the given class.
     */
    @Nullable
    public <T> T getDefaultConfig(@NotNull Class<T> clazz, @NotNull String path) {
        return defaults.getObject(path, clazz, null);
    }



    @Override
    public void save() {
        File file = files[0];
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Cannot save file: " + file.getName(), e);
        }
    }

    /**
     * Loads the configuration from the config file again
     */
    public void reload() {
        File file = files[0];
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException("Cannot reload config: " + file.getName(), e);
        }
    }

    /**
     * Loads the default value of this configuration
     *
     * @return The default configuration
     */
    public YamlConfiguration loadDefault() {
        YamlConfiguration ret = new YamlConfiguration();
        InputStream inputStream = plugin.getResource(name + ".yml");
        if (inputStream == null) {
            return ret;
        }
        try {
            ret.load(new InputStreamReader(inputStream));
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException("Cannot load default config: " + name, e);
        }
        return ret;
    }

    @Override
    @NotNull
    protected YamlConfiguration load(@NotNull File file) {
        if (!file.exists()) {
            try {
                defaults.save(file);
            } catch (IOException e) {
                throw new RuntimeException("Cannot save default config: " + name, e);
            }
            return defaults;
        }
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config: " + name, e);
        } catch (InvalidConfigurationException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load config because of It's invalid.");
        }
        return config;
    }

    @Override
    @NotNull
    public File[] getFiles() {
        return files;
    }

    @Override
    @NotNull
    public String getName() {
        return name;
    }

    @Override
    @NotNull
    public File getFolder() {
        return folder;
    }

    @Override
    @NotNull
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    @NotNull
    public YamlConfiguration getData() {
        return config;
    }

    @NotNull
    public YamlConfiguration getDefaults() {
        return defaults;
    }
}
