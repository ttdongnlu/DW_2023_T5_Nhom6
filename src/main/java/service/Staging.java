package service;

import dao.ConnectStaging;

import java.sql.*;

public class Staging {
    static ConnectStaging connectStaging = new ConnectStaging();

    public void loadStaging() {
        // Buoc 1: Kết nối đến database
        try (Connection connection = connectStaging.connectToMySQL()) {
            System.out.println("Đã kết nối đến MySQL.");

            // Buoc 2: Tạo một truy vấn SQL
            String sqlQuery = "SELECT * FROM tghd_staging";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

                // Buoc 3: Thực hiện truy vấn và nhận kết quả
                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    // Buoc 4: Xử lý kết quả
                    while (resultSet.next()) {
                        // Xử lý dữ liệu từ kết quả truy vấn
                        int id = resultSet.getInt("id");
                        String source = resultSet.getString("source");
                        String get_date = resultSet.getString("get_date");
                        String date_time = resultSet.getString("date_time");
                        String currency_symbol = resultSet.getString("currency_symbol");
                        String currency_name = resultSet.getString("currency_name");
                        String buy_cash = resultSet.getString("buy_cash");
                        String buy_transfer = resultSet.getString("buy_transfer");
                        String sale_cash = resultSet.getString("sale_cash");
                        String sale_transfer = resultSet.getString("sale_transfer");

                        // In ra màn hình hoặc xử lý dữ liệu theo nhu cầu của bạn
                        System.out.println(id + ", " + source + ", " + get_date + ", " + date_time + ", " + currency_symbol + ", "
                                + currency_name + ", " + buy_cash + ", " + buy_transfer + ", " + sale_cash + ", " + sale_transfer);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        Staging staging = new Staging();
        staging.loadStaging();
    }
}
