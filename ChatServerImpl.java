import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

    private final Map<Integer, ChatClient> activeClients = new HashMap<>();

    protected ChatServerImpl() throws RemoteException {
        super();
    }
	//list of astive users using the system

    @Override
    public synchronized List<UserList> getActiveUsers() throws RemoteException {
        return Database.getActiveusers();
    }

    @Override
    public synchronized void logoutUser(int userId) throws RemoteException {
        activeClients.remove(userId);
        System.out.println("User " + userId + " logged out.");
    }
//Regisering the user
    @Override
    public synchronized int registerClient(String username,String password,ChatClient client)
            throws RemoteException {

        int userId = Database.registerClient(username, password);

        if (userId == -1) {
            throw new RuntimeException("Registration failed");
        }

        activeClients.put(userId, client);
        return userId;
    }
//Login after registration 
    @Override
    public synchronized boolean loginUser(String username,String password,ChatClient client)
            throws RemoteException {

        boolean success = Database.userLogin(username, password);

        if (!success) {
            return false;
        }

        int userId = Database.getUserId(username);

        activeClients.put(userId, client);

        // Send pending private messages
        List<Message> pending = Database.getMessagesForUser(userId);

        for (Message m : pending) {
            client.receiveMessage(m);
        }

        return true;
    }
	//send private Messages

   @Override
public synchronized void sendMessage(int senderId, Integer receiverId, String content) throws RemoteException {
    if (receiverId == null) {
        throw new IllegalArgumentException("receiverId cannot be null for private messages");
    }

    Database.insertMessage(senderId, receiverId, null, content);
    Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
    Message message = new Message(senderId, receiverId, null, content, timestamp);

    ChatClient receiver = activeClients.get(receiverId);
    if (receiver != null) {
        receiver.receiveMessage(message);
    }

    System.out.println("Private message from " + senderId + " to " + receiverId + " : " + content);
}


    @Override
    public List<Message> chatHistory(int userId)throws RemoteException {
        return Database.getMessagesForUser(userId);
    }
    @Override
    public synchronized void joinGroup(int userId)
            throws RemoteException {

        Database.joinGroup(userId);
        System.out.println("User "+ userId + " joined GroupForum");
    }

    @Override
    public synchronized void leaveGroup(int userId)throws RemoteException {

        Database.leaveGroup(userId);
        System.out.println("User "+ userId+ " left GroupForum");
    } 

   @Override
public synchronized void sendGroupMessage(int senderId, String content) throws RemoteException {
    int groupId = 1;

    // Check if sender is in the group
    List<Integer> members = Database.getGroupMembers(groupId);
    if (!members.contains(senderId)) {
        System.out.println("User " + senderId + " is not in the group. Message not sent.");
        return;
    }

    Database.insertMessage(senderId, null, groupId, content);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    Message message = new Message(senderId, null, groupId, content, timestamp);

    // Only send to online members in the group
    for (Integer userId : members) {
        ChatClient client = activeClients.get(userId);
        if (client != null) {
            try {
                client.receiveMessage(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    System.out.println("Group message from " + senderId + " : " + content);
}

    @Override
public List<Message> getGroupHistory(int userId) throws RemoteException {
    int groupId = 1;
    List<Integer> members = Database.getGroupMembers(groupId);
    if (!members.contains(userId)) {
        return new ArrayList<>(); 
    }
    return Database.getGroupMessage(groupId);
}

}
