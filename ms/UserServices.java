import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserServices extends UnicastRemoteObject implements UserServicesAI{

    // Set up the JDBC driver name and database URL
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
            UserServices obj = new UserServices();

            // Bind this object instance to the name RetrieveServices in the rmiregistry
            Naming.rebind("//localhost:1099/UserServices", obj);

        } catch (Exception e) {

            System.out.println("UserServices binding err: " + e.getMessage());
            e.printStackTrace();
        }

    } // main

    protected UserServices() throws RemoteException {}


    @Override
    public boolean signin(String username, String password) throws Exception {

        // Local declarations

        Connection conn = null;		// connection to the orderinfo database
        Statement stmt = null;		// A Statement object is an interface that represents a SQL statement.
        // if not you get an error string

        try {
            // Here we load and initialize the JDBC connector. Essentially a static class
            // that is used to provide access to the database from inside this class.

            Class.forName(JDBC_CONNECTOR);

            //Open the connection to the orderinfo database

            //System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Here we create the queery Execute a query. Not that the Statement class is part
            // of the Java.rmi.* package that enables you to submit SQL queries to the database
            // that we are connected to (via JDBC in this case).

            // System.out.println("Creating statement...");
            stmt = conn.createStatement();

            String sql = "SELECT * FROM users where user_name='"+username+"' and password='"+password+"';";
            ResultSet rs = stmt.executeQuery(sql);

            // Extract data from result set. Note there should only be one for this method.
            // I used a while loop should there every be a case where there might be multiple
            // users for a pair of username and password.

            while (rs.next()) {
                // there is at least on pair of valid username and password.
                // authentication succeed
                return true;
            }

            return false;

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String signup(String username, String password) throws Exception {

        // Local declarations

        Connection conn = null;		// connection to the orderinfo database
        Statement stmt = null;		// A Statement object is an interface that represents a SQL statement.
        // if not you get an error string

        try {
            // Here we load and initialize the JDBC connector. Essentially a static class
            // that is used to provide access to the database from inside this class.

            Class.forName(JDBC_CONNECTOR);

            //Open the connection to the orderinfo database

            //System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Here we create the queery Execute a query. Not that the Statement class is part
            // of the Java.rmi.* package that enables you to submit SQL queries to the database
            // that we are connected to (via JDBC in this case).

            // System.out.println("Creating statement...");
            stmt = conn.createStatement();

            String sql = "SELECT * FROM users where user_name='"+username+"';";
            ResultSet rs = stmt.executeQuery(sql);

            // Extract data from result set. Note there should only be one for this method.
            // I used a while loop should there every be a case where there might be multiple
            // users for a pair of username and password.

            while (rs.next()) {
                // there is at least on pair of valid username and password.
                // authentication succeed
                return "This username has been used, please try another.";
            }

            stmt = conn.createStatement();
            sql = "INSERT INTO users(user_name,password) VALUES('"+username+"','"+password+"');";
            int amount = stmt.executeUpdate(sql);

            if(amount > 0){
                return "Signed up successfully! ";
            }else{
                return "Failed to sign up! ";
            }

        }catch (Exception e){
            return e.getMessage();
        }
    }
}
