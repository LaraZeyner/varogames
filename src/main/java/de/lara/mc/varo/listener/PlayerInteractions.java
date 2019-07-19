package de.lara.mc.varo.listener;

import de.lara.mc.varo.util.objectmanager.OnlinePlayerManager;
import de.lara.mc.varo.storage.Messages;
import de.lara.mc.varo.util.Messenger;
import de.lara.mc.varo.util.objectmanager.VaroManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * Created by Lara on 05.06.2019 for varogames
 */
public class PlayerInteractions implements Listener {
  @EventHandler
  public void onInteract(PlayerInteractEvent interactEvent) {
    final Player target = interactEvent.getPlayer();
    if (OnlinePlayerManager.isNotRegistered(target)) {
      Messenger.sendMessage(target, Messages.NOT_REGISTERED_YET);
      interactEvent.setCancelled(true);
    } else {

      final Material type = interactEvent.getClickedBlock().getType();
      if (interactEvent.getAction().equals(Action.RIGHT_CLICK_BLOCK) && (type.equals(Material.CHEST) || type.equals(Material.ENDER_CHEST) || type.equals(Material.TRAPPED_CHEST))) {
        //TODO (Abgie) 24.06.2019: Open Teamchest
        //TODO (Abgie) 24.06.2019: Except Spawn + Created from Generator
      }

    }
  }

  @EventHandler
  public void onInteractAtEntity(PlayerInteractAtEntityEvent interactAtEntityEvent) {
    final Player target = interactAtEntityEvent.getPlayer();
    if (OnlinePlayerManager.isNotRegistered(target)) {
      Messenger.sendMessage(target, Messages.NOT_REGISTERED_YET);
      interactAtEntityEvent.setCancelled(true);
    }
  }

  @EventHandler
  public void onInteractEntity(PlayerInteractEntityEvent interactEntityEvent) {
    final Player target = interactEntityEvent.getPlayer();
    if (OnlinePlayerManager.isNotRegistered(target)) {
      Messenger.sendMessage(target, Messages.NOT_REGISTERED_YET);
      interactEntityEvent.setCancelled(true);
    }
  }

  @EventHandler public void onDrop(PlayerDropItemEvent dropEvent) {
    if (!VaroManager.isStarted()) {
      dropEvent.setCancelled(true);
    }
  }

  @EventHandler public void onConsume(PlayerItemConsumeEvent consumeEvent) {
    if (!VaroManager.isStarted()) {
      consumeEvent.setCancelled(true);
    }
  }

  @EventHandler public void onPickup(PlayerPickupItemEvent pickupEvent) {
    if (!VaroManager.isStarted()) {
      pickupEvent.setCancelled(true);
    }
  }

  // @EventHandler public void onItemHeld(PlayerItemHeldEvent itemHeldEvent) { }

}
