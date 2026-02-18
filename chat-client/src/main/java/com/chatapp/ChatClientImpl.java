
package com.chatapp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {

    private final ChatController controller;

    public ChatClientImpl(ChatController controller) throws RemoteException {
        this.controller = controller;
    }

    @Override
    public void receiveMessage(Message message) throws RemoteException {
        controller.onMessageReceived(message);
    }
}

