/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banking;
import java.sql.*;
/**
 *
 * @author xiao lin
 */
public class Customer extends Person{
    private static Connection con;
    Account myaccount;
    
    public Customer(String Username){
        this.Username = Username;
        getInfo();//get values from database
        myaccount = new Account(Username);
    }
    
    public void getInfo(){
        try{
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/banking", "root", "root");
            String sql = "SELECT * from PERSON WHERE Username ='"+Username+"'";
            ResultSet row = con.createStatement().executeQuery(sql);
            if(row.next()){//get personal info
                this.LastName = row.getString("LastName");
                this.FirstName = row.getString("FirstName");
                this.Email = row.getString("Email");
                this.PhoneNumber = row.getString("PhoneNumber");
                this.Address = row.getString("Address");
                this.Password = row.getString("Password");  
            }
        }catch(SQLException e){
            System.err.println(e);
        }
    }
    
    //@override
    public void ChangePersonalInfo(String email, String ad){
        this.Email = email;
        this.Address = ad;
         try{
            PreparedStatement update = con.prepareStatement("UPDATE PERSON SET Email='"+Email+"',Address='"+Address+"' WHERE UserName='"+Username+"'");//update database
            update.executeUpdate();
         }catch(SQLException ef){
             System.err.println(ef);
         }
    }
    
    //@override
    public void ChangePassword(String newpassword){
        this.Password = newpassword; //update class info
         try{
            PreparedStatement update = con.prepareStatement("UPDATE PsersonTable SET Password=" +Password+ " WHERE UserName="+Username);//update database
            update.executeUpdate();
         }catch(SQLException ef){
             System.err.println(ef);
         }
    }
}

