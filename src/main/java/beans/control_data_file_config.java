package beans;

import java.time.LocalDateTime;

public class control_data_file_config {
    int id;
    String name_file;
    String code_file;
    String description;
    String source_path;
    String location;
    String format;
    String separator_file;
    String columns_file;
    LocalDateTime create_at;
    String create_by;

    public control_data_file_config() {
    }

    public control_data_file_config(int id, String name_file, String code_file, String description, String source_path, String location, String format, String separator_file, String columns_file, LocalDateTime create_at, String create_by) {
        this.id = id;
        this.name_file = name_file;
        this.code_file = code_file;
        this.description = description;
        this.source_path = source_path;
        this.location = location;
        this.format = format;
        this.separator_file = separator_file;
        this.columns_file = columns_file;
        this.create_at = create_at;
        this.create_by = create_by;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_file() {
        return name_file;
    }

    public void setName_file(String name_file) {
        this.name_file = name_file;
    }

    public String getCode_file() {
        return code_file;
    }

    public void setCode_file(String code_file) {
        this.code_file = code_file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource_path() {
        return source_path;
    }

    public void setSource_path(String source_path) {
        this.source_path = source_path;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSeparator_file() {
        return separator_file;
    }

    public void setSeparator_file(String separator_file) {
        this.separator_file = separator_file;
    }

    public String getColumns_file() {
        return columns_file;
    }

    public void setColumns_file(String columns_file) {
        this.columns_file = columns_file;
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

    @Override
    public String toString() {
        return "control_data_file_config{" +
                "id=" + id +
                ", name_file='" + name_file + '\'' +
                ", code_file='" + code_file + '\'' +
                ", description='" + description + '\'' +
                ", source_path='" + source_path + '\'' +
                ", location='" + location + '\'' +
                ", format='" + format + '\'' +
                ", separator_file='" + separator_file + '\'' +
                ", columns_file='" + columns_file + '\'' +
                ", create_at=" + create_at +
                ", create_by='" + create_by + '\'' +
                '}';
    }
}