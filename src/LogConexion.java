/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Capital
 */
public class LogConexion {
   private static final String URL = "jdbc:mysql://127.0.0.1:3306/dolche?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
   private static final String USERNAME = "root";
   private static final String PASSWORD = "";
   private Connection connection; 
   private PreparedStatement selectUser; 
   private PreparedStatement selectPass; 

   
   public void open(){
       try {
           connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
         selectUser = connection.prepareStatement(
            "SELECT * FROM user WHERE username = ?");
         selectPass = connection.prepareStatement(
            "SELECT * FROM user WHERE password = ?");           
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
   
   public boolean compare(String name, String code){
       ResultSet resultSet1 = null;
       ResultSet resultSet2 = null;
       try {
           selectUser.setString(1, name);
           selectPass.setString(1, code);
           resultSet1 = selectUser.executeQuery(); 
           resultSet2= selectPass.executeQuery(); 
        while (resultSet1.next()){
           if(resultSet1.getString("username").equals(name)){
               while(resultSet2.next()){
                   if(resultSet2.getString("password").equals(code)){
                       return true;
                   }
               }
           }
       }   
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
       }
       finally{
         try 
         {
            resultSet1.close();
            resultSet2.close();
         }
         catch (SQLException sqlException)
         {
            sqlException.printStackTrace();         
            close();
         }
      }
       return false;
   }
   
    public void close(){
      try {
         connection.close();
      } 
      catch (SQLException sqlException){
         sqlException.printStackTrace();
      } 
   } 
}
