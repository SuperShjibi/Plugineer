package top.shjibi.plugineer.command.base;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.shjibi.plugineer.command.base.annotations.CommandInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static top.shjibi.plugineer.util.StringUtil.color;


/**
 * A basic command handler
 */
public abstract class CommandHandler implements TabExecutor {

    @NotNull
    protected final JavaPlugin plugin;
    @NotNull
    protected final String[] names;
    protected final int minArgs;
    @NotNull
    protected final String[] usage;

    public CommandHandler(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;

        // Reads the command information
        CommandInfo[] infoArray = getClass().getAnnotationsByType(CommandInfo.class);
        if (infoArray.length == 0) throw new RuntimeException("CommandInfo is not found!");
        CommandInfo info = infoArray[0];

        this.names = (String[]) Arrays.stream(info.name()).map(s -> s.toLowerCase(Locale.ENGLISH)).toArray();
        this.minArgs = info.minArgs();
        this.usage = info.usage();
    }

    /**
     * Registers the command
     *
     * @return The commands using this handler
     */
    public Command[] register() {
        Command[] commands = new Command[names.length];
        for (int i = 0; i < names.length; i++) {
            PluginCommand command = Objects.requireNonNull(plugin.getCommand(names[i]));
            commands[i] = command;
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
        return commands;
    }

    /**
     * Sends the correct usage of the command
     */
    protected final void sendUsage(@NotNull CommandSender sender, Object... replacement) {
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
     * Handles the tab list
     */
    @Nullable
    public List<String> completeTab(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }

    /**
     * Handles the command
     */
    public abstract void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);

    /**
     * Gets the names of commands associated with this handler
     */
    @NotNull
    public String[] getNames() {
        return names;
    }

    /**
     * Gets the minimum argument count required for the command to run
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
