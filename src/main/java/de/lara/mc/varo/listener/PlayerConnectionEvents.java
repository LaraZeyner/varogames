package de.lara.mc.varo.listener;

import java.util.Date;
import java.util.Objects;

import de.lara.mc.varo.gameplay.enums.KickReason;
import de.lara.mc.varo.gameplay.objects.VaroPlayer;
import de.lara.mc.varo.gameplay.objects.VaroStrike;
import de.lara.mc.varo.gameplay.tasks.JoinScheduler;
import de.lara.mc.varo.gameplay.tasks.KickScheduler;
import de.lara.mc.varo.storage.Data;
import de.lara.mc.varo.storage.Messages;
import de.lara.mc.varo.storage.VaroData;
import de.lara.mc.varo.util.Messenger;
import de.lara.mc.varo.util.objectmanager.OnlinePlayerManager;
import de.lara.mc.varo.util.objectmanager.PlayerManager;
import de.lara.mc.varo.util.objectmanager.VaroManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Lara on 01.06.2019 for varogames
 */
public class PlayerConnectionEvents implements Listener {

  @EventHandler
  public void onLogin(PlayerLoginEvent loginEvent) {
    final Player joiner = loginEvent.getPlayer();

    if (VaroManager.isStarted()) {
      final VaroPlayer varoPlayer = PlayerManager.determinePlayer(joiner.getName());
      if (varoPlayer.isAlive() || varoPlayer.canSpectate() || joiner.isOp()) {
        loginEvent.allow();
      } else {
        loginEvent.disallow(PlayerLoginEvent.Result.KICK_BANNED, Messages.PREFIX + Messages.ALREADY_DEAD);
      }

    } else {
      loginEvent.allow();
    }
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent joinEvent) {
    final Player joiner = joinEvent.getPlayer();

    setTexturePack(joiner);

    if (VaroManager.isStarted()) {
      evaluateStarted(joiner);

    } else if (VaroManager.isPrepared()) {
      evaluatePrepared(joiner);

    } else {
      evaluateNotPrepared(joiner);
    }

    if (OnlinePlayerManager.isAdministrator(joiner)) {
      evaluateOperatorJoin(joiner);
    }
  }

  private void setTexturePack(Player joiner) {


    joiner.setResourcePack("http://goo.gl/kzNkX");
  }

  private void evaluateNotPrepared(Player joiner) {
    if (joiner.isOp()) {
      Messenger.administratorMessage(Messages.SPAWN_NOT_READY);
    } else {
      joiner.kickPlayer(Messenger.kickMessage(KickReason.NOT_PREPARED, joiner));
    }
  }

  private void evaluatePrepared(Player joiner) {
    final VaroData varoData = Data.getInstance().getVaroData();
    final int registeredSize = PlayerManager.determineAllPlayers().size();
    if (varoData.getMaxStarters() >= registeredSize) {
      varoData.setMaxStarters(registeredSize + 1);
    }

    joiner.setGameMode(GameMode.ADVENTURE);
    joiner.getInventory().clear();

    if (PlayerManager.isRegistered(joiner.getName())) {
      final VaroPlayer varoPlayer = PlayerManager.determinePlayer(joiner.getName());
      if (checkTeamIsRegistered(joiner)) {
        evaluateReadyToStart(varoPlayer);
      } else {
        evaluateTeamNotFull(varoPlayer);
      }

    } else if (!joiner.isOp()) {
      evaluateFirstJoin(joiner);
    }
  }

  private void evaluateStarted(Player joiner) {
    if (checkRegistered(joiner)) {
      final VaroPlayer varoPlayer = PlayerManager.determinePlayer(joiner.getName());
      if (Objects.requireNonNull(varoPlayer).canSpectate()) {
        evaluateSpectating(varoPlayer);
      } else {
        evaluatePlaying(varoPlayer);
      }
    }
  }

  private boolean checkTeamIsRegistered(Player joiner) {
    final VaroPlayer varoPlayer = PlayerManager.determinePlayer(joiner.getName());
    return varoPlayer.getTeam().getPlayers().size() == 2;
  }

  private boolean checkRegistered(Player joiner) {
    if (!PlayerManager.isRegistered(joiner.getName())) {
      evaluateOperatorJoin(joiner);
      return false;
    }
    return true;
  }

  /**
   * Status:     Prepared
   * Registered: No
   *
   * @param joiner Joined Player
   */
  private void evaluateFirstJoin(Player joiner) {
    final Location worldspawn = VaroManager.getFirstJoinPosition();
    joiner.teleport(worldspawn);
    Messenger.sendMessage(joiner, Messages.NOT_REGISTERED_YET);
  }

  /**
   * Status:     Prepared
   * Registered: Yes
   *
   * @param joiner Joined Player
   */
  private void evaluateReadyToStart(VaroPlayer joiner) {
    final int id = PlayerManager.determinePlayerId(joiner);
    final Location startPoint = VaroManager.getStartPoint(id);
    joiner.getPlayer().teleport(startPoint);
  }

  /**
   * Status:     Prepared
   * Registered: Yes
   *
   * @param joiner Joined Player
   */
  private void evaluateTeamNotFull(VaroPlayer joiner) {
    Messenger.sendMessage(joiner.getPlayer(), Messages.TEAM_NOT_FULL);
  }

  /**
   * Status:     All
   * Registered: All
   *
   * @param joiner Joined Player
   */
  private void evaluateOperatorJoin(Player joiner) {
    if (joiner.isOp()) {
      joiner.teleport(VaroManager.getCenter());
      joiner.setGameMode(VaroManager.isStarted() ? GameMode.SPECTATOR : GameMode.CREATIVE);
    } else {
      joiner.kickPlayer(Messenger.kickMessage(KickReason.NOT_REGISTERED, joiner));
    }

  }

  /**
   * Status:     Started
   * Registered: No
   *
   * @param varoPlayer Joined Player
   */
  private void evaluatePlaying(VaroPlayer varoPlayer) {
    varoPlayer.getPlayer().setOp(false);
    new JoinScheduler(varoPlayer).invoke();


  }

  /**
   * Status:     Started
   * Registered: Yes
   *
   * @param varoPlayer Joined Player
   */
  private void evaluateSpectating(VaroPlayer varoPlayer) {
    Bukkit.getOnlinePlayers().forEach(player -> player.hidePlayer(varoPlayer.getPlayer()));
    varoPlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
    final VaroPlayer mate = varoPlayer.getMate();
    varoPlayer.getPlayer().setSpectatorTarget(mate.getPlayer());
  }

  @EventHandler
  public void onKick(PlayerKickEvent kickEvent) {
    kickMate(kickEvent);
  }

  @EventHandler
  public void onLeave(PlayerQuitEvent quitEvent) {
    final Player player = quitEvent.getPlayer();
    final VaroPlayer varoPlayer = PlayerManager.determinePlayer(player.getName());
    if (varoPlayer != null && VaroManager.isStarted()) {
      quitEvent.setQuitMessage("");
      if (varoPlayer.isCombat()) {
        varoPlayer.addStrike(new VaroStrike(new Date(), "Im Kampf ausgeloggt."));
      }
      varoPlayer.addPlayTime();
      kickMate(quitEvent);
      varoPlayer.save();
    }


  }

  private void kickMate(PlayerEvent kickEvent) {
    final VaroPlayer target = PlayerManager.determinePlayer(kickEvent.getPlayer().getName());
    final VaroPlayer mate = Objects.requireNonNull(target).getMate();

    if (mate != null) {
      if (mate.getPlayer().isOnline() && !mate.isAlive()) {
        mate.getPlayer().kickPlayer(Messenger.kickMessage(KickReason.BANNED, mate.getPlayer()));
      } else if (mate.getPlayer().isOnline() && mate.isAlive() && target.isAlive()) {
        new KickScheduler(mate).invoke();
      }
    }
  }
}
