package de.lara.mc.varo.io.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.lara.mc.varo.gameplay.objects.VaroPlayer;
import de.lara.mc.varo.gameplay.objects.VaroStrike;
import de.lara.mc.varo.gameplay.objects.VaroStrikeStatus;
import de.lara.mc.varo.gameplay.objects.VaroTeam;
import de.lara.mc.varo.storage.Data;
import de.lara.mc.varo.util.Messenger;
import de.lara.mc.varo.util.mcutils.InventorySaver;
import de.lara.mc.varo.util.objectmanager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Created by Lara on 01.06.2019 for varogames
 */
public class VaroPlayerStorageSQLHandler extends SQLData {

  public void addPlayer(VaroPlayer player) {
    try (final PreparedStatement stmt = getConnection().
        prepareStatement("INSERT INTO Varo(UUID, alive, teamname, playtime, strikes) VALUES (?, ?, ?, ?, ?)")) {

      stmt.setString(1, player.getUuid().toString());
      stmt.setBoolean(2, true);
      stmt.setString(3, PlayerManager.determineTeamOfPlayer(player).getTeamName());
      stmt.setInt(4, 0);
      stmt.setString(5, player.getStrikeStatus().toString());
      stmt.executeUpdate();
      updateTeams();

    } catch (SQLException ex) {
      Messenger.administratorMessage(ex.getMessage());
    }
  }

  public void removePlayer(VaroPlayer player) {
    try (final PreparedStatement stmt = getConnection().
        prepareStatement("DELETE FROM Varo WHERE UUID = ?")) {

      stmt.setString(1, player.getUuid().toString());
      stmt.executeUpdate();
      updateTeams();

    } catch (SQLException ex) {
      Messenger.administratorMessage(ex.getMessage());
    }
  }

  public void setPlayer(VaroPlayer varoPlayer) {
    final Player player = varoPlayer.getPlayer();
    if (player.isOnline()) {
      varoPlayer.getStrikeStatus().setLocation(player.getLocation());
      if (varoPlayer.getStrikeStatus().getStrikes().size() < 2) {
        varoPlayer.getStrikeStatus().setInventory(player.getInventory());
      }
    }

    try (final PreparedStatement stmt = getConnection().
        prepareStatement("UPDATE Varo SET alive = ?, teamname = ?, playtime = ?, strikes = ? WHERE UUID = ?")) {

      stmt.setBoolean(1, varoPlayer.isAlive());
      stmt.setString(2, varoPlayer.getTeam().getTeamName());
      stmt.setInt(3, varoPlayer.getPlayTime());
      stmt.setString(4, varoPlayer.getStrikeStatus().toString());
      stmt.setString(5, varoPlayer.getUuid().toString());
      stmt.executeUpdate();
      updateTeams();

    } catch (SQLException ex) {
      Messenger.administratorMessage(ex.getMessage());
    }
  }


  private VaroStrikeStatus determineStrikeStatus(ResultSet resultSet) throws SQLException {
    final String statusString = resultSet.getString(5);
    final VaroStrikeStatus strikeStatus = new VaroStrikeStatus();
    final int amount = Integer.parseInt(String.valueOf(statusString.charAt(0)));

    determineDate(statusString, strikeStatus, amount);
    determineLocation(statusString, strikeStatus, amount);
    determineInventory(statusString, strikeStatus, amount);

    return strikeStatus;
  }

  private void determineInventory(String statusString, VaroStrikeStatus strikeStatus, int amount) {
    String inventoryString = statusString.split("/")[amount + 5];
    inventoryString = inventoryString.replace("[", "").replace("]", "");
    final Inventory inventory = InventorySaver.fromBase64(inventoryString);
    strikeStatus.setInventory(inventory);
  }

  private void determineLocation(String statusString, VaroStrikeStatus strikeStatus, int amount) {
    final String world = statusString.split("/")[amount + 2];
    final String xString = statusString.split("/")[amount + 2];
    final int x = Integer.parseInt(xString.substring(2));
    final String yString = statusString.split("/")[amount + 3];
    final int y = Integer.parseInt(yString.substring(2));
    final String zString = statusString.split("/")[amount + 4];
    final int z = Integer.parseInt(zString.substring(2));

    final Location location = new Location(Bukkit.getWorld(world.substring(2)), x, y, z);
    strikeStatus.setLocation(location);
  }

  private void determineDate(String statusString, VaroStrikeStatus strikeStatus, int amount) {
    if (amount > 0) {
      for (int i = 1; i < amount; i++) {
        final String strikeString = statusString.split("/")[i];
        final String dateString = strikeString.split(" - ")[0];
        final String reasonString = strikeString.split(" - ")[1];
        final int day = Integer.parseInt(dateString.substring(0, 2));
        final int month = Integer.parseInt(dateString.substring(3, 5));
        final int hour = Integer.parseInt(dateString.substring(7, 9));
        final int minute = Integer.parseInt(dateString.substring(10, 12));
        final int second = Integer.parseInt(dateString.substring(13, 15));
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2019, month - 1, day, hour, minute, second);
        final Date date = calendar.getTime();

        final VaroStrike strike = new VaroStrike(date, reasonString);
        strikeStatus.addStrike(strike);
      }
    }
  }

  public List<VaroTeam> determineTeams() {
    final Map<VaroPlayer, String> playerMap = determinePlayerMap();
    final List<VaroTeam> teams = new ArrayList<>();

    for (VaroPlayer varoPlayer : playerMap.keySet()) {
      final String teamName = playerMap.get(varoPlayer);
      boolean b = true;
      for (VaroTeam varoTeam : teams) {
        if (varoTeam.getTeamName().equalsIgnoreCase(teamName)) {
          varoTeam.addPlayer(varoPlayer);
          b = false;
        }
      }
      if (b) {
        final VaroTeam team = new VaroTeam(teamName, varoPlayer);
        teams.add(team);
      }
    }
    return teams;
  }

  private Map<VaroPlayer, String> determinePlayerMap() {
    final Map<VaroPlayer, String> players = new HashMap<>();
    if (getConnection() != null) {
      try (final PreparedStatement stmt = getConnection().
          prepareStatement("SELECT UUID, alive, teamname, playtime, strikes FROM Varo");
           final ResultSet resultSet = stmt.executeQuery()) {
        while (resultSet.next()) {
          final VaroPlayer player = determineVaroPlayer(resultSet);
          players.put(player, resultSet.getString(3));
        }

      } catch (final SQLException ex) {
        Messenger.administratorMessage(ex.getMessage());
      }
    }
    return players;
  }

  private VaroPlayer determineVaroPlayer(ResultSet resultSet) throws SQLException {
    final String uuid = resultSet.getString(1);
    final boolean alive = resultSet.getBoolean(2);
    final int playtime = resultSet.getInt(4);
    final VaroStrikeStatus strikeStatus = determineStrikeStatus(resultSet);
    return new VaroPlayer(UUID.fromString(uuid), alive, playtime, strikeStatus);
  }

  public void updateTeams() {
    final SQLManager sql = Data.getInstance().getSql();
    final List<VaroTeam> teams = (sql != null ? sql.determineTeams() :
        new VaroPlayerStorageSQLHandler().determineTeams());
    Data.getInstance().getTeams().addAll(teams);
  }
}
