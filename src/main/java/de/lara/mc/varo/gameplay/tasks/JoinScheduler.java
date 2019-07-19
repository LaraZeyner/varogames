package de.lara.mc.varo.gameplay.tasks;

import java.util.StringJoiner;

import de.lara.mc.varo.gameplay.enums.KickReason;
import de.lara.mc.varo.gameplay.objects.VaroPlayer;
import de.lara.mc.varo.gameplay.objects.VaroStrikeStatus;
import de.lara.mc.varo.util.Messenger;
import de.lara.mc.varo.util.objectmanager.PlayerManager;
import org.bukkit.entity.Player;

/**
 * Created by Lara on 10.06.2019 for varogames
 */
public class JoinScheduler extends SchedulerImpl {

  public JoinScheduler(VaroPlayer varoPlayer) {
    super(varoPlayer);
  }

  @Override
  public void invoke() {
    final VaroPlayer mate = getVaroPlayer().getMate();
    if (mate.getPlayer() == null) {
      waitingForMate(mate);
    }
    countdown();

    final String strikedString = determineStrikedPlayersLocation();
    if (!strikedString.equals("")) {
      Messenger.sendMessage(getVaroPlayer().getPlayer(), strikedString);
    }
  }

  private String determineStrikedPlayersLocation() {
    final StringJoiner locationsString = new StringJoiner("\n");
    for (VaroPlayer varoPlayer : PlayerManager.determineAlivePlayers()) {
      final VaroStrikeStatus strikeStatus = varoPlayer.getStrikeStatus();
      if (!strikeStatus.getStrikes().isEmpty()) {
        locationsString.add(strikeStatus.determinePlayerLocationString(varoPlayer));
      }
    }
    return locationsString.toString();
  }

  private void countdown() {
    final Player player = getVaroPlayer().getPlayer();

    schedule(0, () -> {
      if (getCounter() > -7 && getCounter() < 0) {
        player.sendTitle(7 + getCounter() + "", "Gleich gehts los.");

      } else if (getCounter() == -7) {
        final int playTimeInSeconds = getVaroPlayer().getAvailablePlayTime();
        player.sendTitle("LOS GEHTS!", "Du darfst " + playTimeInSeconds / 60 + " Minuten spielen.");
        getVaroPlayer().setLastJoined(System.currentTimeMillis());
        cancel();
      }

      decrement();
    });
  }

  private void waitingForMate(VaroPlayer mate) {
    schedule(30, () -> {
      final Player player = getVaroPlayer().getPlayer();
      player.sendTitle("", "Warte auf Mate");
      if (mate.getPlayer() != null) cancel();

      else if (getCounter() == 0) {
        player.kickPlayer(Messenger.kickMessage(KickReason.MATE_OFFLINE, player));
        cancel();
      }

      decrement();
    });
  }
}
