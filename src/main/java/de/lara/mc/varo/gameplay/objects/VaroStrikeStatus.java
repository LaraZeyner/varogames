package de.lara.mc.varo.gameplay.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import de.lara.mc.varo.util.mcutils.InventorySaver;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

/**
 * Created by Lara on 01.06.2019 for varogames
 */
public class VaroStrikeStatus implements Serializable {
  private static final long serialVersionUID = 428254696105777697L;

  private final List<VaroStrike> strikes;
  private Location location;
  private Inventory inventory;

  {
    this.strikes = new ArrayList<>();
  }

  public void addStrike(VaroStrike varoStrike) {
    strikes.add(varoStrike);
  }

  //<editor-fold desc="getter and setter">
  public Inventory getInventory() {
    return inventory;
  }

  public List<VaroStrike> getStrikes() {
    return strikes;
  }

  public Location getLocation() {
    return  (location.getWorld() != null) ? location :
        new Location(Bukkit.getWorld("world"), location.getX(), location.getY(), location.getZ());
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }

  public void setLocation(Location location) {
    this.location = location;
  }
  //</editor-fold>

  public String determinePlayerLocationString(VaroPlayer varoPlayer) {
    final String playerName = varoPlayer.getName();
    final String worldName = location.getWorld().getName();
    return playerName + "&7 ->&c Welt:&e" + worldName + "&c X:&e" + location.getBlockX() +
        "&c Y:&e" + location.getBlockY() + "&c Z:&e" + location.getBlockZ();
  }

  @Override
  public String toString() {
    final StringJoiner joiner = new StringJoiner("/");
    joiner.add(strikes.size() + "");
    strikes.stream().map(VaroStrike::toString).forEach(joiner::add);
    joiner.add("w=" + getLocation().getWorld().getName());
    joiner.add("x=" + getLocation().getBlockX());
    joiner.add("y=" + getLocation().getBlockY());
    joiner.add("z=" + getLocation().getBlockZ());
    joiner.add(InventorySaver.toBase64(inventory));

    return joiner.toString();
  }
}
