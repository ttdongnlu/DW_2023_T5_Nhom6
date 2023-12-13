package service;

import beans.control_data_file;
import beans.control_data_file_config;
import db.JDBIConnector;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigService {
    private static ConfigService instance;

    public static ConfigService getInstance() {
        if (instance == null) {
            instance = new ConfigService();
        }
        return instance;
    }

    public ConfigService() {
    }

    public List<control_data_file> getListConfig() {
        return JDBIConnector.getControlJdbi().withHandle(handle -> {

            List<control_data_file> list = handle.createQuery(
                            "SELECT * from control_data_file")
                    .mapToBean(control_data_file.class).stream().collect(Collectors.toList());
            for (control_data_file c : list
            ) {
                c.setControl_data_file(getConfigByConfigFileId(c.getId()));
            }
            return list;
        });
    }
    public control_data_file getConfig(int id) {
        return JDBIConnector.getControlJdbi().withHandle(handle -> {

            control_data_file config = handle.createQuery(
                            "SELECT * from control_data_file c where c.id="+ id)
                    .mapToBean(control_data_file.class).one();
                config.setControl_data_file(getConfigByConfigFileId(config.getId()));
            return config;
        });
    }

    public control_data_file_config getConfigById(int id) {
        return JDBIConnector.getControlJdbi().withHandle(handle -> {
            return handle.createQuery("SELECT * from control_data_file_config where id=" + id).mapToBean(control_data_file_config.class).one();
        });
    }

    public control_data_file_config getConfigByConfigFileId(int id) {
        return JDBIConnector.getControlJdbi().withHandle(handle -> {
            return handle.createQuery("SELECT * from control_data_file_config c join control_data_file cf on c.id =cf.df_config_id where cf.id=" + id).mapToBean(control_data_file_config.class).one();
        });
    }

    public void setStatusConfig(String stt, int id) {
        JDBIConnector.getControlJdbi().withHandle(handle -> {
            return handle.createUpdate("update control_data_file set stt =? where id = ?")
                    .bind(0, stt)
                    .bind(1, id)
                    .execute();
        });
    }
public void setTimestamp(int id){
        JDBIConnector.getControlJdbi().withHandle(handle -> {
            return handle.createUpdate("update control_data_file set file_timestamp=? where id= ?")
                    .bind(0, LocalDate.now())
                    .bind(1,id)
                    .execute();
        });
}
    public control_data_file_config getConfigByLogId(int id) {
        return JDBIConnector.getControlJdbi().withHandle(handle -> {
            return handle.createQuery(
                            "SELECT cf.id, name_config, value_config, description, url_website,file_format, file_path, run_time, create_date, update_date, cf.status from config cf join log l on cf.id= l.config_id where l.id ="
                                    + id)
                    .mapToBean(control_data_file_config.class).one();
        });
    }

    public LocalDate updateDateConfig(LocalDate update_date, int id) {
        int i = JDBIConnector.getControlJdbi().withHandle(handle -> {
            return handle.createUpdate("UPDATE config set update_date = ? where id =?")
                    .bind(0, update_date)
                    .bind(1, id)
                    .execute();
        });
        if (i == 1)
            return update_date;
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getInstance().updateDateConfig(LocalDate.now(), 0));
    }
}
