package LoginFrom;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author did85
 */
//เชื่อมจ่อฐานข้อมูล
public class MySQLConnection {
    public static Connection getConnection() throws Exception{
        String driver = "com.mysql.cj.jdbc.Driver";
        String utf_8 = "?useUnicode=true&characterEncoding=UTF-8";
        String url = "jdbc:mysql://localhost:3306/testdb" + utf_8;
        String username = "root";
        String password = "";
        
        Class.forName(driver);
        Connection connect = DriverManager.getConnection(url, username, password);
        return connect;
    }
}
