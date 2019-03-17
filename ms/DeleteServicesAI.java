import java.rmi.RemoteException;

public interface DeleteServicesAI extends java.rmi.Remote{

    /*******************************************************
     * Deletes the order corresponding to the order id in
     * method argument form the orderinfo database and
     * returns the operation result.
     *******************************************************/

    int deleteOrder(String orderid) throws RemoteException;

}
