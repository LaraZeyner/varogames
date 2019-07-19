package de.lara.mc.varo.listener;

import de.lara.mc.varo.gameplay.enums.KickReason;
import de.lara.mc.varo.gameplay.statuschanges.EndVaro;
import de.lara.mc.varo.gameplay.tasks.KillScheduler;
import de.lara.mc.varo.util.Messenger;
import de.lara.mc.varo.util.objectmanager.PlayerManager;
import de.lara.mc.varo.gameplay.objects.VaroPlayer;
import de.lara.mc.varo.util.objectmanager.VaroManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.projectiles.ProjectileSource;

/**
 * Created by Lara on 05.06.2019 for varogames
 */
public class PlayerDamageEvents implements Listener {

  @EventHandler
  public void onDamageByPlayer(EntityDamageByEntityEvent damageEvent) {
    final Entity target = damageEvent.getEntity();
    if (target instanceof Player) {
      final Player damager = determineDamager(damageEvent);
      if (damager != null) {
        evaluateDamageByPlayer(damageEvent);
      }
    }
  }

  @EventHandler
  public void onDeath(PlayerDeathEvent deathEvent) {
    final Player target = deathEvent.getEntity();
    final VaroPlayer targetVaroPlayer = PlayerManager.determinePlayer(target.getName());
    final Player killer = target.getKiller();
    deathEvent.setDeathMessage("&a" + killer.getName() + "&e -> &c" + target.getName());

    if (VaroManager.isStarted()) {
      Bukkit.getOnlinePlayers().forEach(online ->
          online.playSound(online.getLocation(), Sound.AMBIENCE_THUNDER, 10, 0.8f));
      targetVaroPlayer.setAlive(false);
      if (VaroManager.isVaroEnded()) {
        EndVaro.invoke();
      }

      new KillScheduler(targetVaroPlayer).invoke();
      if (target.isOnline()) {
        target.kickPlayer(Messenger.kickMessage(KickReason.KILLED, target, killer.getName()));
      }

    } else {
      target.teleport(target.getWorld().getSpawnLocation().add(0, -5, 0));
    }
  }

  @EventHandler
  public void onRespawn(PlayerRespawnEvent respawnEvent) {
    if (VaroManager.isStarted()) {
      final Player target = respawnEvent.getPlayer();
      final Player killer = target.getKiller();
      if (target.isOnline()) {
        target.kickPlayer(Messenger.kickMessage(KickReason.KILLED, target, killer.getName()));
      }
    }
  }
  @EventHandler public void onItemDamage(PlayerItemDamageEvent itemDamageEvent) { }

  private void evaluateDamageByPlayer(EntityDamageByEntityEvent damageEvent) {
    final Player target = (Player) damageEvent.getEntity();
    final VaroPlayer varoTargetPlayer = PlayerManager.determinePlayer(target.getName());
    if (varoTargetPlayer != null) {
      varoTargetPlayer.setCombat(System.currentTimeMillis());
    }
  }

  private Player determineDamager(EntityDamageByEntityEvent damageEvent) {
    final Entity damager = damageEvent.getDamager();


    if (damager instanceof Player) {
      return (Player) damager;
    } else {
      if (damager instanceof Arrow) {
        return determineArrowShooter((Arrow) damager);
      }
    }
    return null;
  }

  private Player determineArrowShooter(Arrow damager) {
    final ProjectileSource arrowSource = damager.getShooter();
    if (arrowSource instanceof Player) {
      return (Player) arrowSource;
    }
    return null;
  }

}