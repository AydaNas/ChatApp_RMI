import java.rmi.registry.*;
import java.rmi.server.*;

public class Server {
    public static void main(String[] args) {
        try {
            // Force RMI to use localhost
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");

            // Create server implementation
            ChatServerImpl impl = new ChatServerImpl();
            ChatServer stub = (ChatServer) UnicastRemoteObject.exportObject(impl, 0);

            // Start or get RMI registry on port 8082
            int port = 8082;
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(port); // creates registry if none
                System.out.println("RMI Registry created on port " + port);
            } catch (Exception e) {
                registry = LocateRegistry.getRegistry(port); // fallback if already exists
                System.out.println("RMI Registry found on port " + port);
            }

            // Bind the stub
            registry.rebind("ChatService", stub);

            System.out.println("Server ready on port " + port);
        } catch (Exception e) {
            System.err.println("Error on server :" + e);
            e.printStackTrace();
        }
    }
}
