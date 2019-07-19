package de.lara.mc.varo.listener;

import de.lara.mc.varo.util.objectmanager.PlayerManager;
import de.lara.mc.varo.util.objectmanager.VaroManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

/**
 * Created by Lara on 05.06.2019 for varogames
 */
public class InventoryEvents implements Listener {

  @EventHandler
  public void onClick(InventoryClickEvent clickEvent) {
    //TODO (Abgie) 24.06.2019: AdministratorMenü
  }

  @EventHandler
  public void onCreative(InventoryCreativeEvent creativeEvent) {
    if (VaroManager.isStarted()) {
      creativeEvent.setCancelled(true);
    }
  }

  @EventHandler
  public void onMove(InventoryMoveItemEvent moveItemEvent) {
    final Player player = (Player) moveItemEvent.getSource().getHolder();
    if (!PlayerManager.isRegistered(player.getName())) {
      moveItemEvent.setCancelled(true);
    }
  }

  @EventHandler
  public void onPickup(InventoryPickupItemEvent pickupItemEvent) {
    if (!VaroManager.isStarted()) {
      pickupItemEvent.setCancelled(true);
    }
  }

  // @EventHandler public void onClose(InventoryCloseEvent closeEvent) { }
  // @EventHandler public void onDrag(InventoryDragEvent dragEvent) { }
  // @EventHandler public void onInteract(InventoryInteractEvent interactEvent) { }
  // @EventHandler public void onOpen(InventoryOpenEvent openEvent) { }

}
