package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectDM implements IConnect {
    private static final Properties properties = new Properties();
    private String url, userName, password;
    private Connection connection;

    public ConnectDM() {
        {
            try {
                // Load file cấu hình từ classpath hoặc đường dẫn tuyệt đối
                properties.load(new FileInputStream("database.properties"));
                this.setUrl(properties.getProperty("urlDM"));
                this.setUserName(properties.getProperty("usernameDM"));
                this.setPassword(properties.getProperty("passwordDM"));
            } catch (IOException e) {
                e.printStackTrace();
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

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
        ConnectDM connectDM=new ConnectDM();
        System.out.println(connectDM.url +connectDM.userName+connectDM.password);
    }
}
