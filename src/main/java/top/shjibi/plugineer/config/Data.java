package top.shjibi.plugineer.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 代表一种数据
 */
public class Data extends Configurable<Map<UUID, JsonObject>> {

    protected final Plugin plugin;
    protected final File folder;
    protected final File[] files;
    protected final Map<UUID, JsonObject> data;
    protected final String name;

    /**
     * 建立一个名字为name的数据
     *
     * @param plugin 数据所属插件
     * @param name   数据名字
     */
    public Data(@NotNull Plugin plugin, @NotNull String name) {
        this(plugin, name, name);
    }

    /**
     * 建立一个名字为name的数据
     *
     * @param plugin     数据所属插件
     * @param name       数据名字
     * @param folderPath 存储数据的文件夹的名字
     */
    public Data(@NotNull Plugin plugin, @NotNull String name, @NotNull String folderPath) {
        this.plugin = plugin;
        this.name = name;
        this.folder = mkdirs(plugin, folderPath);
        files = folder.listFiles((f -> f.isFile() && f.getName().endsWith(".json")));
        this.data = load(folder);
    }

    /**
     * 移除键为uuid的数据
     *
     * @param uuid 要移除数据的键
     */
    public void removeData(@Nullable UUID uuid) {
        data.remove(uuid);
    }

    /**
     * 添加键为uuid, 值为obj的数据
     *
     * @param uuid 要添加的键
     * @param obj  要添加的值
     */
    public void addData(@NotNull UUID uuid, @NotNull JsonObject obj) {
        data.putIfAbsent(uuid, obj);
    }

    /**
     * 将键为uuid的数据的值设置为obj
     *
     * @param uuid 要修改的键
     * @param obj  要修改的值
     * @return 修改前的值，如果没有，则返回null
     */
    @Nullable
    public JsonObject setData(@NotNull UUID uuid, @NotNull JsonObject obj) {
        return data.put(uuid, obj);
    }

    /**
     * 获取以uuid为键的数据对应的值
     *
     * @param uuid 要获取的数据的键
     * @return 以uuid为键的数据对应的值
     */
    @Nullable
    public JsonObject getData(@NotNull UUID uuid) {
        return data.get(uuid);
    }

    /**
     * 保存键为uuid的数据
     */
    public void saveData(@NotNull UUID uuid) {
        File file = new File(folder.getAbsolutePath() + "\\" + uuid + ".json");
        try {
            Files.writeString(file.toPath(), data.get(uuid).toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("无法保存数据");
        }
    }

    @Override
    public void save() {
        for (UUID uuid : data.keySet()) {
            saveData(uuid);
        }
    }

    @Override
    @NotNull
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    @NotNull
    public Map<UUID, JsonObject> getData() {
        return data;
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
    public File[] getFiles() {
        return files;
    }

    @Override
    @NotNull
    protected Map<UUID, JsonObject> load(@NotNull File folder) {
        if (!folder.isDirectory()) throw new RuntimeException("folder必须是一个目录");
        Map<UUID, JsonObject> map = new HashMap<>();
        if (files.length == 0) return map;
        for (File file : files) {
            try {
                String name = file.getName();
                UUID uuid = UUID.fromString(name.substring(0, name.length() - 5));
                String value = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                JsonElement element = JsonParser.parseString(value);
                if (element instanceof JsonObject obj) map.put(uuid, obj);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("无法加载数据: " + file.getName());
            }
        }
        return map;
    }

}