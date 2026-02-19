package com.chatapp;

import javax.swing.*;
import java.awt.*;



public class ChatView extends JFrame {

    public JTextArea chatArea = new JTextArea();

    public JTextField messageField = new JTextField();
    public JButton sendButton = new JButton("Send");

    public DefaultListModel<String> chatListModel = new DefaultListModel<>();
    public JList<String> chatList = new JList<>(chatListModel);
    public JButton refreshButton = new JButton("Refresh");
    public JButton createButton = new JButton("New Chat Room");

    public ChatView() {
        setTitle("RMI Chat");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea.setEditable(false);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Chat Rooms"), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(chatList), BorderLayout.CENTER);

        JPanel leftBottom = new JPanel(new GridLayout(2, 1));
        leftBottom.add(refreshButton);
        leftBottom.add(createButton);

        leftPanel.add(leftBottom, BorderLayout.SOUTH);
        leftPanel.setPreferredSize(new Dimension(200, 0));

        add(leftPanel, BorderLayout.WEST);

        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);


        add(bottomPanel, BorderLayout.SOUTH);
    }
}
