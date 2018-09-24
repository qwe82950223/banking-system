/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banking;

/**
 *
 * @author xiao lin
 */
public abstract class Person {
    
    protected String Username;
    protected String Password;
    protected String LastName;
    protected String FirstName;
    protected String PhoneNumber;
    protected String Email;
    protected String Address;
    
    public abstract void getInfo();
    public abstract void ChangePersonalInfo(String pn, String ad);
    public abstract void ChangePassword(String newpassword);
    
    public String getUsername(){return this.Username;}
    public String getPassword(){return this.Password;}
    public String getLastName(){return this.LastName;}
    public String getFirstName(){return this.FirstName;}
    public String getPhoneNumber(){return this.PhoneNumber;}
    public String getEmail(){return this.Email;}
    public String getAddress(){return this.Address;}
}
