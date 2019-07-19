package de.lara.mc.varo.gameplay.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import de.lara.mc.varo.util.objectmanager.TeamManager;

/**
 * Created by Lara on 05.06.2019 for varogames
 */
public class VaroTeamImpl implements Serializable {
  private static final long serialVersionUID = 2498891330263261486L;
  private final List<VaroPlayer> players;
  private final String teamName;

  VaroTeamImpl(String teamName) {
    this.players = new ArrayList<>();
    this.teamName = teamName;
  }

  //<editor-fold desc="getter and setter">
  public List<VaroPlayer> getPlayers() {
    return players;
  }

  public String getTeamName() {
    return teamName;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append(TeamManager.isAlive(TeamManager.determineTeam(teamName)) ? "&b" : "&3")
        .append(teamName);
    IntStream.of(20 - teamName.length()).forEach(i -> builder.append(" "));
    builder.append("&e -> ");

    if (players.size() >= 1) {
      final VaroPlayer player1 = players.get(0);
      final String player1Name = player1.getName();
      builder.append(player1.isAlive() ? "&a" : "&7")
          .append(player1Name);
      IntStream.of(16 - player1Name.length()).forEach(i -> builder.append(" "));
      builder.append("&e + ");
    }

    if (players.size() >= 2) {
      final VaroPlayer player2 = players.get(1);
      final String player2Name = player2.getName();
      builder.append(player2.isAlive() ? "&a" : "&7")
          .append(player2Name);
    }

    if (players.isEmpty()) {
      builder.append("&cTeam ohne Member");
    }

    return builder.toString();
  }
}
