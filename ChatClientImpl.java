import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {

    private int userId;
	private final BlockingQueue<Message> messagequeue = new LinkedBlockingQueue<>();
    
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
    public void receiveMessage(Message message) throws RemoteException {
       messagequeue.add(message);
       System.out.println("\n [NEW MESSAGE] From"+
       message.getSenderId()+message.getContent());
       
        }
    
    public void printReceivedMessages() {
        Message message;
        while ((message = messagequeue.poll()) != null) { 
            System.out.println("[" + message.getTimestamp() + "] From User " + 
                               message.getSenderId() + ": " + message.getContent());
        }
    }


    }
    
    

