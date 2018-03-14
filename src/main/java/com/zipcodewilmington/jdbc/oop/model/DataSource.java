package com.zipcodewilmington.jdbc.oop.model;

import org.mariadb.jdbc.MySQLDataSource;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 3/13/18.
 * This class is responsible for centralizing the Data Source logic
 */
public class DataSource implements Closeable {
    private MySQLDataSource dataSource;
    private Connection connection;

    /**
     * @param databaseName
     * @param serverName
     * @param user
     * @param password
     */
    public DataSource(String databaseName, String serverName, String user, String password) {
        MySQLDataSource dataSource = new MySQLDataSource();
        dataSource.setDatabaseName(databaseName);
        dataSource.setServerName(serverName);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        this.dataSource = dataSource;
    }

    /**
     * @return connection to the datasource
     */
    public Connection getConnection() {
        if (connection == null) {
            try {
                this.connection = dataSource.getConnection();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return connection;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}