package sistem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Capital
 */
public class LogConexion {
   private static  String URL;
   private static  String USERNAME;
   private static  String PASSWORD;
   private Connection connection; 
   private PreparedStatement selectUser; 
   private PreparedStatement selectPass; 
   private PreparedStatement insertCapture; 
   private PreparedStatement insertPar;
   private PreparedStatement insertMic;
   private PreparedStatement getidUser;
   private PreparedStatement insertUser;
   private PreparedStatement dateSearch;
   private PreparedStatement userData;
   private PreparedStatement userDelete;
   private PreparedStatement userUpdate;

   
   public void open(){
        Properties p = new Properties();
        String ip = "";
        String pr = "";
        String db = "";
        try {   
            p.load(new FileReader("config.properties"));
            ip = p.getProperty("ip");
            pr = p.getProperty("port");
            db = p.getProperty("base");
            USERNAME = p.getProperty("username");
            PASSWORD = p.getProperty("password");
            URL="jdbc:mysql://"+ip+":"+pr+"/"+db+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
       try {
         connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
         selectUser = connection.prepareStatement(
            "SELECT * FROM user WHERE username = ?");
         selectPass = connection.prepareStatement(
            "SELECT * FROM user WHERE password = ?");    
         
         insertCapture = connection.prepareStatement (
         "INSERT INTO register "+
                 "(idregister, fecha_analisis, clave, fecha_produccion, no_cocinada, "+
                 "estatus_final, operador, USER_iduser, id_params, id_micro) "+
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         
         insertPar = connection.prepareStatement(
         "INSERT INTO params "+
                 "(id_params, espec, brix, ph, consistencia, apariencia, viscosidad, acidez, "+
                 "observaciones, estatus_fq, estatus_funcional) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         
         insertMic = connection.prepareStatement(
         "INSERT INTO micro "+
                 "(id_micro, coliformes, cuenta_estandar, hongos, levaduras, estatus_micro) "+
                 "VALUES (?, ?, ?, ?, ?, ?)");
         
         getidUser = connection.prepareStatement(
                 "SELECT * FROM user WHERE username = ?");
         insertUser = connection.prepareStatement (
            "INSERT INTO user"+
                    "(username, password, privilege) VALUES (?, ?, ?)");
         
         dateSearch = connection.prepareStatement (
                 "SELECT idregister, operador, clave, no_cocinada, estatus_final, estatus_fq, estatus_funcional, estatus_micro FROM register "
                         + "INNER JOIN params "
                         + "ON register.id_params = params.id_params "
                         + "INNER JOIN micro "
                         + "ON register.id_micro = micro.id_micro "
                         + "WHERE fecha_analisis = ?"
         );
         
         userData = connection.prepareStatement (
                 "SELECT username, password, privilege from user "
                         + "WHERE iduser = ?"
         );
         
         userDelete = connection.prepareStatement (
                 "DELETE from user WHERE iduser = ?"
         );
         
         userUpdate = connection.prepareStatement (
                 "UPDATE user SET username = ?, "
                         + "password = ?, "
                         + "privilege = ? WHERE iduser = ?"
         );
       
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
           String f_prd, int n_cocina, String stat_final, String opera,
           int u_id, int idparam, int idmicro){
        int result = 0;
       try {
           insertCapture.setInt(1, idreg);
           insertCapture.setString(2, f_anal);
           insertCapture.setString(3, clave);
           insertCapture.setString(4, f_prd);
           insertCapture.setInt(5, n_cocina);
           insertCapture.setString(6, stat_final);
           insertCapture.setString(7, opera);
           insertCapture.setInt(8, u_id);
           insertCapture.setInt(9, idparam);
           insertCapture.setInt(10, idmicro);
           
           result = insertCapture.executeUpdate();
           
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
   
   public int addPara(int idparam, String espec, float brix, float ph,
           float consis, String apa, float visco, float acid, String obs,
           String stat_fq, String stat_fun){
       int result = 0;
       try{
           insertPar.setInt(1, idparam);
           insertPar.setString(2, espec);
           insertPar.setFloat(3, brix);
           insertPar.setFloat(4, ph);
           insertPar.setFloat(5, consis);
           insertPar.setString(6, apa);
           insertPar.setFloat(7, visco);
           insertPar.setFloat(8, acid);
           insertPar.setString(9, obs);
           insertPar.setString(10, stat_fq);
           insertPar.setString(11, stat_fun);
           
           result = insertPar.executeUpdate();
       }catch (SQLException ex){
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null,ex);
           close();
       }
       return result;
   }
   
   public int addMicro(int idmicro, String coli, String cuenta, String hongo,
           String leva, String sta_micro){
       int result = 0;
       try{
           insertMic.setInt(1, idmicro);
           insertMic.setString(2, coli);
           insertMic.setString(3, cuenta);
           insertMic.setString(4, hongo);
           insertMic.setString(5, leva);
           insertMic.setString(6, sta_micro);
           
           result = insertMic.executeUpdate();
       }catch (SQLException ex){
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
   
   public String[] searchUser(int numUser){
       String [] dato = new String [3];
       ResultSet resultSet = null;
       try {
           userData.setInt(1, numUser);
           resultSet = userData.executeQuery();
           while (resultSet.next()){
               dato[0]=resultSet.getString(1);
               dato[1]=resultSet.getString(2);
               dato[2]=resultSet.getString(3);
           }
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return dato;
   } 
   
   public int changeUser(int id, String name, String pass, int priv){
       int result = 0;
       try {
           userUpdate.setString(1, name);
           userUpdate.setString(2, pass);
           userUpdate.setInt(3, priv);
           userUpdate.setInt(4, id);
           result = userUpdate.executeUpdate();
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;     
   }
   
   public int deleUser(int id){
       int result = 0;
       try {
           userDelete.setInt(1, id);
           result = userDelete.executeUpdate();
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
           
   public String[][] getReg(String fecha){
       String [][] dato = new String [8][100];
       ResultSet resultSet = null;
       try {
           dateSearch.setString(1, fecha);
           resultSet = dateSearch.executeQuery(); 
           int i = 0;
           while (resultSet.next()){
               dato[0][i]=resultSet.getString(1);
               dato[1][i]=resultSet.getString(2);
               dato[2][i]=resultSet.getString(3);
               dato[3][i]=resultSet.getString(4);
               dato[4][i]=resultSet.getString(5);
               dato[5][i]=resultSet.getString(6);
               dato[6][i]=resultSet.getString(7);
               dato[7][i]=resultSet.getString(8);
               i++;
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
       return dato;
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
