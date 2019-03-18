public interface UserServicesAI extends java.rmi.Remote{

    boolean signin(String username, String password) throws Exception;

    String signup(String username, String password) throws Exception;

}
