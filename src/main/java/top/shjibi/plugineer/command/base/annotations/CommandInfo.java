package top.shjibi.plugineer.command.base.annotations;

import org.bukkit.command.ConsoleCommandSender;
import top.shjibi.plugineer.command.base.PlayerCommandHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Information of a command handler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo {
    /**
     * Names of commands that the handler processes (Usually one command)
     */
    String[] name();

    /**
     * Minimum argument count for the command(s) to run
     */
    int minArgs() default 0;

    /**
     * Usage of the command(s)
     */
    String[] usage() default {};

    /**
     * Message to send when a {@link ConsoleCommandSender} executed the command (Only available for {@link PlayerCommandHandler})
     */
    String[] playerOnlyMsg() default "&cThis command can only be executed by players!";
}
