import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {
  private int userId;
    public ChatClientImpl() throws RemoteException {}
    
    
    
  
 
    @Override
    public String receiveNotification(String notification) throws RemoteException {
        return notification;
    }
    
    public void setUserId(int userId) {
    	this.userId = userId;
    }
    public int getUserId() {
    	return userId;
    }
    
    @Override
    public void receiveMessage(Message message) throws RemoteException{
    	System.out.println ("["+message.getTimestamp()+"]" + 
        "From" + message.getSender() + 
        "to" + message.getReceiverId()+ 
        "Message" + message.getContent());
    
    
    } 
}
