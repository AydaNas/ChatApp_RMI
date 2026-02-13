import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;


public class ChatServerImpl implements ChatServer {
    private final Map<Integer, ChatClient>activeClients = new HashMap();
	
    public synchronized List<UserList> getActiveUsers() throws RemoteException{
		return Database.getActiveusers();
	}
    @Override
	public void logoutUser(int userId) throws RemoteException{
		activeClients.remove(userId);
	}
	
	@Override
    public synchronized void sendMessage(int senderId, Integer receiverId,Integer groupId, String content)
            throws RemoteException {
    	// select * from message where client ID is same 
       Database.insertMessage(senderId,receiverId,groupId,content);
       Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
       Message message = new Message(senderId,receiverId,content,timestamp);
       
       ChatClient reciveClient = activeClients.get(receiverId);
       if(reciveClient != null) {
    	  
    		  // Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
    		  // Message message = new Message(senderId,receiverId,content,timestamp);
    		   reciveClient.receiveMessage(message);
    	   }
    	  System.out.println("sent From" + senderId + "to" + receiverId + "Message" + content);
    	  //System.out.println("Message send");
       }
       
    


	
	public synchronized int registerClient(String username, String password, ChatClient client) throws RemoteException {
		//private Map<Integer,client> activeClients = new HashMap<>();
		int userId = Database.registerClient(username, password);
		if(userId == -1) {
			throw new RuntimeException("Registration failed");
		}
		activeClients.put(userId, client);
		return userId;
				
		
	}
	
	public boolean loginUser(String username,String password, ChatClient client) {
		boolean info = Database.userLogin(username, password);
		if(info) {
			int userId = Database.getUserId(username);
			activeClients.put(userId, client);
		
		List<Message> pendingMessages = Database.getMessagesForUser(userId);
		for(Message m : pendingMessages){
			try{
				client.receiveMessage(m);
			}
			catch(RemoteException e){
							
               e.printStackTrace();
 			}

			}
			}
		
		
		
		return info;
		
	}


	@Override
	 public List<Message> chatHistory(int userId) {
		return Database.getMessagesForUser(userId);
		

	 }
    //private final Map<String, ChatClient> clients = new HashMap<>();
    //private final List<Message> history = new ArrayList<>();
    //private static final String HISTORY_FILE = "history.ser";

//    public ChatServerImpl() throws RemoteException {
//        loadHistory();
//    }
//    
//    
//
//    @Override
//    public synchronized void join(String username, ChatClient client)
//            throws RemoteException {
//    	// insert client in to the group table here 
//        clients.put(username, client);
//        broadcast(new Message("Server", username + " joined the chat"));
//    }

//    @Override
//    public synchronized void leave(String username)
//            throws RemoteException {
//    	//delete the client from the group table 
//        clients.remove(username);
//        broadcast(new Message("Server", username + " left the chat"));
//    }

    

//    @Override
//    public synchronized List<Message> getHistory()
//            throws RemoteException {
//        return new ArrayList<>(history);
//    }

//    @Override
//    public synchronized List<String> getJoinedUsers() {
//        return clients.keySet().stream().toList();
//    }
//
//
//    private void broadcast(Message message) {
//        for (ChatClient client : clients.values()) {
//            try {
//                client.receiveMessage(message);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
//    }


//    private void saveHistory() {
//        try (ObjectOutputStream out =
//                     new ObjectOutputStream(new FileOutputStream(HISTORY_FILE))) {
//            out.writeObject(history);
//        } catch (IOException ignored) {}
//    }
//
//    @SuppressWarnings("unchecked")
//    private void loadHistory() {
//        File f = new File(HISTORY_FILE);
//        if (!f.exists()) return;
//
//        try (ObjectInputStream in =
//                     new ObjectInputStream(new FileInputStream(f))) {
//            history.addAll((List<Message>) in.readObject());
//        } catch (IOException | ClassNotFoundException ignored) {}
//    }
}
