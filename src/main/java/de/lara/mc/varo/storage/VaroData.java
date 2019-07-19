package de.lara.mc.varo.storage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import de.lara.mc.varo.io.FileManager;
import de.lara.mc.varo.security.GlobalPermissionManager;
import de.lara.mc.varo.util.Messenger;

/**
 * Created by Lara on 10.06.2019 for varogames
 */
public class VaroData {
  private final GlobalPermissionManager globalPermissions;
  private final Map<String, String> data;

  VaroData() {
    this.globalPermissions = new GlobalPermissionManager();
    this.data = FileManager.loadConfig(Const.VARO_CONFIG);
  }

  public int getMaxStarters() {
    try {
      return Integer.parseInt(data.get("starters"));
    } catch (NumberFormatException ignored) {
      Messenger.administratorMessage(Messages.CONFIG_ERROR);
      setMaxStarters(28);
      return 28;
    }
  }

  public boolean isPrepared() {
    return data.get("prepared").equals("true");
  }

  public GlobalPermissionManager getGlobalPermissions() {
    return globalPermissions;
  }

  public int getSpawnDistance() {
    try {
      return Integer.parseInt(data.get("spawnDistance"));
    } catch (NumberFormatException ignored) {
      Messenger.administratorMessage(Messages.CONFIG_ERROR);
      setSpawnDistance(25);
      return 25;
    }
  }

  public Date getStartDate() {
    try {
      final String pattern = "dd/MM/yyyy HH:mm";
      final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
      return dateFormat.parse(data.get("startDate"));

    } catch (ParseException ignored) {
      Messenger.administratorMessage(Messages.CONFIG_ERROR);
      return null;
    }
  }

  public void setMaxStarters(int maxStarters) {
    data.put("starters", String.valueOf(maxStarters));
    save();
  }

  public void setPrepared(boolean prepared) {
    data.put("prepared", String.valueOf(prepared));
    save();
  }

  //TODO (Abgie) 13.07.2019: Command for Spawndistance
  public void setSpawnDistance(int spawnDistance) {
    data.put("starters", String.valueOf(spawnDistance));
    save();
  }

  public void setStartDate(Date startDate) {
    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    data.put("startDate", dateFormat.format(startDate));
    save();
  }

  private void save() {
    FileManager.writeIntoConfig(Const.VARO_CONFIG, data);
  }

}
