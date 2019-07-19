package de.lara.mc.varo.storage;

/**
 * Created by Lara on 28.05.2019 for varo
 */
public final class Messages {
  //<editor-fold desc="Log-Messages">
  public static final String ENABLING = "Varo startet...";
  public static final String DISABLING = "Varo fährt herunter...";
  public static final String MYSQL_CONNECTION_FAILED = "Verbindung zur Datenbank konnte nicht aufgebaut werden";
  public static final String MYSQL_DATA_NOT_SET = "Datenbank-Konfigurationsdatei benötigt";
  public static final String CONFIG_ERROR = "Fehler in der Config";
  public static final String ERROR_WHILE_DISABLING = "Varo konnte nicht erfolgreich heruntergefahren werden.";
  public static final String SUCCESSFULLY_DISABLED = "Varo wurde erfolgreich heruntergefahren.";
  public static final String SUCCESSFULLY_STARTED = "Varo wurde erfolgreich gestartet.";
  //</editor-fold>

  //<editor-fold desc="General">
  public static final String NOT_A_NUMBER = "Du musst eine Zahl angeben.";
  public static final String PREFIX = "&7[&4&lVaro&r&7] &e";
  //</editor-fold>

  //<editor-fold desc="Gameplay">
  public static final String ALREADY_DEAD = "Du bist bereits&c gestorben.&e \n Probier es&c nächsten Monat&e erneut";
  public static final String ALREADY_STARTED = "Das Spiel läuft bereits.";
  //</editor-fold>

  //<editor-fold desc="Preparation">
  public static final String SPAWN_CREATED = PREFIX + "Der Spawn wurde erfolgreich erstellt.";
  public static final String SPAWN_CREATION_ERROR = PREFIX + "Spawn nicht geeignet.";
  public static final String SPAWN_NOT_READY = "Varo muss noch vorbereitet werden. Bitte setze den Weltspawn.";
  //</editor-fold>

  //<editor-fold desc="Registration">
  public static final String ALREADY_REGISTERED = "Du bist bereits in einem Team angemeldet.";
  public static final String NOT_REGISTERED = "Du bist nicht registriert.";
  public static final String NOT_REGISTERED_YET = "Du musst dich mit register registrieren oder Admin-Rechte " +
      "erhalten!!!";
  public static final String REGISTERED = "Du wurdest erfolgreich registriert.";
  public static final String TEAM_ALREADY_EXIST = "Dieser Teamname ist bereits vergeben.";
  public static final String TEAM_NOT_FULL = "Dein Team ist noch nicht voll.";
  public static final String UNREGISTERED = "Du wurdest erfolgreich entfernt. &4ACHTUNG: Ab Projektstart kannst du " +
      "den Server nicht mehr betreten.";
  //</editor-fold>

}

