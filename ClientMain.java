import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static java.awt.SystemColor.text;

public class ClientMain {

    public static void main(String[] args) throws Exception {
        try {
            if (args.length < 2) {
                System.out.println("Usage: java HelloClient <rmiregistry host> <rmiregistry port>");
                return;
            }

            String host = args[0];
            int port = Integer.parseInt(args[1]);

            System.out.println("Your name: ");
            String userName = System.console().readLine();

            Registry registry = LocateRegistry.getRegistry(host, port);
            ChatServer server = (ChatServer) registry.lookup("ChatService");

            ChatClientImpl client = new ChatClientImpl();
            server.join(userName, client);

            for (Message m : server.getHistory()) {
                System.out.println(m);
            }

            while (true) {
                String msg = System.console().readLine();
                if (msg.equalsIgnoreCase("q"))  {
                    break;
                };

                server.sendMessage(new Message(userName, msg));
            }

            server.leave(userName);

        } catch (Exception e)  {
    		System.err.println("Error on client: " + e);
            e.printStackTrace();
        }
    }

}
