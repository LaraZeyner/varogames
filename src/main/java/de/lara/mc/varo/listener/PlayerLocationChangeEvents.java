package de.lara.mc.varo.listener;

import de.lara.mc.varo.security.GlobalPermission;
import de.lara.mc.varo.storage.Data;
import de.lara.mc.varo.util.objectmanager.OnlinePlayerManager;
import de.lara.mc.varo.util.objectmanager.VaroManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

/**
 * Created by Lara on 05.06.2019 for varogames
 */
public class PlayerLocationChangeEvents implements Listener {

  @EventHandler
  public void onMove(PlayerMoveEvent moveEvent) {
    if (OnlinePlayerManager.isSpectator(moveEvent.getPlayer())) {
      moveEvent.setCancelled(true);
    }
  }

  @EventHandler
  public void onPortal(PlayerPortalEvent portalEvent) {
    if (!VaroManager.isStarted()) {
      portalEvent.setCancelled(true);
    }
  }

  @EventHandler public void onWorldChange(PlayerChangedWorldEvent worldChangeEvent) { }

  @EventHandler
  public void onTeleport(PlayerTeleportEvent teleportEvent) {
    final PlayerTeleportEvent.TeleportCause teleportCause = teleportEvent.getCause();
    if (teleportCause.equals(PlayerTeleportEvent.TeleportCause.SPECTATE) ||
        teleportCause.equals(PlayerTeleportEvent.TeleportCause.UNKNOWN)) {
      teleportEvent.setCancelled(true);

    } else if ((teleportCause.equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL) ||
        teleportCause.equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) ||
        teleportCause.equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) &&
        Data.getInstance().getVaroData().getGlobalPermissions().hasPermission(GlobalPermission.TELEPORT)) {
      teleportEvent.setCancelled(true);
    }


    switch (teleportCause) {
      case ENDER_PEARL:
      case NETHER_PORTAL:
      case END_PORTAL:
      case PLUGIN:
      case COMMAND:

      case SPECTATE:
        teleportEvent.setCancelled(true);
      case UNKNOWN:
        break;
      default:
        teleportEvent.setCancelled(true);
    }
  }

  @EventHandler public void onFlight(PlayerToggleFlightEvent toggleFlightEvent) { }
  @EventHandler public void onSneak(PlayerToggleSneakEvent toggleSneakEvent) { }
  @EventHandler public void onSprint(PlayerToggleSprintEvent toggleSprintEvent) { }

}
