import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface ChatServer extends Remote {
  
  public  List<UserList> getActiveUsers() throws RemoteException;
  public void sendMessage(int senderId, Integer reciverId, Integer groupId, String content) throws RemoteException;
  
  public int registerClient(String username,String password, ChatClient client) throws RemoteException;
//  public void join(String userName, ChatClient client) throws RemoteException;
//
//  public void leave(String userName) throws RemoteException;
//  
//  public void createGroup() throws RemoteException;
//  
//  public void showHistory() throws RemoteException;
//
//  public List<String> getJoinedUsers() throws RemoteException;
//
//  public List<Message> getHistory() throws RemoteException;

}