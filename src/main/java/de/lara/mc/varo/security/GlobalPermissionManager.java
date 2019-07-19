package de.lara.mc.varo.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import de.lara.mc.varo.io.FileManager;
import de.lara.mc.varo.storage.Const;

/**
 * Created by Lara on 13.07.2019 for varogames
 */
public class GlobalPermissionManager {
  private final Set<GlobalPermission> permissions;

  public GlobalPermissionManager() {
    final String permissionsString = FileManager.loadConfig(Const.VARO_PERMISSIONS).get("global");

    this.permissions = permissionsString == null ? new HashSet<>() :
        Arrays.stream(permissionsString.split(", ")).map(GlobalPermission::valueOf)
        .collect(Collectors.toSet());
  }

  public boolean hasPermission(GlobalPermission permission) {
    return permissions.contains(permission);
  }

  public void setPermission(GlobalPermission permission) {
    permissions.add(permission);
  }

  public void removePermission(GlobalPermission permission) {
    permissions.remove(permission);
  }

  public void resetPermissions() {
    permissions.clear();
  }

  private void save() {
    final StringJoiner joiner = new StringJoiner(", ");
    for (GlobalPermission permission : permissions) {
      joiner.add(permission.name());
    }

    final Map<String, String> permissionsMap = new HashMap<>();
    permissionsMap.put("global", joiner.toString());
    FileManager.writeIntoConfig(Const.VARO_PERMISSIONS, permissionsMap);
  }
}