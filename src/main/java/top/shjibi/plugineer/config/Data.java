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
 * Data of a plugin
 */
public class Data extends Configurable<Map<UUID, JsonObject>> {

    protected final Plugin plugin;
    protected final File folder;
    protected final File[] files;
    protected final Map<UUID, JsonObject> data;
    protected final String name;

    /**
     * Creates a {@link Data} with the specified name
     *
     * @param plugin {@link Plugin} that the {@link Data} belongs to.
     * @param name   Name of the data, will be used to create the folder
     */
    public Data(@NotNull Plugin plugin, @NotNull String name) {
        this(plugin, name, name);
    }

    /**
     * Creates a {@link Data} with the specified name
     *
     * @param plugin     {@link Plugin} that the {@link Data} belongs to.
     * @param name       Name of the data
     * @param folderPath Folder which stores all the data files.
     */
    public Data(@NotNull Plugin plugin, @NotNull String name, @NotNull String folderPath) {
        this.plugin = plugin;
        this.name = name;
        this.folder = mkdirs(plugin, folderPath);
        files = folder.listFiles((f -> f.isFile() && f.getName().endsWith(".json")));
        this.data = load(folder);
    }

    /**
     * Removes the mapping for the specified {@link UUID} from this map if present.
     *
     * @param uuid The specified key
     */
    public void removeData(@Nullable UUID uuid) {
        data.remove(uuid);
    }

    /**
     * If the specified {@link UUID} doesn't exist in this data, then puts the {@link UUID} and {@link JsonObject}, otherwise does nothing.
     *
     * @param uuid The specified UUID
     * @param obj  The value to put
     * @return The previous value associated with the specified UUID.
     */
    @Nullable
    public JsonObject putDataIfAbsent(@NotNull UUID uuid, @NotNull JsonObject obj) {
        return data.putIfAbsent(uuid, obj);
    }

    /**
     * Puts the {@link UUID} and {@link JsonObject}
     *
     * @param uuid The specified UUID
     * @param obj  The value to put
     * @return The previous value associated with the specified UUID.
     */
    @Nullable
    public JsonObject putData(@NotNull UUID uuid, @NotNull JsonObject obj) {
        return data.put(uuid, obj);
    }

    /**
     * Gets the value associated with the provided {@link UUID}
     *
     * @param uuid The specified UUID
     * @return The value associated with the provided UUID
     */
    @Nullable
    public JsonObject getData(@NotNull UUID uuid) {
        return data.get(uuid);
    }

    /**
     * Saves the data associated with the provided {@link UUID} to uuid.json
     *
     * @param uuid The specified UUID
     */
    public void saveData(@NotNull UUID uuid) {
        File file = new File(folder.getAbsolutePath() + "\\" + uuid + ".json");
        try {
            Files.writeString(file.toPath(), data.get(uuid).toString());
        } catch (IOException e) {
            throw new RuntimeException("Cannot save file: " + file.getName(), e);
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
                throw new RuntimeException("Cannot load file: " + file.getName(), e);
            }
        }
        return map;
    }

}