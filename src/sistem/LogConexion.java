package sistem;

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
import javax.swing.JOptionPane;

/**
 *
 * @author Capital
 */
public class LogConexion {
   private static  String URL;
   private static final String USERNAME = "root";
   private static final String PASSWORD = "";
   private Connection connection; 
   private PreparedStatement selectUser; 
   private PreparedStatement selectPass; 
   private PreparedStatement insertCapture; 
   private PreparedStatement getidUser;
   private PreparedStatement insertUser;

   
   public void open(String PIN){
       this.URL = PIN;
       try {
         connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
         selectUser = connection.prepareStatement(
            "SELECT * FROM user WHERE username = ?");
         selectPass = connection.prepareStatement(
            "SELECT * FROM user WHERE password = ?");       
         insertCapture = connection.prepareStatement (
         "INSERT INTO register "+
                 "(idregister, fecha_analisis, clave, fecha_produccion, no_cocinada, "+
                 "espec, brix, ph, consistencia, apariencia, viscosidad, acidez, observaciones, "+
                 "estatus_fq, estatus_funcional, coliformes, cuenta_estandar, hongos, levaduras, estatus_micro, "+
                 "estatus_final, USER_iduser) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         getidUser = connection.prepareStatement(
                 "SELECT * FROM user WHERE username = ?");
         insertUser = connection.prepareStatement (
            "INSERT INTO user"+
                    "(username, password, privilege) VALUES (?, ?, ?)");
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           JOptionPane.showMessageDialog(null,"NO CONEXION CON BASE DE DATOS",
                         "System",JOptionPane.PLAIN_MESSAGE);
       }
   }
   
   public String compare(String name, String code){
       ResultSet resultSet1 = null;
       ResultSet resultSet2 = null;
       try {
           selectUser.setString(1, name);
           selectPass.setString(1, code);
           resultSet1 = selectUser.executeQuery(); 
           resultSet2 = selectPass.executeQuery(); 
        while (resultSet1.next()){
           if(resultSet1.getString("username").equals(name)){
               while(resultSet2.next()){
                   if(resultSet2.getString("password").equals(code)){
                       return resultSet1.getString("privilege");
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
       return null;
   }
   
   public int getUId(String name){
       ResultSet resultSet = null;
       try {
           getidUser.setString(1, name);
           resultSet = getidUser.executeQuery(); 
           while (resultSet.next()){
               if(resultSet.getString("username").equals(name)){
                   return resultSet.getInt("iduser");
               }
           }
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
       } finally{
           try {
               resultSet.close();
           } catch (SQLException ex) {
               Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
               close();
           }
       }
       return 0;
   }
   
   public int getRId(){
       int n = 0;
       ResultSet resultSet = null;
       try {
           resultSet = getidUser.executeQuery("SELECT MAX(idregister) FROM register"); 
           if (resultSet.next()){
               n = resultSet.getInt(1);
           }
           return n;
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
       }finally{
           try {
               resultSet.close();
           } catch (SQLException ex) {
               Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
               close();
           }
       }
       return n;
   }
   
   public int addRegister(int idreg, String f_anal, String clave,
           int f_prd, int n_cocina, String espec, float brix, float ph,
           float consist, String aparen, float visco, float acidez,
           String observ, String stat_fq, String stat_fun, String colifor,
           String cuenta_std, String hongos, String leva, String stat_micro,
           String stat_final, int u_id){
        int result = 0;
       try {
           insertCapture.setInt(1, idreg);
           insertCapture.setString(2, f_anal);
           insertCapture.setString(3, clave);
           insertCapture.setInt(4, f_prd);
           insertCapture.setInt(5, n_cocina);
           insertCapture.setString(6, espec);
           insertCapture.setFloat(7, brix);
           insertCapture.setFloat(8, ph);
           insertCapture.setFloat(9, consist);
           insertCapture.setString(10, aparen);
           insertCapture.setFloat(11, visco);
           insertCapture.setFloat(12, acidez);
           insertCapture.setString(13, observ);
           insertCapture.setString(14, stat_fq);
           insertCapture.setString(15, stat_fun);
           insertCapture.setString(16, colifor);
           insertCapture.setString(17, cuenta_std);
           insertCapture.setString(18, hongos);
           insertCapture.setString(19, leva);
           insertCapture.setString(20, stat_micro);
           insertCapture.setString(21, stat_final);
           insertCapture.setInt(22, u_id);
           
           result = insertCapture.executeUpdate();
           
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
   
   public int addUser(String name, String pass, int privilege){
       int result = 0;
       try {
           insertUser.setString(1, name);
           insertUser.setString(2, pass);
           insertUser.setInt(3, privilege);
           
           result = insertUser.executeUpdate();
           
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
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
