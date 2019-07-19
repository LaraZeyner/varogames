package de.lara.mc.varo.commands;

import de.lara.mc.varo.util.objectmanager.VaroManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Lara on 21.06.2019 for varogames
 */
public class Start implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (VaroManager.isPrepared() && !VaroManager.isStarted()) {
      if (args.length == 1) {
        if (args[0].equalsIgnoreCase("1")) {
          VaroManager.powerPistons(true);
          System.out.println("Aktiv");
        } else if (args[0].equalsIgnoreCase("0")) {
          VaroManager.powerPistons(false);
          System.out.println("Inaktiv");
        }
      }
    }
    return false;
  }
}
