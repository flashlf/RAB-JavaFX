package Model;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
/**
 * Class untuk membantu konfigurasi setiap COMPONENT JavaFX
 * yang terdapat pada java dengan koneksi Database.
 * @author REDHAT
 * @version 0.1a
 */
public class HandlerComponent {
    protected Connection H_CONN;
    protected ResultSet H_RESULTSET;
    // Properti yang dibutuhkan untuk Handler Tabel
    public String[] COLUMN_TYPE, COLUMN_NAME;
    public Object[] OBJ_DATA;
    protected File[] REPORT_JASPER = new File[7];
    protected String[] 
    FILE_PATH = {"/View/SuratPenawaranHarga",
        "/View/SuratTugas",
        "/View/Kwitansi",
        "/View/HeadRiwayatKwitansi",
        "/View/RiwayatKwitansi",
        "/View/REPORT_Supplier",
        "/View/SubSTRUCK"};
    /**
     * <b>Constructor</b> untuk kelas <i>HandlerComponent</i>, akan memanggil metode untuk 
     * koneksi driver database pada kelas database, dengan menyimpan 
     * koneksi pada variable <i><b>H_CONN</b></i>
     * @throws SQLException 
     */
    public HandlerComponent() throws SQLException {
        H_CONN = new Database().inisiasiDB();
    }
    
    public void initiateResourceFile() {
        for (int x=0; x<5; x++) {
            copyFile(getClass().getResourceAsStream(FILE_PATH[x]+".jrxml"), "src"+FILE_PATH[x]+".jrxml");
            copyFile(getClass().getResourceAsStream(FILE_PATH[x]+".jasper"), "src"+FILE_PATH[x]+".jasper");
        }
    }
    
    public static boolean copyFile(InputStream SOURCE, String DESTINATION) {
        boolean success = false;
        try {
            //PrintStream fileErr = new PrintStream("./InitiateFileErrorLog.txt");
            //System.setErr(fileErr);
            System.out.println("Copying ->" + SOURCE + "\n\tto ->" + DESTINATION);
            File fl = new File(DESTINATION);
            fl.getParentFile().mkdirs();
            Files.copy(SOURCE, Paths.get(DESTINATION), StandardCopyOption.REPLACE_EXISTING);
            success = true;
        } catch (Exception ex) {
            System.err.println(ex);
            JOptionPane.showMessageDialog(null, ex);
            System.setErr(System.err);
            ex.printStackTrace();
        }   
        return success;
    }
    
    public TableView iniTable(TableView TABLE, String SQL, Database DBClass) {
        ObservableList<ObservableList> DATA = FXCollections.observableArrayList();
        try 
        {
            //H_CONN = new DBClass().inisiasiDB();
            H_RESULTSET = DBClass.getSQL(SQL);
            int count = H_RESULTSET.getMetaData().getColumnCount();
            COLUMN_NAME = new String[count];
            COLUMN_TYPE = new String[count];
            OBJ_DATA = new Object[count];
            
            /**
             * ======================================
             * Pembuatan Kolom tabel secara dinamis *
             * ======================================
             */
            for(int x=0; x<count; x++) {
                final int j = x;
                // pengecekan nama-kolom pada database untuk digenerate pada 
                // tabel yang dituju serta pengecekan tipedata pada tiap kolom
                // yang kemudian akan disimpan pada variable String array
                // COLUMN_NAME[] dan COLUMN_TYPE
                COLUMN_NAME[x] = H_RESULTSET.getMetaData().getColumnName(x+1);
                COLUMN_TYPE[x] = H_RESULTSET.getMetaData().getColumnTypeName(x+1);
                System.out.print(COLUMN_TYPE[x]);
                
                // pembuatan kolom tabel berdasarkan COLUMN_NAME
                // yang telah dibuat
                TableColumn col = new TableColumn(COLUMN_NAME[x]);
                
                // this is where the NullPointerException are triggered
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        //System.out.println(param.getValue());
                        Object value = param.getValue().get(j) ;
                        //return new SimpleStringProperty(param == null ? "" : param.getValue().get(j).toString());
                        return new SimpleStringProperty(value == null ? "-" : value.toString());
                    }
                });
                
                // Menambahkan kolom yang sudah di generate sebelumnya
                // ke dalam tabel tabel tujuan
                TABLE.getColumns().addAll(col);
                System.out.println(" | Column [" + x + "] "+col);
            }
            
            /** 
             * ============================
             * Penambahan Data pada tabel *
             * ============================
             */
            while(H_RESULTSET.next()) {
                // pembuatan tipe data ObservableList untuk digunakan 
                // dalam menampung data yang akan dimasukan dalam tabel perbarisnya
                ObservableList<Object> row = FXCollections.observableArrayList();
                for (int x = 1; x <= count; x++) {
                    // pengecekan data pada setiap kolom yang ada di database
                    // pada baris yang sama untuk disesuaikan tipedata yg nantinya
                    // akan disimpan dalam tipedata Object
                    switch(COLUMN_TYPE[x-1]) {
                        case "INT" :
                            OBJ_DATA[x-1] = H_RESULTSET.getInt(x);
                            break;
                        case "MEDIUMINT UNSIGNED" :
                            OBJ_DATA[x-1] = H_RESULTSET.getInt(x);
                            break;
                        case "DOUBLE" :
                            OBJ_DATA[x-1] = H_RESULTSET.getDouble(x);
                            break;
                        case "FLOAT" :
                            OBJ_DATA[x-1] = H_RESULTSET.getFloat(x);
                            break;
                        case "VARCHAR" :
                            OBJ_DATA[x-1] = H_RESULTSET.getString(x);
                            break;
                        case "CHAR" :
                            OBJ_DATA[x-1] = H_RESULTSET.getString(x);
                            break;
                        case "TEXT" :
                            OBJ_DATA[x-1] = H_RESULTSET.getString(x);
                            break;
                        case "DATE" :
                            OBJ_DATA[x-1] = H_RESULTSET.getString(x);
                            break;                        
                        default :
                            OBJ_DATA[x-1] = "Unkown Datatype (HandlerComponent.Java)";
                            break;
                    }
                    //
                    row.add(OBJ_DATA[x-1]);
                }
                System.out.println("Row [1] added " + row);
                DATA.add(row);
            }
            
            TABLE.setItems(DATA);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error on Building Data");
        }
        return TABLE;
    }
    
    public static class Report {
        private static String fileName;
        public static Map param;
        private Locale locale;
        private Connection conn;
        Database DB;
        public Report() {
            fileName = "";
            param = new HashMap();
            // Setting bahasa Lokal ke Indonesia
            locale = new Locale("id","ID");
            param.put(JRParameter.REPORT_LOCALE, locale);
        }
        // Getter Setter Report
        public static void setFileName(String aFileName) {
            fileName = aFileName;
        }
        
        public static void setParam(String paramName, String paramValue) {
            param.put(paramName, paramValue);
        }
        
        public static void setParam(String paramName, Integer paramValue) {
            param.put(paramName, paramValue);
        }
        
        public static void setParam(String paramName, Double paramValue) {
            param.put(paramName, paramValue);
        }
        
        public static void setParam(String paramName, Float paramValue) {
            param.put(paramName, paramValue);
        }
        
        public static void setParam(String paramName, Long paramValue) {
            param.put(paramName, paramValue);
        }
        
        public static void setParam(String paramName, Date paramValue) {
            param.put(paramName, paramValue);
        }
        
        //Cetak Report 
        public void printReport() {
            String fName = "src/View/"+fileName+".jrxml";
            String fFill = "src/View/"+fileName+".jasper";
            try {
                DB = new Database();
                conn = DB.inisiasiDBnoStatic();
                JasperCompileManager.compileReport(fName);
                JasperFillManager.fillReport(fFill, param, conn);
                JasperPrint jp = JasperFillManager.fillReport(fFill, param, conn);
                JasperViewer.viewReport(jp, false);
            } catch (SQLException | JRException ex) {
                ex.printStackTrace();
            }
        }
        // Cetak report tanpa Datasource
        public void printReportEmptyDatasource() {
            String fName = "src/View/"+fileName+".jrxml";
            String fFill = "src/View/"+fileName+".jasper";
            try {
                JasperCompileManager.compileReport(fName);
                JasperPrint jp = JasperFillManager.fillReport(fFill, param, new JREmptyDataSource(1));
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                ex.printStackTrace();
            }
        }
        // Cetak report dengan datasource dari Table
        public void printReportDataTable(DefaultTableModel TABLE) {
            String fName = "src/View/"+fileName+".jrxml";
            String fFill = "src/View/"+fileName+".jasper";
            try {
                JasperCompileManager.compileReport(fName);
                JasperPrint jp = JasperFillManager.fillReport(fFill, param, new JRTableModelDataSource(TABLE));
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                ex.printStackTrace();
            }
        }
        
    }
}
