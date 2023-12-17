package beans;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class control_data_file {
    int id;
    control_data_file_config control_data_file;
//    LocalDateTime file_timestamp;
    LocalDate data_range;
    String note;
    LocalDateTime create_at;
    String create_by;
    String stt;
    public control_data_file(){

    }

    public control_data_file(int id, control_data_file_config control_data_file, LocalDate data_range, String note, LocalDateTime create_at, String create_by, String stt) {
        this.id = id;
        this.control_data_file = control_data_file;
        this.data_range = data_range;
        this.note = note;
        this.create_at = create_at;
        this.create_by = create_by;
        this.stt = stt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public control_data_file_config getControl_data_file() {
        return control_data_file;
    }

    public void setControl_data_file(control_data_file_config control_data_file) {
        this.control_data_file = control_data_file;
    }

    public LocalDate getData_range() {
        return data_range;
    }

    public void setData_range(LocalDate data_range) {
        this.data_range = data_range;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreate_at() {
        return create_at;
    }

    public void setCreate_at(LocalDateTime create_at) {
        this.create_at = create_at;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    @Override
    public String toString() {
        return "control_data_file{" +
                "id=" + id +
                ", control_data_file=" + control_data_file +
                ", data_range=" + data_range +
                ", note='" + note + '\'' +
                ", datecreate_at=" + create_at +
                ", create_by='" + create_by + '\'' +
                ", stt='" + stt + '\'' +
                '}';
    }
}
