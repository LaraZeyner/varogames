package de.lara.mc.varo.gameplay.objects;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lara on 01.06.2019 for varogames
 */
public class VaroStrike {
  private final Date time;
  private final String reason;

  public VaroStrike(Date time, String reason) {
    this.time = time;
    this.reason = reason;
  }

  public Date getTime() {
    return time;
  }

  public String getReason() {
    return reason;
  }

  @Override
  public String toString() {
    return new SimpleDateFormat("dd.MM. HH:mm:ss").format(time) + " - " + reason;
  }
}
