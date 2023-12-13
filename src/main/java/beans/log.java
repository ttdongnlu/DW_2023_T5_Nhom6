package beans;

import java.time.LocalDateTime;

public class log {
    int id;
    String location;
    String stt;
    String description;
    LocalDateTime create_at;
    public log(){

    }

    public log(int id, String location, String stt, String description, LocalDateTime create_at) {
        this.id = id;
        this.location = location;
        this.stt = stt;
        this.description = description;
        this.create_at = create_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreate_at() {
        return create_at;
    }

    public void setCreate_at(LocalDateTime create_at) {
        this.create_at = create_at;
    }
}
