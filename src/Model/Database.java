/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import com.mysql.jdbc.Driver;
import java.sql.Connection; import java.sql.DriverManager;
import java.sql.ResultSet; import java.sql.PreparedStatement;
import java.sql.Statement; import java.sql.SQLException;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
/**
 *
 * @author REDHAT
 */
public class Database {
    private static Connection config;
    private Connection CONFIG;
    private static Driver DRIVER_MANAGER;
    private static String PARAMETER_CONN;
    public static String DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASS, STMT, LOG;
    public ResultSet RES;
    public Statement STM;
    public PreparedStatement PREP;
    
    public Database() {
        Database.setDB_HOST("localhost");
        Database.setDB_NAME("rab");
        Database.setDB_USER("root");
        Database.setDB_PASS("");
        Database.setDB_PORT("3306");
    }
    
    public Database(String db_name, String user, String pass) {
        Database.DB_NAME = db_name; Database.DB_USER = user;
        Database.DB_PASS = pass;
    }
    
    public static void setDB_HOST(String DB_HOST) {
        Database.DB_HOST = DB_HOST;
    }

    public static void setDB_PORT(String DB_PORT) {
        Database.DB_PORT = DB_PORT;
    }

    public static void setDB_NAME(String DB_NAME) {
        Database.DB_NAME = DB_NAME;
    }

    public static void setDB_USER(String DB_USER) {
        Database.DB_USER = DB_USER;
    }

    public static void setDB_PASS(String DB_PASS) {
        Database.DB_PASS = DB_PASS;
    }

    public static void setConfig(Connection config) {
        Database.config = config;
    }
    
    public void setConfigs(Connection config) {
        CONFIG = config;
    }
    
    public static void setSTMT(String STMT) {
        Database.STMT = STMT;
    }
    
    public Connection getConfig() {
        return config;
    }
    
    protected String getLog() {
        return LOG;
    }
    protected void setLog(String Description)  {
        Database.LOG = ("\nQuery Performed :"+Description+" =========================================\n"
                + Database.STMT
                + "\n========================================================\n");
    }
    protected void setLog(String Description, String SQL)  {
        Database.LOG = ("\nQuery Performed :"+Description+" =========================================\n"
                + SQL
                + "\n========================================================\n");
    }
    /**
     * Query Select untuk SQL 
     * dengan syarat harus memberikan query pada property STMT
     * e.g. "<i>SELECT * FROM <b>namaTable</b></i>"<br> atau "<i>SELECT * FROM <b>namaTable</b> WHERE <b>kolom</b> = "<u>kondisi</u>"</i>"
     * @return ResultSet executeQuery();
     * @throws SQLException 
     * @see setSTMT();
     */
    public ResultSet getSQL() throws SQLException {
        checkConnection();
        STM = Database.config.createStatement();
        LOG = Database.STMT;
        RES = STM.executeQuery(Database.STMT);
        setLog("Getting Data");
        System.out.println(getLog());
        return RES;
    }
    
    /**
     * Query Select untuk SQL dengan Custom Parameter
     * @param Query String SQL
     * e.g. "<i>SELECT * FROM <b>namaTable</b></i>"<br> atau "<i>SELECT * FROM <b>namaTable</b> WHERE <b>kolom</b> = "<u>kondisi</u>"</i>"
     * @return ResultSet executeQuery();
     * @throws SQLException 
     */ 
    public ResultSet getSQL(String Query) throws SQLException {
        //checkConnection();
        STM = Database.config.createStatement();;
        setLog("Getting Data", Query);
        System.out.println(getLog());
        return (RES = STM.executeQuery(Query));
    }
    
    /**
     * Fungsi untuk Insert/Update data ke DB
     * menggunakan 3 paramater
     * Manipulation Function for Model Database
     * Setting Query terlebih dahulu pada property STMT
     * e.g. "INSERT INTO table VALUES (?, ?, ?, ?, ?, ?)"
     * @param DATA array OBJECT dengan ukuran yang disesuaikan dengan jumlah
     * kolom pada database Sesuaikan urutannya dengan field pada tabel yang dituju
     * @param TABLE String nama table yang dituju
     * @param PRIMARY_KEY kolom primary key pada table yang dituju
     * @return Mengembalikan nilai 1 jika berhasil dan sebaliknya
     * bernilai 0 jika gagal
     * @throws SQLException 
     * @see setSTMT() untuk setting Query
     */
    public int insertSQL(Object[] DATA, String TABLE, String PRIMARY_KEY) throws SQLException {
        String[] SQL = Database.STMT.split("\\s");
        int result = 0;
        // pengecualian table cek
        if( (TABLE.equalsIgnoreCase("riwayatKwitansi"))
            || (SQL[0].equalsIgnoreCase("UPDATE"))
            || (TABLE.equalsIgnoreCase("koordinator"))
            || (TABLE.equalsIgnoreCase("kalkulasi"))
            || (TABLE.equalsIgnoreCase("sph"))
            || (TABLE.equalsIgnoreCase("st")) 
            || (searchIndexDB(DATA[0].toString(),PRIMARY_KEY,TABLE) == -1))  {
            //System.out.println("Data ["+DATA[0]+"] belum ada di DB");
            PREP = Database.config.prepareStatement(STMT);
            String typedata = "";
            int pointer = 0;
            if(SQL[0].equalsIgnoreCase("UPDATE")){
                setLog("Update Data");
                result = 2;
            }
            else {
                setLog("Insert Data");
                result = 1;
            }
            System.out.println(getLog());
            for (Object temp : DATA) {
                typedata = temp.getClass().getSimpleName();
                System.out.println(temp.getClass().getSimpleName()+" : "+DATA[pointer]);
                switch (typedata) {
                    case "String" :
                        PREP.setString(pointer+1, DATA[pointer].toString());
                        break;
                    case "Integer" :
                        PREP.setInt(pointer+1, Integer.parseInt(DATA[pointer].toString()));
                        break;
                    case "Float" :
                        PREP.setFloat(pointer+1, Float.parseFloat(DATA[pointer].toString()));
                        break;
                    case "Double" :
                        PREP.setDouble(pointer+1,Double.parseDouble(DATA[pointer].toString()));
                        break;                    
                }
                pointer++;
            }
            PREP.executeUpdate();
            return result;
        }else{  
            System.out.println("Data ["+DATA[0]+"] Sudah ada di DB\n"
                            + "Silahkan Gunakan fitur Update");
            JOptionPane.showMessageDialog(null, "Ooppss!. Data tersebut sudah ada\n"
                    + "Silahkan gunakan Fitur UPDATE","Warning",1);
            return result;
        }
    }
    
    public int deleteSQL(String KEY) throws SQLException{
        checkConnection();
        PREP = Database.config.prepareStatement(STMT);
        PREP.setString(1, KEY);
        setLog("Delete Data");
        System.out.println(getLog());
        return (PREP.executeUpdate());
    }
    
    public int deleteSQL(Object[] MULTIKEY) throws SQLException{
        checkConnection();
        PREP = Database.config.prepareStatement(STMT);
        int pointer = 0; String typedata = "";
        for (Object temp : MULTIKEY) {
                typedata = temp.getClass().getSimpleName();
                System.out.println(temp.getClass().getSimpleName()+" : "+MULTIKEY[pointer]);
                switch (typedata) {
                    case "String" :
                        PREP.setString(pointer+1, MULTIKEY[pointer].toString());
                        break;
                    case "Integer" :
                        PREP.setInt(pointer+1, Integer.parseInt(MULTIKEY[pointer].toString()));
                        break;
                    case "Float" :
                        PREP.setFloat(pointer+1, Float.parseFloat(MULTIKEY[pointer].toString()));
                        break;
                    case "Double" :
                        PREP.setDouble(pointer+1,Double.parseDouble(MULTIKEY[pointer].toString()));
                        break;                    
                }
                pointer++;
            }
        setLog("Delete Data");
        System.out.println(getLog());
        return (PREP.executeUpdate());
    }
    public int deleteSQL(int KEY) throws SQLException{
        checkConnection();
        PREP = Database.config.prepareStatement(STMT);
        PREP.setInt(1, KEY);
        setLog("Delete Data");
        System.out.println(getLog());
        return (PREP.executeUpdate());
    }
    public int deleteSQL(String KEY, String SQL) throws SQLException{
        checkConnection();
        setSTMT(SQL);
        setLog("Delete Data");
        System.out.println(getLog());
        PREP = Database.config.prepareStatement(STMT);
        PREP.setString(1, KEY);
        return (PREP.executeUpdate());
    }
    
    /**
     * Metode untuk mencari adanya data dalam DB
     * e.g. searchIndexDB(<u>textField.getText(), "kolom", "table"</u>) atau<br>
     * searchIndexDB(<u>txID.getText(), "id", "user"</u>)
     * @param SEARCH data yang ingin dicari
     * @param FIELD kolom field yang dicari
     * @param TABLE table yang dicari
     * @return mengembalikan tipedata Integer, bernilai -1 jika tidak ditemukan,<br>dan akan mengembalikan nilai index jika ditemukan dalam DB;
     * @throws SQLException 
     */ 
    public int searchIndexDB(String SEARCH, String FIELD, String TABLE) throws SQLException {
        int index = -1, i = 0;
        //Database.setSTMT("SELECT "+FIELD+" FROM "+TABLE+" ORDER BY "+FIELD+" ASC");
        RES = getSQL("SELECT "+FIELD+" FROM "+TABLE+" ORDER BY "+FIELD+" ASC");
        while (RES.next()) {
            if(true == RES.getString(1).equalsIgnoreCase(SEARCH)) {                
                index = i;
                break;
            }
            i++;
        }
        setLog("Search Index");
        System.out.println(getLog());
        return index;
    }
    
    public static Connection inisiasiDB() throws SQLException {
        if ((Database.DB_NAME == null) || (Database.DB_USER == null) || (Database.DB_PASS == null) || (Database.DB_PORT == null) || (Database.DB_HOST == null)) {
            JOptionPane.showMessageDialog(null,"Database Belum disetting");
             if (Database.DB_NAME == null) {
                 String DBname = JOptionPane.showInputDialog("Nama Database");
                 Database.DB_NAME = DBname;
             }
             if (Database.DB_USER == null) setDB_USER(JOptionPane.showInputDialog("User"));
             if (Database.DB_PASS == null) Database.DB_PASS = JOptionPane.showInputDialog("Password");
             if (Database.DB_PORT == null) Database.DB_PORT = JOptionPane.showInputDialog("Port");
             if (Database.DB_HOST == null) Database.DB_HOST = JOptionPane.showInputDialog("Host");
             Database.PARAMETER_CONN = "jdbc:mysql://"+Database.DB_HOST+":"+Database.DB_PORT+"/"+Database.DB_NAME;
        }
        try {
            Database.DRIVER_MANAGER = new com.mysql.jdbc.Driver();
            Database.PARAMETER_CONN = "jdbc:mysql://"+Database.DB_HOST+":"+Database.DB_PORT+"/"+Database.DB_NAME;
            DriverManager.registerDriver(DRIVER_MANAGER);
            setConfig(DriverManager.getConnection(Database.PARAMETER_CONN,Database.DB_USER, Database.DB_PASS));
            System.out.println("Koneksi Sukses ========================\n DB_NAME : "+DB_NAME);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,"Driver untuk Koneksi database tidak ada\nSilahkan nyalakan services MySQL pada XAMPP");
        }
        return config;
    }
    
    public Connection inisiasiDBnoStatic() throws SQLException {
        if ((Database.DB_NAME == null) || (Database.DB_USER == null) || (Database.DB_PASS == null) || (Database.DB_PORT == null) || (Database.DB_HOST == null)) {
            JOptionPane.showMessageDialog(null,"Database Belum disetting");
             if (Database.DB_NAME == null) {
                 String DBname = JOptionPane.showInputDialog("Nama Database");
                 Database.DB_NAME = DBname;
             }
             if (Database.DB_USER == null) setDB_USER(JOptionPane.showInputDialog("User"));
             if (Database.DB_PASS == null) Database.DB_PASS = JOptionPane.showInputDialog("Password");
             if (Database.DB_PORT == null) Database.DB_PORT = JOptionPane.showInputDialog("Port");
             if (Database.DB_HOST == null) Database.DB_HOST = JOptionPane.showInputDialog("Host");
             Database.PARAMETER_CONN = "jdbc:mysql://"+Database.DB_HOST+":"+Database.DB_PORT+"/"+Database.DB_NAME;
        }
        try {
            Database.DRIVER_MANAGER = new com.mysql.jdbc.Driver();
            Database.PARAMETER_CONN = "jdbc:mysql://"+Database.DB_HOST+":"+Database.DB_PORT+"/"+Database.DB_NAME;
            DriverManager.registerDriver(DRIVER_MANAGER);
            setConfigs(DriverManager.getConnection(Database.PARAMETER_CONN,Database.DB_USER, Database.DB_PASS));
            System.out.println("Koneksi Sukses ========================\n DB_NAME : "+DB_NAME);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,"Driver untuk Koneksi database tidak ada\nSilahkan nyalakan services MySQL pada XAMPP");
        }
        return CONFIG;
    }
    
    private void checkConnection() throws SQLException{
        if(getConfig() == null) {
            setConfig(inisiasiDB());
        }
    }
    
    public String generateOrderID() {
        try {
            checkConnection();
            String temp;
            RES = getSQL("SELECT kdItems FROM items ORDER BY kdItems ASC");
            LOG = STMT;
            RES.last();
            temp = RES.getString("kdItems").substring(2, 6);
            int increase = Integer.parseInt(temp);
            int digit = String.valueOf(increase).length();
            switch (digit) {
                case 1: {increase++;temp = "000"+increase;}
                    break;
                case 2: {increase++;temp = "00"+increase;}
                    break;
                case 3: {increase++;temp = "0"+increase;}
                    break;
                default: {increase++;temp = ""+increase;}
                    break;
            }
            RES.close();
            return temp;            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }
    /**
     * Function untuk memisahkan String menjadi array
     * dengan tanda pemisah |
     * @param param String yang ingin dipisah
     * @return array berupa string
     * 
     */    
    public String[] serializeString(String param) {
        String data = param;
        String[] output = data.split(Pattern.quote("|"));
        return output;
    }
}
