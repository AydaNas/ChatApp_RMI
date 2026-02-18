package com.chatapp;

import com.chatapp.db.DatabaseManager;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class ChatServerImpl implements ChatServer {
    private final Map<String, Map<String, ChatClient>> chatRooms = new ConcurrentHashMap<>();

    @Override
    public synchronized void createChat(String chatName) throws RemoteException {
        chatRooms.putIfAbsent(chatName, new ConcurrentHashMap<>());
    }

    @Override
    public synchronized void joinChat(String chatName, String userName, ChatClient client)
            throws RemoteException {

        chatRooms.putIfAbsent(chatName, new ConcurrentHashMap<>());
        chatRooms.get(chatName).put(userName, client);
    }

    @Override
    public synchronized void leaveChat(String chatName, String userName) throws RemoteException {
        Map<String, ChatClient> clients = chatRooms.get(chatName);
        if (clients != null) {
            clients.remove(userName);
        }
    }

    @Override
    public List<String> listChats() throws RemoteException {
        return new ArrayList<>(chatRooms.keySet());
    }

    @Override
    public void sendMessage(String chatName, Message message) throws RemoteException {
        try {
            saveMessage(chatName, message);
            Map<String, ChatClient> clients = chatRooms.get(chatName);
            if (clients != null) {
                for (ChatClient client : clients.values()) {
                    client.receiveMessage(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Failed to send message", e);
        }
    }

    @Override
    public List<Message> getHistory(String chatName) throws RemoteException {
        List<Message> history = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT sender, content FROM messages WHERE chat_name = ? ORDER BY id")) {

            ps.setString(1, chatName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                history.add(new Message(
                        rs.getString("sender"),
                        rs.getString("content")));
            }
            return history;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Failed to fetch history", e);
        }
    }

    private void saveMessage(String chatName, Message message) throws Exception {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO messages (chat_name, sender, content) VALUES (?, ?, ?)")) {

            ps.setString(1, chatName);
            ps.setString(2, message.getSender());
            ps.setString(3, message.getContent());
            ps.executeUpdate();
        }
    }


}
















