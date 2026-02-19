import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) throws Exception {

        Database.databaseCreation();
        Database.createGroup();

        ChatServerImpl server = new ChatServerImpl();

        Registry registry = LocateRegistry.createRegistry(8082);
        registry.rebind("ChatService", server);

        System.out.println("Server is running...");
    }
}
