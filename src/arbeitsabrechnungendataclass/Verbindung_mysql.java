package arbeitsabrechnungendataclass;

/**
 * @author markus
 */
import java.sql.*;
import java.util.Properties;

public class Verbindung_mysql extends Verbindung {

   static String treiber = "com.mysql.jdbc.Driver";
   static String URL = "jdbc:mysql://192.168.0.1";

   Connection verbindung = null;

   protected Verbindung_mysql(String datenbank, String benutzer, String password) {
      /**
       * Treiber laden, Verbindung aufbauen und die Tabellen der Datenbank
       * auslesen
       * und in die tabellenliste speichern.
       */

      // Treiber laden
      try {
         Class.forName(treiber).newInstance();
      } catch (Exception e) {
         System.out.println("JDBC/MySql-Treiber konnte nicht geladen werden!");
         return;
      }
      // Verbindung aufbauen
      try {
         verbindung = DriverManager.getConnection(URL + "/" + datenbank, benutzer, password);
      } catch (Exception e) {
         System.err.println("Verbindung zu " + URL + " konnte nicht hergestellt werden.");
      }
   }

   protected Verbindung_mysql(String server, String datenbank, String benutzer, String password) {
      /**
       * Treiber laden, Verbindung aufbauen und die Tabellen der Datenbank
       * auslesen
       * und in die tabellenliste speichern.
       */
      String URL2 = "jdbc:mysql://" + server;

      // Treiber laden
      try {
         Class.forName(treiber).newInstance();
      } catch (Exception e) {
         System.out.println("JDBC/MySql-Treiber konnte nicht geladen werden!");
         return;
      }

      // Verbindung aufbauen
      try {
         verbindung = DriverManager.getConnection(URL2 + "/" + datenbank, benutzer, password);
      } catch (Exception e) {
         System.err.println("Verbindung zu " + URL2 + " konnte nicht hergestellt werden.");
      }
   }

   public Verbindung_mysql(Properties options) {
      this(options.getProperty("sqlserver"), options.getProperty("datenbank"), options.getProperty("user"), options.getProperty("password"));
   }

   /*
    * (non-Javadoc)
    * @see arbeitsabrechnungendataclass.Verbindung#connected()
    */
   @Override
   public boolean connected() {
      try {
         return verbindung.isValid(3);
      } catch (Exception e) {
         return false;
      }
   }

   /*
    * (non-Javadoc)
    * @see arbeitsabrechnungendataclass.Verbindung#query(java.lang.String)
    */
   @Override
   public ResultSet query(CharSequence sql) {

      ResultSet daten = null;
      
      try {
         Statement befehl = verbindung.createStatement();
         daten = befehl.executeQuery(sql.toString());
      } catch (Exception e) {
         System.err.println("SQL: " + sql);
         e.printStackTrace();
      }
      
      return daten;
   }

   /*
    * (non-Javadoc)
    * @see arbeitsabrechnungendataclass.Verbindung#sql(java.lang.String)
    */
   @Override
   public boolean sql(CharSequence sql) {
      boolean ok = false;
      try {

         Statement befehl = verbindung.createStatement();
         befehl.execute(sql.toString());
         ok = true;
         
      } catch (Exception e) {
         System.err.println("SQL: " + sql);
         e.printStackTrace();
      }
      return ok;
   }

   /*
    * (non-Javadoc)
    * @see arbeitsabrechnungendataclass.Verbindung#close()
    */
   @Override
   public void close() {
      try {
         verbindung.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
