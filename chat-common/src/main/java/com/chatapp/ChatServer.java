package com.chatapp;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatServer extends Remote {

  void createChat(String chatName) throws RemoteException;

  void joinChat(String chatName, String userName, ChatClient client)
          throws RemoteException;

  void leaveChat(String chatName, String userName)
          throws RemoteException;

  void sendMessage(String chatName, Message message)
          throws RemoteException;

  List<Message> getHistory(String chatName)
          throws RemoteException;

  List<String> listChats()
          throws RemoteException;
}
