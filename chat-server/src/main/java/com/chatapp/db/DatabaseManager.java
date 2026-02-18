package com.chatapp.db;

import java.sql.*;

public class DatabaseManager {

    private static final String URL = "jdbc:h2:./data/chatdb";
    private static final String USER = "superuser";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("org.h2.Driver");  // ensures driver is loaded
            System.out.println("H2 driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try (Connection c = getConnection();
             Statement stmt = c.createStatement()) {

            //TODO: replace usernames with userId?
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS messages (
                    id IDENTITY PRIMARY KEY,
                    chat_name VARCHAR(100),
                    sender VARCHAR(100),
                    content VARCHAR(1000),
                    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}



