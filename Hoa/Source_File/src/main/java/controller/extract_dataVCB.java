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
import java.time.format.DateTimeFormatter;

public class extract_dataVCB {
    public void crawlerData() {
        // lấy ra 1 dòng dữ liệu trong bảng config
        control_data_file_config fileConfig = ConfigService.getInstance().getConfigById(1);
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
            Document document2 = Jsoup.parse(String.valueOf(document));
            Elements tableBXH = document2.select("ul.dropdown-options-wrapper li");
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


            for (Element li : tableBXH) {
                String code = li.attr("data-code");
                String cashRate = li.attr("data-cash-rate");
                String transferRate = li.attr("data-transfer-rate");
                String sellRate = li.attr("data-sell-rate");
                String[] rowData = new String[tableBXH.size() + 4];
                rowData = new String[]{code, cashRate, transferRate, sellRate};

                LocalDateTime dt = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");

                // Write the data to the CSV file
                csvWriter.writeNext(rowData);

            }

            // Close the CSVWriter and FileWriter
            csvWriter.close();
            fileWriter.close();
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

//    public static void main(String[] args) {
//        crawlerData("https://www.vietcombank.com.vn/KHCN/Cong-cu-tien-ich/Ty-gia");
////        crawlerData("https://acb.com.vn/ty-gia-hoi-doai");
//
//    }

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

    private static String getLast5Matches(Elements cells) {
        // Implement your logic to extract information about the last 5 matches from the 'cells'
        // For now, I'll return an empty string
        return "";
    }
}
