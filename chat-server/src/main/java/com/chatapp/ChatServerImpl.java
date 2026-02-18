package com.chatapp;

import java.rmi.RemoteException;
import java.util.*;
import java.io.*;


public class ChatServerImpl implements ChatServer {

    private final Map<String, ChatClient> clients = new HashMap<>();
    private final List<Message> history = new ArrayList<>();
    private static final String HISTORY_FILE = "history.ser";

    public ChatServerImpl() throws RemoteException {
        loadHistory();
    }

    @Override
    public synchronized void join(String username, ChatClient client)
            throws RemoteException {
        clients.put(username, client);
        broadcast(new Message("Server", username + " joined the chat"));
    }

    @Override
    public synchronized void leave(String username)
            throws RemoteException {
        clients.remove(username);
        broadcast(new Message("Server", username + " left the chat"));
    }

    @Override
    public synchronized void sendMessage(Message message)
            throws RemoteException {
        history.add(message);
        saveHistory();
        broadcast(message);
    }

    @Override
    public synchronized List<Message> getHistory()
            throws RemoteException {
        return new ArrayList<>(history);
    }

    @Override
    public synchronized List<String> getJoinedUsers() {
        return clients.keySet().stream().toList();
    }


    private void broadcast(Message message) {
        for (ChatClient client : clients.values()) {
            try {
                client.receiveMessage(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    private void saveHistory() {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(HISTORY_FILE))) {
            out.writeObject(history);
        } catch (IOException ignored) {}
    }

    @SuppressWarnings("unchecked")
    private void loadHistory() {
        File f = new File(HISTORY_FILE);
        if (!f.exists()) return;

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(f))) {
            history.addAll((List<Message>) in.readObject());
        } catch (IOException | ClassNotFoundException ignored) {}
    }
}
