package controller;

import beans.control_data_file;
import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import service.ConfigService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class extract_dataACB {
    public static void crawlerData() {
        control_data_file fileConfig= ConfigService.getInstance().getConfig(2);
        ConfigService.getInstance().setTimestamp(fileConfig.getId());
        try {
            // Connect to a website and get the HTML
            System.out.println("ACB");
            Document document = Jsoup.connect(fileConfig.getControl_data_file().getSource_path()).get();

            // Extract and print the title of the HTML document
            String title = document.title();
            System.out.println("Title: " + title);

            // Đặt tên cho cột chứa đường dẫn hình ảnh (ví dụ: cột thứ 2)
            int imageColumnIndex = 1;

            Element tableBXH = document.select("table.exTbl").first();
            Elements rows = tableBXH.select("tr.ex-even, tr.ex-odd");
            // Create a CSVWriter
            String location =fileConfig.getControl_data_file().getLocation();
            String namefile = fileConfig.getControl_data_file().getName_file();
            String format = fileConfig.getControl_data_file().getFormat();
            String seperal= fileConfig.getControl_data_file().getSeparator_file();
            String timestamp = fileConfig.getFile_timestamp().toString();
            System.out.println(timestamp);
            String sourceFile = location+namefile+seperal+timestamp+format;

            File file = new File(sourceFile);
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
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
//                rowData[rowData.length - 2] = dt.format(formatter);
//                rowData[rowData.length - 1] = getLast5Matches(cells);
//                rowData[rowData.length - 3] = title;
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
        crawlerData();
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
