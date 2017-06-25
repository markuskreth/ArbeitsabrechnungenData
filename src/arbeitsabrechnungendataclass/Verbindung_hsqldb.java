
package arbeitsabrechnungendataclass;

/**
 * @author markus
 */
import java.sql.*;

import org.apache.log4j.Logger;

public class Verbindung_hsqldb {

   Logger logger = Logger.getLogger(getClass());
   static String treiber = "org.hsqldb.jdbcDriver";
   static String URL = "jdbc:hsqldb:file:";
   static String benutzer = "sa";
   static String password = "";

   private java.util.Properties sysProps = System.getProperties();
   Statement befehl = null;
   Connection verbindung = null;

   /**
    * Treiber laden, Verbindung aufbauen und die Tabellen der Datenbank auslesen
    * und in die tabellenliste speichern.
    * Datenbank wird im Arbeitsverzeichnis im Unterverzeichnis "daten"
    * gespeichert
    */
   public Verbindung_hsqldb() {
      this("daten");
   }

   public void createTables() {

      logger.trace("creating tables klienten, angebote, einheiten, klienten_angebote, rechnungen, rechnungen_einheiten");
      sql("CREATE TABLE klienten ( " + "klienten_id int NOT NULL, " + "Kunde varchar(50) DEFAULT NULL, " + "KAdresse1 varchar(50) DEFAULT NULL, "
            + "KAdresse2 varchar(50) DEFAULT NULL, " + "KPLZ varchar(5) DEFAULT NULL, " + "KOrt varchar(50) DEFAULT NULL, " + "KTelefon varchar(30) DEFAULT NULL, "
            + "KEmail varchar(30) DEFAULT NULL, " + "Auftraggeber varchar(50) NOT NULL, " + "AAdresse1 varchar(50) NOT NULL, " + "AAdresse2 varchar(50) DEFAULT NULL, "
            + "APLZ varchar(5) NOT NULL, " + "AOrt varchar(50) NOT NULL, " + "ATelefon varchar(30) DEFAULT NULL, " + "AEmail varchar(30) DEFAULT NULL, "
            + "Bemerkungen varchar(250), " + "Zusatz1 tinyint DEFAULT NULL, " + "Zusatz1_Name varchar(100) DEFAULT NULL, " + "Zusatz2 tinyint DEFAULT NULL, "
            + "Zusatz2_Name varchar(100) DEFAULT NULL, " + "tex_datei varchar(255) DEFAULT NULL, " + "rechnungnummer_bezeichnung varchar(255) DEFAULT NULL,"
            + "PRIMARY KEY (klienten_id));");
      sql("CREATE TABLE angebote (" + "angebote_id int  NOT NULL, " + "klienten_id int  DEFAULT NULL, " + "Inhalt varchar(60) NOT NULL, " + "Preis float NOT NULL, "
            + "preis_pro_stunde tinyint NOT NULL, " + "Beschreibung varchar(250), " + "PRIMARY KEY (angebote_id));");
      sql("CREATE TABLE einheiten (" + "einheiten_id int NOT NULL, " + "klienten_id int NOT NULL, " + "angebote_id int NOT NULL, " + "Datum date NOT NULL, "
            + "Beginn datetime DEFAULT NULL , " + "Ende datetime DEFAULT NULL , " + "Dauer float NOT NULL , " + "zusatz1 varchar(255) DEFAULT NULL, "
            + "zusatz2 varchar(255) DEFAULT NULL, " + "Preis float NOT NULL , " + "Preisänderung float NOT NULL , " + "Rechnung_verschickt boolean DEFAULT NULL, "
            + "Rechnung_Datum datetime DEFAULT NULL, " + "rechnung_id int DEFAULT NULL, " + "Bezahlt boolean DEFAULT NULL, " + "Bezahlt_Datum timestamp NULL, "
            + "rechnungen_id int DEFAULT NULL, " + "PRIMARY KEY (einheiten_id));");
      sql("CREATE TABLE klienten_angebote (" + "klienten_angebote_id int NOT NULL , " + "klienten_id int NOT NULL, " + "angebote_id int NOT NULL, "
            + "PRIMARY KEY (klienten_angebote_id), " + "UNIQUE (klienten_id,angebote_id));");
      sql("CREATE TABLE rechnungen ( " + "rechnungen_id int NOT NULL, " + "klienten_id int NOT NULL, " + "datum datetime NOT NULL, " + "rechnungnr varchar(255) NOT NULL, "
            + "betrag float NOT NULL, " + "texdatei varchar(255) NOT NULL, " + "pdfdatei varchar(255) NOT NULL, " + "adresse varchar(255) NOT NULL, " + "zusatz1 boolean NOT NULL, "
            + "zusatz2 boolean NOT NULL, " + "zusammenfassungen boolean NOT NULL, " + "zahldatum date NOT NULL, " + "geldeingang date DEFAULT NULL, "
            + "timestamp timestamp NOT NULL , " + "PRIMARY KEY (rechnungen_id));");
      sql("CREATE TABLE rechnungen_einheiten ( " + "rechnungen_einheiten_id bigint NOT NULL, " + "rechnungen_id int NOT NULL, " + "einheiten_id int NOT NULL, "
            + "PRIMARY KEY (rechnungen_einheiten_id));");
   }

   /**
    * Treiber laden, Verbindung aufbauen und die Tabellen der Datenbank auslesen
    * und in die tabellenliste speichern.
    */
   public Verbindung_hsqldb(String pfad) {
      String datenbank = pfad + sysProps.getProperty("file.separator") + "daten" + ";shutdown=true";

      URL += datenbank;

      logger.debug("Pfad zur Datenbank: " + datenbank);
      logger.debug("URL: " + URL);

      try {
         Class.forName(treiber).newInstance();
      } catch (Exception e) {
         logger.error(treiber + "-Treiber konnte nicht geladen werden!", e);
         return;
      }
      // Verbindung aufbauen

      try {
         verbindung = DriverManager.getConnection(URL, benutzer, password);
         befehl = verbindung.createStatement();
      } catch (Exception e) {
         logger.error("Verbindung zu " + URL + " konnte nicht hergestellt werden.", e);
      }
   }

   public boolean connected() {
      try {
         return verbindung.isValid(3);
      } catch (Exception e) {
         return false;
      }
   }

   public java.sql.DatabaseMetaData getMetaData() {
      try {
         return verbindung.getMetaData();
      } catch (java.sql.SQLException ex) {
         logger.error("Fehler bei abruf!", ex);
         return null;
      }
   }

   /**
    * Die Sql-Abfrage gibt den ResultSet zurück. Dafür muss vermutlich
    * java.sql.*
    * in der aufrufenden Klasse definiert sein :(
    */
   public ResultSet query(String sqltext) {

      ResultSet daten = null;
      try {
         logger.error("Executing: " + sqltext);
         daten = befehl.executeQuery(sqltext);
      } catch (Exception e) {
         logger.error("SQL: " + sqltext, e);
      }
      return daten;
   }

   public boolean sql(String sqltext) {
      boolean ok = false;
      try {

         logger.debug("Executing " + sqltext);
         befehl.execute(sqltext);
         ok = true;
      } catch (Exception e) {
         logger.debug("SQL: " + sqltext, e);
      }
      return ok;
   }

   public void close() {
      try {
         verbindung.close();
      } catch (Exception e) {
         logger.debug("on close: ", e);
      }
   }
}
