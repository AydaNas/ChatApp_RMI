import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ChatClient extends Remote {
    public void receiveMessage(Message message) throws RemoteException;
    public String receiveNotification(String notification) throws RemoteException;
    

}