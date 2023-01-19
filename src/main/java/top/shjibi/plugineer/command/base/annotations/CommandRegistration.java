package top.shjibi.plugineer.command.base.annotations;

import top.shjibi.plugineer.command.base.CommandHandler;

import java.lang.annotation.*;

/**
 * Use this annotation on {@link CommandHandler} to register a command without writing the command in plugin.yml
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(CommandRegistrations.class)
public @interface CommandRegistration {

    /**
     * Name of this command
     */
    String name();

    /**
     * Description of this command
     */
    String description() default "";

    /**
     * Aliases of this command
     */
    String[] aliases() default "";

    /**
     * If this command is already registered, whether to replace the registered one.
     */
    boolean replaceConflict() default false;
}
