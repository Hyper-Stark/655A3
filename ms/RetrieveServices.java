/******************************************************************************************************************
* File: RetrieveServices.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2018 Carnegie Mellon University
* Versions:
*	1.0 February 2018 - Initial write of assignment 3 (ajl).
*
* Description: This class provides the concrete implementation of the retrieve micro services. These services run
* in their own process (JVM).
*
* Parameters: None
*
* Internal Methods:
*  String retrieveOrders() - gets and returns all the orders in the orderinfo database
*  String retrieveOrders(String id) - gets and returns the order associated with the order id
*
* External Dependencies: 
*	- rmiregistry must be running to start this server
*	- MySQL
	- orderinfo database 
******************************************************************************************************************/
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class RetrieveServices extends UnicastRemoteObject implements RetrieveServicesAI
{ 
    // Set up the JDBC driver name and database URL
    static final String JDBC_CONNECTOR = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost/ms_orderinfo?autoReconnect=true&useSSL=false";

    // Set up the orderinfo database credentials
    static final String USER = "archims";
    static final String PASS = "msorder"; //replace with your MySQL root password
    private UserServicesAI userServices;
    private static final Logger logger = Logger.getInstance(RetrieveServices.class);

    // Do nothing constructor
    public RetrieveServices() throws RemoteException {}

    // Main service loop
    public static void main(String args[]) 
    { 	
    	// What we do is bind to rmiregistry, in this case localhost, port 1099. This is the default
    	// RMI port. Note that I use rebind rather than bind. This is better as it lets you start
    	// and restart without having to shut down the rmiregistry. 

        try 
        { 
            RetrieveServices obj = new RetrieveServices();

            // Bind this object instance to the name RetrieveServices in the rmiregistry 
            Naming.rebind("//localhost:1099/RetrieveServices", obj); 

        } catch (Exception e) {

            System.out.println("RetrieveServices binding err: " + e.getMessage());
            logger.error("RetrieveServices binding err: " + e.getMessage());
        } 

    } // main


    // Inplmentation of the abstract classes in RetrieveServicesAI happens here.

    // This method will return all the entries in the orderinfo database

    public String retrieveOrders(String credential) throws RemoteException
    {

        //validate user
        validate(credential);

        // Local declarations

        Connection conn = null;		// connection to the orderinfo database
        Statement stmt = null;		// A Statement object is an interface that represents a SQL statement.
        String ReturnString = "[";	// Return string. If everything works you get an ordered pair of data
        							// if not you get an error string
        try
        {
            // Here we load and initialize the JDBC connector. Essentially a static class
            // that is used to provide access to the database from inside this class.

            Class.forName(JDBC_CONNECTOR);

            //Open the connection to the orderinfo database

            //System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // Here we create the queery Execute a query. Not that the Statement class is part
            // of the Java.rmi.* package that enables you to submit SQL queries to the database
            // that we are connected to (via JDBC in this case).

            logger.info("Trying to retrieve all orders");

            // System.out.println("Creating statement...");
            stmt = conn.createStatement();
            
            String sql;
            sql = "SELECT * FROM Orders";
            ResultSet rs = stmt.executeQuery(sql);

            //Extract data from result set

            while(rs.next())
            {
                //Retrieve by column name
                int id  = rs.getInt("order_id");
                String date = rs.getString("order_date");
                String first = rs.getString("first_name");
                String last = rs.getString("last_name");
                String address = rs.getString("address");
                String phone = rs.getString("phone");

                //Display values
                //System.out.print("ID: " + id);
                //System.out.print(", date: " + date);
                //System.out.print(", first: " + first);
                //System.out.print(", last: " + last);
                //System.out.print(", address: " + address);
                //System.out.println("phone:"+phone);

                ReturnString = ReturnString +"{order_id:"+id+", order_date:"+date+", first_name:"+first+", last_name:"
                               +last+", address:"+address+", phone:"+phone+"}";

            }

            ReturnString = ReturnString +"]";

            logger.info("Retrieved the following orders: "+ReturnString);

            //Clean-up environment

            rs.close();
            stmt.close(); 
            conn.close();

        } catch(Exception e) {
            logger.error("Retrieve orders occurred an error: " + e.getMessage());
            ReturnString = e.toString();
        } 
        
        return(ReturnString);

    } //retrieve all orders

    // This method will returns the order in the orderinfo database corresponding to the id
    // provided in the argument.

    public String retrieveOrders(String credential, String orderid) throws RemoteException
    {
        //validate user
        validate(credential);

        // Local declarations

        Connection conn = null;		// connection to the orderinfo database
        Statement stmt = null;		// A Statement object is an interface that represents a SQL statement.
        String ReturnString = "[";	// Return string. If everything works you get an ordered pair of data
        							// if not you get an error string

        try
        {
            // Here we load and initialize the JDBC connector. Essentially a static class
            // that is used to provide access to the database from inside this class.

            Class.forName(JDBC_CONNECTOR);

            //Open the connection to the orderinfo database

            //System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // Here we create the queery Execute a query. Not that the Statement class is part
            // of the Java.rmi.* package that enables you to submit SQL queries to the database
            // that we are connected to (via JDBC in this case).

            logger.info("Trying to retrieve order by id "+orderid);

            // System.out.println("Creating statement...");
            stmt = conn.createStatement();
            
            String sql;
            sql = "SELECT * FROM Orders where order_id=" + orderid;
            ResultSet rs = stmt.executeQuery(sql);

            // Extract data from result set. Note there should only be one for this method.
            // I used a while loop should there every be a case where there might be multiple
            // orders for a single ID.

            while(rs.next())
            {
                //Retrieve by column name
                int id  = rs.getInt("order_id");
                String date = rs.getString("order_date");
                String first = rs.getString("first_name");
                String last = rs.getString("last_name");
                String address = rs.getString("address");
                String phone = rs.getString("phone");

                //Display values
                //System.out.print("ID: " + id);
                //System.out.print(", date: " + date);
                //System.out.print(", first: " + first);
                //System.out.print(", last: " + last);
                //System.out.print(", address: " + address);
                //System.out.println("phone:"+phone);

                ReturnString = ReturnString +"{order_id:"+id+", order_date:"+date+", first_name:"+first+", last_name:"
                               +last+", address:"+address+", phone:"+phone+"}";
            }

            ReturnString = ReturnString +"]";

            logger.info("Retrieved the following orders: "+ReturnString);

            //Clean-up environment

            rs.close();
            stmt.close(); 
            conn.close();

        } catch(Exception e) {

            logger.error("Retrieve order by order id ("+orderid+") occurred an error: " + e.getMessage());
            ReturnString = e.toString();
        } 

        return(ReturnString);

    } //retrieve order by id

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
} // RetrieveServices