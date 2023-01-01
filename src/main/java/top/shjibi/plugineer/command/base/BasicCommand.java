package top.shjibi.plugineer.command.base;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static top.shjibi.plugineer.util.StringUtil.color;


/**
 * 基本的指令
 */
public abstract class BasicCommand implements TabExecutor {

    @NotNull
    protected final JavaPlugin plugin;
    @NotNull
    protected final String name;
    protected final int minArgs;
    @NotNull
    protected final String[] usage;

    public BasicCommand(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        CommandInfo[] infoArray = getClass().getAnnotationsByType(CommandInfo.class);
        if (infoArray.length == 0) throw new RuntimeException("无指令信息");
        CommandInfo info = infoArray[0];
        this.name = info.name();
        this.minArgs = info.minArgs();
        this.usage = info.usage();
    }

    /**
     * 注册指令
     */
    public void register() {
        PluginCommand command = Objects.requireNonNull(plugin.getCommand(name));
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    /**
     * 向指令发送者发送指令的正确用法
     */
    protected final void sendUsage(@NotNull CommandSender sender, Object... replacement) {
        if (usage.length == 0) return;
        for (String line : usage) {
            sender.sendMessage(color(String.format(line, replacement)));
        }
    }

    @Override
    public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < minArgs) {
            sendUsage(sender, label);
            return true;
        }
        execute(sender, command, label, args);
        return true;
    }

    @Override
    public final List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return completeTab(sender, command, label, args);
    }

    /**
     * 决定tab列表中出现哪些词
     */
    @Nullable
    public List<String> completeTab(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }

    /**
     * 决定执行指令出现的效果
     */
    public abstract void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);

    /**
     * 获取指令名字
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * 获取指令所需的最少参数
     */
    public int getMinArgs() {
        return minArgs;
    }

    /**
     * 获取指令用法
     */
    @NotNull
    public String[] getUsage() {
        return usage;
    }
}
