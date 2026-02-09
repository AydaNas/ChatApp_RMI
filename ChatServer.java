import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface ChatServer extends Remote {

  public void sendMessage(Message message) throws RemoteException;

  public void join(String userName, ChatClient client) throws RemoteException;

  public void leave(String userName) throws RemoteException;

  public List<String> getJoinedUsers() throws RemoteException;

  public List<Message> getHistory() throws RemoteException;

}