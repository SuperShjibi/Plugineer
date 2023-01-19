package top.shjibi.plugineer.command;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.shjibi.plugineer.command.base.CommandHandler;
import top.shjibi.plugineer.command.base.annotations.CommandRegistration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

/**
 * A class that helps you manage your commands and command handlers.
 */
public final class CommandManager {

    @SuppressWarnings("unchecked")
    private CommandManager(@NotNull JavaPlugin plugin, @NotNull Class<?>[] handlerClasses) {
        this.plugin = plugin;
        this.handlerClasses = handlerClasses;
        commandHandlerMap = new HashMap<>();

        try {
            Server server = Bukkit.getServer();
            Field commandMapField = server.getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (SimpleCommandMap) commandMapField.get(server);

            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Cannot get command map or known commands", e);
        }
    }


    @Nullable
    private static CommandManager instance;
    @NotNull
    private final JavaPlugin plugin;
    @NotNull
    private final Class<?>[] handlerClasses;
    @NotNull
    private final Map<CommandHandler, Command[]> commandHandlerMap;
    @NotNull
    private final Map<String, Command> knownCommands;
    @NotNull
    private final SimpleCommandMap commandMap;

    /**
     * Gets or create the only instance of CommandManager for this plugin.
     */
    @NotNull
    public static CommandManager getInstance(JavaPlugin plugin, Class<?>... classes) {
        if (instance == null) instance = new CommandManager(plugin, classes);
        return instance;
    }

    /**
     * Registers all the command handlers, if the handler used {@link CommandRegistration}, also registers the commands.
     */
    public void register() {
        for (Class<?> clazz : handlerClasses) {
            try {
                Object obj = clazz.getConstructor(JavaPlugin.class).newInstance(plugin);
                if (!(obj instanceof CommandHandler handler)) continue;
                List<String> nameList = List.of(handler.getNames());
                for (CommandRegistration registration : handler.getClass().getAnnotationsByType(CommandRegistration.class)) {
                    String name = registration.name().toLowerCase(Locale.ENGLISH);
                    if (nameList.contains(name)) {
                        registerCommand(registration.replaceConflict(),
                                registration.name(),
                                registration.description(),
                                registration.aliases());
                    }
                }
                Command[] commands = handler.register();
                commandHandlerMap.put(handler, commands);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Cannot register command handler: " + clazz.getSimpleName(), e);
            }
        }
    }

    /**
     * Registers a command using the given name, description and aliases
     *
     * @param replaceConflict If a command with the same name exists, whether to replace the conflicted command
     * @param name            Name of the command
     * @param description     Description of the command
     * @param aliases         Aliases of the command
     * @return whether the conflicted command exists.
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean registerCommand(boolean replaceConflict, String name, String description, String... aliases) {
        boolean result = false;
        try {
            Constructor<PluginCommand> pluginCommandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            pluginCommandConstructor.setAccessible(true);

            PluginCommand command = pluginCommandConstructor.newInstance(name, plugin);
            command.setDescription(description);
            command.setAliases(List.of(aliases));

            result = commandMap.register(plugin.getName(), command);

            if (!result && replaceConflict) {
                unregisterCommand(name);
                commandMap.register(plugin.getName(), command);
            }
        } catch (ReflectiveOperationException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot register command: " + name);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Unregisters commands with the given name
     */
    public void unregisterCommand(String name) {
        Set<Map.Entry<String, Command>> entrySet = knownCommands.entrySet();
        entrySet.stream()
                .filter(x -> x.getValue().getName().equals(name))
                .peek(x -> x.getValue().unregister(commandMap))
                .toList().forEach(entrySet::remove);
    }


    /**
     * Gets the command handler map
     */
    @NotNull
    public Map<CommandHandler, Command[]> getCommandHandlerMap() {
        return commandHandlerMap;
    }

    /**
     * Gets a BasicCommand instance according to the given class
     */
    @Nullable
    public CommandHandler getCommandHandler(Class<? extends CommandHandler> clazz) {
        for (CommandHandler handler : commandHandlerMap.keySet()) {
            if (handler.getClass() == clazz) return handler;
        }
        return null;
    }

    /**
     * Get all the registered commands on this server
     * */
    @NotNull
    public Map<String, Command> getKnownCommands() {
        return knownCommands;
    }

    /**
     * Gets the command map of this server
     * */
    @NotNull
    public SimpleCommandMap getCommandMap() {
        return commandMap;
    }
}
