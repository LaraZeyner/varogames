package de.lara.mc.varo.gameplay.tasks;

import de.lara.mc.varo.gameplay.objects.VaroPlayer;
import de.lara.mc.varo.util.objectmanager.OnlinePlayerManager;

/**
 * Created by Lara on 19.06.2019 for varogames
 */
public class MainScheduler extends SchedulerImpl {

  public MainScheduler(VaroPlayer varoPlayer) {
    super(varoPlayer);
  }

  @Override
  public void invoke() {
    schedule(0, () -> {
      for (VaroPlayer varoPlayer : OnlinePlayerManager.determineAlivePlayers()) {
        if (getCounter() % 60 == 0) {
        }
        if (varoPlayer.getLogoutTime() - System.currentTimeMillis() <= 30_000) {
          new KickScheduler(varoPlayer).invoke();
        }
      }

      if (getCounter() == Integer.MAX_VALUE) {
        setCounter(0);
      }
      increment();
    });
  }
}
