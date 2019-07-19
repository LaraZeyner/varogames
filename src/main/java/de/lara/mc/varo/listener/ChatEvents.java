package de.lara.mc.varo.listener;

import de.lara.mc.varo.storage.VaroData;
import de.lara.mc.varo.util.objectmanager.PlayerManager;
import de.lara.mc.varo.storage.Data;
import de.lara.mc.varo.util.Messenger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by Lara on 05.06.2019 for varogames
 */
public class ChatEvents implements Listener {

  @EventHandler
  public void onChat(AsyncPlayerChatEvent chatEvent) {
    final Player chatter = chatEvent.getPlayer();
    final String msg = chatEvent.getMessage();

    if (msg.startsWith("@t")) {
      Messenger.teamMessage(chatter, "[TEAM] " + msg.substring(2));
      chatEvent.setCancelled(true);
    } else if (!PlayerManager.determinePlayer(chatter.getName()).isAlive()) {
      Messenger.teamMessage(chatter, "[TEAM] " + msg);
      chatEvent.setCancelled(true);
    }
  }

  @EventHandler
  public void onCommand(PlayerCommandPreprocessEvent commandEvent) {
    //TODO (Abgie) 24.06.2019: Whats going on
    final VaroData varoData = Data.getInstance().getVaroData();
    if (!varoData.isPrepared() && !commandEvent.getMessage().startsWith("prepare") &&
        !commandEvent.getPlayer().isOp()) {
      commandEvent.setCancelled(true);
    }
  }
}
