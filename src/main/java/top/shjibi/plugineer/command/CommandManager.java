package top.shjibi.plugineer.command;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.shjibi.plugineer.command.base.BasicCommand;

import java.util.HashMap;
import java.util.Map;

/** 一个负责管理指令的类 */
public final class CommandManager {

    private CommandManager(@NotNull JavaPlugin plugin, @NotNull Class<? extends BasicCommand>[] classes) {
        this.plugin = plugin;
        this.classes = classes;
        commandMap = new HashMap<>();
    }

    @NotNull private final JavaPlugin plugin;
    @NotNull private final Class<? extends BasicCommand>[] classes;
    @NotNull private final Map<Class<? extends BasicCommand>, BasicCommand> commandMap;

    /** 创建一个指令管理者的实例 */
    public static CommandManager newInstance(JavaPlugin plugin, Class<? extends BasicCommand>[] classes) {
        return new CommandManager(plugin, classes);
    }

    /** 注册所有的指令 */
    public void register() {
        for (Class<? extends BasicCommand> clazz : classes) {
            try {
                BasicCommand command = clazz.getConstructor(JavaPlugin.class).newInstance(plugin);
                command.register();
                commandMap.put(clazz, command);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("无法注册指令类: " + clazz.getSimpleName());
            }
        }
    }

    /** 获取指令表 */
    public @NotNull Map<Class<? extends BasicCommand>, BasicCommand> getCommandMap() {
        return commandMap;
    }

    /** 通过指令类型获取对应的BasicCommand */
    public @Nullable BasicCommand getCommand(Class<? extends BasicCommand> clazz) {
        return commandMap.get(clazz);
    }

}
