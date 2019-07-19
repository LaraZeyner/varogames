package de.lara.mc.varo.util.objectmanager;

import java.util.List;
import java.util.stream.Collectors;

import de.lara.mc.varo.gameplay.objects.VaroPlayer;
import de.lara.mc.varo.gameplay.objects.VaroTeam;
import de.lara.mc.varo.storage.Data;

/**
 * Created by Lara on 01.06.2019 for varogames
 */
public final class TeamManager {
  private static final List<VaroTeam> teams;

  static {
    teams = Data.getInstance().getTeams();
  }

  public static VaroTeam determineTeam(String teamName) {
    return teams.stream().filter(team -> team.getTeamName().equalsIgnoreCase(teamName))
        .findFirst()
        .orElse(null);
  }

  public static boolean doesTeamExist(String teamName) {
    return teams.stream().anyMatch(team -> team.getTeamName().equalsIgnoreCase(teamName));
  }

  public static boolean isAlive(VaroTeam team) {
    return team.getPlayers().stream().anyMatch(VaroPlayer::isAlive);
  }

  public List<VaroTeam> determineAliveTeams() {
    return teams.stream().filter(TeamManager::isAlive).collect(Collectors.toList());
  }

  public List<VaroTeam> determineDeadTeams() {
    return teams.stream().filter(team -> !isAlive(team)).collect(Collectors.toList());
  }

}
