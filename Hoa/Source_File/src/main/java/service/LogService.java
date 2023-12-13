package service;

import beans.log;

public class LogService {
    private static LogService instance;
    public static LogService getInstance() {
        if (instance == null) {
            instance = new LogService();
        }
        return instance;
    }
    public LogService() {
    }
    public  void addLog(log log){
    }
}
