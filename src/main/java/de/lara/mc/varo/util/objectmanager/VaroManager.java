package de.lara.mc.varo.util.objectmanager;

import java.util.Date;

import de.lara.mc.varo.gameplay.SpawnBuilder;
import de.lara.mc.varo.gameplay.objects.VaroPlayer;
import de.lara.mc.varo.gameplay.objects.VaroTeam;
import de.lara.mc.varo.storage.Data;
import de.lara.mc.varo.storage.VaroData;
import de.lara.mc.varo.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Created by Lara on 01.06.2019 for varogames
 */
public final class VaroManager {

  public static boolean isPortalAvailable() {
    final VaroData varoData = Data.getInstance().getVaroData();
    final Date startDate = varoData.getStartDate();
    return System.currentTimeMillis() - startDate.getTime() > 86_400_000L;
  }

  public static boolean isPrepared() {
    final VaroData varoData = Data.getInstance().getVaroData();
    return varoData.isPrepared();
  }

  public static boolean isStarted() {
    final VaroData varoData = Data.getInstance().getVaroData();
    return new Date().after(varoData.getStartDate());
  }

  public static void powerPistons(boolean power) {
    final Location spawn = Bukkit.getWorld("world").getSpawnLocation();

    for (Location location : new SpawnBuilder(spawn).getStartPoints()) {
      final Location pistonLocation = new Location(location.getWorld(), location.getX(), location.getY(),
          location.getZ()).add(0,-18,0);
      final Block pistonBlock = pistonLocation.getBlock();

      if (pistonBlock.getType().equals(Material.PISTON_STICKY_BASE)) {
        final Location redstoneLocation = new Location(pistonLocation.getWorld(), pistonLocation.getX(),
            pistonLocation.getY(), pistonLocation.getZ()).add(0, -1, 0);
        final Block redstoneBlock = redstoneLocation.getBlock();
        redstoneBlock.setType(power ? Material.REDSTONE_BLOCK : Material.STONE);

      } else {
        Messenger.administratorMessage("Spawn funktioniert nicht.");
      }
    }
  }

  public static Location getFirstJoinPosition() {
    final int online = Bukkit.getOnlinePlayers().size(); //TODO (Abgie) 10.06.2019: spawnlocationchange
    return Bukkit.getWorld("world").getSpawnLocation()/*.add(online, 0, 0)*/;
  }

  public static Location getCenter() {
    return Bukkit.getWorld("world").getSpawnLocation();
  }

  public static Location getStartPoint(int nr) {
    return new SpawnBuilder(Bukkit.getWorld("world").getSpawnLocation()).getStartPoint(nr - 1);
  }

  public static boolean isVaroEnded() {
    return Data.getInstance().getTeams().stream().filter(VaroTeam::isAlive).count() <= 1;
  }
}
