package com.chatapp.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:chatdb"); //for now just an in-memory db, for debugging
        config.setUsername("superuser");
        config.setPassword("");
        config.setMaximumPoolSize(10);       // max simultaneous connections
        config.setMinimumIdle(2);            // keep at least 2 idle connections
        config.setAutoCommit(true);
        config.setPoolName("ChatDBPool");

        dataSource = new HikariDataSource(config);

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS messages (
                    id IDENTITY PRIMARY KEY,
                    chat_name VARCHAR(100),
                    sender VARCHAR(100),
                    content VARCHAR(1000),
                    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)
            """);
            System.out.println("H2 database initialized, connection pool is ready!");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize the database");
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
            System.out.println("Database connection pool shut down.");
        }
    }
}
