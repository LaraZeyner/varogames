package de.lara.mc.varo.gameplay;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.lara.mc.varo.storage.Const;
import de.lara.mc.varo.storage.Data;
import de.lara.mc.varo.storage.VaroData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Created by Lara on 19.06.2019 for varogames
 */
public class SpawnBuilder {
  private final Location spawn;

  public SpawnBuilder(Location spawn) {
    this.spawn = spawn;
  }

  public boolean invoke(int amount) {
    Data.getInstance().getVaroData().setMaxStarters(amount);
    return invoke();
  }

  private boolean invoke() {
    if (checkArea()) {
      evaluateBuild();

      return true;
    }
    return false;
  }

  public void reset() {
    final Map<Location, Material> filled = determineFilledBlocks();
    for (Location location : filled.keySet()) {
      final Material origin = filled.get(location);
      final Block block = location.getBlock();
      block.setType(origin);
    }
  }

  /**
   * 8765432109876543210987654321|123456789012345678901234567890
   * |=========================================================|
   * 26                      O    O    O                       0
   * 25                     OXO  OXO  OXO                      0
   * 24                 O    O    O    O    O                  0
   * 23                OXO                 OXO                 0
   * 22                 O                   O                  0
   * 21            O                             O             0
   * 20           OXO                           OXO            0
   * 19            O                             O             0
   * 18                                                        0
   * 17                                                        0
   * 16       O                                       O        0
   * 15      OXO       Q   Q  QQQ    QQQ   QQQ       OXO       0
   * 14       O        Q   Q Q   Q  Q   Q Q   Q       O        0
   * 13                Q   Q Q   Q  Q   Q Q   Q                0
   * 12                Q   Q QQQQQ  QQQQ  Q   Q                0
   * 11    O           QQ QQ Q   Q  Q Q   Q   Q          O     0
   * 10   OXO           QQQ  Q   Q  Q  Q  Q   Q         OXO    0
   * 09    O             Q   Q   Q  Q   Q  QQQ           O     0
   * 08                                                        0
   * 07                 SSSSSSSSSSSSSSSSSSSSS                  0
   * 06  O              S-------------------S              O   0
   * 05 OXO             S-------------------S             OXO  0
   * 04  O              S-SSSSSSSSSSSSSSSSS-S              O   0
   * 03                 S-S---------------S-S                  0
   * 02                 S-S---------------S-S                  0
   * 01  O              S-S-SSSSSSSSSSSSS-S-S              O   0
   * 00 OXO             S-S-B-----X-----B-S-S             OXO  0
   * 01  O              S-S-SSSSSSSSSSSSS-S-S              O   0
   * 02                 S-S---------------S-S                  0
   * 03                 S-S---------------S-S                  0
   * 04  O              S-SSSSSSSSSSSSSSSSS-S              O   0
   * 05 OXO             S-------------------S             OXO  0
   * 06  O              S-------------------S              O   0
   * 07                 SSSSSSSSSSSSSSSSSSSSS                  0
   * 08                                                        0
   * 09    O                                             O     0
   * 10   OXO                                           OXO    0
   * 11    O                                             O     0
   * 12                                                        0
   * 13                                                        0
   * 14       O                                       O        0
   * 15      OXO                                     OXO       0
   * 16       O                                       O        0
   * 17                                                        0
   * 18                                                        0
   * 19            O                             O             0
   * 20           OXO                           OXO            0
   * 21            O                             O             0
   * 22                 O                   O                  0
   * 23                OXO                 OXO                 0
   * 24                 O    O    O    O    O                  0
   * 25                     OXO  OXO  OXO                      0
   * 26                      O    O    O                       0
   * |=========================================================|
   * 8765432109876543210987654321|123456789012345678901234567890
   */
  private void evaluateBuild() {
    buildStartPoints();
    buildCenter();
    buildLetters();
  }

  private Map<Location, Material> determineFilledBlocks() {
    final Map<Location, Material> changed = new HashMap<>();
    final VaroData varoData = Data.getInstance().getVaroData();

    for (int x = -(varoData.getSpawnDistance() + 5); x < varoData.getSpawnDistance() + 5; x++) {
      for (int y = -11; y < 1; y++) {
        for (int z = -(varoData.getSpawnDistance() + 5); z < varoData.getSpawnDistance() + 5; z++) {
          final Location checkLocation = new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ())
              .add(x, y, z);
          final Block checkBlock = checkLocation.getBlock();
          final Material blockMaterial = checkBlock.getType();

          if (blockMaterial.equals(Material.OBSIDIAN) || blockMaterial.equals(Material.BEACON) ||
              blockMaterial.equals(Material.IRON_BLOCK) || blockMaterial.equals(Material.PORTAL) ||
              blockMaterial.equals(Material.CHEST)) {
            changed.put(checkLocation, Material.AIR);

          } else if (blockMaterial.equals(Material.PISTON_STICKY_BASE) || blockMaterial.equals(Material.GLOWSTONE)) {
            changed.put(checkLocation, Material.STONE);

          } else if (blockMaterial.equals(Material.BARRIER)) {
            changed.put(checkLocation, Material.GRASS);
            changed.put(new Location(checkLocation.getWorld(), checkLocation.getX(), checkLocation.getY(),
                checkLocation.getZ()).add(0, -1, 0), Material.STONE);

          } else if (blockMaterial.equals(Material.SEA_LANTERN)) {
            changed.put(checkLocation, Material.STONE);
            changed.put(new Location(checkLocation.getWorld(), checkLocation.getX(), checkLocation.getY(),
                checkLocation.getZ()).add(0, 1, 0), Material.GRASS);
            changed.put(new Location(checkLocation.getWorld(), checkLocation.getX(), checkLocation.getY(),
                checkLocation.getZ()).add(1, 2, 0), Material.AIR);
            changed.put(new Location(checkLocation.getWorld(), checkLocation.getX(), checkLocation.getY(),
                checkLocation.getZ()).add(0, 2, 1), Material.AIR);
            changed.put(new Location(checkLocation.getWorld(), checkLocation.getX(), checkLocation.getY(),
                checkLocation.getZ()).add(-1, 2, 0), Material.AIR);
            changed.put(new Location(checkLocation.getWorld(), checkLocation.getX(), checkLocation.getY(),
                checkLocation.getZ()).add(0, 2, -1), Material.AIR);
          }
        }
      }
    }
    return changed;
  }

  private void buildLetters() {
    final List<Location> locations = determineLetterLocations();

    for (int y = -3; y <= -1; y++) {
      for (Location location : locations) {
        final Location toFill = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ())
            .add(0, y, 0);

        if (y == -3) toFill.getBlock().setType(Material.GLOWSTONE);
        else if (y == -2) toFill.getBlock().setTypeIdAndData(Material.STEP.getId(), (byte) 7, true);
        else toFill.getBlock().setType(Material.BARRIER);
      }
    }
  }

  private List<Location> determineLetterLocations() {
    return Arrays.asList(
          // V
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(15, 0, -11),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(14, 0, -11),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(13, 0, -11),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(12, 0, -11),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(11, 0, -11),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(11, 0, -10),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(10, 0, -10),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(10, 0, -9),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(9, 0, -9),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(10, 0, -8),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(11, 0, -8),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(11, 0, -7),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(12, 0, -7),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(13, 0, -7),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(14, 0, -7),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(15, 0, -7),
          // A
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(15, 0, -4),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(15, 0, -3),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(15, 0, -2),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(14, 0, -5),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(14, 0, -1),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(13, 0, -5),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(13, 0, -1),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(12, 0, -5),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(12, 0, -4),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(12, 0, -3),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(12, 0, -2),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(12, 0, -1),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(11, 0, -5),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(11, 0, -1),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(10, 0, -5),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(10, 0, -1),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(9, 0, -5),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(9, 0, -1),
          // R
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(15, 0, 3),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(15, 0, 4),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(15, 0, 5),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(14, 0, 2),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(14, 0, 6),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(13, 0, 2),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(13, 0, 6),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(12, 0, 2),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(12, 0, 3),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(12, 0, 4),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(12, 0, 5),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(11, 0, 2),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(11, 0, 4),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(10, 0, 2),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(10, 0, 5),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(9, 0, 2),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(9, 0, 6),
          // O
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(15, 0, 9),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(15, 0, 10),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(15, 0, 11),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(14, 0, 8),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(13, 0, 8),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(12, 0, 8),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(11, 0, 8),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(10, 0, 8),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(14, 0, 12),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(13, 0, 12),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(12, 0, 12),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(11, 0, 12),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(10, 0, 12),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(9, 0, 9),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(9, 0, 10),
          new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(9, 0, 11));
  }

  private void buildCenter() {
    for (int x = -7; x <= 7; x++) {
      for (int z = -10; z <= 10; z++) {
        final Location toFill = new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(x, 0, z);

        if (Math.abs(x) == 7 || Math.abs(z) == 10) {
          toFill.getBlock().setTypeIdAndData(Material.STEP.getId(), (byte) 6, true);
        } else {
          toFill.getBlock().setType(Material.OBSIDIAN);
        }
      }
    }

    for (int x = -4; x <= 4; x++) {
      for (int z = -8; z <= 8; z++) {
        final Location toFill = new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ())
            .add(x, 1, z);
        if (Math.abs(x) == 4 || Math.abs(z) == 8) {
          toFill.getBlock().setTypeIdAndData(Material.STEP.getId(), (byte) 6, true);

        } else if (x == 0 && Math.abs(z) == 6) {
          toFill.getBlock().setType(Material.BEACON);
          for (int i = -1; i < 2; i++)
            for (int j = -1; j < 2; j++) {
              new Location(toFill.getWorld(), toFill.getX(), toFill.getY(), toFill.getZ())
                  .add(i, -1, j).getBlock().setType(Material.IRON_BLOCK);
            }

        } else {
          toFill.getBlock().setType(Material.OBSIDIAN);
        }
      }
    }

    for (int x = -1; x <= 1; x++) {
      for (int z = -6; z <= 6; z++) {
        final Location toFill = new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(x, 2, z);
        if (x != 0) {
          toFill.getBlock().setTypeIdAndData(Material.STEP.getId(), (byte) 6, true);
        }
      }
    }

    for (int y = 2; y <= 11; y++) {
      for (int z = -5; z <= 5; z++) {
        final Location toFill = new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()).add(0, y, z);
        toFill.getBlock().setType(Math.abs(z) == 5 || y == 11 ? Material.OBSIDIAN : Material.PORTAL);
      }
    }
  }

  private void buildStartPoints() {
    final VaroData varoData = Data.getInstance().getVaroData();
    final int starters = varoData.getMaxStarters();

    // Winkelberechnung
    for (int i = 0; i < starters; i++) {
      final Location hole = getStartPoint(i);
      new Location(hole.getWorld(), hole.getX(), hole.getY(), hole.getZ())
          .add(0, -1, 0).getBlock().setType(Material.AIR);
      new Location(hole.getWorld(), hole.getX(), hole.getY(), hole.getZ())
          .add(0, -2, 0).getBlock().setType(Material.SEA_LANTERN);
      new Location(hole.getWorld(), hole.getX(), hole.getY(), hole.getZ())
          .add(0, -3, 0).getBlock().setTypeIdAndData(Material.PISTON_STICKY_BASE.getId(), (byte) 1, false);
      new Location(hole.getWorld(), hole.getX(), hole.getY(), hole.getZ())
          .add(-1, 0, 0).getBlock().setTypeIdAndData(Material.STEP.getId(), (byte) 6, true);
      new Location(hole.getWorld(), hole.getX(), hole.getY(), hole.getZ())
          .add(0, 0, -1).getBlock().setTypeIdAndData(Material.STEP.getId(), (byte) 6, true);
      new Location(hole.getWorld(), hole.getX(), hole.getY(), hole.getZ())
          .add(0, 0, 1).getBlock().setTypeIdAndData(Material.STEP.getId(), (byte) 6, true);
      new Location(hole.getWorld(), hole.getX(), hole.getY(), hole.getZ())
          .add(1, 0, 0).getBlock().setTypeIdAndData(Material.STEP.getId(), (byte) 6, true);
    }
  }

  public List<Location> getStartPoints() {
    final VaroData varoData = Data.getInstance().getVaroData();
    final int starters = varoData.getMaxStarters();
    return IntStream.range(0, starters).mapToObj(this::getStartPoint).collect(Collectors.toList());
  }

  public Location getStartPoint(int id) {
    final VaroData varoData = Data.getInstance().getVaroData();
    final int starters = varoData.getMaxStarters();
    final double period = (double) 360 / starters;
    final double radiansAlpha = Math.toRadians(period * (id));
    final double x = Math.cos(radiansAlpha) * varoData.getSpawnDistance();
    final double z = Math.sin(radiansAlpha) * varoData.getSpawnDistance();

    return new Location(spawn.getWorld(), spawn.getBlockX() + 0.5, spawn.getBlockY(), spawn.getBlockZ() + 0.5)
        .add(x, 0, z);
  }

  private boolean checkArea() {
    final VaroData varoData = Data.getInstance().getVaroData();
    for (int x = -(varoData.getSpawnDistance() + 5); x < varoData.getSpawnDistance() + 5; x++) {
      for (int y = -1; y < 20; y++) {
        for (int z = -(varoData.getSpawnDistance() + 5); z < varoData.getSpawnDistance() + 5; z++) {
          final Location checkLocation = new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ())
              .add(x, y, z);
          final Block checkBlock = checkLocation.getBlock();

          if (y > -1 && !Const.allowedMaterialsAtSpawn.contains(checkBlock.getType())) {
            return false;
          }
        }
      }
    }

    return true;
  }
}
