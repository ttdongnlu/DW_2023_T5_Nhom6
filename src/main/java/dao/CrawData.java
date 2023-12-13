//package service;
//
//import beans.control_data_file_config;
//import beans.log;
//import com.opencsv.CSVWriter;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CrawData {
//        public static void crawlerDataformConfig() {
//            List<control_data_file_config> list = ConfigService.getInstance().;
//            log log ;
//            // 1. Lấy từng dòng file trong bảng config
//            for (control_data_file_config c : list) {
//                System.out.println(c.getId());
//                log = LogService.getInstance().getLogByConfigId(c.getId());
//                if (log == null) { // Nếu chưa có log thì thêm log vào
//                    log = new log(1, LocalDateTime.now(), "crawling data", c, "CRAWLING");
//                    LogService.getInstance().addLog(log);
//                    continue;
//                }
//                // 2. Kiểm tra status trong bảng log
//                if (log.getStatus() == "CRAWLING") { // kiểm tra status xem dòng dữ liệu hiện có đang chạy hay không
//                    continue;
//                }
//                //3. Cập nhật status trong log thành CRAWLLING
//                LogService.getInstance().setStatus("CRAWLING", log.getId());
//                // Tiến hành crawl dữ liệu
//                LocalDate date= ConfigService.getInstance().updateDateConfig(LocalDate.now(), c.getId());
//                crawlerData(c.getUrl_website(), c.getFile_path(), c.getFile_format(), date, log.getId());
//            }
//        }
//
//        public static void crawlerData(String url, String sourceFile, String fileFormat, LocalDate update_date, int idLog) {
//            try {
//                // 4. Lấy đường dẫn URL của website
//                Document document = Jsoup.connect(url).get();
//                // 5. Kiểm tra đường dẫn có lỗi hay không
//                if (document == null) {
//                    // 5.1. Nếu đường dẫn có lỗi xãy ra cập nhật status vào bảng log
//                    LogService.getInstance().setStatus("ERROR", idLog);
//                    // 5.2. Ghi lỗi vào bảng log
//                    LogService.getInstance().setMessage("Lỗi khi truy cập link website: " + document, idLog);
//                    return;
//                }
//
//                //6. Tiến hành đọc dữ liệu các thẻ html của website
//                String title = document.select("h2.title-giaidau").text();
//                System.out.println("Title: " + title);
//
//                // Đặt tên cho cột chứa đường dẫn hình ảnh (ví dụ: cột thứ 2)
//                int imageColumnIndex = 1;
//
//                Element tableBXH = document.select("table.table-bxh").first();
//                Elements rows = tableBXH.select("tr");
//
//                List<String[]> listDataRow = new ArrayList<>();
//                //7. Duyệt lần lượt các dòng dữ liệu
//                for (Element row : rows) {
//                    // 8. Lấy một dòng dữ liệu
//                    Elements cells = row.select("td");
//                    if (cells.text().isEmpty()) continue;
//
//                    //10. Lưu 1 dòng dữ liệu vào dạng mãng String
//                    String[] rowData = new String[cells.size() + 4];
//                    // 11. Duyệt đến khi hết dòng dữ liệu
//                    for (int i = 0; i < cells.size(); i++) {
//                        if (i < imageColumnIndex) {
//                            rowData[i] = cells.get(i).text();
//                        } else {
//                            rowData[i + 1] = cells.get(i).text();
//                        }
//                    }
//
//                    // lưu Url hình ảnh vào mãng
//                    String imageUrl = getImageUrlFromCell(cells.get(imageColumnIndex));
//                    rowData[imageColumnIndex] = imageUrl;
//
//                    // Lưu thời gian vào mãng
//                    LocalDateTime dt = LocalDateTime.now();
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
//                    rowData[rowData.length - 2] = dt.format(formatter);
//
//                    // thông tin của các đội
//                    rowData[rowData.length - 1] = getLast5Matches(cells);
//                    // tên giải đấu
//                    rowData[rowData.length - 3] = title;
//
//                    // năm trận gần nhất
////                rowData[rowData.length - 1] = getLast5Matches(cells);
//
//                    // 11.lưu tất cả dòng dữ liệu vào list
////                csvWriter.writeNext(rowData);
//                    listDataRow.add(rowData);
//                }
//                //12. Đọc đường dẫn file csv từ bảng config
//                File file = new File(sourceFile + update_date + fileFormat);
//                //13. Kiểm tra lỗi đường dẫn hay không
//                if (file.getPath() == null ) {
//                    // 5.1 cập nhật status trong bảng log
//                    LogService.getInstance().setStatus("ERROR", idLog);
//                    // 5.2 Ghi lỗi vào bảng log
//                    LogService.getInstance().setMessage("Lỗi đường dẫn", idLog);
//                    return;
//                }
//                System.out.println(file.getPath());
//                FileWriter fileWriter = new FileWriter(file);
//                CSVWriter csvWriter = new CSVWriter(fileWriter);
//                //14. Lưu tất cả vào dữ liệu vào file.csv
//                for (String[] s : listDataRow) {
//                    csvWriter.writeNext(s);
//                }
//                //15. Đóng file
//                csvWriter.close();
//                fileWriter.close();
//                System.out.println("Data has been written to output.csv");
//                LogService.getInstance().setStatus("SUCCESS", idLog);
//            } catch (IOException e) {
//                LogService.getInstance().setStatus("ERROR", idLog);
//                e.printStackTrace();
//            }
//        }
//
//        public static void main(String[] args) {
//            crawlerDataformConfig();
//        }
//
//        // Thêm phương thức để lấy đường dẫn hình ảnh từ cell
//        private static String getImageUrlFromCell(Element cell) {
//            // Điều chỉnh selector tùy thuộc vào cách dữ liệu hình ảnh được đặt trong HTML
//            Element imageElement = cell.select("img").first();
//            if (imageElement != null) {
//                // Lấy đường dẫn từ thuộc tính src hoặc data-src
//                return imageElement.attr("src");
//            }
//            return "";
//        }
//
//        private static String getLast5Matches(Elements cells) {
//            return "";
//
//        }
//    }
//
//}
