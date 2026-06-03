package com.cityguardian.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:cityguardian.db";
    private static Connection connection;

    public static void initialize() {
        try {
            connection = DriverManager.getConnection(URL);
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Grid tiles/Nodes
            stmt.execute("CREATE TABLE IF NOT EXISTS tiles (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "x INTEGER," +
                    "y INTEGER," +
                    "type TEXT" +
                    ")");

            // Citizens
            stmt.execute("CREATE TABLE IF NOT EXISTS citizens (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "age INTEGER," +
                    "health INTEGER," +
                    "x INTEGER," +
                    "y INTEGER" +
                    ")");

            // Stats or past simulation runs
            stmt.execute("CREATE TABLE IF NOT EXISTS simulation_runs (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "casualties INTEGER," +
                    "saved INTEGER" +
                    ")");
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
