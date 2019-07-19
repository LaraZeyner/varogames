package de.lara.mc.varo.commands;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import de.lara.mc.varo.util.objectmanager.PlayerManager;
import de.lara.mc.varo.gameplay.enums.KickReason;
import de.lara.mc.varo.gameplay.objects.VaroPlayer;
import de.lara.mc.varo.gameplay.objects.VaroStrike;
import de.lara.mc.varo.storage.Data;
import de.lara.mc.varo.util.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Lara on 10.06.2019 for varogames
 */
public class Strike implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender.isOp()) {
      if (args.length >= 2) {
        final String targetName = args[0];
        final VaroPlayer target = PlayerManager.determinePlayer(targetName);

        if (target != null) {
          final String reason = Arrays.stream(args, 1, args.length).collect(Collectors.joining());
          final VaroStrike varoStrike = new VaroStrike(new Date(), reason);
          target.addStrike(varoStrike);
          if (target.getStrikeStatus().getStrikes().size() == 3) {
            target.setAlive(false);
            if (target.getPlayer().isOnline()) {
              target.getPlayer().kickPlayer(Messenger.kickMessage(KickReason.STRIKED, target.getPlayer()));
            }
          }

          Data.getInstance().getSql().setPlayer(target);
        }

      }
    }
    return false;
  }
}
