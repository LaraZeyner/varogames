package de.lara.mc.varo.util.objectmanager;

import java.util.List;
import java.util.stream.Collectors;

import de.lara.mc.varo.gameplay.objects.VaroPlayer;
import de.lara.mc.varo.gameplay.objects.VaroPlayerImpl;
import de.lara.mc.varo.security.PermissionUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;

/**
 * Created by Lara on 05.06.2019 for varogames
 */
public final class OnlinePlayerManager {

  public static List<Player> determineAdministrators() {
    return Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp)
        .collect(Collectors.toList());
  }

  public static List<VaroPlayer> determinePlayers() {
    return Bukkit.getOnlinePlayers().stream().map(player -> PlayerManager.determinePlayer(player.getName()))
        .collect(Collectors.toList());

  }

  public static List<VaroPlayer> determineAlivePlayers() {
    return determinePlayers().stream().filter(VaroPlayerImpl::isAlive)
        .collect(Collectors.toList());
  }

  public static List<VaroPlayer> determineSpectators() {
    return determinePlayers().stream().filter(VaroPlayer::canSpectate)
        .collect(Collectors.toList());
  }

  public static List<Player> determineNotRegistered() {
    return Bukkit.getOnlinePlayers().stream().filter(online -> !isPlayer(online) && !isAdministrator(online))
        .collect(Collectors.toList());
  }

  public static boolean isAdministrator(Player player) {
    return determineAdministrators().contains(player);
  }

  public static boolean isPlayer(Player player) {
    return determinePlayers().contains(player);
  }

  public static boolean isSpectator(Player player) {
    return determineSpectators().contains(player);
  }

  public static boolean isNotRegistered(Player player) {
    return !PlayerManager.isRegistered(player.getName());
  }

  public static PermissionUser getPermissions(Player player) {
    return PermissionUser.getUser(player);
  }
}
