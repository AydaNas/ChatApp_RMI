package com.chatapp;

import javax.swing.JOptionPane;

public class ClientMain {

    public static void main(String[] args) throws Exception {
        try {
            if (args.length < 2) {
                System.out.println("Usage: java ClientMain <rmiregistry host> <rmiregistry port>");
                return;
            }

            String host = args[0];
            int port = Integer.parseInt(args[1]);

            String username = JOptionPane.showInputDialog("Enter username:");
            new ChatController(host, port, username);

            } catch (Exception e) {
            System.err.println("Error on client: " + e);
            e.printStackTrace();
        }
    }

}
