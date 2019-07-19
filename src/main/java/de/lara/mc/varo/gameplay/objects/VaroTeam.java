package de.lara.mc.varo.gameplay.objects;

/**
 * Created by Lara on 01.06.2019 for varogames
 */
public class VaroTeam extends VaroTeamImpl {

  private VaroTeam(String teamName) {
    super(teamName);
  }

  public VaroTeam(String teamName, VaroPlayer player1) {
    this(teamName);
    getPlayers().add(player1);
  }

  public boolean addPlayer(VaroPlayer player2) {
    if (getPlayers().size() != 2) {
      getPlayers().add(player2);
      return true;
    }
    return false;
  }

  public boolean isAlive() {
    return getPlayers().stream().anyMatch(VaroPlayer::isAlive);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof VaroTeam && ((VaroTeam) obj).getTeamName().equalsIgnoreCase(getTeamName());
  }
}
