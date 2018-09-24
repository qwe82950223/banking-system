/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author xiao lin
 */
public class Staff extends Person{
    
    int staffId;
    private static Connection con;
    
    public Staff(String Username){
        this.Username = Username;
        getInfo();
    }
    //override
    public void getInfo(){
        try{
            con = DriverManager.getConnection(" ", "root", "root");
            String sql = "SELECT * from Persontable WHERE Username =" + Username;
            ResultSet row = con.createStatement().executeQuery(sql);
            this.LastName = row.getString("LastName");
            this.FirstName = row.getString("FirstName");
            this.Email = row.getString("Email");
            this.PhoneNumber = row.getString("PhoneNumber");
            this.Address = row.getString("Address");
            this.Password = row.getString("Password");
            this.staffId = getID();
        }catch(SQLException e){
            System.err.println(e);
        }
    }
    
    //override
    public void ChangePersonalInfo(String pn, String ad){
        this.PhoneNumber = pn;
        this.Address = ad;
         try{
            PreparedStatement update = con.prepareStatement("UPDATE PERSON SET PHONENUMBER='"+PhoneNumber+"',ADDRESS='"+Address+"' WHERE USERNAME='"+Username+"'");//update database
            update.executeUpdate();
         }catch(SQLException ef){
             System.err.println(ef);
         }
    }
    
    //override
    public void ChangePassword(String newpassword){
        this.Password = newpassword; //update class info
         try{
            PreparedStatement update = con.prepareStatement("UPDATE PERSON SET PASSWORD='" +Password+ "' WHERE USERNAME='"+Username+"'");//update database
            update.executeUpdate();
         }catch(SQLException ef){
             System.err.println(ef);
         }
    }

    //get all customers infomation from database
    public ResultSet GetCustomerInfo(){
        ResultSet row = null;  
        try{
            row = con.createStatement().executeQuery("SELECT * from PERSON WHERE PERSONTYPE='customer'"); // get all customers 
        }catch(SQLException e){
            System.err.println(e);
        }
        return row;
    }

    private int getID() throws SQLException {
        con = DriverManager.getConnection("jdbc:derby://localhost:1527/banking", "root", "root");
        String sql = "SELECT * from STAFF WHERE Username ='"+Username+"'";
        ResultSet rs = con.createStatement().executeQuery(sql);
        rs.next();
        return rs.getInt("STAFFID");
        
    }
    
    
}
