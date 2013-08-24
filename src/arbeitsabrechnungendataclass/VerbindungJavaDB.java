/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package arbeitsabrechnungendataclass;

/**
 *
 * @author markus
 */
import java.sql.*;

public class VerbindungJavaDB {
    static String treiber = "org.apache.derby.jdbc.ClientDriver";
    static String URL = "jdbc:derby://localhost:1527/arbeitrechnungenData";
//    static String datenbank = "Arbeitrechnungen";
    Statement befehl = null;
    Connection verbindung = null;

    public VerbindungJavaDB(String datenbank, String benutzer, String password){
        /**
         * Treiber laden, Verbindung aufbauen und die Tabellen der Datenbank auslesen
         * und in die tabellenliste speichern.
         */

        // Treiber laden
        try {
            Class.forName(treiber).newInstance();
        } catch (Exception e) {
            System.out.println("JDBC/Derby-Treiber konnte nicht geladen werden!");
            return;
        }
        // Verbindung aufbauen
        try {
            verbindung = DriverManager.getConnection(URL + "/" + datenbank, benutzer, password);
            befehl = verbindung.createStatement();
        } catch (Exception e) {
            System.err.println("Verbindung zu " + URL +
                    " konnte nicht hergestellt werden.");
        }
    }

    public VerbindungJavaDB(String server, String datenbank, String benutzer, String password){
        /**
         * Treiber laden, Verbindung aufbauen und die Tabellen der Datenbank auslesen
         * und in die tabellenliste speichern.
         */
        String URL2 = "jdbc:derby://" + server;

        // Treiber laden
        try {
            Class.forName(treiber).newInstance();
        } catch (Exception e) {
            System.out.println("JDBC/Derby-Treiber konnte nicht geladen werden!");
            return;
        }

        // Verbindung aufbauen
        try {
            verbindung = DriverManager.getConnection(URL2 + "/" + datenbank, benutzer, password);
            befehl = verbindung.createStatement();
        } catch (Exception e) {
            System.err.println("Verbindung zu " + URL2 +
                    " konnte nicht hergestellt werden.");
        }
    }

    public boolean connected(){
        try{
            return verbindung.isValid(3);
        }catch (Exception e) {
            return false;
        }
    }
    
    public ResultSet query(String sqltext){
                /**
         * Die Sql-Abfrage gibt den ResultSet zurück. Dafür muss vermutlich java.sql.*
         * in der aufrufenden Klasse definiert sein :(
         */
        ResultSet daten = null;
        try {
            daten = befehl.executeQuery(sqltext);
        } catch (Exception e) {
            System.err.println("SQL: " + sqltext);
            e.printStackTrace();
        }
        return daten;
    }

    public boolean sql(String sqltext){
        boolean ok = false;
        try {
            befehl.execute(sqltext);
            ok = true;
        } catch (Exception e) {
            System.err.println("SQL: " + sqltext);
            e.printStackTrace();
        }
        return ok;
    }

    public void close(){
        try {
            verbindung.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
