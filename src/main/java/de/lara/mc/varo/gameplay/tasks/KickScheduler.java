package de.lara.mc.varo.gameplay.tasks;

import de.lara.mc.varo.gameplay.enums.KickReason;
import de.lara.mc.varo.gameplay.objects.VaroPlayer;
import de.lara.mc.varo.util.Messenger;
import org.bukkit.entity.Player;

/**
 * Created by Lara on 19.06.2019 for varogames
 */
public class KickScheduler extends SchedulerImpl {
  private boolean longer;

  public KickScheduler(VaroPlayer varoPlayer) {
    super(varoPlayer);
  }

  @Override
  public void invoke() {
    schedule(30, () -> {
      final Player player = getVaroPlayer().getPlayer();
      if ((getCounter() == 30 || getCounter() == 15 || getCounter() == 10 || (getCounter() < 4 && getCounter() > 0)) && !longer) {
        Messenger.broadcast("Der Spieler &c" + player.getName() + "&e wird in &c" + getCounter() + "&e Sekunden gekickt.");
      } else if (getCounter() == 0) {
        if (getVaroPlayer().isCombat()) {
          longer = true;
          increment(10);
        } else {
          player.kickPlayer(Messenger.kickMessage((getVaroPlayer().getLogoutTime() - System.currentTimeMillis()
              <= 30_000) ? KickReason.TIME_OVER : KickReason.MATE_OFFLINE, player));
          cancel();
        }
      }
      decrement();
    });
  }

}
