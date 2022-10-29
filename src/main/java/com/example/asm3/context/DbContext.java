package com.example.asm3.context;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * lớp singleton tạo {@link Connection} kết nối database bằng JNDI
 */
public class DbContext {
    public static DbContext instance;
    private Connection connection;

    private DbContext() {
        try {
            InitialContext initialContext = new InitialContext();
            Context envContext = (Context) initialContext.lookup("java:comp/env");
            DataSource dataSource = (DataSource) envContext.lookup("jdbc/shoppingdb");
            connection = dataSource.getConnection();
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized static DbContext getInstance() {
        if (instance == null) {
            instance = new DbContext();
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
