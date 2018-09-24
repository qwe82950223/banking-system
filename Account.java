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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JTable;

/**
 *
 * @author xiao lin
 */
public class Account {
    
    private static Connection con;
    int AccountNumber;
    double CurrentAmount;
    
    public Account(String un){
        getAccountInfo(un);
    }
    
    /**try{
            con = DriverManager.getConnection("banking", "root", "qwe137035006");
            String sql = "SELECT * from Accounttable WHERE AcountNumber =" + AccountNumber;
            ResultSet row = con.createStatement().executeQuery(sql);
            CurrentAmount = Float.parseFloat(row.getString("Amount"));
        }catch(SQLException e){
            System.err.println(e);
        }
     *
     */
    private void getAccountInfo(String un){
        try{
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/banking", "root", "root");
            String sql = "SELECT * from ACCOUNT WHERE Username = '" +un+"'";
            ResultSet row = con.createStatement().executeQuery(sql);
            if(row.next()){//get account info
                AccountNumber = Integer.parseInt(row.getString("AccountNumber"));
                CurrentAmount = row.getDouble("CurrentAmount");
            }
            
        }catch(SQLException e){
            System.err.println(e);
        }
    }
    
    public ResultSet getHistory(){
        ResultSet row = null;
        try{
            String sql = "SELECT * from Historytable WHERE AcountNumber =" + AccountNumber;
            row = con.createStatement().executeQuery(sql);
        
        }catch(SQLException e){
            System.err.println(e);
        }
        return row;
    }
    
    public String Transfer(double n, String email){
        if(CurrentAmount < n ){//if amount is more than the current Amount, cancel transaction
            return "not enought money";
        }
        try{
            String sql = "SELECT * FROM PERSON WHERE EMAIL='"+email+"'";//find the customer with the same email
            ResultSet rs = con.prepareStatement(sql).executeQuery();
            if(rs.next()){
                String username = rs.getString("USERNAME");//get the username of other person
            
                String getAmountsql = "SELECT * FROM ACCOUNT WHERE USERNAME='"+username+"'";//find the person who you want transfer to in database
                rs = con.prepareStatement(getAmountsql).executeQuery();
                rs.next();
                double OtherCurrentAmount = rs.getDouble("CURRENTAMOUNT");//get the current amount of other person
                String OtherAccountNumber = rs.getString("ACCOUNTNUMBER");//get account number of other person
                
                double previous = CurrentAmount;
                CurrentAmount-=n;//decrease my current amount
                double after = CurrentAmount;
        
                double previousOther = OtherCurrentAmount;
                OtherCurrentAmount+=n;//increase other person's current amount
                double afterOther = OtherCurrentAmount;
                
                PreparedStatement updateMy = con.prepareStatement("UPDATE ACCOUNT SET CurrentAmount=" +CurrentAmount+ " WHERE AccountNumber='"+AccountNumber+"'");//update my Acount current amount 
                updateMy.executeUpdate();
            
                PreparedStatement updateOther = con.prepareStatement("UPDATE ACCOUNT SET CurrentAmount=" +OtherCurrentAmount+ " WHERE USERNAME='"+username+"'");//update Other person current amount 
                updateOther.executeUpdate();
            
                Timestamp timeStamp = new Timestamp(new Date().getTime());
                PreparedStatement insert = con.prepareStatement("INSERT INTO HISTORY (ACCOUNTNUMBER,TYPE,AMOUNT,PREVIOUSAMOUNT,AFTERAMOUNT,TIME) VALUES ('"+String.valueOf(AccountNumber)+"','Transfer',"+n+","+previous+","+after+",'"+timeStamp+"')");//insert history to historytable
                insert.executeUpdate();
            
                PreparedStatement insertOther = con.prepareStatement("INSERT INTO HISTORY (ACCOUNTNUMBER,TYPE,AMOUNT,PREVIOUSAMOUNT,AFTERAMOUNT,TIME) VALUES ('"+OtherAccountNumber+"','Recieve',"+n+","+previousOther+","+afterOther+",'"+timeStamp+"')");//insert history to historytable
                insertOther.executeUpdate();
            }else{
                return "Email can't find!";
            }
            
        }catch(SQLException e){
            System.err.println(e);
        }
        return n +" has been transfer to " + email;
    }
    
    public void Deposit(Double n){
        double previous = CurrentAmount;
        CurrentAmount+=n;
        double after = CurrentAmount;
        try{
            PreparedStatement update = con.prepareStatement("UPDATE ACCOUNT SET CurrentAmount=" +CurrentAmount+ " WHERE AccountNumber='"+String.valueOf(AccountNumber)+"'");//update myAcount database
            update.executeUpdate();
            Timestamp timeStamp = new Timestamp(new Date().getTime());
            PreparedStatement insert = con.prepareStatement("INSERT INTO HISTORY (ACCOUNTNUMBER,TYPE,AMOUNT,PREVIOUSAMOUNT,AFTERAMOUNT,TIME) VALUES ('"+String.valueOf(AccountNumber)+"','Deposit',"+n+","+previous+","+after+",'"+timeStamp+"')");//insert history to historytable
            insert.executeUpdate();
        }catch(SQLException e){
            System.err.println(e);
        }
    }
    
    public String Cash(Double n){
        if(CurrentAmount < n ){
            return "not enough money";
        }
        double previous = CurrentAmount;
        CurrentAmount-=n;
        double after = CurrentAmount;
       try{
            PreparedStatement update = con.prepareStatement("UPDATE ACCOUNT SET CurrentAmount=" +CurrentAmount+ " WHERE AccountNumber='"+String.valueOf(AccountNumber)+"'");//update myAcount database
            update.executeUpdate();
            Timestamp timeStamp = new Timestamp(new Date().getTime());
            PreparedStatement insert = con.prepareStatement("INSERT INTO HISTORY (ACCOUNTNUMBER,TYPE,AMOUNT,PREVIOUSAMOUNT,AFTERAMOUNT,TIME) VALUES ('"+String.valueOf(AccountNumber)+"','Cash',"+n+","+previous+","+after+",'"+timeStamp+"')");//insert history to historytable
            insert.executeUpdate();
           
        }catch(SQLException e){
            System.err.println(e);
        }
        return n+" has been Cashed!";
    }
}
