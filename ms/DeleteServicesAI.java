/****************************************************************************************************************
 * File: DeleteServicesAI.java
 * Course: 17655
 * Project: Assignment A3
 * Author: Li Zhang
 *
 * Internal methods:
 * int deleteOrder(String orderid)
 *
 * Description:
 * This interface defined methods need to be implement that will be used to delete an order from the database
 *
 ***************************************************************************************************************/

import java.rmi.RemoteException;

public interface DeleteServicesAI extends java.rmi.Remote{

    /*******************************************************
     * Deletes the order corresponding to the order id in
     * method argument form the orderinfo database and
     * returns the operation result.
     *******************************************************/

    int deleteOrder(String credential, String orderid) throws RemoteException;

}
