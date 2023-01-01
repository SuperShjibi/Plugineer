package top.shjibi.plugineer.config;

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
import java.util.Objects;

/**
 * 代表了该插件的一种配置文件
 */
public class Config extends Configurable<YamlConfiguration> {

    private final JavaPlugin plugin;
    private final File folder;
    private final File[] files;
    private final YamlConfiguration config;
    private final String name;

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
     * 获取一个路径的配置
     *
     * @param path 要获取的配置的路径
     */
    @NotNull
    public String getConfig(String path) {
        return Objects.toString(config.get(path));
    }

    /**
     * 获取一个路径的配置
     *
     * @param clazz 指定配置的类型，如果不符合该类型，则返回null
     * @param path  要获取的配置的路径
     */
    @Nullable
    public <T> T getConfig(Class<T> clazz, String path) {
        return config.getObject(path, clazz);
    }

    /**
     * 获取一个路径中所有的注释
     *
     * @param path 要获取的注释的路径
     */
    @NotNull
    public List<String> getComments(String path) {
        return config.getComments(path);
    }

    /**
     * 获取一个路径中行内注释
     *
     * @param path 行内注释所在的路径
     */
    @NotNull
    public List<String> getInlineComments(String path) {
        return config.getInlineComments(path);
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
