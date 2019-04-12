package com.nihillon.utils;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.nihillon.models.Category;
import com.nihillon.models.SubCategory;
import com.nihillon.models.Word;

import java.io.IOException;
import java.sql.SQLException;

public class DbManager {


    private static final String JDBC_DRIVER_SQLLITE = "jdbc:sqlite:database.db";
    private static ConnectionSource connectionSource;

    public static void initializeDatabase() {
        createConnectionSource();
        dropTable();
        crateTable();
        closeConnection();
    }

    private static void createConnectionSource() {
        try {
            connectionSource = new JdbcConnectionSource(JDBC_DRIVER_SQLLITE);
        } catch (SQLException e) {
        }
    }

    public static ConnectionSource getConnectionSource() {
        if (connectionSource == null) {
            createConnectionSource();
        }
        return connectionSource;
    }

    public static void closeConnection() {
        if (connectionSource != null) {
            try {
                connectionSource.close();
            } catch (IOException e) {

            }
        }
    }



    public static void crateTable() {
      try {


          TableUtils.createTableIfNotExists(connectionSource, Word.class);
           TableUtils.createTableIfNotExists(connectionSource, Category.class);
           TableUtils.createTableIfNotExists(connectionSource, SubCategory.class);


        } catch (SQLException e) {
           e.printStackTrace();
        }
    }

    public static void dropTable() {
      try {

           TableUtils.dropTable(connectionSource,Word.class, true);
            TableUtils.dropTable(connectionSource,Category.class, true);
           TableUtils.dropTable(connectionSource,SubCategory.class, true);


       } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
