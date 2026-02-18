package com.chatapp;

public class ChatModel {
    private final String userName;
    private String currentChat;

    public ChatModel(String username) {
        this.userName = username;
    }

    public String getUsername() { return userName; }
    public String getCurrentChat() { return currentChat; }
    public void setCurrentChat(String chat) { this.currentChat = chat; }
}
