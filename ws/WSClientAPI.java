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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WSClientAPI
{
	/********************************************************************************
	* Description: Gets and returns all the orders in the orderinfo database
	* Parameters: None
	* Returns: String of all the current orders in the orderinfo database
	********************************************************************************/

	public String retrieveOrders() throws Exception
	{
		// Set up the URL and connect to the node server

		String url = "http://localhost:3000/api/orders";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//Form the request header and instantiate the response code
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();


		//Set up a buffer to read the response from the server
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		//Loop through the input and build the response string.
		//When done, close the stream.
		while ((inputLine = in.readLine()) != null) 
		{
			response.append(inputLine);
		}
		in.close();

		return(response.toString());

	}
	
	/********************************************************************************
	* Description: Gets and returns the order based on the provided id from the
	*              orderinfo database.
	* Parameters: None
	* Returns: String of all the order corresponding to the id argument in the 
	*		   orderinfo database.
	********************************************************************************/

	public String retrieveOrders(String id) throws Exception
	{
		// Set up the URL and connect to the node server
		String url = "http://localhost:3000/api/orders/"+id;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//Form the request header and instantiate the response code
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();

		//Set up a buffer to read the response from the server
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		//Loop through the input and build the response string.
		//When done, close the stream.		

		while ((inputLine = in.readLine()) != null) 
		{
			response.append(inputLine);
		}
		in.close();

		return(response.toString());

	}

	/********************************************************************************
	* Description: Posts the new order to the orderinfo database
	* Parameters: None
	* Returns: String that contains the status of the POST operation
	********************************************************************************/

   	public String newOrder(String Date, String FirstName, String LastName, String Address, String Phone) throws Exception
	{
		// Set up the URL and connect to the node server		
		URL url = new URL("http://localhost:3000/api/orders");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// The POST parameters
		String input = "order_date="+Date+"&first_name="+FirstName+"&last_name="+LastName+"&address="+Address+"&phone="+Phone;

		//Configure the POST connection for the parameters
		conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept-Language", "en-GB,en;q=0.5");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-length", Integer.toString(input.length()));
        conn.setRequestProperty("Content-Language", "en-GB");
        conn.setRequestProperty("charset", "utf-8");
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
		
    } // newOrder

	public String deleteOrder(String credential, String orderId) throws Exception{
		String resp = post(credential,"http://localhost:3000/api/orders/delete/"+orderId,null);
		return extractField("Message",resp);
	}

	public String signin(String username, String passwd) throws Exception{
   		Map<String,String> params = new HashMap<String,String>();
   		params.put("username",username);
   		params.put("password",passwd);
		String resp = post(null,"http://localhost:3000/api/orders/signin/",params);
		return extractField("Credential",resp);
	}

	public String signup(String username, String passwd) throws Exception{
		Map<String,String> params = new HashMap<String,String>();
		params.put("username",username);
		params.put("password",passwd);
		String resp = post(null,"http://localhost:3000/api/orders/signup/",params);
		return extractField("Message",resp);
	}

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

	private String get(String url) throws Exception{
		// Set up the URL and connect to the node server
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

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

	private static String extractField(String key, String msg){
		Matcher matcher = Pattern.compile("\"[\\s|\\S]*?\"").matcher(msg);
		while (matcher.find()){
			String tmp = matcher.group();
			if (("\""+key+"\"").equals(tmp)){
				String cred = matcher.find() ? matcher.group() : null;
				cred = cred != null ? cred.substring(1,cred.length()-1) : "";
				return cred;
			}
		}
		return "";
	}

} // WSClientAPI