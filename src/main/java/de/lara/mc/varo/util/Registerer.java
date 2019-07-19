package de.lara.mc.varo.util;

import java.util.Arrays;
import java.util.List;

import de.lara.mc.varo.Varo;
import de.lara.mc.varo.commands.Register;
import de.lara.mc.varo.commands.SetSpawn;
import de.lara.mc.varo.commands.Start;
import de.lara.mc.varo.commands.Strike;
import de.lara.mc.varo.commands.Team;
import de.lara.mc.varo.commands.Unregister;
import de.lara.mc.varo.listener.ChatEvents;
import de.lara.mc.varo.listener.InventoryEvents;
import de.lara.mc.varo.listener.PlayerConnectionEvents;
import de.lara.mc.varo.listener.PlayerDamageEvents;
import de.lara.mc.varo.listener.PlayerEvents;
import de.lara.mc.varo.listener.PlayerInteractions;
import de.lara.mc.varo.listener.PlayerLocationChangeEvents;
import de.lara.mc.varo.storage.Data;
import de.lara.mc.varo.storage.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Lara on 28.05.2019 for varo
 */
public final class Registerer {
  private static boolean checkErrors() {
    return Data.getInstance().getVaroData().getStartDate() == null;
  }

  public static void performRegistration(Varo varo) {
    registerCommands(varo);
    registerEvents();

    if (checkErrors()) {
      Messenger.administratorMessage(Messages.CONFIG_ERROR);
      Varo.getInstance().onDisable();
    }
  }

  private static void registerEvents() {
    // Insert Events here
    final List<Listener> listeners = Arrays.asList(new ChatEvents(), new InventoryEvents(),
        new PlayerConnectionEvents(), new PlayerDamageEvents(), new PlayerEvents(), new PlayerInteractions(),
        new PlayerLocationChangeEvents());

    for (final Listener listener : listeners) {
      Bukkit.getPluginManager().registerEvents(listener, Varo.getInstance());
    }
  }

  private static void registerCommands(JavaPlugin plugin) {
    // Insert Commands here
    final List<CommandExecutor> commands = Arrays.asList(new Register(), new SetSpawn(), new Start(), new Strike(),
        new Team(), new Unregister());

    for (final CommandExecutor commandExecutor : commands) {
      final Class<? extends CommandExecutor> commandExecutorClass = commandExecutor.getClass();
      final String commandName = commandExecutorClass.getSimpleName().toLowerCase();
      plugin.getCommand(commandName).setExecutor(commandExecutor);
    }
  }

  /*private void registerCommands() {
    final Reflections reflections = new Reflections("net.mmm.template.commands");
    for (Class<? extends CommandExecutor> commandClass : reflections.getSubTypesOf(CommandExecutor.class)) {
      final String name = commandClass.getSimpleName().toLowerCase();
      try {
        getCommand(name).setExecutor(commandClass.newInstance());
      } catch (ReflectiveOperationException ignored) {
        logger.warning("Command " + name + " could not loaded.");
      }
    }
  }

  private void registerEvents() {
    final Reflections reflections = new Reflections("net.mmm.template.listener");
    for (Class<? extends Listener> listenerClass : reflections.getSubTypesOf(Listener.class)) {
      try {
        Bukkit.getPluginManager().registerEvents(listenerClass.newInstance(), this);
      } catch (ReflectiveOperationException ignored) {
        logger.warning("Event " + listenerClass.getName() + " could not loaded.");
      }

    }
  }*/
}
