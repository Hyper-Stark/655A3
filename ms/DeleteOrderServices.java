import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DeleteOrderServices extends UnicastRemoteObject implements DeleteOrderServicesAI {

    static final String JDBC_CONNECTOR = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/ms_orderinfo?autoReconnect=true&useSSL=false";

    // Set up the orderinfo database credentials
    static final String USER = "archims";
    static final String PASS = "msorder"; //replace with your MySQL root password


    // Main service loop
    public static void main(String args[])
    {
        // What we do is bind to rmiregistry, in this case localhost, port 1099. This is the default
        // RMI port. Note that I use rebind rather than bind. This is better as it lets you start
        // and restart without having to shut down the rmiregistry.

        try
        {
            CreateServices obj = new CreateServices();

            // Bind this object instance to the name RetrieveServices in the rmiregistry
            Naming.rebind("//localhost:1099/DeleteOrderServices", obj);

        } catch (Exception e) {

            System.out.println("DeleteOrderServices binding err: " + e.getMessage());
            e.printStackTrace();
        }

    } // main

    public DeleteOrderServices() throws RemoteException {}

    @Override
    public boolean deleteOrder(String orderid) throws RemoteException {
        // Local declarations

        Connection conn = null;        // connection to the orderinfo database
        Statement stmt = null;        // A Statement object is an interface that represents a SQL statement.

        // if not you get an error string
        try {
            // Here we load and initialize the JDBC connector. Essentially a static class
            // that is used to provide access to the database from inside this class.

            Class.forName(JDBC_CONNECTOR);

            //Open the connection to the orderinfo database

            //System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Here we create the queery Execute a query. Note that the Statement class is part
            // of the Java.rmi.* package that enables you to submit SQL queries to the database
            // that we are connected to (via JDBC in this case).

            // System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql = "DELETE FROM orders where order_id=" + orderid;
            ResultSet rs = stmt.executeQuery(sql);

            // Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
