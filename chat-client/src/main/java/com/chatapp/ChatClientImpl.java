package com.chatapp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {

    public ChatClientImpl() throws RemoteException {}

    @Override
    public void receiveMessage(Message message) throws RemoteException {
        System.out.println(message);
    }
}
