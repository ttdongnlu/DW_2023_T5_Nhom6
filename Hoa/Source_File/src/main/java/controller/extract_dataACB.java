package controller;

import beans.control_data_file;
import beans.control_data_file_config;
import beans.log;
import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import service.ConfigService;
import service.LogService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class extract_dataACB {
    public void crawlerData() {
        // lấy ra 1 dòng dữ liệu trong bảng config
        control_data_file_config fileConfig = ConfigService.getInstance().getConfigById(2);
        // Lấy ra 1 dòng liệu trong bảng data_file của ngày hôm nay
        control_data_file filedata;
         filedata = ConfigService.getInstance().getFileToday(fileConfig.getId());
        // Kiểm tra nếu file_data không có dữ liệu thì thêm dữ liệu mới vào
        if (filedata == null) {
            filedata = new control_data_file(1, fileConfig, LocalDateTime.now(), LocalDate.now(), "tiến hành trích xuất dữ liệu từ nguồn", LocalDateTime.now(), "HOA", "PREPARE");
            ConfigService.getInstance().insertDataFile(filedata);
        }

        // Kiểm tra dòng dữ liệu có đang chạy hay không nếu không thì tiến hành chạy dòng dữ liệu
        if (!filedata.getStt().equals("CRAWLING")) {
            ConfigService.getInstance().setStatusConfig("CRAWLING", fileConfig.getId());
            // Tiến hành crawl dữ liệu
            ConfigService.getInstance().setTimestamp(fileConfig.getId());
        }
        try {
            // Connect to a website and get the HTML
            System.out.println("ACB");
            Document document = Jsoup.connect(fileConfig.getSource_path()).get();
            // Kiểm tra url website
            if (document.toString() =="") {
                // Nếu lỗi đường dẫn cập nhật status ERROR và ghi lỗi vào log
                ConfigService.getInstance().setStatusConfig("ERROR", fileConfig.getId());
                LogService.getInstance().addLog(new log(1, "CRAWL DATA FROM SOURCE TO FILE", "ERROR", "URL NOT FOUND", LocalDateTime.now()));
                return;
            }
            // Extract and print the title of the HTML document
            String title = document.title();
            System.out.println("Title: " + title);

            // Đặt tên cho cột chứa đường dẫn hình ảnh (ví dụ: cột thứ 2)
            int imageColumnIndex = 1;

            Element table = document.select("table.exTbl").first();
            Elements rows = table.select("tr.ex-even, tr.ex-odd");
            // Create a CSVWriter
            String location = fileConfig.getLocation();
            String namefile = fileConfig.getName_file();
            String format = fileConfig.getFormat();
            String seperal = fileConfig.getSeparator_file();
            String timestamp = filedata.getFile_timestamp().toLocalDate().toString();
            System.out.println(timestamp);
            String sourceFile = location + namefile + seperal + timestamp + format;
            System.out.println(sourceFile);
            File file = new File(sourceFile);
            // Kiểm tra lôi đường dẫn file csv
            if (file.getPath() == "") {
                // Nếu lỗi cập nhật status và ghi lỗi vào log
                ConfigService.getInstance().setStatusConfig("ERROR", filedata.getId());
                LogService.getInstance().addLog(new log(1, "CRAWL DATA FROM SOURCE TO FILE", "ERROR", "PATH FILE NOT FOUND", LocalDateTime.now()));
            }
            FileWriter fileWriter = new FileWriter(file);
            CSVWriter csvWriter = new CSVWriter(fileWriter);
            // Iterate over each row
            for (Element row : rows) {
                // Select all cells in the row
                Elements cells = row.select("td");

                if (cells.text().isEmpty()) continue;

                // Create an array to hold data for each row
                String[] rowData = new String[cells.size() + 4]; // Increased size by 2 for the image and datetime columns

                // Populate the array with data from each cell
                for (int i = 0; i < cells.size(); i++) {
                    if (i < imageColumnIndex) {
                        rowData[i] = cells.get(i).text();
                    } else {
                        rowData[i + 1] = cells.get(i).text();
                    }
                }
                // Add image URL to the array
                String imageUrl = getImageUrlFromCell(cells.get(imageColumnIndex));
                rowData[imageColumnIndex] = imageUrl;

                LocalDateTime dt = LocalDateTime.now();
                csvWriter.writeNext(rowData);
            }

            // Close the CSVWriter and FileWriter
            csvWriter.close();
            fileWriter.close();
            // Cập status trong bảng data_file DONE
            ConfigService.getInstance().setStatusConfig("DONE", filedata.getId());
            // Viết vào log crawl dữ liệu thành công
            LogService.getInstance().addLog(new log(1, "CRAWL DATA FROM SOURCE TO FILE", "DONE", "CRAWL DATA SUCCESS", LocalDateTime.now()));

            System.out.println("Data has been written to output.csv");
        } catch (IOException e) {
            ConfigService.getInstance().setStatusConfig("ERROR", filedata.getId());
            // Viết vào log crawl dữ liệu thành công
            LogService.getInstance().addLog(new log(1, "CRAWL DATA FROM SOURCE TO FILE", "ERROR", "CRAWL DATA FAILED", LocalDateTime.now()));

        }
    }

    // Thêm phương thức để lấy đường dẫn hình ảnh từ cell
    private static String getImageUrlFromCell(Element cell) {
        // Điều chỉnh selector tùy thuộc vào cách dữ liệu hình ảnh được đặt trong HTML
        Element imageElement = cell.select("img").first();
        if (imageElement != null) {
            // Lấy đường dẫn từ thuộc tính src hoặc data-src
            return imageElement.attr("src");
        }
        return "";
    }

}
