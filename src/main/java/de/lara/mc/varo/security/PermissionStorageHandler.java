package de.lara.mc.varo.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import de.lara.mc.varo.io.FileManager;
import de.lara.mc.varo.storage.Const;

/**
 * Created by Lara on 13.07.2019 for varogames
 */
public final class PermissionStorageHandler {

  public static List<PermissionUser> loadUserPermissions() {
    final List<PermissionUser> user = new ArrayList<>();
    final Map<String, String> permissionsStringsMap = FileManager.loadConfig(Const.VARO_PERMISSIONS);
    for (String uuid : permissionsStringsMap.keySet()) {
      if (!uuid.equalsIgnoreCase("global")) {
        final String permissionsString = permissionsStringsMap.get(uuid);
        final Set<UserPermission> userPermissions = Arrays.stream(permissionsString.split(", "))
            .map(UserPermission::valueOf).collect(Collectors.toSet());
        final PermissionUser permissionUser = new PermissionUser(UUID.fromString(uuid), userPermissions);
        user.add(permissionUser);
      }
    }

    return user;
  }
}
