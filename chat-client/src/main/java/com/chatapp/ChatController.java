package com.chatapp;

import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;


public class ChatController {

    private final ChatView view;
    private final ChatModel model;
    private final ChatServer server;
    private final ChatClientImpl client;

    public ChatController(String host, int port, String username) throws Exception {

        model = new ChatModel(username);
        view = new ChatView();

        Registry registry = LocateRegistry.getRegistry(host, port);
        server = (ChatServer) registry.lookup("ChatService");

        client = new ChatClientImpl(this);

        initListeners();
        refreshChatList();

        view.setVisible(true);
    }

    private void initListeners() {

        view.refreshButton.addActionListener(e -> refreshChatList());

        view.createButton.addActionListener(e -> createChat());

        view.chatList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedChat = view.chatList.getSelectedValue();
                if (selectedChat != null) {
                    joinChat(selectedChat);
                }
            }
        });

        view.sendButton.addActionListener(e -> sendMessage());
        view.messageField.addActionListener(e -> sendMessage());
    }

    private void refreshChatList() {
        try {
            List<String> chats = server.listChats();

            view.chatListModel.clear();
            for (String chat : chats) {
                view.chatListModel.addElement(chat);
            }

        } catch (Exception e) {
            showError("Failed to refresh chat list", e);
        }
    }

    private void createChat() {
        try {
            String chatName = JOptionPane.showInputDialog(
                    view,
                    "Enter new chat name:",
                    "Create Chat",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (chatName == null || chatName.trim().isEmpty()) {
                return;
            }

            chatName = chatName.trim();

            server.createChat(chatName);

            refreshChatList();

            // seems quite logical - creating chat --> joining it right away?
            joinChat(chatName);

        } catch (Exception e) {
            showError("Failed to create chat", e);
        }
    }

    private void joinChat(String chatName) {
        try {

            //leave current chat if necessary
            if (model.getCurrentChat() != null) {
                server.leaveChat(model.getCurrentChat(), model.getUsername());
            }

            model.setCurrentChat(chatName);

            server.joinChat(chatName, model.getUsername(), client);

            view.chatArea.setText("");

            List<Message> history = server.getHistory(chatName);
            for (Message m : history) {
                view.chatArea.append(m.toString() + "\n");
            }

            view.setTitle("RMI Chat - " + chatName);

        } catch (Exception e) {
            showError("Failed to join chat", e);
        }
    }

    private void sendMessage() {
        try {
            String text = view.messageField.getText().trim();

            if (text.isEmpty()) return;

            if (model.getCurrentChat() == null) {
                JOptionPane.showMessageDialog(view,
                        "Join a chat room first.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Message msg = new Message(model.getUsername(), text);
            server.sendMessage(model.getCurrentChat(), msg);

            view.messageField.setText("");

        } catch (Exception e) {
            showError("Failed to send message", e);
        }
    }
    public void onMessageReceived(Message message) {
        SwingUtilities.invokeLater(() -> view.chatArea.append(message.toString() + "\n"));
    }


    private void showError(String message, Exception e) {
        e.printStackTrace();

        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(view, message + "\n" + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE)
        );
    }
}
