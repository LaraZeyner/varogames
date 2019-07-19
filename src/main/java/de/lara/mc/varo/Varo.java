package de.lara.mc.varo;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.lara.mc.varo.gameplay.enums.KickReason;
import de.lara.mc.varo.gameplay.objects.VaroPlayer;
import de.lara.mc.varo.storage.Data;
import de.lara.mc.varo.storage.Messages;
import de.lara.mc.varo.util.Messenger;
import de.lara.mc.varo.util.Registerer;
import de.lara.mc.varo.util.objectmanager.OnlinePlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Lara on 28.05.2019 for varo
 */
public class Varo extends JavaPlugin {
  private static Varo instance;
  private final Logger logger = Logger.getLogger(getClass().getName());

  public static Varo getInstance() {
    return instance;
  }

  @Override
  public void onEnable() {
    logger.log(Level.INFO, Messages.ENABLING);

    instance = this;
    final Data data = Data.getInstance();
    data.getSql().updateTeams();
    data.getCache().putAll(data.getSql().getPlayers());
    Registerer.performRegistration();

    logger.log(Level.INFO, Messages.SUCCESSFULLY_STARTED);
  }


  @Override
  public void onDisable() {
    logger.log(Level.INFO, Messages.DISABLING);
    saveOnlinePlayers();
    Data.getInstance().getSql().disconnect();
    logger.log(Level.INFO, Messages.SUCCESSFULLY_DISABLED);
  }

  private void saveOnlinePlayers() {
    for (VaroPlayer varoPlayer : OnlinePlayerManager.determineAlivePlayers()) {
      varoPlayer.addPlayTime();
      final Player player = varoPlayer.getPlayer();
      player.kickPlayer(Messenger.kickMessage(KickReason.SERVER_OFFLINE, player));
    }
  }
}
