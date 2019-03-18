public interface UserServicesAI extends java.rmi.Remote{

    boolean signin(String username, String password) throws Exception;

    boolean signup(String username, String password) throws Exception;

}
