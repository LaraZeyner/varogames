package de.lara.mc.varo.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.lara.mc.varo.gameplay.enums.KickReason;
import de.lara.mc.varo.gameplay.objects.VaroPlayer;
import de.lara.mc.varo.gameplay.objects.VaroTeam;
import de.lara.mc.varo.gameplay.tasks.KillScheduler;
import de.lara.mc.varo.storage.Messages;
import de.lara.mc.varo.util.objectmanager.OnlinePlayerManager;
import de.lara.mc.varo.util.objectmanager.PlayerManager;
import de.lara.mc.varo.util.objectmanager.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Lara on 01.06.2019 for varogames
 */
public final class Messenger {
  private static final Logger logger;

  static {
    logger = Logger.getLogger(Messenger.class.getName());
  }

  public static void administratorMessage(String msg) {
    for (Player player : OnlinePlayerManager.determineAdministrators()) sendMessage(player, "&a&l" + msg);
    logger.log(Level.INFO, msg);
  }

  public static void sendMessage(Player player, String msg) {
    player.sendMessage(Messages.PREFIX + msg);
  }

  public static String kickMessage(KickReason kickReason, Player target, Object... objects) {
    if (kickReason.equals(KickReason.STRIKED)) {
      final VaroPlayer targetPlayer = PlayerManager.determinePlayer(target.getName());
      return Messages.PREFIX + "&4&l Du bist aus Varo ausgeschieden. \n" +
          "Grund:&e 3 Strikes \n" +
          "\n&c1. Strike:&e " + targetPlayer.getStrikeStatus().getStrikes().get(0).getReason() +
          "\n&c2. Strike:&e " + targetPlayer.getStrikeStatus().getStrikes().get(1).getReason() +
          "\n&c3. Strike:&e " + targetPlayer.getStrikeStatus().getStrikes().get(2).getReason();

    } else if (kickReason.equals(KickReason.NOT_PREPARED)) {
      return Messages.PREFIX + "&4&l Du kannst noch nicht beitreten. \n" +
          "&e Varo ist noch nicht vorbereitet.";

    } else if (kickReason.equals(KickReason.TIME_OVER)) {
      return Messages.PREFIX + "&4&l Deine Spielzeit ist abgelaufen. \n" +
          "&e Du kannst pro Stunde weitere 36 Sekunden spielen.";

    } else if (kickReason.equals(KickReason.MATE_OFFLINE)) {
      return Messages.PREFIX + "&4&l Dein Teammate ist offline. \n" +
          "&e Daher wurdest du auch gekickt.";

    } else if (kickReason.equals(KickReason.KILLED)) {
      return Messages.PREFIX + "&4&l Du bist aus Varo ausgeschieden. \n" +
      "Grund:&e Getötet durch " + "&c " + objects[0] + " \n";

    } else if (kickReason.equals(KickReason.RESOURCE_PACK)) {
      return Messages.PREFIX + "&4&l Du hast das falsche Resource Pack. \n" +
          "Bei Fragen wende dich an den Support.";

    } else if (kickReason.equals(KickReason.NOT_REGISTERED)) {
      return Messages.PREFIX + "&4&l Du hast dich nicht angemeldet.";

    } else if (kickReason.equals(KickReason.SERVER_OFFLINE)) {
      return Messages.PREFIX + "&4&l Der Server ist offline. Melde dich im Support. Deine volle Spielzeit wird wiederhergestellt und das Backup der letzten Minute wird geladen.";
    }

    return null;
  }

  public static void teamMessage(Player player, String msg) {
    final VaroPlayer varoPlayer = PlayerManager.determinePlayer(player.getName());
    teamMessage(varoPlayer.getTeam(), msg);
  }

  public static void teamMessage(String teamName, String msg) {
    final VaroTeam varoTeam = TeamManager.determineTeam(teamName);
    teamMessage(varoTeam, msg);
  }

  public static void teamMessage(VaroTeam team, String msg) {
    team.getPlayers().forEach(varoPlayer -> sendMessage(varoPlayer.getPlayer(), msg));
  }

  public static void broadcast(String msg) {
    Bukkit.broadcastMessage(Messages.PREFIX + msg);
  }

}
