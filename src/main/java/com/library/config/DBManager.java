package com.library.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/feminist_library";
    private static final String USER = "postgres";
    private static final String PASS = "zulash2020";
    private static Connection connection;

    public static Connection getConnection(){
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
