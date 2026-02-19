import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class ClientMain {

    private static int getIntInput(Scanner input) {
        while (true) {
            try {
            return Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
            System.out.println("Enter a valid number:");
            }
        }
    }
    public static void main(String[] args) throws Exception {

        Scanner input = new Scanner(System.in);
        String host = "127.0.0.1";
        int port = 8082;

        Registry registry = LocateRegistry.getRegistry(host, port);
        ChatServer server = (ChatServer) registry.lookup("ChatService");
        ChatClientImpl client = new ChatClientImpl();

        boolean loggedIn = false;
        boolean joinedGroup = false;

        while (true) {

            if (!loggedIn) {

                System.out.println("\n=== MAIN MENU ===");
                System.out.println("1 Register");
                System.out.println("2 Login");
                System.out.println("3 Exit");

                int choice = getIntInput(input);

                switch (choice) {

                    case 1:
                        System.out.println("Username:");
                        String username = input.nextLine();

                        System.out.println("Password:");
                        String password = input.nextLine();

                        int userId = server.registerClient(username, password, client);
                        client.setUserId(userId);

                        System.out.println("Registered successfully. Your ID: " + userId);
                        break;

                    case 2:
                        System.out.println("Username:");
                        String loginUser = input.nextLine();

                        System.out.println("Password:");
                        String loginPass = input.nextLine();

                        boolean success = server.loginUser(loginUser, loginPass, client);

                        if (success) {
                            System.out.println("Login successful");
                            loggedIn = true;
                        } else {
                            System.out.println("Login failed");
                        }
                        break;

                    case 3:
                        System.out.println("Goodbye!");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice");
                }
            }

            else if (!joinedGroup) {

                System.out.println("\n=== USER MENU ===");
                System.out.println("1 Private Chat");
                System.out.println("2 Show Private History");
                System.out.println("3 Join Group");
                System.out.println("4 Logout");

                int choice = getIntInput(input);

                switch (choice) {

                    case 1:

                        List<UserList> users = server.getActiveUsers();

                        if (users.isEmpty()) {
                            System.out.println("No users found.");
                            break;
                        }

                        System.out.println("Active Users:");
                        for (UserList u : users) {
                            System.out.println(u.getUserId() + " - " + u.getUserName());
                        }

                        System.out.println("Select user ID:");
                        int selectedId = getIntInput(input);

                        UserList selectedUser = null;
                        for (UserList u : users) {
                            if (u.getUserId() == selectedId) {
                                selectedUser = u;
                                break;
                            }
                        }

                        if (selectedUser == null) {
                            System.out.println("Invalid user.");
                            break;
                        }
                     
                        System.out.println("Type /exit to leave");

                        while (true) {

                            String message = input.nextLine();

                            if (message.equalsIgnoreCase("/exit")) {
                                break;
                            }

                            server.sendMessage(
                                    client.getUserId(),
                                    selectedUser.getUserId(),
                                    
                                    message
                            );
                        }
                        break;

                    case 2:

                        List<Message> history = server.chatHistory(client.getUserId());

                        if (history.isEmpty()) {
                            System.out.println("No messages.");
                        }

                        for (Message m : history) {
                            System.out.println("(" + m.getTimestamp() + ") From "
                                    + m.getSenderId() + " To "
                                    + m.getReceiverId() + " : "
                                    + m.getContent());
                        }

                        break;

                    case 3:

                        server.joinGroup(client.getUserId());
                        joinedGroup = true;
                        System.out.println("Joined GroupForum");
                        break;

                    case 4:

                        server.logoutUser(client.getUserId());
                        loggedIn = false;
                        joinedGroup = false;
                        System.out.println("Logged out");
                        break;

                    default:
                        System.out.println("Invalid option");
                }
            }

            else {

                System.out.println("\n=== GROUP MENU ===");
                System.out.println("1 Send Group Message");
                System.out.println("2 Show Group History");
                System.out.println("3 Leave Group");
                System.out.println("4 Back");

                int groupChoice = getIntInput(input);

                switch (groupChoice) {

                    case 1: // Send group message
    System.out.println("=== Group Chat ===");
    System.out.println("Type /exit to leave");

    while (true) {
        String message = input.nextLine();
        if (message.equalsIgnoreCase("/exit")) {
            break;
        }
          server.sendGroupMessage(client.getUserId(), message);
        
        
    }
    break;

                    case 2:

                        List<Message> groupHistory =
                                server.getGroupHistory(client.getUserId());

                        if (groupHistory.isEmpty()) {
                            System.out.println("No group messages.");
                        }

                        for (Message m : groupHistory) {
                            System.out.println("(" + m.getTimestamp() + ") User " + m.getSenderId()+ " : "
                                    + m.getContent());
                        }

                        break;

                    case 3:

                        server.leaveGroup(client.getUserId());
                        joinedGroup = false;
                        System.out.println("Left group");
                        break;

                    case 4:

                        joinedGroup = false;
                        break;

                    default:
                        System.out.println("Invalid option");
                }
            }
        }
    }
}
