package de.lara.mc.varo.storage;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

/**
 * Created by Lara on 28.05.2019 for varo
 */
public final class Const {
  public static final File SQL_CONFIG =
      new File("plugins" + File.separator + "config" + File.separator + "sql.properties");
  static final File VARO_CONFIG =
      new File("plugins" + File.separator + "config" + File.separator + "varo.properties");
  public static final File VARO_PERMISSIONS =
      new File("plugins" + File.separator + "config" + File.separator + "permissions.properties");

  public static final List<Material> allowedMaterialsAtSpawn = Arrays.asList(Material.AIR, Material.LONG_GRASS);
}