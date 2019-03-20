/******************************************************************************************************************
* File:RESTClientAPI.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2018 Carnegie Mellon University
* Versions:
*	1.0 February 2018 - Initial write of assignment 3 (ajl).
*
* Description: This class is used to provide access to a Node.js server. The server for this application is 
* Server.js which provides RESTful services to access a MySQL database.
*
* Parameters: None
*
* Internal Methods: None
*  String retrieveOrders() - gets and returns all the orders in the orderinfo database
*  String retrieveOrders(String id) - gets and returns the order associated with the order id
*  String sendPost(String Date, String FirstName, String LastName, String Address, String Phone) - creates a new 
*  order in the orderinfo database
*
* External Dependencies: None
******************************************************************************************************************/

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WSClientAPI
{
	/********************************************************************************
	* Description: Gets and returns all the orders in the orderinfo database
	* Parameters: None
	* Returns: String of all the current orders in the orderinfo database
	********************************************************************************/

	public String retrieveOrders(String credential) throws Exception
	{
		// Set up the URL and connect to the node server
		String resp = get(credential, "http://localhost:3000/api/orders");
		return extractField("Message",resp) + ":" + extractField("Orders",resp);
	}
	
	/********************************************************************************
	* Description: Gets and returns the order based on the provided id from the
	*              orderinfo database.
	* Parameters: None
	* Returns: String of all the order corresponding to the id argument in the 
	*		   orderinfo database.
	********************************************************************************/

	public String retrieveOrders(String credential,String id) throws Exception
	{
		// Set up the URL and connect to the node server
		String resp = get(credential,"http://localhost:3000/api/orders/"+id);
		return extractField("Message",resp) + " : " + extractField("Order",resp);
	}

	/********************************************************************************
	* Description: Posts the new order to the orderinfo database
	* Parameters: None
	* Returns: String that contains the status of the POST operation
	********************************************************************************/

   	public String newOrder(String credential,String Date, String FirstName, String LastName, String Address, String Phone) throws Exception
	{

		Map<String,String> params = new HashMap<String,String>();
		params.put("order_date",Date);
		params.put("first_name",FirstName);
		params.put("last_name",LastName);
		params.put("address",Address);
		params.put("phone",Phone);

		String resp = post(credential,"http://localhost:3000/api/orders",params);
		return extractField("Message",resp);
    } // newOrder


	/*******************************************************************************
	 * Description: This method is used to provide delete order service
	 * The implementation is the method send a get http request to server
	 * and server do the delete action.
	 * @param credential user credential, the user has to sign in to do this
	 * @param orderId the id of the order need to be deleted.
	 * @return the server end message.
	 * @throws Exception
	 *******************************************************************************/
	public String deleteOrder(String credential, String orderId) throws Exception{
		String resp = get(credential,"http://localhost:3000/api/orders/delete/"+orderId);
		return extractField("Message",resp);
	}

	/******************************************************************************
	 * Description: This method is used to provide sign in function, user need to
	 * provide a pair of username and password.
	 * @param username the username need to be verified
	 * @param passwd the corresponding password need to be verified
	 * @return the server end message
	 * @throws Exception
	 *****************************************************************************/
	public String signin(String username, String passwd) throws Exception{
   		Map<String,String> params = new HashMap<String,String>();
   		params.put("username",username);
   		params.put("password",passwd);
		String resp = post(null,"http://localhost:3000/api/orders/signin/",params);
		return extractField("Credential",resp);
	}

	/******************************************************************************
	 * Description: This method is used to provide sign up function, user need to
	 * provide a pair of username and password.
	 * @param username the username need to be created
	 * @param passwd the corresponding password need to be created
	 * @return the server end message
	 * @throws Exception
	 *****************************************************************************/
	public String signup(String username, String passwd) throws Exception{
		Map<String,String> params = new HashMap<String,String>();
		params.put("username",username);
		params.put("password",passwd);
		String resp = post(null,"http://localhost:3000/api/orders/signup/",params);
		return extractField("Message",resp);
	}

	/******************************************************************************
	 * Description: This method is a helper method which can simplify the process to
	 * send a HTTP post request. It encapsulated the whole sending and reading process
	 * @param credential the user credential
	 * @param urlStr the target url
	 * @param data the data that need to be passed
	 * @return the server end message
	 * @throws Exception
	 *****************************************************************************/
	private String post(String credential, String urlStr, Map<String,String> data) throws Exception{
		// Set up the URL and connect to the node server
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// The POST parameters
		boolean firstParam = true;
		StringBuilder sb = new StringBuilder();
		if (data != null){
			for(Map.Entry<String,String> entry: data.entrySet()){
				if(firstParam){
					sb.append(entry.getKey()+"="+entry.getValue());
					firstParam = false;
				}else {
					sb.append("&"+entry.getKey()+"="+entry.getValue());
				}
			}
		}
		String input = sb.toString();

		//Configure the POST connection for the parameters
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Accept-Language", "en-GB,en;q=0.5");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-length", Integer.toString(input.length()));
		conn.setRequestProperty("Content-Language", "en-GB");
		conn.setRequestProperty("charset", "utf-8");
		if (credential != null && credential.length() > 0){
			conn.setRequestProperty("Credential",credential);
		}
		conn.setUseCaches(false);
		conn.setDoOutput(true);

		// Set up a stream and write the parameters to the server
		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();

		//Loop through the input and build the response string.
		//When done, close the stream.
		BufferedReader in = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String inputLine;
		StringBuffer response = new StringBuffer();

		//Loop through the input and build the response string.
		//When done, close the stream.

		while ((inputLine = in.readLine()) != null)
		{
			response.append(inputLine);
		}

		in.close();
		conn.disconnect();

		return(response.toString());
	}

	/******************************************************************************
	 * Description: This method is a helper method which can simplify the process to
	 * send a HTTP get request. It encapsulated the whole sending and reading process
	 * @param credential the user credential
	 * @param url the target url
	 * @return the server end message
	 * @throws Exception
	 *****************************************************************************/
	private String get(String credential, String url) throws Exception{
		// Set up the URL and connect to the node server
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		if (credential != null && credential.length() > 0){
			con.setRequestProperty("Credential",credential);
		}

		//Form the request header and instantiate the response code
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();

		//Set up a buffer to read the response from the server
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		//Loop through the input and build the response string.
		//When done, close the stream.

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return (response.toString());
	}

	/******************************************************************************
	 * Description: this is a helper function that can help us to extract a specific
	 * field from a json string.
	 * @param key the field's name
	 * @param msg the whole json string
	 * @return the field's value
	 ******************************************************************************/
	private static String extractField(String key, String msg){
		JSONObject obj = new JSONObject(msg);
		return obj.get(key).toString();
	}

} // WSClientAPI