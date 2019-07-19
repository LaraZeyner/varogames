package de.lara.mc.varo.gameplay.objects;

import java.io.Serializable;
import java.util.UUID;

import de.lara.mc.varo.util.mcutils.UUIDUtils;

/**
 * Created by Lara on 05.06.2019 for varogames
 */
public class VaroPlayerImpl implements Serializable {
  private static final long serialVersionUID = -3565416446096569816L;

  private final UUID uuid;
  private final VaroStrikeStatus strikeStatus;
  private boolean alive;
  private int playTime;

  private transient long combat, lastJoined;

  VaroPlayerImpl(UUID uuid, VaroStrikeStatus strikeStatus, boolean alive, int playTime) {
    this.uuid = uuid;
    this.strikeStatus = strikeStatus;
    this.alive = alive;
    this.playTime = playTime;
  }

  //<editor-fold desc="getter and setter">
  public boolean isCombat() {
    return System.currentTimeMillis() - combat < 10_000L;
  }

  public long getLastJoined() {
    return lastJoined;
  }

  public String getName() {
    return UUIDUtils.getName(uuid);
  }

  public int getPlayTime() {
    return playTime;
  }

  public VaroStrikeStatus getStrikeStatus() {
    return strikeStatus;
  }

  public UUID getUuid() {
    return uuid;
  }

  public boolean isAlive() {
    return alive;
  }

  protected void setAlive(boolean alive) {
    this.alive = alive;
  }

  public void setCombat(long combat) {
    this.combat = combat;
  }

  public void setLastJoined(long lastJoined) {
    this.lastJoined = lastJoined;
  }

  protected void setPlayTime(int playTime) {
    this.playTime = playTime;
  }
}
