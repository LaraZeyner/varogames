package de.lara.mc.varo.gameplay.statuschanges;

import de.lara.mc.varo.gameplay.tasks.StartScheduler;
import de.lara.mc.varo.util.objectmanager.VaroManager;

/**
 * Created by Lara on 05.06.2019 for varogames
 */
public class StartVaro {
  private static final StartScheduler scheduler;

  static {
    scheduler = new StartScheduler(null);
  }

  public void invoke() {
    if (VaroManager.isPrepared() && !VaroManager.isStarted()) {
      scheduler.invoke();
    }
  }

  //TODO (Abgie) 05.06.2019: SPieler verlieren op
  //TODO (Abgie) 05.06.2019: clear inv
}
