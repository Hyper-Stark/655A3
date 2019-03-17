import java.rmi.RemoteException;

public interface DeleteOrderServicesAI extends java.rmi.Remote{

    /*******************************************************
     * Deletes the order corresponding to the order id in
     * method argument form the orderinfo database and
     * returns the operation result.
     *******************************************************/

    boolean deleteOrder(String orderid) throws RemoteException;

}
