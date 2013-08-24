
/**
 *
 * @author markus
 */
public class DatabaseManager {
javax.swing.JFrame frame = new javax.swing.JFrame("Transfermanager");
//org.hsqldb.util.Transfer trans = new org.hsqldb.util.Transfer();
org.hsqldb.util.DatabaseManager dm = new org.hsqldb.util.DatabaseManager();

    public DatabaseManager(){
	java.awt.GridLayout layout = new java.awt.GridLayout(1, 1);
	frame.setLayout(layout);
	dm.setVisible(true);
	frame.add(dm);
	frame.setVisible(true);
	frame.pack();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DatabaseManager dbman = new DatabaseManager();
	
    }

}
