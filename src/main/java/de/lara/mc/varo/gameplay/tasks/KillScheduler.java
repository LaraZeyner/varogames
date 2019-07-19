package de.lara.mc.varo.gameplay.tasks;

import de.lara.mc.varo.gameplay.objects.VaroPlayer;

/**
 * Created by Lara on 24.06.2019 for varogames
 */
public class KillScheduler extends SchedulerImpl {

  public KillScheduler(VaroPlayer varoPlayer) {
    super(varoPlayer);
  }

  @Override
  public void invoke() {
    schedule(20, () -> {
      if (getCounter() == 0) cancel();
      decrement();
    });
  }
}
