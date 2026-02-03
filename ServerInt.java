import java.rmi.*;
import java.util.List;

public interface ServerInt extends Remote {
  public String sendMessage(String from , String to, String Message) throws RemoteException;
  public void registerClient(String name, ClientInt client) throws RemoteException;
  List<String> getOnlineUsers() throws RemoteException;
}