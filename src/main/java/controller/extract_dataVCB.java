package controller;

import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class extract_dataVCB {
    public static void crawlerData(String url) {
        try {
            // Connect to a website and get the HTML
            Document document = Jsoup.connect(url).get();
          System.out.println("VCB");
            // Extract and print the title of the HTML document
            String title = document.title();
            System.out.println("Title: " + title);

            // Đặt tên cho cột chứa đường dẫn hình ảnh (ví dụ: cột thứ 2)
            int imageColumnIndex = 1;
            Document document2 = Jsoup.parse(String.valueOf(document));
            Elements tableBXH = document2.select("ul.dropdown-options-wrapper li");
            File file = new File("D:\\DW_currency-exchange-rate\\Currency_Exchange_Rate\\vcb_"+ LocalDate.now() +".csv");
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
            System.out.println("Data has been written to output.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        crawlerData("https://www.vietcombank.com.vn/KHCN/Cong-cu-tien-ich/Ty-gia");
//        crawlerData("https://acb.com.vn/ty-gia-hoi-doai");

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

    private static String getLast5Matches(Elements cells) {
        // Implement your logic to extract information about the last 5 matches from the 'cells'
        // For now, I'll return an empty string
        return "";
    }
}
