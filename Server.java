import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    public static void main(String[] args) {
        int port = 8082;

        try {
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");

            Database.databaseCreation();
            Database.createGroup();

         
            ChatServerImpl impl = new ChatServerImpl();
            ChatServer stub = (ChatServer) UnicastRemoteObject.exportObject(impl, port);

            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(port);
                System.out.println("RMI Registry created on port " + port);
            } catch (Exception e) {
                registry = LocateRegistry.getRegistry(port);
                System.out.println("Using existing RMI registry on port " + port);
            }

            // Bind the service
            registry.rebind("ChatService", stub);
            System.out.println("Server ready. Listening on port " + port);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
