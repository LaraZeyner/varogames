package de.lara.mc.varo.listener;

import de.lara.mc.varo.gameplay.enums.KickReason;
import de.lara.mc.varo.util.Messenger;
import de.lara.mc.varo.util.objectmanager.VaroManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

/**
 * Created by Lara on 05.06.2019 for varogames
 */
public class PlayerEvents implements Listener {
  @EventHandler public void onAchievement(PlayerAchievementAwardedEvent achievementEvent) {
    if (!VaroManager.isStarted()) {
      achievementEvent.setCancelled(true);
    }
  }

  @EventHandler public void onArmorStand(PlayerArmorStandManipulateEvent armorStandEvent) {
    if (!VaroManager.isStarted()) {
      armorStandEvent.setCancelled(true);
    }
  }

  @EventHandler public void onBedEnter(PlayerBedEnterEvent bedEnterEvent) {
    bedEnterEvent.setCancelled(true);
  }

  @EventHandler public void onExperience(PlayerExpChangeEvent experienceEvent) {
    if (!VaroManager.isStarted()) {
      experienceEvent.setAmount(0);
    }
  }
  @EventHandler public void onFish(PlayerFishEvent fishEvent) {
    if (!VaroManager.isStarted()) {
      fishEvent.setCancelled(true);
    }
  }

  @EventHandler public void onResourcePack(PlayerResourcePackStatusEvent resourcePackEvent) {
    if (!resourcePackEvent.getStatus().equals(PlayerResourcePackStatusEvent.Status.DECLINED)) {
      final Player player = resourcePackEvent.getPlayer();
      player.kickPlayer(Messenger.kickMessage(KickReason.RESOURCE_PACK, player));
    }

  }

}
