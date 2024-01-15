package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {

    private Connection co;
    private Scanner sc;

    public AccountManager(Connection co, Scanner sc) {
        this.co = co;
        this.sc = sc;
    }

    public void credit_money(long account_number) throws SQLException {

        sc.nextLine();
        System.out.println("Enter Amount");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security Pin");
        String security_pin = sc.nextLine();
        co.setAutoCommit(false);
        if (account_number != 0) {
            PreparedStatement ps = co.prepareStatement("select * from accounts where security_pin = ?");
            ps.setString(1, security_pin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String debit_query = "update accounts set balance = balance + ? where account_number = ?";
                PreparedStatement p = co.prepareStatement(debit_query);
                p.setDouble(1, amount);
                p.setLong(2, account_number);
                int rowAffected = p.executeUpdate();
                if (rowAffected > 0) {
                    System.out.println("Rs" + amount + "  succesfully");
                    co.commit();
                    co.setAutoCommit(true);
                    return;

                } else {
                    System.out.println("Transction failed");
                    co.rollback();
                    co.setAutoCommit(true);
                }

            } else {
                System.out.println("Invalid Pin");
            }
            co.setAutoCommit(true);
        }
    }


    public void debit_money(long account_number) throws SQLException {

        sc.nextLine();
        System.out.println("Enter Amount");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security Pin");
        String security_pin = sc.nextLine();
        co.setAutoCommit(false);
        if (account_number != 0) {
            PreparedStatement ps = co.prepareStatement("select * from accounts where security_pin = ?");
            ps.setString(1, security_pin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double current_balance = rs.getDouble("balance");
                if (amount < current_balance) {
                    String debit_query = "update accounts set balance = balance - ? where account_number = ?";
                    PreparedStatement p = co.prepareStatement(debit_query);
                    p.setDouble(1, amount);
                    p.setLong(2, account_number);
                    int rowAffected = p.executeUpdate();
                    if (rowAffected > 0) {
                        System.out.println("Rs" + amount + "  succesfully");
                        co.commit();
                        co.setAutoCommit(true);
                        return;

                    } else {
                        System.out.println("Transction failed");
                        co.rollback();
                        co.setAutoCommit(true);
                    }

                } else {
                    System.out.println("Insufficient Funds");
                }
            } else {
                System.out.println("Invalid Pin");
            }
        }
        co.setAutoCommit(true);
    }
    public void transfer_money(long sender_account_number) throws SQLException {

        sc.nextLine();
        System.out.println("Enter receiver Account Number");
        long receiver_account_number = sc.nextLong();
        System.out.println("Enter Amount");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security Pin");
        String security_pin = sc.nextLine();

        co.setAutoCommit(false);
        if(sender_account_number != 0 && receiver_account_number !=0){
            PreparedStatement ps = co.prepareStatement("select * from accounts where account_number = ? and security_pin = ?");
            ps.setLong(1,sender_account_number);
            ps.setString(2,security_pin);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                double current_balance = rs.getDouble("balance");
                if(amount > current_balance){
                    String debit_query = "update accounts set balance = balance - ? where account_number = ?";
                    String credit_query = "update accounts set balance = balance + ? where account_number = ?";
                    PreparedStatement pc = co.prepareStatement(credit_query);
                    PreparedStatement pd =  co.prepareStatement(debit_query);
                    pc.setDouble(1,amount);
                    pc.setLong(2,receiver_account_number);
                    pd.setDouble(1,amount);
                    pd.setLong(2,sender_account_number);
                    int rowsAffected = pd.executeUpdate();
                    int rowsAffected2 = pc.executeUpdate();
                    if(rowsAffected > 0 && rowsAffected2 > 0){
                        System.out.println("Transction Succesfull");
                        co.commit();
                        co.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transction failed");
                        co.rollback();
                        co.setAutoCommit(true);
                    }


                } else {
                    System.out.println("Insufficient balance !");
                }
                }else {
                System.out.println("invalid security pin");

            }
        }
        co.setAutoCommit(true);

    }


    public void getbalance(long account_number) throws SQLException {
        sc.nextLine();
        System.out.println("Enter Security Pin");
        String security_pin = sc.nextLine();
        PreparedStatement ps = co.prepareStatement("select balance from accounts where account_number = ? and security_pin = ? ");
        ps.setLong(1,account_number);
        ps.setString(2,security_pin);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
           double balance = rs.getDouble("balance" );
            System.out.println("Balance " + balance);
        }else {
            System.out.println("Invalid Pin");
        }
    }

}
