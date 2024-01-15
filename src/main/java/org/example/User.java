package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    private Connection co ;
    private Scanner sc;

    public User(Connection co ,Scanner sc)
    {
        this.co= co;
        this.sc= sc;
    }
     public  void register() throws SQLException {
        sc.nextLine();
         System.out.println("Enter full Name");
         String full_name = sc.next();
         System.out.println("Enter your Email");
         String email = sc.next();
         System.out.println("Enter your password");
         String password = sc.next();
         if(user_exists(email)){
             System.out.println("User Already Exists For This Email Addresss");
             return;
         }
         String register_query = "insert into user(full_name,email,password) values(?,?,?)";
         PreparedStatement ps = co.prepareStatement(register_query);
         ps.setString(1,full_name);
         ps.setString(2,email);
         ps.setString(3,password);
         int affectedrows = ps.executeUpdate();
         if(affectedrows >0){
             System.out.println("Registration Succesfull");
         }else {
             System.out.println("Registration Failed");
         }
     }
     public String login() throws SQLException
     {
        sc.nextLine();
         System.out.println("Email : ");
         String email = sc.nextLine();
         System.out.println("Enter Passowrd");
         String password = sc.nextLine();

         String login_query = "select * from user where email =? and password =?";
         PreparedStatement ps = co.prepareStatement(login_query);
         ps.setString(1,email);
         ps.setString(2,password);
         ResultSet rs = ps.executeQuery();
         if(rs.next()){
             return email;
         }else {
             return null;
         }
     }

     public boolean user_exists(String email) throws SQLException {
        String query = "select * from user where email = ?";
        PreparedStatement ps = co.prepareStatement(query);
        ps.setString(1,email);
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            return true;
        }else {
            return false;
        }
     }
}
