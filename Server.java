import java.rmi.registry.*;
import java.rmi.server.*;

public class Server {
    public static void main(String[] args) {
        try {
           
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            ChatServerImpl impl = new ChatServerImpl();
            ChatServer stub = (ChatServer) UnicastRemoteObject.exportObject(impl, 0);
            int port = 8082;
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(port); 
                System.out.println("RMI Registry created on port " + port);
            } catch (Exception e) {
                registry = LocateRegistry.getRegistry(port); 
                System.out.println("RMI Registry found on port " + port);
            }
         
            registry.rebind("ChatService", stub);
            System.out.println("Server ready on port " + port);
        } catch (Exception e) {
            System.err.println("Error on server :" + e);
            e.printStackTrace();
        }
    }
}
