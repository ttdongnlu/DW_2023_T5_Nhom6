package service;

import beans.log;
import db.JDBIConnector;

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

    public int getMaxID() {
        return 1+ JDBIConnector.getControlJdbi().withHandle(handle -> {
            return handle.createQuery("select max(id) from  log").mapTo(Integer.class).one();
        });
    }

    public void addLog(log log) {
        JDBIConnector.getControlJdbi().withHandle(handle -> {
            return handle.createUpdate("INSERT INTO log values (:id,:location,:stt,:description,:time)")
                    .bind("id", getMaxID())
                    .bind("location", log.getLocation())
                    .bind("stt", log.getStt())
                    .bind("description", log.getDescription())
                    .bind("time", log.getCreate_at()).execute();
        });

    }
}
