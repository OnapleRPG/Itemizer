package com.ylinor.brawlator.data.handler;

import com.ylinor.brawlator.data.dao.EffectDAO;
import com.ylinor.brawlator.data.dao.MonsterDAO;


import javax.inject.Inject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class SqliteHandler {
    @Inject
    private static Logger logger;

    public static void testConnection(){
   Connection c = null;

   try {
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection(url);
    } catch ( Exception e ) {
        System.out.print( e.getClass().getName() + ": " + e.getMessage() );

    }
      System.out.print("Opened database successfully");
    }
    public static final String NAME = "brawlator.db";

    public static final String url = "jdbc:sqlite:"+NAME;



    private static Connection connection = null;



    public static Connection getConnection() {
        if (connection == null) {
            createConnexion();
        }
        return connection;
    }


    private static Connection createConnexion() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);

            createDatabase();
            System.out.println("connexion");
            return connection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }/* finally {
          /*  try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }*/ catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static void createDatabase(){
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(MonsterDAO.tableCreation);
            stmt.execute(EffectDAO.tableCreation);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
