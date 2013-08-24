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

public class Verbindung {
    static String treiber = "com.mysql.jdbc.Driver";
    static String URL = "jdbc:mysql://192.168.0.1";
//    static String datenbank = "Arbeitrechnungen";
    Statement befehl = null;
    Connection verbindung = null;

    public Verbindung(String datenbank, String benutzer, String password){
        /**
         * Treiber laden, Verbindung aufbauen und die Tabellen der Datenbank auslesen
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
            befehl = verbindung.createStatement();
        } catch (Exception e) {
            System.err.println("Verbindung zu " + URL +
                    " konnte nicht hergestellt werden.");
        }
    }

    public Verbindung(String server, String datenbank, String benutzer, String password){
        /**
         * Treiber laden, Verbindung aufbauen und die Tabellen der Datenbank auslesen
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

     /**
     * Die Sql-Abfrage gibt den ResultSet zurück. Dafür muss vermutlich java.sql.*
     * in der aufrufenden Klasse definiert sein :(
     */
    public ResultSet query(String sqltext){

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
