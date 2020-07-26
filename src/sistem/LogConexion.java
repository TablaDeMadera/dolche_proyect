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
   private PreparedStatement minData;
   private PreparedStatement maxData;
   private PreparedStatement promData;
   private PreparedStatement dsData;
   private PreparedStatement kpiSet;
   private PreparedStatement kpiGet;
   private PreparedStatement sample;

   
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
         
         sample = connection.prepareStatement (
                 "SELECT brix, ph, consistencia, viscosidad, acidez from register "
                         + "inner join params "
                         + "on register.id_params = params.id_params "
                         + "where clave = ? "
                         + "and fecha_analisis >= ? "
                         + "and fecha_analisis <= ?;"
         );
         minData = connection.prepareStatement (
                "SELECT MIN(brix), MIN(ph), MIN(consistencia), MIN(viscosidad), MIN(acidez) from register "
                         + "inner join params "
                         + "on register.id_params = params.id_params "
                         + "where clave = ? "
                         + "and fecha_analisis >= ? "
                         + "and fecha_analisis <= ?;"
         );
         
         maxData = connection.prepareStatement (
                "SELECT MAX(brix), MAX(ph), MAX(consistencia), MAX(viscosidad), MAX(acidez) from register "
                         + "inner join params "
                         + "on register.id_params = params.id_params "
                         + "where clave = ? "
                         + "and fecha_analisis >= ? "
                         + "and fecha_analisis <= ?;"
         );
         
         promData = connection.prepareStatement (
                 "SELECT AVG(brix), AVG(ph), AVG(consistencia), AVG(viscosidad), AVG(acidez) from register "
                         + "inner join params "
                         + "on register.id_params = params.id_params "
                         + "where clave = ? "
                         + "and fecha_analisis >= ? "
                         + "and fecha_analisis <= ?;"
         );
         
         dsData = connection.prepareStatement (
                "SELECT STD(brix), STD(ph), STD(consistencia), STD(viscosidad), STD(acidez) from register "
                         + "inner join params "
                         + "on register.id_params = params.id_params "
                         + "where clave = ? "
                         + "and fecha_analisis >= ? "
                         + "and fecha_analisis <= ?;"
         );
         
         kpiSet = connection.prepareStatement (
                 "INSERT INTO kpi "
                         + "(date_kpi, min_brix, min_ph, min_consistencia, min_viscocidad, min_acidez, "
                         + "max_brix, max_ph, max_consistencia, max_viscocidad, max_acidez, "
                         + "prom_brix, prom_ph, prom_consistencia, prom_viscocidad, prom_acidez, "
                         + "desv_brix, desv_ph, desv_consistencia, desv_viscocidad, desv_acidez, "
                         + "clave, descripcion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                         + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
         );
         
         kpiGet = connection.prepareStatement(
         "SELECT * FROM kpi ORDER BY idKPI DESC LIMIT 1");
         
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           JOptionPane.showMessageDialog(null,"NO CONEXION CON BASE DE DATOS",
                         "System",JOptionPane.PLAIN_MESSAGE);
       }
   }
   
   //-----------------------------------------------------------LOGIN 
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
   
   //---------------------------------------------VARIUOS
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
//---------------------------------------CAPTURE AND ADMIN   
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
   
   //-------------------------------ADMIN CONTROL
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
   
//-----------------------------------VIEWER          
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
   
   //-----------------------------------------STADISTICS AND KPI GRAPHER
      public float[][] getSample(String fi, String ff, String clv){
       float [][] dato = new float [5][100];
       ResultSet resultSet = null;
       try {
           sample.setString(1, clv);
           sample.setString(2, fi);
           sample.setString(3, ff);
           resultSet = sample.executeQuery(); 
           int i = 0;
           while (resultSet.next()){
               dato[i][0]=resultSet.getFloat(1);
               dato[i][1]=resultSet.getFloat(2);
               dato[i][2]=resultSet.getFloat(3);
               dato[i][3]=resultSet.getFloat(4);
               dato[i][4]=resultSet.getFloat(5);
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
      
   public float[] getMin(String fi, String ff, String clv){
       float [] dato = new float [5];
       ResultSet resultSet = null;
       try {
           minData.setString(1, clv);
           minData.setString(2, fi);
           minData.setString(3, ff);
           resultSet = minData.executeQuery();
           while (resultSet.next()){
               dato[0]=resultSet.getFloat(1);
               dato[1]=resultSet.getFloat(2);
               dato[2]=resultSet.getFloat(3);
               dato[3]=resultSet.getFloat(4);
               dato[4]=resultSet.getFloat(5);
           }
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return dato;
   }
   
    public float[] getMax(String fi, String ff, String clv){
       float [] dato = new float [5];
       ResultSet resultSet = null;
       try {
           maxData.setString(1, clv);
           maxData.setString(2, fi);
           maxData.setString(3, ff);
           resultSet = maxData.executeQuery();
           while (resultSet.next()){
               dato[0]=resultSet.getFloat(1);
               dato[1]=resultSet.getFloat(2);
               dato[2]=resultSet.getFloat(3);
               dato[3]=resultSet.getFloat(4);
               dato[4]=resultSet.getFloat(5);
           }
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return dato;
   }

    public float[] getProm(String fi, String ff, String clv){
       float [] dato = new float [5];
       ResultSet resultSet = null;
       try {
           promData.setString(1, clv);
           promData.setString(2, fi);
           promData.setString(3, ff);
           resultSet = promData.executeQuery();
           while (resultSet.next()){
               dato[0]=resultSet.getFloat(1);
               dato[1]=resultSet.getFloat(2);
               dato[2]=resultSet.getFloat(3);
               dato[3]=resultSet.getFloat(4);
               dato[4]=resultSet.getFloat(5);
           }
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return dato;
   }
   
    public float[] getDS(String fi, String ff, String clv){
       float [] dato = new float [5];
       ResultSet resultSet = null;
       try {
           dsData.setString(1, clv);
           dsData.setString(2, fi);
           dsData.setString(3, ff);
           resultSet = dsData.executeQuery();
           while (resultSet.next()){
               dato[0]=resultSet.getFloat(1);
               dato[1]=resultSet.getFloat(2);
               dato[2]=resultSet.getFloat(3);
               dato[3]=resultSet.getFloat(4);
               dato[4]=resultSet.getFloat(5);
           }
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return dato;
   }
    
   public String[] gKPI(){
       String [] dato = new String [24];
       ResultSet resultSet = null;
       try {
           resultSet = kpiGet.executeQuery();
           while (resultSet.next()){
               dato[0]=resultSet.getString(1);
               dato[1]=""+resultSet.getDate(2);
               dato[2]=""+resultSet.getFloat(3);
               dato[3]=""+resultSet.getFloat(4);
               dato[4]=""+resultSet.getFloat(5);
               dato[5]=""+resultSet.getFloat(6);
               dato[6]=""+resultSet.getFloat(7);
               dato[7]=""+resultSet.getFloat(8);
               dato[8]=""+resultSet.getFloat(9);
               dato[9]=""+resultSet.getFloat(10);
               dato[10]=""+resultSet.getFloat(11);
               dato[11]=""+resultSet.getFloat(12);
               dato[12]=""+resultSet.getFloat(13);
               dato[13]=""+resultSet.getFloat(14);
               dato[14]=""+resultSet.getFloat(15);
               dato[15]=""+resultSet.getFloat(16);
               dato[16]=""+resultSet.getFloat(17);
               dato[17]=""+resultSet.getFloat(18);
               dato[18]=""+resultSet.getFloat(19);
               dato[19]=""+resultSet.getFloat(20);
               dato[20]=""+resultSet.getFloat(21);
               dato[21]=""+resultSet.getFloat(22);
               dato[22]=resultSet.getString(23);
               dato[23]=resultSet.getString(24);
           }
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return dato;
   }
   
   public int sKPI(String date_kpi, float min_brix, float min_ph, float min_consistencia, float min_viscocidad, float min_acidez, float max_brix, float max_ph,
           float max_consistencia, float max_viscocidad, float max_acidez, float prom_brix, float prom_ph, float prom_consistencia, float prom_viscocidad, 
           float prom_acidez, float desv_brix, float desv_ph, float desv_consistencia, float desv_viscocidad, float desv_acidez, String clave, String descripcion){
       int result = 0;
       try {
           kpiSet.setString(1, date_kpi);
           kpiSet.setFloat(2, min_brix);
           kpiSet.setFloat(3, min_ph);
           kpiSet.setFloat(4, min_consistencia);
           kpiSet.setFloat(5, min_viscocidad);
           kpiSet.setFloat(6, min_acidez);
           kpiSet.setFloat(7, max_brix);
           kpiSet.setFloat(8, max_ph);
           kpiSet.setFloat(9, max_consistencia);
           kpiSet.setFloat(10, max_viscocidad);
           kpiSet.setFloat(11, max_acidez);
           kpiSet.setFloat(12, prom_brix);
           kpiSet.setFloat(13, prom_ph);
           kpiSet.setFloat(14, prom_consistencia);
           kpiSet.setFloat(15, prom_viscocidad);
           kpiSet.setFloat(16, prom_acidez);
           kpiSet.setFloat(17, desv_brix);
           kpiSet.setFloat(18, desv_ph);
           kpiSet.setFloat(19, desv_consistencia);
           kpiSet.setFloat(20, desv_viscocidad);
           kpiSet.setFloat(21, desv_acidez);
           kpiSet.setString(22, clave);
           kpiSet.setString(23, descripcion);
           result = kpiSet.executeUpdate();
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
