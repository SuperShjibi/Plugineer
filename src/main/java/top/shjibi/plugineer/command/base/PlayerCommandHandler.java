package top.shjibi.plugineer.command.base;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import top.shjibi.plugineer.command.base.annotations.CommandInfo;

import java.util.Collections;
import java.util.List;

/**
 * A command handler for the player only commands
 */
public abstract class PlayerCommandHandler extends CommandHandler {

    protected final String[] playerOnlyMsg;

    public PlayerCommandHandler(JavaPlugin plugin) {
        super(plugin);
        CommandInfo info = getClass().getAnnotationsByType(CommandInfo.class)[0];
        this.playerOnlyMsg = info.playerOnlyMsg();
    }

    @Override
    public final List<String> completeTab(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return Collections.emptyList();
        return completeTab(p, command, label, args);
    }

    @Override
    public final void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(playerOnlyMsg);
            return;
        }
        execute(p, command, label, args);
    }

    public List<String> completeTab(@NotNull Player p, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }

    public abstract void execute(@NotNull Player p, @NotNull Command command, @NotNull String label, @NotNull String[] args);

    /**
     * Gets the player only message
     */
    public String[] getPlayerOnlyMsg() {
        return playerOnlyMsg;
    }
}
