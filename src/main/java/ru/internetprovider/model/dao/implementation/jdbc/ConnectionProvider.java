package ru.internetprovider.model.dao.implementation.jdbc;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides a DataSource for JDBC DAO objects.
 */
public class ConnectionProvider {

    private static final String DATA_SOURCE_NAME = "java:/comp/env/jdbc/provider_db";
    private static DataSource dataSource;

    static {
        try {
            Context context = new InitialContext();
            dataSource = (DataSource) context.lookup(DATA_SOURCE_NAME);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
