package utils;

import java.sql.*;
import java.util.Properties;

public class MySqlConnector {
    private static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/stx_analyzer?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "danielroot";
    private Connection connection;
    private Properties properties;

    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
        }
        return properties;
    }

    public void connect() throws SQLException, ClassNotFoundException {
        Class.forName(DATABASE_DRIVER);
        connection = DriverManager.getConnection(DATABASE_URL, getProperties());
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(DATABASE_DRIVER);
        connection = DriverManager.getConnection(DATABASE_URL, getProperties());
        return connection;
    }

    public ResultSet query(String sql) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement.executeQuery(sql);
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}