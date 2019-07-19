package de.lara.mc.varo.gameplay.tasks;

import java.util.Calendar;
import java.util.Date;

import de.lara.mc.varo.gameplay.objects.VaroPlayer;
import de.lara.mc.varo.storage.Data;
import de.lara.mc.varo.util.objectmanager.VaroManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Lara on 24.06.2019 for varogames
 */
public class StartScheduler extends SchedulerImpl {

  public StartScheduler(VaroPlayer varoPlayer) {
    super(varoPlayer);
  }

  @Override
  public void invoke() {
    if (VaroManager.isPrepared() && !VaroManager.isStarted()) {
      countdown();
    }
  }

  private void countdown() {
    final Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.add(Calendar.MINUTE, 65);
    Data.getInstance().getVaroData().setStartDate(c.getTime());

    schedule(3_900, () -> {

      if (getCounter() % 600 == 0 || (getCounter() % 300 == 0 && getCounter() < 1_800)) {
        rememberStart(getCounter());
      }

      if (getCounter() < 900) {

      }

      if (getCounter() == 0) {
        initStart();
      }

      decrement();
    });
  }

  private void initStart() {
    Data.getInstance().getVaroData().setStartDate(new Date());

    for (Player online : Bukkit.getOnlinePlayers()) {
      online.getInventory().clear();
      online.setLevel(0);
      online.setExp(0);
      online.setHealth(20);
      online.setFoodLevel(20);
    }
  }

  private void rememberStart(int counter) {
    for (Player online : Bukkit.getOnlinePlayers()) {
      online.sendTitle(counter / 60 + "", "Minuten bis zum Start");
    }
  }

  public void startIn(int minutes) {
    final Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.add(Calendar.MINUTE, minutes);
    Data.getInstance().getVaroData().setStartDate(c.getTime());
    setCounter(minutes * 60);
  }
}
