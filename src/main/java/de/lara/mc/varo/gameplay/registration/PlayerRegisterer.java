package de.lara.mc.varo.gameplay.registration;

import de.lara.mc.varo.util.objectmanager.PlayerManager;
import de.lara.mc.varo.util.objectmanager.TeamManager;
import de.lara.mc.varo.util.objectmanager.VaroManager;
import de.lara.mc.varo.gameplay.objects.VaroPlayer;
import de.lara.mc.varo.gameplay.objects.VaroTeam;
import de.lara.mc.varo.io.sql.SQLManager;
import de.lara.mc.varo.storage.Data;
import de.lara.mc.varo.storage.Messages;
import de.lara.mc.varo.util.Messenger;
import org.bukkit.entity.Player;

/**
 * Created by Lara on 01.06.2019 for varogames
 */
public class PlayerRegisterer {
  private final VaroPlayer player;

  public PlayerRegisterer(Player player) {
    this.player = PlayerManager.isRegistered(player.getName()) ?
        PlayerManager.determinePlayer(player.getName()) : new VaroPlayer(player.getUniqueId());
  }

  public void registerPlayer(String teamName) {
    final VaroTeam team = TeamManager.determineTeam(teamName);
    if (checkNotStarted() && checkNotRegistered() && checkTeamAvailable(team) && addPlayerToTeam(teamName, team)) {
      final Player target = player.getPlayer();
      player.getStrikeStatus().setInventory(target.getInventory());
      player.getStrikeStatus().setLocation(target.getLocation());
      registerPlayerInDatabase();
      Messenger.sendMessage(player.getPlayer(), Messages.REGISTERED);
    }
  }

  public void unregisterPlayer() {
    if (checkNotStarted() && checkRegistered()) {
      final VaroTeam team = PlayerManager.determineTeamOfPlayer(player);
      removePlayerFromTeam(team);
      removePlayerFromDatabase();
      Messenger.sendMessage(player.getPlayer(), Messages.UNREGISTERED);
    }
  }

  private void removePlayerFromDatabase() {
    final SQLManager sqlManager = Data.getInstance().getSql();
    sqlManager.removePlayer(player);
  }

  private void removePlayerFromTeam(VaroTeam team) {
    if (team.getPlayers().size() == 2) {
      team.getPlayers().remove(player);

    } else if (team.getPlayers().size() == 1) {
      Data.getInstance().getTeams().remove(team);

    }
  }

  private boolean checkRegistered() {
    if (!PlayerManager.isRegistered(player.getName())) {
      Messenger.sendMessage(player.getPlayer(), Messages.NOT_REGISTERED);
      return false;
    }
    return true;
  }

  private void registerPlayerInDatabase() {
    final SQLManager sqlManager = Data.getInstance().getSql();
    sqlManager.addPlayer(player);
  }

  private boolean addPlayerToTeam(String teamName, VaroTeam team) {
    if (!TeamManager.doesTeamExist(teamName)) {
      team = new VaroTeam(teamName, player);
      Data.getInstance().getTeams().add(team);
      return true;
    }
    return team.addPlayer(player);
  }

  private boolean checkTeamAvailable(VaroTeam team) {
    if (team != null && team.getPlayers().size() == 2) {
      Messenger.sendMessage(player.getPlayer(), Messages.TEAM_ALREADY_EXIST);
      return false;
    }
    return true;
  }

  private boolean checkNotRegistered() {
    if (PlayerManager.isRegistered(player.getName())) {
      Messenger.sendMessage(player.getPlayer(), Messages.ALREADY_REGISTERED);
      return false;
    }
    return true;
  }

  private boolean checkNotStarted() {
    if (VaroManager.isStarted()) {
      Messenger.sendMessage(player.getPlayer(), Messages.ALREADY_STARTED);
      return false;
    }
    return true;
  }
}
