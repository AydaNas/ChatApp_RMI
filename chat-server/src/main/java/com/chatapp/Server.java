package com.chatapp;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Server {
    public static void main(String[] args) {
        try {

            System.setProperty("java.rmi.server.hostname", "127.0.0.1");

            ChatServerImpl impl = new ChatServerImpl();
            ChatServer stub = (ChatServer) UnicastRemoteObject.exportObject(impl, 0);

            Registry registry = null;
            if (args.length>0)
                registry= LocateRegistry.getRegistry(Integer.parseInt(args[0]));
            else
                registry = LocateRegistry.getRegistry();
            registry.rebind("ChatService", stub);

            System.out.println ("Server ready");
        } catch (Exception e) {
            System.err.println("Error on server :" + e) ;
            e.printStackTrace();
        }
    }
}