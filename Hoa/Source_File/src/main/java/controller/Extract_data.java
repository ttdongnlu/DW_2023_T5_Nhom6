package controller;

public class Extract_data {
    public static void main(String[] args) {
        extract_dataACB acb= new extract_dataACB();
        extract_dataVCB vcb = new extract_dataVCB();
        acb.crawlerData();
        vcb.crawlerData();
    }
}
