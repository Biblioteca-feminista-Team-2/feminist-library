package com.library.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class DBManager {
    public static Dotenv dotenv = Dotenv.load();

    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASS = dotenv.get("DB_PASSWORD");
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        try{ 
            System.out.println("Base de datos conectada");
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
        return connection;
    }

    public static void closeConnection() {
        try {
            System.out.println("Base de datos desconectada");
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }
}
