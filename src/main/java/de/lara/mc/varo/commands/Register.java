package de.lara.mc.varo.commands;

import de.lara.mc.varo.gameplay.registration.PlayerRegisterer;
import de.lara.mc.varo.util.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Lara on 28.05.2019 for varo
 * /register >Teamname<
 */
public class Register implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
    if (checkPlayer(commandSender) && checkLength(commandSender, strings)) {
      final Player player = (Player) commandSender;
      final String teamname = strings[0];
      new PlayerRegisterer(player).registerPlayer(teamname);
    }

    return false;
  }

  private boolean checkLength(CommandSender player, String[] strings) {
    if (strings.length != 1) {
      Messenger.sendMessage((Player) player, "&cSyntax falsch!&e Benutze:&9 /register &6<Teamname>");
      return false;
    }
    return true;
  }

  private boolean checkPlayer(CommandSender sender) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("Du musst ein Spieler sein, um dich registrieren zu können.");
      return false;
    }
      return true;
  }
}
