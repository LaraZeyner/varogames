package de.lara.mc.varo.security;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import de.lara.mc.varo.storage.Data;
import org.bukkit.entity.Player;

/**
 * Created by Lara on 03.07.2019 for varogames
 */
public final class PermissionUser {
  private final UUID uuid;
  private final Set<UserPermission> permissions;

  public static PermissionUser getUser(Player player) {
    final UUID uuid = player.getUniqueId();
    return Data.getInstance().getPermissionUsers().stream().findFirst()
        .filter(permissionUser -> permissionUser.getUuid().equals(uuid))
        .orElse(new PermissionUser(uuid));
  }

  public PermissionUser(UUID uuid, Set<UserPermission> permissions) {
    this.uuid = uuid;
    this.permissions = permissions;
  }

  private PermissionUser(UUID uuid) {
    this.uuid = uuid;
    this.permissions = new HashSet<>();
  }

  public UUID getUuid() {
    return uuid;
  }

  public boolean hasPermission(UserPermission permission) {
    return permissions.contains(permission);
  }

  public void setPermission(UserPermission permission) {
    permissions.add(permission);
  }

  public void removePermission(UserPermission permission) {
    permissions.remove(permission);
  }

  public void resetPermissions() {
    permissions.clear();
  }
}
