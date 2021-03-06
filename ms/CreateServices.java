/******************************************************************************************************************
* File: CreateServices.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2018 Carnegie Mellon University
* Versions:
*	1.0 February 2018 - Initial write of assignment 3 (ajl).
*
* Description: This class provides the concrete implementation of the create micro services. These services run
* in their own process (JVM).
*
* Parameters: None
*
* Internal Methods:
*  String newOrder() - creates an order in the ms_orderinfo database from the supplied parameters.
*
* External Dependencies: 
*	- rmiregistry must be running to start this server
*	= MySQL
	- orderinfo database 
******************************************************************************************************************/
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class CreateServices extends UnicastRemoteObject implements CreateServicesAI
{ 
    // Set up the JDBC driver name and database URL
    static final String JDBC_CONNECTOR = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost/ms_orderinfo?autoReconnect=true&useSSL=false";

    // Set up the orderinfo database credentials
    static final String USER = "archims";
    static final String PASS = "msorder"; //replace with your MySQL root password
    private UserServicesAI userServices;
    private static final Logger logger = Logger.getInstance(CreateServices.class);

    // Do nothing constructor
    public CreateServices() throws RemoteException {}

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
            Naming.rebind("//localhost:1099/CreateServices", obj); 

        } catch (Exception e) {
            System.out.println("CreateServices binding err: " + e.getMessage()); 
            logger.error("CreateServices binding err: " + e.getMessage());
        } 

    } // main


    // Inplmentation of the abstract classes in RetrieveServicesAI happens here.

    // This method add the entry into the ms_orderinfo database

    public String newOrder(String credential, String idate, String ifirst, String ilast, String iaddress, String iphone) throws RemoteException
    {
      	// Local declarations

        validate(credential);

        Connection conn = null;		                 // connection to the orderinfo database
        Statement stmt = null;		                 // A Statement object is an interface that represents a SQL statement.
        String ReturnString = "Order Created";	     // Return string. If everything works you get an 'OK' message
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

            stmt = conn.createStatement();

            logger.info("Adding a new order -> Date: "+idate+" First name: " + ifirst + " Last name: " +ilast + " Address: "+ iaddress +" Phone: "+iphone);
            
            String sql = "INSERT INTO ORDERS(order_date, first_name, last_name, address, phone) VALUES (\""+idate+"\",\""+ifirst+"\",\""+ilast+"\",\""+iaddress+"\",\""+iphone+"\")";

            // execute the update

            stmt.executeUpdate(sql);

            // clean up the environment

            stmt.close();
            conn.close();

        } catch(Exception e) {
            logger.error("Creating an order occurred an error: "+e.getMessage());
            ReturnString = e.toString();
        } 
        
        return(ReturnString);

    } //retrieve all orders

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
