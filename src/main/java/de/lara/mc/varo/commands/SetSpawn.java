package de.lara.mc.varo.commands;

import de.lara.mc.varo.gameplay.SpawnBuilder;
import de.lara.mc.varo.storage.Data;
import de.lara.mc.varo.storage.Messages;
import de.lara.mc.varo.storage.VaroData;
import de.lara.mc.varo.util.objectmanager.VaroManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Lara on 19.06.2019 for varogames
 */
public class SetSpawn implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (VaroManager.isPrepared() && !VaroManager.isStarted()) {
      if (sender.isOp() && sender instanceof Player) {
        final Player player = (Player) sender;
        final Location spawnLocation = player.getLocation();
        try {
          final VaroData varoData = Data.getInstance().getVaroData();
          final int amount = args.length == 1 ? Integer.parseInt(args[0]) : varoData.getMaxStarters();
          if (amount * 4 <= varoData.getSpawnDistance() * 2 * Math.PI && new SpawnBuilder(spawnLocation).invoke(amount)) {
            player.teleport(spawnLocation.add(0, 15, 0));
            player.getWorld().setSpawnLocation(spawnLocation.getBlockX(), spawnLocation.getBlockY(),
                spawnLocation.getBlockZ());
            sender.sendMessage(Messages.SPAWN_CREATED);
            varoData.setPrepared(true);

          } else {
            sender.sendMessage(Messages.SPAWN_CREATION_ERROR);
          }

        } catch (NumberFormatException ex) {
          sender.sendMessage(Messages.NOT_A_NUMBER);
        }
      }
    }

    return false;
  }
}
