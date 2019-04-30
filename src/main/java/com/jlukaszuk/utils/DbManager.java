package com.jlukaszuk.utils;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jlukaszuk.models.Category;
import com.jlukaszuk.models.SubCategory;
import com.jlukaszuk.models.Word;
import java.io.IOException;
import java.sql.SQLException;

public class DbManager {


    private static final String JDBC_DRIVER_SQLLITE = "jdbc:sqlite:database.db";
    private static ConnectionSource connectionSource;

    public static void initializeDatabase() throws SQLException, IOException {
        createConnectionSource();
        crateTable();
        closeConnection();
    }

    private static void createConnectionSource() throws SQLException {

        connectionSource = new JdbcConnectionSource(JDBC_DRIVER_SQLLITE);

    }

    public static ConnectionSource getConnectionSource() throws SQLException {
        if (connectionSource == null) {
            createConnectionSource();
        }
        return connectionSource;
    }

    public static void closeConnection() throws IOException {
        if (connectionSource != null) {
            connectionSource.close();
        }
    }



    public static void crateTable() throws SQLException {

        TableUtils.createTableIfNotExists(connectionSource, Word.class);
        TableUtils.createTableIfNotExists(connectionSource, Category.class);
        TableUtils.createTableIfNotExists(connectionSource, SubCategory.class);
    }

    public static void dropTable() throws SQLException {

        TableUtils.dropTable(connectionSource,Word.class, true);
        TableUtils.dropTable(connectionSource,Category.class, true);
        TableUtils.dropTable(connectionSource,SubCategory.class, true);

    }
}
