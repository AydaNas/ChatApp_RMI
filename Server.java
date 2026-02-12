import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class Server {
    public static void main(String[] args) {
    	 System.setProperty("java.rmi.server.hostname", "127.0.0.1");
    	int port = 8082;
        try {
            ChatServerImpl impl = new ChatServerImpl();
            ChatServer stub = (ChatServer) UnicastRemoteObject.exportObject(impl, 0);

            Registry registry = null;
            try {
                registry = LocateRegistry.createRegistry(port);
                System.out.println("RMI registry created at port 8082");
            } catch (ExportException e) {
                // registry already exists
                registry = LocateRegistry.getRegistry(port);
                System.out.println("RMI registry already running at port 8082");
            }

            registry.rebind("ChatService", stub);
            System.out.println("Server ready");

        } catch (Exception e) {
            System.err.println("Error on server: " + e);
            e.printStackTrace();
        }
    }
}
