/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LoginFrom;


import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


/**
 *
 * @author did85
 */
public class Operation {
    //ตรวจสอบ Login
    public static boolean isLogin(String userID, String password , JFrame frame){
        try {
            Connection connect = MySQLConnection.getConnection();
            String mySqlQuery =
                    "SELECT id, uid, name FROM logindatabase WHERE uid = '"+
                    userID+
                    "' And password = '"+
                    password+
                    "'";
            PreparedStatement preparedStatement = connect.prepareStatement(mySqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                LoginSession.ID = resultSet.getInt("id");
                LoginSession.UserID = resultSet.getString("uid");
                LoginSession.Name = resultSet.getString("name");
//                System.out.println(LoginSession.Name );
                return true;
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
        }
        
        return false;
    }
    //เก็บข้อมูลการจองลง db
    public static void addBooking(String userID, String room, String date, String time){
        
        try {
            
            Connection connect = MySQLConnection.getConnection();
            String sql = "INSERT INTO booking VALUES ( ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setString(1, userID);
            ps.setString(2, LoginSession.Name);
            ps.setString(3, room);
            ps.setString(4, date.trim());
            ps.setString(5, time);
            ps.setInt(6, Integer.parseInt(time.substring(0,2)));
            ps.setString(7, "รอดำเนินการ");
            ps.executeUpdate();
//            System.out.println(userID+room+date.trim()+time);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //แสดงการจองของ user
    public static void showMyBooking(JTable tbMyBooking){
        String[] col = {"ลำดับ","ห้อง","วันที่", "เวลา", "สถานะ"};
        DefaultTableModel model = new DefaultTableModel(col,0);
        tbMyBooking.setModel(model);
        try {
            Connection connect = MySQLConnection.getConnection();
            String sqlb = "SELECT  room, date, time, status FROM booking WHERE uid ='"+
                    LoginSession.UserID+
                    "'";
            PreparedStatement ps = connect.prepareStatement(sqlb);
            ResultSet result = ps.executeQuery();
            int i=1;
            while(result.next()){
                
                String[] data ={String.valueOf(i), result.getString("room"), result.getString("date"), result.getString("time"), result.getString("status")};
                model.addRow(data);
                i++;
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    //ตรวจสอบว่ามีคนจองไปยัง
    public static boolean checkBooking(String room, String date, String time){
        Connection connect;
        try {
            connect = MySQLConnection.getConnection();
            String sqlb = "SELECT room, date, time FROM booking WHERE room = '"+
                    room+
                    "' And date = '"+
                    date+
                    "' And time = '"+
                    time+
                    "'";
            PreparedStatement ps = connect.prepareStatement(sqlb);
            ResultSet result = ps.executeQuery();
            if(result.next()){
                return true;
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;    
        
    }
    
    //ดูรายละเอียดที่กดในตารางการจอง 
    public static void viewMyBooking(JTable jTable1) {
        int index = jTable1.getSelectedRow();
        
        TableModel model = jTable1.getModel();
        String room = model.getValueAt(index, 1).toString();
        String date = model.getValueAt(index, 2).toString();
        String time = model.getValueAt(index, 3).toString();
        
        ViewMyBooking vb = new ViewMyBooking();
        vb.setVisible(true);
        vb.viewDate.setText(date);
        vb.viewRoom.setText(room);
        vb.viewTime.setText(time);
    }
    
    
    //ยกเลิกการจอง
    public static void cancleBooking(String room, String date, String time) {
        boolean chBooking = Operation.checkBooking(room, date, time);
        if(chBooking){
            Connection connect;
            try {
                connect = MySQLConnection.getConnection();
                String sql = "DELETE FROM booking WHERE room = '"+
                    room+
                    "' And date = '"+
                    date+
                    "' And time = '"+
                    time+
                    "'";
                PreparedStatement ps = connect.prepareStatement(sql);
                ps.executeUpdate();
            } catch (Exception ex) {
                }
            
            
        }
    }
    
    //แสดงตารางตรวจสอบการจอง admin
    public static void showAdminCheck(JTable jTable1) {
        String[] col = {"ลำดับ","ชื่อผู้จอง","ห้อง","วันที่", "เวลา" };
        DefaultTableModel model = new DefaultTableModel(col,0);
        jTable1.setModel(model);
        try {
            Connection connect = MySQLConnection.getConnection();
            String sqlb = "SELECT name, room, date, time FROM booking WHERE status = '"+
                    "รอดำเนินการ'";
            PreparedStatement ps = connect.prepareStatement(sqlb);
            ResultSet result = ps.executeQuery();
            int i=1;
            while(result.next()){
               
         
               String[] data ={String.valueOf(i), result.getString("name"), result.getString("room"), result.getString("date"), result.getString("time")};
               model.addRow(data);
               i++;
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    //แสดงรายละเอียดการจอง admin
    public static void viewAdminCheck(JTable jTable1) {
        int index = jTable1.getSelectedRow();
        
        TableModel model = jTable1.getModel();
        String room = model.getValueAt(index, 1).toString();
        String date = model.getValueAt(index, 2).toString();
        String time = model.getValueAt(index, 3).toString();
        
        ViewAdminCheckBooking vb = new ViewAdminCheckBooking();
        vb.setVisible(true);
        vb.viewDate.setText(date);
        vb.viewRoom.setText(room);
        vb.viewTime.setText(time);
    }
    
    //admin ยืนยันการยืมห้อง 
    public static void confirmBooking(String room, String date, String time) {
        try {
                Connection connect = MySQLConnection.getConnection();
                String sql = "UPDATE booking SET status = '"+
                    "ยังไม่คืนห้อง'"+
                    " WHERE room = '"+
                    room+
                    "' And date = '"+
                    date+
                    "' And time = '"+
                    time+
                    "'";
                PreparedStatement ps = connect.prepareStatement(sql);
                ps.executeUpdate();
            } catch (Exception ex) {
                }
    }
    
    //แสดงตารางตรวจสอบสถานะ admin
    static void showAdminStatus(JTable jTable2) {
        String[] col = {"ลำดับ","ชื่อผู้จอง","ห้อง","วันที่", "เวลา" , "สถานะ"};
        DefaultTableModel model = new DefaultTableModel(col,0);
        jTable2.setModel(model);
        try {
            Connection connect = MySQLConnection.getConnection();
            String sqlb = "SELECT name, room, date, time, status FROM booking WHERE status = '"+
                    "ยังไม่คืนห้อง'";
            
            PreparedStatement ps = connect.prepareStatement(sqlb);
            ResultSet result = ps.executeQuery();
            int i=1;
            while(result.next()){
               String[] data ={String.valueOf(i), result.getString("name"), result.getString("room"), result.getString("date"), result.getString("time") , result.getString("status")};
               model.addRow(data);
               i++;
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    //แสดงรายละเอียดสถานะ admin
    static void viewAdminBookingStatus(JTable jTable2) {
        int index = jTable2.getSelectedRow();
        
        TableModel model = jTable2.getModel();
        String room = model.getValueAt(index, 1).toString();
        String date = model.getValueAt(index, 2).toString();
        String time = model.getValueAt(index, 3).toString();
        
        ViewAdminBookingStatus vb = new ViewAdminBookingStatus();
        vb.setVisible(true);
        vb.viewDate.setText(date);
        vb.viewRoom.setText(room);
        vb.viewTime.setText(time);
    }
    
    //admin ยืนยันการคืนห้อง
    static void ReturnBooking(String room, String date, String time) {
        try {
                Connection connect = MySQLConnection.getConnection();
                String sql = "UPDATE booking SET status = '"+
                    "คืนแล้ว'"+
                    " WHERE room = '"+
                    room+
                    "' And date = '"+
                    date+
                    "' And time = '"+
                    time+
                    "'";
                PreparedStatement ps = connect.prepareStatement(sql);
                ps.executeUpdate();
            } catch (Exception ex) {
                }
    }
    
    //แสดงตารางตรวจสอบการคืน admin
    static void showAdminCheckReturn(JTable jTable4) {
        String[] col = {"ลำดับ","ชื่อผู้จอง","ห้อง","วันที่", "เวลา" , "สถานะ"};
        DefaultTableModel model = new DefaultTableModel(col,0);
        jTable4.setModel(model);
        try {
            Connection connect = MySQLConnection.getConnection();
            String sqlb = "SELECT room, date, time, status FROM booking WHERE status = '"+
                    "คืนแล้ว'";
            
            PreparedStatement ps = connect.prepareStatement(sqlb);
            ResultSet result = ps.executeQuery();
            int i=1;
            while(result.next()){
               String[] data ={String.valueOf(i), result.getString("name"), result.getString("room"), result.getString("date"), result.getString("time") , result.getString("status")};
               model.addRow(data);
               i++;
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    //แสดงรายละเอียดการคืน admin
    static void viewAdminBookingReturn(JTable jTable4) {
        int index = jTable4.getSelectedRow();
        TableModel model = jTable4.getModel();
        String room = model.getValueAt(index, 1).toString();
        String date = model.getValueAt(index, 2).toString();
        String time = model.getValueAt(index, 3).toString();
        
        ViewAdminBookingReturn vb = new ViewAdminBookingReturn();
        vb.setVisible(true);
        vb.viewDate.setText(date);
        vb.viewRoom.setText(room);
        vb.viewTime.setText(time);
        vb.viewStatus.setText("คืนแล้ว");
        
    }



    



}


    
    

