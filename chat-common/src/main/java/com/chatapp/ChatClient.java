package com.chatapp;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ChatClient extends Remote {
    public void receiveMessage(Message message) throws RemoteException;
}