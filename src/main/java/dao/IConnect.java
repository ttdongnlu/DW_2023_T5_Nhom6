package dao;

import java.sql.Connection;

public interface IConnect {
    public Connection connectToMySQL();
    public  void closeConnection() ;
}
