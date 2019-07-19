package de.lara.mc.varo.util.objectmanager;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import de.lara.mc.varo.gameplay.objects.VaroPlayer;
import de.lara.mc.varo.gameplay.objects.VaroTeam;
import de.lara.mc.varo.storage.Data;
import de.lara.mc.varo.util.mcutils.UUIDUtils;

/**
 * Created by Lara on 01.06.2019 for varogames
 */
public final class PlayerManager {
  private static final List<VaroTeam> teams = Data.getInstance().getTeams();

  public static List<VaroPlayer> determineAllPlayers() {
    return Data.getInstance().getTeams().stream().flatMap(varoTeam -> varoTeam.getPlayers().stream())
        .collect(Collectors.toList());
  }

  public static VaroPlayer determinePlayer(String playerName) {
    if (isRegistered(playerName)) {
      final UUID uuid = UUIDUtils.getUUID(playerName);
      return determineAllPlayers().stream().filter(varoPlayer -> varoPlayer.getUuid().equals(uuid))
          .findFirst()
          .orElse(null);
    }
    return null;
  }

  public static int determinePlayerId(VaroPlayer player) {
    final VaroTeam varoTeam = player.getTeam();
    final int teamIndex = teams.indexOf(varoTeam);
    return teamIndex*2 + varoTeam.getPlayers().indexOf(player) + 1;
  }

  public static VaroTeam determineTeamOfPlayer(VaroPlayer player) {
    return teams.stream().filter(varoTeam -> varoTeam.getPlayers().contains(player))
        .findFirst()
        .orElse(null);
  }

  public static boolean isRegistered(String playerName) {
    final UUID uuid = UUIDUtils.getUUID(playerName);
    return determineAllPlayers().stream().anyMatch(varoPlayer -> varoPlayer.getUuid().equals(uuid));
  }

  public static List<VaroPlayer> determineAlivePlayers() {
    return determineAllPlayers().stream().filter(VaroPlayer::isAlive)
        .collect(Collectors.toList());
  }

  public static List<VaroPlayer> determineDeadPlayers() {
    return determineAllPlayers().stream().filter(varoPlayer -> !varoPlayer.isAlive())
        .collect(Collectors.toList());
  }

}
