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
        // Lấy ra 1 dòng liệu trong bảng control_data_file của ngày hôm nay
        control_data_file filedata;
        filedata = ConfigService.getInstance().getFileToday(fileConfig.getId());
        // 1. Kiểm tra nếu file_data không có dữ liệu thì thêm dữ liệu mới vào
        if (filedata == null) {
            //1.1 insert data in control_data_file
            filedata = new control_data_file(0, fileConfig, LocalDate.now(), "tiến hành trích xuất dữ liệu từ nguồn", LocalDateTime.now(), "HOA", "PREPARE");
            ConfigService.getInstance().insertDataFile(filedata);
        }
        // 2 Kiểm tra dòng dữ liệu có đang chạy hay không nếu không thì tiến hành chạy dòng dữ liệu
        if (!filedata.getStt().equals("CRAWLING")) {
            // 2.1 update stt
            ConfigService.getInstance().setStatusConfig("CRAWLING",filedata.getId());
            // Tiến hành crawl dữ liệu
//            ConfigService.getInstance().setTimestamp(filedata.getId());
        }
        try {
            //3.Extract data and save
            System.out.println("ACB");
            Document document = Jsoup.connect(fileConfig.getSource_path()).get();
            // Kiểm tra url website
            if (document.toString() == "") {
                // Nếu lỗi đường dẫn cập nhật status ERROR và ghi lỗi vào log
                ConfigService.getInstance().setStatusConfig("ERROR", filedata.getId());
                LogService.getInstance().addLog(new log(1, "CRAWL DATA FROM SOURCE TO FILE", "ERROR", "URL NOT FOUND", LocalDateTime.now()));
                return;
            }
            // Extract and print the title of the HTML document
            String title = document.title();
            System.out.println("Title: " + "ACBank");
            Element table = document.select("table.exTbl").first();
            Elements rows = table.select("tr.ex-even, tr.ex-odd");
            String location = fileConfig.getLocation();
            String namefile = fileConfig.getName_file();
            String format = fileConfig.getFormat();
            String seperal = fileConfig.getSeparator_file();
            String create_at = filedata.getCreate_at().toLocalDate().toString();
            System.out.println(create_at);
            // tên file
            String sourceFile = location + namefile + seperal + create_at + format;
            System.out.println(sourceFile);
            File file = new File(sourceFile);
            // 4. Kiểm tra đường dẫn file csv
            if (file.getPath() == "") {
                // 5 Nếu lỗi cập nhật status và ghi lỗi vào log
                ConfigService.getInstance().setStatusConfig("ERROR", filedata.getId());
                LogService.getInstance().addLog(new log(1, "CRAWL DATA FROM SOURCE TO FILE", "ERROR", "PATH FILE NOT FOUND", LocalDateTime.now()));
            }
            FileWriter fileWriter = new FileWriter(file);
            CSVWriter csvWriter = new CSVWriter(fileWriter);
            for (Element row : rows) {
                Elements cells = row.select("td");
                if (cells.text().isEmpty()) continue;
                String[] rowData = new String[cells.size() + 4];
                for (int i = 0; i < cells.size(); i++) {
                    rowData[i] = cells.get(i).text();
                }
                csvWriter.writeNext(rowData);
            }
            // Close the CSVWriter and FileWriter
            csvWriter.close();
            fileWriter.close();
            //6.update stt và insert stt vào log khi lấy dữ liệu thành công
            ConfigService.getInstance().setStatusConfig("DONE", filedata.getId());
            LogService.getInstance().addLog(new log(1, "CRAWL DATA FROM SOURCE TO FILE", "DONE", "CRAWL DATA SUCCESS", LocalDateTime.now()));
            System.out.println("Data has been written to output.csv");
        } catch (IOException e) {
            ConfigService.getInstance().setStatusConfig("ERROR", filedata.getId());
            // Viết vào log crawl dữ liệu thành công
            LogService.getInstance().addLog(new log(1, "CRAWL DATA FROM SOURCE TO FILE", "ERROR", "CRAWL DATA FAILED", LocalDateTime.now()));

        }
    }


}
