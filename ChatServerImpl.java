import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;


public class ChatServerImpl implements ChatServer {

    private final Map<Integer, ChatClient>activeClients = new HashMap();
	private final Map<String, ChatClient> clients = new HashMap<>();
    private final List<Message> history = new ArrayList<>();

	// public ChatServerImpl() throws RemoteException {
    //     super(8082);  // let RMI manage properly
    // }
	
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


  
    //private static final String HISTORY_FILE = "history.ser";

//    public ChatServerImpl() throws RemoteException {
//        loadHistory();
//    }
//    
//    
//
@Override
    public synchronized void joinGroup(int userId) throws RemoteException{
          
			
			Database.joinGroup(userId);    	        
		   ChatClient client = activeClients.get(userId);
			if(client != null){
				clients.put(String.valueOf(userId),client);
				}
				System.out.println("User"+ userId + "Joined GroupForum");
			}
      //broadcast(new Message("Server", username + " joined the chat"));
     


//leave Group 

@Override
    public synchronized void leaveGroup(int userId) throws RemoteException{
          
			
		Database.leaveGroup(userId);    	        
		   ChatClient client = activeClients.get(userId);
			if(client != null){
				clients.remove(String.valueOf(userId),client);
				}
				System.out.println("User"+ userId + "Joined GroupForum");
			}
      //broadcast(new Message("Server", username + " joined the chat"));
     

 
//Send Group Message
@Override
public synchronized void sendGroupMessage(int senderId, String content) {
    int groupId = 1;  // your group ID
    Database.insertMessage(senderId, null, groupId, content);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    Message message = new Message(senderId, null, content, timestamp);

    // Get all users in the group
    List<Integer> members = Database.getGroupMembers(groupId);

    // Send message to each member if they are currently online
    for (Integer memberId : members) {
        ChatClient client = activeClients.get(memberId);
        if (client != null) {
            try {
                client.receiveMessage(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    System.out.println("User " + senderId + " sent group message: " + content);
}
@Override
public List<Message> getGroupHistory(int userId) throws RemoteException{
	int groupId = 1;
	List<Integer>members = Database.getGroupMembers(groupId);
	if(!members.contains(userId)){
		return new ArrayList<>();
	}
	return Database.getGroupMessage(groupId);
}
}
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
