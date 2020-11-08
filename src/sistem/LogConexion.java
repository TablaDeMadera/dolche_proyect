package sistem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileInputStream;
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
 * @author Abraham Ascencio
 */
public class LogConexion {
   private static  String URL;
   private static  String USERNAME;
   private static  String PASSWORD;
   public Connection connection; 
   private PreparedStatement selectUser; 
   private PreparedStatement selectPass; 
   private PreparedStatement insertCapture; 
   private PreparedStatement insertPar;
   private PreparedStatement insertMic;
   private PreparedStatement insertUser;
   private PreparedStatement dateSearch;
   private PreparedStatement userData;
   private PreparedStatement userDelete;
   private PreparedStatement userUpdate;
   private PreparedStatement promData;
   private PreparedStatement dsData;
   private PreparedStatement insertKpi;
   private PreparedStatement lstKpi;
   private PreparedStatement kpiData;
   private PreparedStatement sample;
   private PreparedStatement insertDesv;
   private PreparedStatement insertProm;
   private PreparedStatement insertGraf;
   private PreparedStatement insertAlarm;
   private PreparedStatement lstPrm;
   private PreparedStatement lstDsv;
   private PreparedStatement lstUser;
   private PreparedStatement lstIdKpi;
   private PreparedStatement lstIdPr;
   private PreparedStatement lstIdDe;
   private PreparedStatement lstIdGr;
   private PreparedStatement lstIdPa;
   private PreparedStatement lstIdMi;
   private PreparedStatement lstAlarm;
   private PreparedStatement dataReg;
   private PreparedStatement updateReg;
   private PreparedStatement updatePar;
   private PreparedStatement updateMic;
   private PreparedStatement getParMicId;
   private PreparedStatement regDelete;
   private PreparedStatement paramDelete;
   private PreparedStatement microDelete;
   private PreparedStatement alarmDelete;
   private PreparedStatement selectAlarm;
   private PreparedStatement setEvent;
   private PreparedStatement getLogg;
   private PreparedStatement getStat;
   private PreparedStatement setStat;
   private PreparedStatement freeStat;
   private PreparedStatement setMsg;
   private PreparedStatement getMsg;
   private PreparedStatement alrmComp;
   private PreparedStatement alrmTrigger;
   private PreparedStatement getAlm;
   
   //Metodo que inicia conexión con la información
   //del configurador y prepara las consultas a la base de datos
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
//-------------------------------------------------------------ALL SELECTS         
         selectUser = connection.prepareStatement(
            "SELECT * FROM usuarios WHERE user_name = ?");
         
         lstUser = connection.prepareStatement(
            "SELECT MAX(id_register) FROM registros");
         
         lstAlarm = connection.prepareStatement(
            "SELECT MAX(id_alarm) FROM alarmas");
         
         lstIdKpi = connection.prepareStatement(
            "SELECT MAX(id_kpi) FROM kpis");
         
         lstIdPr= connection.prepareStatement(
            "SELECT MAX(id_avg) FROM promedios");
         
         lstIdDe = connection.prepareStatement(
            "SELECT MAX(id_desv) FROM desviaciones");
         
         lstIdGr = connection.prepareStatement(
            "SELECT MAX(id_graphs) FROM graficas");
         
         lstIdPa = connection.prepareStatement(
            "SELECT MAX(id_params) FROM params");
         
         lstIdMi = connection.prepareStatement(
            "SELECT MAX(id_micro) FROM micros");
         
         selectPass = connection.prepareStatement(
            "SELECT * FROM usuarios WHERE password = ?");    
         
         dateSearch = connection.prepareStatement (
                 "SELECT * FROM registros "
                         + "WHERE fecha_analisis = ?"
         );
         
         userData = connection.prepareStatement (
                 "SELECT user_name, name, password, privilege from usuarios "
                         + "WHERE id_user = ?"
         );
         
         sample = connection.prepareStatement (
                 "SELECT brix, ph, consistencia, viscosidad, acidez from registros "
                         + "inner join params "
                         + "on registros.id_params = params.id_params "
                         + "where clave = ? "
                         + "and fecha_analisis >= ? "
                         + "and fecha_analisis <= ?;"
         );
                  
         promData = connection.prepareStatement (
                 "SELECT AVG(brix), AVG(ph), AVG(consistencia), AVG(viscosidad), AVG(acidez) from registros "
                         + "inner join params "
                         + "on registros.id_params = params.id_params "
                         + "where clave = ? "
                         + "and fecha_analisis >= ? "
                         + "and fecha_analisis <= ?;"
         );
         
         dsData = connection.prepareStatement (
                "SELECT STD(brix), STD(ph), STD(consistencia), STD(viscosidad), STD(acidez) from registros "
                         + "inner join params "
                         + "on registros.id_params = params.id_params "
                         + "where clave = ? "
                         + "and fecha_analisis >= ? "
                         + "and fecha_analisis <= ?;"
         );
         
         dataReg = connection.prepareStatement (
                 "SELECT fecha_analisis, clave, fecha_produccion, no_cocinada, estatus_final, operador, " +
                    "coliformes, cuenta_estandar, hongos, levaduras, estatus_micro, " +
                    "espec, brix, ph, consistencia, apariencia, viscosidad, acidez, observaciones, estatus_fq, estatus_funcional " +
                    "FROM registros JOIN micros JOIN params ON registros.id_micro = micros.id_micro AND registros.id_params = params.id_params " +
                    "WHERE registros.id_register = ?;");
         
         
         lstKpi = connection.prepareStatement(
         "SELECT id_kpi, date_kpi, clave, description, fi, ff FROM kpis ORDER BY id_kpi DESC LIMIT 1");
         
         lstPrm = connection.prepareStatement(
         "SELECT prom_brix, prom_ph, prom_consistencia, prom_viscocidad, prom_acidez FROM promedios ORDER BY id_avg DESC LIMIT 1");
         
         lstDsv = connection.prepareStatement(       
         "SELECT desv_brix, desv_ph, desv_consistencia, desv_viscocidad, desv_acidez FROM desviaciones ORDER BY id_desv DESC LIMIT 1");
         
         
         kpiData = connection.prepareStatement(
         "SELECT " +
                    "id_kpi, date_kpi, clave, description, fi, ff, " +
                    "prom_brix, prom_ph, prom_consistencia, prom_viscocidad, prom_acidez, " +
                    "desv_brix, desv_ph, desv_consistencia, desv_viscocidad, desv_acidez " +
                    "FROM kpis JOIN desviaciones JOIN promedios " +
                    "ON kpis.id_desv = desviaciones.id_desv " +
                    "AND kpis.id_avg = promedios.id_avg " +
                    "WHERE " +
                    "kpis.id_kpi = ?");
         
         getParMicId = connection.prepareStatement(
                 "SELECT id_params, id_micro FROM registros WHERE id_register = ?");
         
         selectAlarm = connection.prepareStatement(
                 "SELECT parameter_target, low_limit, high_limit, create_date, id_alarm FROM alarmas WHERE id_alarm = ?");
         
         getLogg = connection.prepareStatement(
                 "SELECT * from sesiones WHERE ini_date >= ?"
                         + " AND ini_date < ?");
         
         getStat = connection.prepareStatement (
                 "SELECT status from usuarios WHERE id_user = ?");
         
         getMsg = connection.prepareStatement (
                 "SELECT user_name, fecha_crea, msg FROM mensages INNER JOIN usuarios ON mensages.id_user = usuarios.id_user "
                         + "ORDER BY fecha_crea ASC");
         
         getAlm = connection.prepareStatement (
                 "SELECT id_alarm, parameter_target, shot_date FROM alarmas WHERE shot = true "
                         + "ORDER BY shot_date ASC");
         
         alrmComp = connection.prepareStatement (
                 "SELECT id_alarm FROM alarmas WHERE parameter_target = ? AND "
                         + "(? <= low_limit OR ? >= high_limit)");
//-----------------------------------------------------------ALL INSERTS

         insertCapture = connection.prepareStatement (
         "INSERT INTO registros "+
                 "(fecha_analisis, clave, fecha_produccion, no_cocinada, "+
                 "estatus_final, operador, id_user, id_micro, id_params) "+
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
         
         insertPar = connection.prepareStatement(
         "INSERT INTO params "+
                 "(espec, brix, ph, consistencia, apariencia, viscosidad, acidez, "+
                 "observaciones, estatus_fq, estatus_funcional) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         
         insertMic = connection.prepareStatement(
         "INSERT INTO micros "+
                 "(coliformes, cuenta_estandar, hongos, levaduras, estatus_micro) "+
                 "VALUES (?, ?, ?, ?, ?)");
         
         insertUser = connection.prepareStatement (
            "INSERT INTO usuarios"+
                    "(user_name, name, password, privilege) VALUES (?, ?, ?, ?)");
         
         insertKpi = connection.prepareStatement (
                 "INSERT INTO kpis "
                         + "(date_kpi, clave, description, fi, ff, id_user, id_avg, id_desv, id_graphs) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");        
         
         insertDesv = connection.prepareStatement(
            "INSERT INTO desviaciones (desv_brix, desv_ph, desv_consistencia, desv_viscocidad, "
                    + "desv_acidez) VALUES (?, ?, ?, ?, ?);");
         
         insertProm = connection.prepareStatement(
            "INSERT INTO promedios (prom_brix, prom_ph, prom_consistencia, prom_viscocidad, "
                    + "prom_acidez) VALUES (?, ?, ?, ?, ?)");
         
         insertGraf = connection.prepareStatement(
            "INSERT INTO graficas (g_brix, g_ph, g_consistencia, g_viscocidad, g_acidez) "
                    + "VALUES (?, ?, ?, ?, ?)");
         
         insertAlarm = connection.prepareStatement(
            "INSERT INTO alarmas (create_date, parameter_target, low_limit, high_limit, description) "
                    + "VALUES (NOW(), ?, ?, ?, ?)");
                 
         setEvent = connection.prepareStatement ("INSERT INTO sesiones (ini_date, id_user, actv) "
                 + "VALUES (NOW(), ?, ?)");
         
         setMsg = connection.prepareStatement ("INSERT INTO mensages (msg, fecha_crea, id_user) "
                 + "VALUES (?, NOW(), ?)");
         
//---------------------------------------------------------ALL DELETES     
         userDelete = connection.prepareStatement (
                 "DELETE from usuarios WHERE id_user = ?"
         );
         
         regDelete = connection.prepareStatement  (
                 "DELETE from registros WHERE id_register = ?"
         );
         
         paramDelete = connection.prepareStatement  (
                 "DELETE from params WHERE id_params = ?"
         );
         
         microDelete = connection.prepareStatement  (
                 "DELETE from micros WHERE id_micro = ?"
         );
         
         alarmDelete = connection.prepareStatement  (
                 "DELETE from alarmas WHERE id_alarm = ?"
         );
//--------------------------------------------------------ALL UPDATES         
         userUpdate = connection.prepareStatement (
                 "UPDATE usuarios SET user_name = ?, "
                         + "name = ?, "
                         + "password = ?, "
                         + "privilege = ? WHERE id_user = ?"
         );
         
         updateReg = connection.prepareStatement (
                 "UPDATE registros SET  fecha_analisis = ?, clave = ?, fecha_produccion = ?, no_cocinada = ?, estatus_final = ?, operador = ? "
                         + "WHERE id_register = ?");
         
         updatePar = connection.prepareStatement (
                 "UPDATE params SET espec = ?, brix = ?, ph = ?, consistencia = ?, apariencia = ?, viscosidad = ?, acidez = ?, observaciones = ?, estatus_fq = ?, estatus_funcional = ? "
                         + "WHERE id_params = ?;");
         
         updateMic = connection.prepareStatement (
                 "UPDATE micros SET coliformes = ?, cuenta_estandar = ?, hongos = ?, levaduras = ?, estatus_micro = ? "
                         + "WHERE id_micro = ?;");
         
         setStat = connection.prepareStatement ("UPDATE usuarios SET status = false "
                 + "WHERE id_user = ? ");
         
         freeStat = connection.prepareStatement ("UPDATE usuarios SET status = true "
                 + "WHERE id_user = ? ");
         
         alrmTrigger = connection.prepareStatement (
                 "UPDATE alarmas SET shot = true, shot_date = NOW() WHERE"
                         + " id_alarm = ?");
         
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           JOptionPane.showMessageDialog(null,"NO CONEXION CON BASE DE DATOS",
                         "System",JOptionPane.PLAIN_MESSAGE);
       }
   }
   
   //-----------------------------------------------------------LOGIN methods
   public String compare(String name, String code){
       ResultSet resultSet1 = null;
       ResultSet resultSet2 = null;
       try {
           selectUser.setString(1, name);
           selectPass.setString(1, code);
           resultSet1 = selectUser.executeQuery(); 
           resultSet2 = selectPass.executeQuery(); 
        while (resultSet1.next()){
           if(resultSet1.getString("user_name").equals(name)){
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
           selectUser.setString(1, name);
           resultSet = selectUser.executeQuery(); 
           while (resultSet.next()){
               if(resultSet.getString("user_name").equals(name)){
                   return resultSet.getInt("id_user");
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
   
   public int addEvent(int user, String actv){
       int result = 0;
       try{
           setEvent.setInt(1, user);
           setEvent.setString(2, actv);
           result = setEvent.executeUpdate();
       }catch (SQLException ex){
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
   
   public int userBlock(int user){
       int result = 0;
       try{
           setStat.setInt(1, user);
           result = setStat.executeUpdate();
       }catch (SQLException ex){
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
   
   public int userUnblock(int user){
       int result = 0;
       try{
           freeStat.setInt(1, user);
           result = freeStat.executeUpdate();
       }catch (SQLException ex){
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
   
   public boolean userStatus(int id){
       boolean n = true;
       ResultSet resultSet = null;
       try {
           getStat.setInt(1, id);
           resultSet = getStat.executeQuery(); 
           if (resultSet.next()){
               n = resultSet.getBoolean(1);
               return n;
           }
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
   //---------------------------------------------VARIUOS methods
   public int getRId(){
       int n = 0;
       ResultSet resultSet = null;
       try {
           resultSet = lstUser.executeQuery(); 
           if (resultSet.next()){
               n = resultSet.getInt(1);
               return n;
           }
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
   
   public int getKId(){
       int n = 0;
       ResultSet resultSet = null;
       try {
           resultSet = lstIdKpi.executeQuery(); 
           if (resultSet.next()){
               n = resultSet.getInt(1);
               return n;
           }
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
   
   public int getPId(){
       int n = 0;
       ResultSet resultSet = null;
       try {
           resultSet = lstIdPr.executeQuery(); 
           if (resultSet.next()){
               n = resultSet.getInt(1);
               return n;
           }
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
   
   public int getDId(){
       int n = 0;
       ResultSet resultSet = null;
       try {
           resultSet = lstIdDe.executeQuery(); 
           if (resultSet.next()){
               n = resultSet.getInt(1);
               return n;
           }
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
   
   public int getGId(){
       int n = 0;
       ResultSet resultSet = null;
       try {
           resultSet = lstIdGr.executeQuery(); 
           if (resultSet.next()){
               n = resultSet.getInt(1);
               return n;
           }
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
   
   
   public int getPaId(){
       int n = 0;
       ResultSet resultSet = null;
       try {
           resultSet = lstIdPa.executeQuery(); 
           if (resultSet.next()){
               n = resultSet.getInt(1);
               return n;
           }
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
   
   public int getAlId(){
       int n = 0;
       ResultSet resultSet = null;
       try {
           resultSet = lstAlarm.executeQuery(); 
           if (resultSet.next()){
               n = resultSet.getInt(1);
               return n;
           }
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
   
   public int getMiId(){
       int n = 0;
       ResultSet resultSet = null;
       try {
           resultSet = lstIdMi.executeQuery(); 
           if (resultSet.next()){
               n = resultSet.getInt(1);
               return n;
           }
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
   
   
   public int[] getRegIds(int id){
       int [] i = new int[2];
       ResultSet resultSet = null;
       try {
           getParMicId.setInt(1, id);
           resultSet = getParMicId.executeQuery(); 
           while (resultSet.next()){
               i[0] = resultSet.getInt(1);
               i[1] = resultSet.getInt(2);
               }
           return i;
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
       return i;
   }
//---------------------------------------CAPTURE AND ADMIN  methods
   public int addRegister(String f_anal, String clave,
           String f_prd, int n_cocina, String stat_final, String opera,
           int u_id, int m_id, int p_id){
        int result = 0;
       try {
           insertCapture.setString(1, f_anal);
           insertCapture.setString(2, clave);
           insertCapture.setString(3, f_prd);
           insertCapture.setInt(4, n_cocina);
           insertCapture.setString(5, stat_final);
           insertCapture.setString(6, opera);
           insertCapture.setInt(7, u_id);
           insertCapture.setInt(8, m_id);
           insertCapture.setInt(9, p_id);
           
           result = insertCapture.executeUpdate();
           
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
   
   public int addPara(String espec, float brix, float ph,
           float consis, String apa, float visco, float acid, String obs,
           String stat_fq, String stat_fun){
       int result = 0;
       try{
           insertPar.setString(1, espec);
           insertPar.setFloat(2, brix);
           insertPar.setFloat(3, ph);
           insertPar.setFloat(4, consis);
           insertPar.setString(5, apa);
           insertPar.setFloat(6, visco);
           insertPar.setFloat(7, acid);
           insertPar.setString(8, obs);
           insertPar.setString(9, stat_fq);
           insertPar.setString(10, stat_fun);
           
           result = insertPar.executeUpdate();
       }catch (SQLException ex){
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null,ex);
           close();
       }
       return result;
   }
   
   public int addMicro(String coli, String cuenta, String hongo,
           String leva, String sta_micro){
       int result = 0;
       try{
           insertMic.setString(1, coli);
           insertMic.setString(2, cuenta);
           insertMic.setString(3, hongo);
           insertMic.setString(4, leva);
           insertMic.setString(5, sta_micro);
           
           result = insertMic.executeUpdate();
       }catch (SQLException ex){
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
   
   public int [] alarmaCompara(String param, float reg){
       int [] dato = new int [1000];
       ResultSet resultSet = null;
       try {
           alrmComp.setString(1, param);
           alrmComp.setFloat(2, reg);
           alrmComp.setFloat(3, reg);
           resultSet = alrmComp.executeQuery();
           int i = 0;
           while (resultSet.next()){
               dato[i]=resultSet.getInt(1);
               i++;
           }
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return dato;
   }
   
   //alrmTrigger
   public int alarmaTrigg(int id_alarm){
       int result = 0;
       try {
           alrmTrigger.setInt(1, id_alarm);
           
           result = alrmTrigger.executeUpdate();
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;     
   }
   
   //-------------------------------ADMIN CONTROL methods
   public int addUser(String nick, String name, String pass, int privilege){
       int result = 0;
       try {
           insertUser.setString(1, nick);
           insertUser.setString(2, name);
           insertUser.setString(3, pass);
           insertUser.setInt(4, privilege);
           
           result = insertUser.executeUpdate();
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
   
   public int addAlarm(String param, float low, float high, String descript){
       int result = 0;
       try {
           insertAlarm.setString(1, param);
           insertAlarm.setFloat(2, low);
           insertAlarm.setFloat(3, high);
           insertAlarm.setString(4, descript);
           
           result = insertAlarm.executeUpdate();
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
   
   public int alarmDele(int id){
       int result = 0;
       try {
           alarmDelete.setInt(1, id);
           result = alarmDelete.executeUpdate();
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
   
   public String[] searchAlarm(int id){
       String [] dato = new String [5];
       ResultSet resultSet = null;
       try {
           selectAlarm.setInt(1, id);
           resultSet = selectAlarm.executeQuery();
           while (resultSet.next()){
               dato[0]=resultSet.getString(1);
               dato[1]=""+resultSet.getInt(2);
               dato[2]=""+resultSet.getInt(3);
               dato[3]=resultSet.getString(4);
               dato[4]=""+resultSet.getInt(5);
           }
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return dato;
   } 
   
   public String logger(String fi, String ff){
       String dato = "id\tfecha\t\tid_usuario\tactividad\n";
       ResultSet resultSet = null;
       try {
           getLogg.setString(1, fi);
           getLogg.setString(2, ff);
           resultSet = getLogg.executeQuery();
           while (resultSet.next()){
               dato += resultSet.getString(1)+"\t";
               dato += resultSet.getString(2)+"\t";
               dato += resultSet.getString(3)+"\t";
               dato += resultSet.getString(4)+"\t";
               dato += "\n";
           }
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return dato;
   } 
   
   public String[] searchUser(int numUser){
       String [] dato = new String [4];
       ResultSet resultSet = null;
       try {
           userData.setInt(1, numUser);
           resultSet = userData.executeQuery();
           while (resultSet.next()){
               dato[0]=resultSet.getString(1);
               dato[1]=resultSet.getString(2);
               dato[2]=resultSet.getString(3);
               dato[3]=resultSet.getString(4);
           }
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return dato;
   } 
   
   public int changeUser(int id, String nick, String name, String pass, int priv){
       int result = 0;
       try {
           userUpdate.setString(1, nick);
           userUpdate.setString(2, name);
           userUpdate.setString(3, pass);
           userUpdate.setInt(4, priv);
           userUpdate.setInt(5, id);
           
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
   
   public int deleReg(int id_rg, int id_para, int id_mic){
       int a = 0;
       int b = 0;
       int c = 0;
       try {
           regDelete.setInt(1, id_rg);
           a = regDelete.executeUpdate();
           paramDelete.setInt(1, id_para);
           b = paramDelete.executeUpdate();
           microDelete.setInt(1, id_mic);
           c = microDelete.executeUpdate();
           
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       if ((a!=0)&&(b!=0)&&(c!=0)){
           return 1;
       }else{
           return 0;
       }
   }
   
   public String[] allRegister(int id){
       String [] dato = new String [21]; 
       ResultSet resultSet = null;
       try{
           dataReg.setInt(1, id);
           resultSet = dataReg.executeQuery();
           while (resultSet.next()){
               dato[0]=""+resultSet.getDate(1);
               dato[1]=""+resultSet.getString(2);
               dato[2]=""+resultSet.getDate(3);
               dato[3]=""+resultSet.getInt(4);
               dato[4]=""+resultSet.getString(5);
               dato[5]=""+resultSet.getString(6);
               dato[6]=""+resultSet.getString(7);
               dato[7]=""+resultSet.getString(8);
               dato[8]=""+resultSet.getString(9);
               dato[9]=""+resultSet.getString(10);
               dato[10]=""+resultSet.getString(11);
               dato[11]=""+resultSet.getString(12);
               dato[12]=""+resultSet.getFloat(13);
               dato[13]=""+resultSet.getFloat(14);
               dato[14]=""+resultSet.getFloat(15);
               dato[15]=""+resultSet.getString(16);
               dato[16]=""+resultSet.getFloat(17);
               dato[17]=""+resultSet.getFloat(18);
               dato[18]=""+resultSet.getString(19);
               dato[19]=""+resultSet.getString(20);
               dato[20]=""+resultSet.getString(21);
           }
       }catch (SQLException ex) {
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
   
   public int upReg(String fecha_analisis, String clave, String fecha_produccion, int no_cocinada, String estatus_final, String operador, int id_reg){
       int result = 0;
       try {
           updateReg.setString(1, fecha_analisis);
           updateReg.setString(2, clave);
           updateReg.setString(3, fecha_produccion);
           updateReg.setInt(4, no_cocinada);
           updateReg.setString(5, estatus_final);
           updateReg.setString(6, operador);
           updateReg.setInt(7, id_reg);
           
           result = updateReg.executeUpdate();
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
   
   public int upPar(String espec, float brix, float ph, float consistencia, String apariencia, float viscosidad, 
           float acidez, String observaciones, String estatus_fq, String estatus_funcional, int id_par){
       int result = 0;
       try {
           updatePar.setString(1, espec);
           updatePar.setFloat(2, brix);
           updatePar.setFloat(3, ph);
           updatePar.setFloat(4, consistencia);
           updatePar.setString(5, apariencia);
           updatePar.setFloat(6, viscosidad);
           updatePar.setFloat(7, acidez);
           updatePar.setString(8, observaciones);
           updatePar.setString(9, estatus_fq);
           updatePar.setString(10, estatus_funcional);
           updatePar.setInt(11, id_par);
           
           result = updatePar.executeUpdate();
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
   
   public int upMic(String coliformes, String cuenta_estandar, String hongos, String levaduras, String estatus_micro, int id_par){
       int result = 0;
       try {
           updateMic.setString(1, coliformes);
           updateMic.setString(2, cuenta_estandar);
           updateMic.setString(3, hongos);
           updateMic.setString(4, levaduras);
           updateMic.setString(5, estatus_micro);
           updateMic.setInt(6, id_par);
           
           result = updateMic.executeUpdate();
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
//-----------------------------------VIEWER     methods
   public String[][] getReg(String fecha){
       String [][] dato = new String [10][100];
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
               dato[8][i]=resultSet.getString(9);
               dato[9][i]=resultSet.getString(10);
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
   
   //-----------------------------------------STADISTICS AND KPI GRAPHER methods
      public float[][] getSample(String fi, String ff, String clv){
       float [][] dato = new float [5][10000];
       ResultSet resultSet = null;
       try {
           sample.setString(1, clv);
           sample.setString(2, fi);
           sample.setString(3, ff);
           resultSet = sample.executeQuery(); 
           int i = 0;
           while (resultSet.next()){
               dato[0][i]=resultSet.getFloat(1);
               dato[1][i]=resultSet.getFloat(2);
               dato[2][i]=resultSet.getFloat(3);
               dato[3][i]=resultSet.getFloat(4);
               dato[4][i]=resultSet.getFloat(5);
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
//--------------obtener el ultimo registro de kpis    
    public String[] gKPI(){
       String [] dato = new String [16];
       ResultSet resultSet1 = null;
       ResultSet resultSet2 = null;
       ResultSet resultSet3 = null;
       try {
           resultSet1 = lstKpi.executeQuery();
           resultSet2 = lstPrm.executeQuery();
           resultSet3 = lstDsv.executeQuery();
           while (resultSet1.next()){
               dato[0]=""+resultSet1.getInt(1);
               dato[1]=""+resultSet1.getDate(2);
               dato[2]=resultSet1.getString(3);
               dato[3]=resultSet1.getString(4);
               dato[4]=""+resultSet1.getDate(5);
               dato[5]=""+resultSet1.getDate(6);
           }
           while (resultSet2.next()){
               dato[6]=""+resultSet2.getFloat(1);
               dato[7]=""+resultSet2.getFloat(2);
               dato[8]=""+resultSet2.getFloat(3);
               dato[9]=""+resultSet2.getFloat(4);
               dato[10]=""+resultSet2.getFloat(5);
           }
           while (resultSet3.next()){
               dato[11]=""+resultSet3.getFloat(1);
               dato[12]=""+resultSet3.getFloat(2);
               dato[13]=""+resultSet3.getFloat(3);
               dato[14]=""+resultSet3.getFloat(4);
               dato[15]=""+resultSet3.getFloat(5);
           }
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return dato;
   }

//--------------------contiene la data de un kpi_id
    public String[] gKPI_2(int id){
       String [] dato = new String [16];
       ResultSet resultSet1 = null;
       try {
           kpiData.setInt(1, id);
           resultSet1 = kpiData.executeQuery();

           while (resultSet1.next()){
               dato[0]=""+resultSet1.getInt(1);
               dato[1]=""+resultSet1.getDate(2);
               dato[2]=resultSet1.getString(3);
               dato[3]=resultSet1.getString(4);
               dato[4]=""+resultSet1.getDate(5);
               dato[5]=""+resultSet1.getDate(6);
               dato[6]=""+resultSet1.getFloat(7);
               dato[7]=""+resultSet1.getFloat(8);
               dato[8]=""+resultSet1.getFloat(9);
               dato[9]=""+resultSet1.getFloat(10);
               dato[10]=""+resultSet1.getFloat(11);
               dato[11]=""+resultSet1.getFloat(12);
               dato[12]=""+resultSet1.getFloat(13);
               dato[13]=""+resultSet1.getFloat(14);
               dato[14]=""+resultSet1.getFloat(15);
               dato[15]=""+resultSet1.getFloat(16);
           }
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return dato;
   }
   
   public int sKPI(String date_kpi, String clave, String description, String fi, String ff, int id_user, int id_avg, int id_desv, int id_graph){
       int result = 0;
       try {
           insertKpi.setString(1, date_kpi);
           insertKpi.setString(2, clave);
           insertKpi.setString(3, description);
           insertKpi.setString(4, fi);
           insertKpi.setString(5, ff);
           insertKpi.setInt(6, id_user);
           insertKpi.setInt(7, id_avg);
           insertKpi.setInt(8, id_desv);
           insertKpi.setInt(9, id_graph);
           
           result = insertKpi.executeUpdate();
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
   
   public int sDSV(float desv_brix, float desv_ph, float desv_consistencia, float desv_viscocidad, 
           float desv_acidez){
       int result = 0;
       try {
           insertDesv.setFloat(1, desv_brix);
           insertDesv.setFloat(2, desv_ph);
           insertDesv.setFloat(3, desv_consistencia);
           insertDesv.setFloat(4, desv_viscocidad);
           insertDesv.setFloat(5, desv_acidez);
           
           result = insertDesv.executeUpdate();
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
   
   public int sPRM(float prm_brix, float prm_ph, float prm_consistencia, float prm_viscocidad, 
           float prm_acidez){
       int result = 0;
       try {
           insertProm.setFloat(1, prm_brix);
           insertProm.setFloat(2, prm_ph);
           insertProm.setFloat(3, prm_consistencia);
           insertProm.setFloat(4, prm_viscocidad);
           insertProm.setFloat(5, prm_acidez);
           
           result = insertProm.executeUpdate();
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
       
   public int sGR(String gbrix, String gph, String gconsistencia, String gviscocidad, 
           String gacidez) throws FileNotFoundException{
       int result = 0;
       try {
           FileInputStream brix = new FileInputStream(gbrix);
           FileInputStream ph = new FileInputStream(gph);
           FileInputStream consistencia = new FileInputStream(gconsistencia);
           FileInputStream viscocidad = new FileInputStream(gviscocidad);
           FileInputStream acidez = new FileInputStream(gacidez);
           
           insertGraf.setBlob(1, brix);
           insertGraf.setBlob(2, ph);
           insertGraf.setBlob(3, consistencia);
           insertGraf.setBlob(4, viscocidad);
           insertGraf.setBlob(5, acidez);
           
           result = insertGraf.executeUpdate();
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return result;
   }
   
   //-----------------------TASK MANAGER methods
   public String gMensage(){
       String dato = "";
       ResultSet resultSet = null;
       try {
           resultSet = getMsg.executeQuery();
           while (resultSet.next()){
               dato += resultSet.getString(1)+"\t";
               dato += resultSet.getString(2)+"\t";
               dato += resultSet.getString(3)+"\n";
           }
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return dato;
   }
   

   public String gAlarmOn(){
       String dato = "";
       ResultSet resultSet = null;
       try {
           resultSet = getAlm.executeQuery();
           while (resultSet.next()){
               dato += "Parametro: "+resultSet.getString(2)+" fuera de limites,\t";
               dato += "Fecha de evento: "+resultSet.getString(3)+",\t";
               dato += "ID: "+resultSet.getString(1)+"\n";
           }
       } catch (SQLException ex) {
           Logger.getLogger(LogConexion.class.getName()).log(Level.SEVERE, null, ex);
           close();
       }
       return dato;
   }
   
   public int sMensage(String msg, int id){
       int result = 0;
       try {
           setMsg.setString(1, msg);
           setMsg.setInt(2, id);
           
           result = setMsg.executeUpdate();
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