package service;

import beans.control_data_file;
import beans.control_data_file_config;
import db.JDBIConnector;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
                            "SELECT cf.id, cf.note, cf.create_at,cf.create_by, cf.data_range, cf.df_config_id, cf.stt from control_data_file cf")
                    .mapToBean(control_data_file.class).stream().collect(Collectors.toList());
            for (control_data_file c : list
            ) {
                c.setControl_data_file(getConfigByConfigFileId(c.getId()));
            }
            return list;
        });
    }

    public control_data_file getFileToday(int id) {
        return JDBIConnector.getControlJdbi().withHandle(handle -> {
         List<control_data_file> listFile= handle.createQuery("SELECT cf.id, cf.data_range,cf.note, cf.create_at,cf.create_by, cf.stt from control_data_file cf join control_data_file_config c on c.id =cf.df_config_id  where c.id=" + id + " and cf.data_range= '" + LocalDate.now() + "'").mapToBean(control_data_file.class).stream().toList();
          if(listFile.size() != 1){
              return null;
          }
            listFile.get(0).setControl_data_file(getConfigByConfigFileId(listFile.get(0).getId()));
          return listFile.get(0);
        });
    }

    public control_data_file_config getConfigById(int id) {
        return JDBIConnector.getControlJdbi().withHandle(handle -> {
            return handle.createQuery("SELECT c.code_file, c.columns_file, c.create_at, c.create_by,c.description, c.format, c.id, c.location, c.name_file, c.separator_file, c.source_path from control_data_file_config c where c.id=" + id).mapToBean(control_data_file_config.class).one();
        });
    }

    public control_data_file_config getConfigByConfigFileId(int id) {
        return JDBIConnector.getControlJdbi().withHandle(handle -> {
            return handle.createQuery("SELECT c.code_file, c.columns_file, c.create_at, c.create_by,c.description, c.format, c.id, c.location, c.name_file, c.separator_file, c.source_path from control_data_file_config c join control_data_file cf on c.id= cf.df_config_id  where cf.id=" + id).mapToBean(control_data_file_config.class).one();
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

    public void setTimestamp(int id) {
        JDBIConnector.getControlJdbi().withHandle(handle -> {
            return handle.createUpdate("update control_data_file set file_timestamp=? where id= ?")
                    .bind(0, LocalDateTime.now())
                    .bind(1, id)
                    .execute();
        });
    }

    public List<control_data_file> getMaxID() {
        return  JDBIConnector.getControlJdbi().withHandle(handle -> {
            return handle.createQuery("select * from control_data_file ").mapToBean(control_data_file.class).stream().toList();
        });
    }

    public void insertDataFile(control_data_file df) {
         JDBIConnector.getControlJdbi().withHandle(handle -> {
            return handle.createUpdate("INSERT INTO control_data_file VALUES (:id,:df_config_id,:date_range,:note,:create_at,:create_by,:stt)")
                    .bind("id",getMaxID().size()+1)
                    .bind("df_config_id",df.getControl_data_file().getId())
                    .bind("date_range",df.getData_range())
                    .bind("note",df.getNote())
                    .bind("create_at",df.getCreate_at())
                    .bind("create_by",df.getCreate_by())
                    .bind("stt",df.getStt())
                    .execute();
        });
    }


    public static void main(String[] args) {
//        getInstance().setTimestamp(1);
        System.out.println(getInstance().getFileToday(2));
//        System.out.println(getInstance().getFileToday(3));
    }
}
