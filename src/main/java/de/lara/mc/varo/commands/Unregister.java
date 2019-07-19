package de.lara.mc.varo.commands;

import de.lara.mc.varo.gameplay.registration.PlayerRegisterer;
import de.lara.mc.varo.util.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Lara on 28.05.2019 for varo
 * /unregister
 */
public class Unregister implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
    if (checkPlayer(commandSender) && checkLength(commandSender, strings)) {
      final Player player = (Player) commandSender;
      new PlayerRegisterer(player).unregisterPlayer();
    }

    return false;
  }

  private boolean checkLength(CommandSender player, String[] strings) {
    if (strings.length != 0) {
      Messenger.sendMessage((Player) player, "&cSyntax falsch!&e Benutze:&9 /unregister");
      return false;
    }
    return true;
  }

  private boolean checkPlayer(CommandSender sender) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("Du musst ein Spieler sein, um dich abmelden zu können.");
      return false;
    }
      return true;
  }
}
