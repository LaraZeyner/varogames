package de.lara.mc.varo.io.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.lara.mc.varo.storage.Data;
import de.lara.mc.varo.util.Messenger;

/**
 * Created by Lara on 28.05.2019 for varo
 */
public class SQLManager extends VaroPlayerStorageSQLHandler {

  public SQLManager() {
    init(connect());
  }

  public Map<UUID, String> getPlayers() {
    final SQLData sqlManager = Data.getInstance().getSql();
    final Map<UUID, String> players = new HashMap<>();
    final Connection connection = sqlManager.getSql().getConnection();
    try (final PreparedStatement statement = connection.prepareStatement("SELECT UUID, name FROM Playerstatus");
         final ResultSet resultSet = statement.executeQuery()) {

      while (resultSet.next()) {
        final String uuid = resultSet.getString(1);
        final String name = resultSet.getString(2);
        players.put(UUID.fromString(uuid), name);
      }

    } catch (final SQLException ex) {
      Messenger.administratorMessage(ex.getMessage());
    }
    return players;
  }
}
