package com.chatapp;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) throws Exception {
        try {
            if (args.length < 2) {
                System.out.println("Usage: java HelloClient <rmiregistry host> <rmiregistry port>");
                return;
            }

            String host = args[0];
            int port = Integer.parseInt(args[1]);

            Scanner scanner = new Scanner(System.in);

            System.out.println("Your name: ");
            String userName = scanner.nextLine().trim();

            Registry registry = LocateRegistry.getRegistry(host, port);
            ChatServer server = (ChatServer) registry.lookup("ChatService");

            ChatClientImpl client = new ChatClientImpl();

            System.out.println("Welcome, " + userName + "!");
            System.out.println("Commands:");
            System.out.println("/create <chatName>  - Create a new chat room");
            System.out.println("/join <chatName>    - Join a chat room");
            System.out.println("/list               - List available chat rooms");
            System.out.println("Type messages to send, or 'q' to quit.\n");

            String currentChat = null;

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("q")) {
                    if (currentChat != null) {
                        server.leaveChat(currentChat, userName);
                    }
                    System.out.println("Goodbye!");
                    break;
                }

                // Create new chat
                if (input.startsWith("/create ")) {
                    String chatName = input.substring(8).trim();
                    server.createChat(chatName);
                    System.out.println("Chat '" + chatName + "' created.");
                    continue;
                }

                // Join chat
                if (input.startsWith("/join ")) {
                    currentChat = input.substring(6).trim();
                    server.joinChat(currentChat, userName, client);

                    // Show chat history
                    List<Message> history = server.getHistory(currentChat);
                    System.out.println("----- Chat History for '" + currentChat + "' -----");
                    for (Message m : history) {
                        System.out.println(m);
                    }
                    System.out.println("----------------------------------------");
                    continue;
                }

                // List chats
                if (input.equals("/list")) {
                    List<String> chats = server.listChats();
                    System.out.println("Available chats: " + chats);
                    continue;
                }

                // Sending message
                if (currentChat == null) {
                    System.out.println("You must join a chat first. Use /join <chatName>");
                    continue;
                }

                Message msg = new Message(userName, input);
                server.sendMessage(currentChat, msg);
            }

            scanner.close();

        } catch (Exception e) {
            System.err.println("Error on client: " + e);
            e.printStackTrace();
        }
    }

}
