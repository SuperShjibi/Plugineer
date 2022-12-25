package top.shjibi.plugineer.command.base;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * 只有玩家能用的指令
 */
public abstract class PlayerCommand extends BasicCommand {

    protected final String[] playerOnlyMsg;

    public PlayerCommand(JavaPlugin plugin) {
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

    /** 决定tab列表中出现哪些词 */
    public List<String> completeTab(@NotNull Player p, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }

    /** 决定执行指令出现的效果 */
    public abstract void execute(@NotNull Player p, @NotNull Command command, @NotNull String label, @NotNull String[] args);

    /** 获取提醒这是一个仅限玩家指令的消息 */
    public String[] getPlayerOnlyMsg() {
        return playerOnlyMsg;
    }
}
