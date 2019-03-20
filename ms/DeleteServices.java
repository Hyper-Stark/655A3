/****************************************************************************************************************
 * File: DeleteServices.java
 * Course: 17655
 * Project: Assignment A3
 * Author: Li Zhang
 *
 * Internal methods:
 * int deleteOrder(String orderid)
 *
 * Description:
 * This class is an implementation of DeleteServicesAI
 * This class implemented the methods that will be used to delete an order.
 *
 ***************************************************************************************************************/
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DeleteServices extends UnicastRemoteObject implements DeleteServicesAI {

    static final String JDBC_CONNECTOR = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/ms_orderinfo?autoReconnect=true&useSSL=false";

    // Set up the orderinfo database credentials
    static final String USER = "archims";
    static final String PASS = "msorder"; //replace with your MySQL root password
    private UserServicesAI userServices;
    private static Logger logger = Logger.getInstance(DeleteServices.class);

    // Main service loop
    public static void main(String args[])
    {
        // What we do is bind to rmiregistry, in this case localhost, port 1099. This is the default
        // RMI port. Note that I use rebind rather than bind. This is better as it lets you start
        // and restart without having to shut down the rmiregistry.

        try
        {
            DeleteServices obj = new DeleteServices();
            // Bind this object instance to the name RetrieveServices in the rmiregistry
            Naming.rebind("//localhost:1099/DeleteServices", obj);

        } catch (Exception e) {
            System.out.println("DeleteServices binding err: " + e.getMessage());
            logger.error("DeleteServices binding err: " + e.getMessage());
        }

    } // main

    public DeleteServices() throws RemoteException{ }

    @Override
    public int deleteOrder(String credential, String orderid) throws RemoteException {

        // Validate user credential
        validate(credential);

        // Local declarations

        int amount = 0;                  // the number of records were deleted
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

            logger.info("Trying to delete order by id: "+orderid);

            // Here we create the queery Execute a query. Note that the Statement class is part
            // of the Java.rmi.* package that enables you to submit SQL queries to the database
            // that we are connected to (via JDBC in this case).

            // System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql = "DELETE FROM orders where order_id=" + orderid;
            amount = stmt.executeUpdate(sql);

            logger.info(amount+" lines were affected! ");

            // Clean-up environment
            stmt.close();
            conn.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            logger.error("Delete order by order id("+orderid+") occurred an error: "+e.getMessage());
            return -1;
        }

        return amount;
    }

    /*****************************************************************
     * This method is used to validate user's credential.
     * CreateServices only provide services to users who have
     * been authenticated.
     * @param credential the current request's user credential
     * @throws RemoteException
     *****************************************************************/
    private void validate(String credential) throws RemoteException {

        if (this.userServices == null){
            initUserService();
        }

        try {
            if (!this.userServices.validateCredential(credential)) {
                logger.error("Verify user credential failed: ");
                throw new RemoteException("Verify user credential failed");
            }
        }catch (Exception e2) {
            logger.error("Verify user credential failed: "+e2.getMessage());
            throw new RemoteException("Verify user credential failed",e2);
        }
    }

    /*****************************************************************
     * Since this service has to rely on UserService to do credential
     * validation, this method is used to looking for avaliable UserServices
     *****************************************************************/
    private void initUserService(){
        try {
            this.userServices = (UserServicesAI)Naming.lookup("UserServices");
        } catch (NotBoundException e) {
            logger.error("Cannot found user service"+e.getMessage());
        } catch (MalformedURLException e) {
            logger.error("Wrong url for looking user service"+e.getMessage());
        } catch (RemoteException e) {
            logger.error("RemoteException: "+e.getMessage());
        }
    }
}
