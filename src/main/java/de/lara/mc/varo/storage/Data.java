package de.lara.mc.varo.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.lara.mc.varo.gameplay.objects.VaroTeam;
import de.lara.mc.varo.io.sql.SQLManager;
import de.lara.mc.varo.security.PermissionStorageHandler;
import de.lara.mc.varo.security.PermissionUser;

/**
 * Created by Lara on 28.05.2019 for varo
 */
public final class Data {
  private static Data instance;

  /**
   * Singleton-Muster: nur eine Instanz im gesamten Programm
   *
   * @return Instanz
   */
  public static Data getInstance() {
    if (instance == null) {
      instance = new Data();
    }
    return instance;
  }

  private final List<PermissionUser> permissionUsers;
  private final List<VaroTeam> teams;
  private final Map<UUID, String> cache;
  private final SQLManager sql;
  private final VaroData varoData;

  private Data() {
    this.cache = new HashMap<>();
    this.permissionUsers = PermissionStorageHandler.loadUserPermissions();
    this.sql = new SQLManager();
    this.teams = new ArrayList<>();
    this.varoData = new VaroData();
  }

  //<editor-fold desc="getter and setter">
  public Map<UUID, String> getCache() {
    return cache;
  }

  public List<PermissionUser> getPermissionUsers() {
    return permissionUsers;
  }

  public SQLManager getSql() {
    return sql;
  }

  public List<VaroTeam> getTeams() {
    return teams;
  }

  public VaroData getVaroData() {
    return varoData;
  }
  //</editor-fold>
}
