package de.lara.mc.varo.io.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.lara.mc.varo.Varo;
import de.lara.mc.varo.storage.Const;
import de.lara.mc.varo.storage.Messages;
import de.lara.mc.varo.io.FileManager;

/**
 * Created by Lara on 28.05.2019 for varo
 */
public class SQLData {
  private final Logger logger = Logger.getLogger(getClass().getName());
  private Connection connection;
  private final SQL sql;

  SQLData() {
    final Map<String, String> sqlConfig = FileManager.loadConfig(Const.SQL_CONFIG);
    final short port = Short.parseShort(Objects.requireNonNull(sqlConfig).get("port"));
    final String hostname = sqlConfig.get("host");
    final String username = sqlConfig.get("username");
    final String password = sqlConfig.get("password");
    final String database = sqlConfig.get("database");
    this.sql = new SQL(hostname, port, username, password, database);
  }

  public Connection connect() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      this.connection = DriverManager.getConnection(sql.getUrl(), sql.getUsername(), sql.getPassword());
    } catch (ClassNotFoundException | SQLException ex) {
      getLogger().log(Level.SEVERE, Messages.MYSQL_CONNECTION_FAILED, ex);
      Varo.getInstance().onDisable();
    }
    return connection;
  }

  public void init(Connection connection) {
    if (connection != null) {
      try (final PreparedStatement stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Varo" +
          "(" +
          "UUID varchar(50) unique, " +
          "type varchar(10)," +
          "alive boolean," +
          "teamname varchar(20)," +
          "playtime int," +
          "strikes varchar(2000)" +
          ");")) {
        stmt.executeUpdate();
      } catch (SQLException ex) {
        logger.log(Level.SEVERE, Messages.MYSQL_CONNECTION_FAILED, ex);
        Varo.getInstance().onDisable();
      }
    } else {
      getLogger().log(Level.SEVERE, Messages.MYSQL_CONNECTION_FAILED);
      Varo.getInstance().onDisable();
    }
  }

  private boolean checkConnection() throws SQLException {
    if (sql.getConnection() != null) {
      return !sql.getConnection().isClosed();
    }
    return false;
  }

  public void disconnect() {
    try {
      if (checkConnection()) {
        sql.getConnection().close();
      }
    } catch (final SQLException ex) {
      getLogger().log(Level.SEVERE, Messages.MYSQL_CONNECTION_FAILED, ex);
      Varo.getInstance().onDisable();
    }
  }

  public Connection getConnection() {
    return connection;
  }

  SQL getSql() {
    return sql;
  }

  private Logger getLogger() {
    return logger;
  }
}
