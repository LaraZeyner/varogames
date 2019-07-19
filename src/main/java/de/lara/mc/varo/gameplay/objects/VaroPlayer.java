package de.lara.mc.varo.gameplay.objects;

import java.util.Date;
import java.util.UUID;

import de.lara.mc.varo.storage.VaroData;
import de.lara.mc.varo.util.objectmanager.PlayerManager;
import de.lara.mc.varo.storage.Data;
import de.lara.mc.varo.util.Messenger;
import de.lara.mc.varo.util.mcutils.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Lara on 01.06.2019 for varogames
 */
public class VaroPlayer extends VaroPlayerImpl {
  private static final long serialVersionUID = 4698926815291931492L;

  public VaroPlayer(UUID uuid) {
    this(uuid, true, 0, new VaroStrikeStatus());
  }

  public VaroPlayer(UUID uuid, boolean alive, int playTime, VaroStrikeStatus strikeStatus) {
    super(uuid, strikeStatus, alive, playTime);
  }

  @Override
  public void setAlive(boolean alive) {
    super.setAlive(alive);
    save();
  }

  @Override
  protected void setPlayTime(int playTime) {
    super.setPlayTime(playTime);
    save();
  }

  public void addStrike(VaroStrike varoStrike) {
    Messenger.broadcast("Strike für Spieler &c" + this.getName() + "&e erteilt.");
    getStrikeStatus().addStrike(varoStrike);
    save();
  }

  public void addPlayTime() {
    setPlayTime(getPlayTime() + (int) ((System.currentTimeMillis() - getLastJoined()) / 60));
    save();
  }

  public void addPlayTime(int playTime) {
    setPlayTime(getPlayTime() + playTime);
    save();
  }

  public boolean canSpectate() {
    return getTeam().isAlive() && !isAlive();
  }

  public long getLogoutTime() {
    return getAvailablePlayTime() + getLastJoined();
  }

  public VaroPlayer getMate() {
    final VaroTeam varoTeam = getTeam();
    if (varoTeam.getPlayers().size() == 2) {
      return (varoTeam.getPlayers().get(0).equals(this)) ? varoTeam.getPlayers().get(1) : varoTeam.getPlayers().get(0);
    }
    return null;
  }

  public Player getPlayer() {
    return Bukkit.getPlayer(getUuid());
  }

  public int getAvailablePlayTime() {
    final VaroData varoData = Data.getInstance().getVaroData();
    final Date startDate = varoData.getStartDate();
    final long seconds = (System.currentTimeMillis() - startDate.getTime()) / 1_000L;
    final int available = (int) (seconds / 100);
    return available - getPlayTime();
  }

  public VaroTeam getTeam() {
    final String playerName = UUIDUtils.getName(getUuid());
    final VaroPlayer varoPlayer = PlayerManager.determinePlayer(playerName);
    return PlayerManager.determineTeamOfPlayer(varoPlayer);
  }

  public void save() {
    Data.getInstance().getSql().setPlayer(this);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof VaroPlayer && ((VaroPlayer) obj).getUuid().equals(getUuid());
  }
}
