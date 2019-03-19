/******************************************************************************************************************
* File: MSClientAPI.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2018 Carnegie Mellon University
* Versions:
*	1.0 February 2018 - Initial write of assignment 3 (ajl).
*
* Description: This class provides access to the webservices via RMI. Users of this class need not worry about the
* details of RMI (provided the services are running and registered via rmiregistry).  
*
* Parameters: None
*
* Internal Methods:
*  String retrieveOrders() - gets and returns all the orders in the orderinfo database
*  String retrieveOrders(String id) - gets and returns the order associated with the order id
*  String newOrder(String Date, String FirstName, String LastName, String Address, String Phone) - creates a new 
*  order in the orderinfo database
*
*
* External Dependencies: None
******************************************************************************************************************/

import java.rmi.Naming;

public class MSClientAPI
{
	String response = null;

	/********************************************************************************
	* Description: Retrieves all the orders in the orderinfo database. Note 
	*              that this method is serviced by the RetrieveServices server 
	*			   process.
	* Parameters: None
	* Returns: String of all the current orders in the orderinfo database
	********************************************************************************/

	public String retrieveOrders(String credential) throws Exception
	{
           RetrieveServicesAI obj = (RetrieveServicesAI) Naming.lookup("RetrieveServices");  
           response = obj.retrieveOrders(credential);
           return(response);
	}
	
	/********************************************************************************
	* Description: Retrieves the order based on the id argument provided from the
	*              orderinfo database. Note that this method is serviced by the 
	*			   RetrieveServices server process.
	* Parameters: None
	* Returns: String of all the order corresponding to the order id argument 
	*          in the orderinfo database.
	********************************************************************************/

	public String retrieveOrders(String credential, String id) throws Exception
	{
           RetrieveServicesAI obj = (RetrieveServicesAI) Naming.lookup("RetrieveServices");  
           response = obj.retrieveOrders(credential, id);
           return(response);	

	}

	/********************************************************************************
	* Description: Creates the new order to the orderinfo database
	* Parameters: None
	* Returns: String that contains the status of the create operatation
	********************************************************************************/

   	public String newOrder(String credential, String Date, String FirstName, String LastName, String Address, String Phone) throws Exception
	{
           CreateServicesAI obj = (CreateServicesAI) Naming.lookup("CreateServices"); 
           response = obj.newOrder(credential, Date, FirstName, LastName, Address, Phone);
           return(response);	
		
    }

	/********************************************************************************
	 * Description: Delete an order from the orderinfo database
	 * Parameters: Order ID
	 * Returns: the number of the deleted records.
	 ********************************************************************************/
	public int deleteOrder(String credential, String orderid) throws Exception {
   		   DeleteServicesAI stub = (DeleteServicesAI) Naming.lookup("DeleteServices");
   		   return stub.deleteOrder(credential, orderid);
	}

	/********************************************************************************
	 * Description: Verify whether the given pair of username and password is valid or not
	 * Parameters: user name, password
	 * Returns: whether this combination registered or not
	 ********************************************************************************/
	public String signin(String username, String password) throws Exception{
		   UserServicesAI stub = (UserServicesAI) Naming.lookup("UserServices");
		   return stub.signin(username,password);
	}

	/********************************************************************************
	 * Description: Create a new user account in the database.
	 * Parameters: user name, password
	 * Returns: the message returned by the server
	 ********************************************************************************/
	public String signup(String username, String password) throws Exception{
		   UserServicesAI stub = (UserServicesAI) Naming.lookup("UserServices");
   		   return stub.signup(username, password);
	}

	public void signout(String credential){
		try {
			UserServicesAI stub = (UserServicesAI) Naming.lookup("UserServices");
			stub.signout(credential);
		} catch (Exception e) {
			Logger.error("Failed to logout, but it does not matter! "+e.getMessage());
		}
	}
}