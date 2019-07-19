package de.lara.mc.varo.gameplay.tasks;

import de.lara.mc.varo.Varo;
import de.lara.mc.varo.gameplay.objects.VaroPlayer;
import org.bukkit.Bukkit;

/**
 * Created by Lara on 10.06.2019 for varogames
 */
abstract class SchedulerImpl {
  private final VaroPlayer varoPlayer;
  private int counter, id;

  SchedulerImpl(VaroPlayer varoPlayer) {
    this.counter = 0;
    this.id = 0;
    this.varoPlayer = varoPlayer;
  }

  public abstract void invoke();

  void increment(int amount) {
    counter += amount;
  }

  void increment() {
    increment(1);
  }

  void decrement(int amount) {
    increment(-amount);
  }

  void decrement() {
    increment(-1);
  }

  int getCounter() {
    return counter;
  }

  void setCounter(int counter) {
    this.counter = counter;
  }

  VaroPlayer getVaroPlayer() {
    return varoPlayer;
  }

  void schedule(int startCounter, Runnable runnable) {
    this.counter = startCounter;
    this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Varo.getInstance(), runnable, 20L, 20L);
  }

  void cancel() {
    Bukkit.getScheduler().cancelTask(id);
  }
}
