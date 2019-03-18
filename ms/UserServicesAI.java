/****************************************************************************************************************
 * File: UserServicesAI.java
 * Course: 17655
 * Project: Assignment A3
 * Author: Li Zhang
 *
 * Internal methods:
 * boolean signin(String username, String password)
 * String signup(String username, String password)
 *
 * Description:
 * This interface defined to what kind of methods will be used to deal with user authentication related problems,
 * including signing in and signing up
 *
 ***************************************************************************************************************/
public interface UserServicesAI extends java.rmi.Remote{

    /*******************************************************
     * Verify a user by user name and password
     * Returns true if the user is valid otherwise false
     *******************************************************/
    boolean signin(String username, String password) throws Exception;

    /*******************************************************
     * Creates a new user according given user name and password
     * Returns message to the user.
     *     if the username has been used, return a message to inform this situation
     *     if there was at least one record which was inserted successfully, inform this.
     *     if there was an exception happened, report this.
     *******************************************************/
    String signup(String username, String password) throws Exception;

}
