package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectStaging implements IConnect {
    private static final Properties properties = new Properties();
    private String url, userName, password;
    private Connection connection;

    public ConnectStaging() {
        {
            try {
                // Load file cấu hình từ classpath hoặc đường dẫn tuyệt đối
                properties.load(new FileInputStream("database.properties"));
                this.setUrl(properties.getProperty("urlStaging"));
                this.setUserName(properties.getProperty("usernameStaging"));
                this.setPassword(properties.getProperty("passwordStaging"));
            } catch (IOException e) {
                System.err.println("Error config properties to the database: " + e.getMessage());
            }
        }
    }

    @Override
    public Connection connectToMySQL() {
        connection = null;

        try {
            // Đăng ký Driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Tạo kết nối
            connection = DriverManager.getConnection( url, userName, password);
            System.out.println("Đã kết nối đến MySQL.");

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }

        return connection;
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đã đóng kết nối đến MySQL.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing to the database: " + e.getMessage());
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void main(String[] args) {
        ConnectStaging connectStaging=new ConnectStaging();
        System.out.println(connectStaging.url +connectStaging.userName+connectStaging.password);
        System.out.println(connectStaging.connectToMySQL());
    }
}