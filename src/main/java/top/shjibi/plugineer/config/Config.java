package top.shjibi.plugineer.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 代表了该插件的一种配置文件
 */
public class Config extends Configurable<YamlConfiguration> {

    protected final JavaPlugin plugin;
    protected final File folder;
    protected final File[] files;
    protected final YamlConfiguration config;
    protected final String name;

    public Config(@NotNull JavaPlugin plugin, @NotNull String name) {
        this(plugin, name, null);
    }


    public Config(@NotNull JavaPlugin plugin, @NotNull String name, @Nullable String folderPath) {
        this.plugin = plugin;
        this.name = name;
        this.folder = mkdirs(plugin, folderPath);
        this.files = new File[]{new File(folder, name + ".yml")};
        this.config = load(files[0]);
    }

    /**
     * 在指定路径创建一个ConfigurationSection，之前在此路径的数据将被替换为map
     *
     * @param path 要使用的路径
     * @param map  要使用的数据
     * @return 创建的ConfigurationSection
     */
    @NotNull
    public ConfigurationSection createSection(@NotNull String path, @NotNull Map<?, ?> map) {
        return config.createSection(path, map);
    }

    /**
     * 在指定路径创建一个ConfigurationSection，之前在此路径的数据将被清空
     *
     * @param path 要使用的路径
     * @return 创建的ConfigurationSection
     */
    @NotNull
    public ConfigurationSection createSection(@NotNull String path) {
        return config.createSection(path);
    }

    /**
     * 将指定路径的数据设置成value
     *
     * @param path  要使用的路径
     * @param value 要设置的数据
     */
    public void setConfig(@NotNull String path, @Nullable Object value) {
        config.set(path, value);
    }

    /**
     * 获取一个路径的配置
     *
     * @param path 要获取的配置的路径
     */
    @NotNull
    public String getConfig(@NotNull String path) {
        return Objects.toString(config.get(path));
    }

    /**
     * 获取一个路径的配置
     *
     * @param clazz 指定配置的类型，如果不符合该类型，则返回null
     * @param path  要获取的配置的路径
     */
    @Nullable
    public <T> T getConfig(@NotNull Class<T> clazz, @NotNull String path) {
        return config.getObject(path, clazz);
    }

    /**
     * 获取一个路径中所有的注释
     *
     * @param path 注释所在的路径
     */
    @NotNull
    public List<String> getComments(@NotNull String path) {
        return config.getComments(path);
    }

    /**
     * 获取一个路径中行内注释
     *
     * @param path 行内注释所在的路径
     */
    @NotNull
    public List<String> getInlineComments(@NotNull String path) {
        return config.getInlineComments(path);
    }

    /**
     * 设置一个路径中所有的注释
     *
     * @param path     注释所在的路径
     * @param comments 注释
     */
    public void setComments(@NotNull String path, @Nullable List<String> comments) {
        config.setComments(path, comments);
    }

    /**
     * 设置一个路径中行内注释
     *
     * @param path     行内注释所在的路径
     * @param comments 注释
     */
    public void setInlineComments(@NotNull String path, @Nullable List<String> comments) {
        config.setInlineComments(path, comments);
    }


    @Override
    public void save() {
        File file = files[0];
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("无法保存文件: " + file.getName());
        }
    }

    public void reload() {
        File file = files[0];
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException("无法重新加载配置文件: " + file.getName());
        }
    }

    @Override
    @NotNull
    protected YamlConfiguration load(@NotNull File file) {
        YamlConfiguration config = new YamlConfiguration();
        if (!file.isFile()) throw new RuntimeException("file必须是一个文件");
        try {
            if (!file.exists()) {
                InputStream inputStream = plugin.getResource(name + ".yml");
                if (inputStream == null) return config;
                config.load(new InputStreamReader(inputStream));
                return config;
            }
            config.load(file);
            return config;
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException("无法加载数据: " + file.getName());
        }
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
}
