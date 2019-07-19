package de.lara.mc.varo.commands;

import de.lara.mc.varo.gameplay.SpawnBuilder;
import de.lara.mc.varo.storage.Data;
import de.lara.mc.varo.storage.VaroData;
import de.lara.mc.varo.util.objectmanager.VaroManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Lara on 24.06.2019 for varogames
 */
public class ResetSpawn implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (VaroManager.isPrepared() && !VaroManager.isStarted()) {
      if (sender.isOp() && sender instanceof Player) {
        final Player player = (Player) sender;
        final Location spawnLocation = player.getLocation();
        new SpawnBuilder(spawnLocation).reset();

        final VaroData varoData = Data.getInstance().getVaroData();
        varoData.setPrepared(false);
      }
    }

    return false;
  }

}
