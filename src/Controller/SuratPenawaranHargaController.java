/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author REDHAT
 */
public class SuratPenawaranHargaController implements Initializable {
    Model.Database DB;
    String SQL,noSurat="";
    public Double PPn=0.0,Profit=0.0,Jasa=0.0,GrandTotal=0.0;
    private Stage subStage;
    private Scene proyek;    
    private MasterProyekController proyekController;
    private MenuLaporanController menuLaporan;
    public MenuLaporanController.Proyek currentProyek; public MenuLaporanController.Konsumen currentKonsumen;
    static String[] angkaTerbilang={"","Satu","Dua","Tiga","Empat",
        "Lima","Enam","Tujuh","Delapan","Sembilan","Sepuluh","Sebelas"};
    static String[] bulanTerbilang={"","JAN","FEB","MAR","APR","MEI"
            ,"JUN","JUL","AUG","SEP","OKT","NOV","DES"};
    @FXML
    private TextField txNoSPH;
    @FXML
    private TextField txKodeProyek;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField txKodeKonsumen;
    @FXML
    private Label lblProyek;
    @FXML
    private Label lblJasa;
    @FXML
    private TextField txPPn;
    @FXML
    private TextField txProfit;
    @FXML
    private TextField txGrandTotal;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnCancel;
    @FXML
    private Label lblKonsumen;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txKodeKonsumen.textProperty().addListener(new ChangeListener<String>(){
           @Override
           public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               SQL = "SELECT * FROM konsumen WHERE kdKonsumen='"+t1+"' ";
               DB.setSTMT(SQL);
               try {
                   java.sql.ResultSet SET = DB.getSQL();
                   while(SET.next()) {
                       currentKonsumen.setKODE(SET.getString(1));
                       currentKonsumen.setNAMA(SET.getString(2));
                       currentKonsumen.setALAMAT(SET.getString(3));
                       currentKonsumen.setNO_TELP(SET.getString(4));
                       lblKonsumen.setText(SET.getString(2));
                   }
               } catch (Exception e) {
               }
           }
        });
        txPPn.textProperty().addListener(new ChangeListener<String>(){
           @Override
           public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if(t1.length() != 0)
                    PPn = Double.parseDouble(t1);
                else
                    PPn = 0.0;
                hitungGrandTotal();
                System.out.println(t1);
           }
        });
        txProfit.textProperty().addListener(new ChangeListener<String>(){
           @Override
           public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if(t1.length() != 0) 
                    Profit = Double.parseDouble(t1);
                else
                    Profit = 0.0;
                hitungGrandTotal();
                System.out.println(t1);
           }
        });
        //txGrandTotal.textProperty().bind(new SimpleStringProperty(String.format("%,.2f", this.GrandTotal)));
    }    
    public void injectController(MenuLaporanController Controller) {
        this.menuLaporan = Controller;
    }
    public SuratPenawaranHargaController(){
        DB = new Model.Database();
    }
        
    public void hitungGrandTotal() {
        this.Jasa = currentProyek.getNILAI();
        this.GrandTotal = Jasa + (Jasa * (Profit / 100.0)) + (Jasa * (PPn /100.0));
        txGrandTotal.setText(String.format("%,.2f", this.GrandTotal));
//        Tooltip tooltip1 = new Tooltip(angkaToTerbilang(Long.parseLong(this.GrandTotal.toString())));
//        txGrandTotal.setTooltip(tooltip1);
        System.out.println(this.GrandTotal);
    }
    @FXML     
    private void printSPH(ActionEvent event) throws IOException {
        String[] hari = currentProyek.getMULAI().split("-");
        Object[] DATA = new Object[9];
        DATA[0] = txNoSPH.getText();
        DATA[1] = currentProyek.getNILAI();
        DATA[2] = this.PPn;
        DATA[3] = this.GrandTotal;
        DATA[4] = currentProyek.getKODE();
        DATA[5] = currentProyek.getNILAI();
        DATA[6] = this.PPn;
        DATA[7] = this.GrandTotal;
        DATA[8] = currentProyek.getKODE();
        File a = new File("misc/logo.jpg");
        String absolute = a.getCanonicalPath();
        String replace = absolute.replace("\\", "\\\\");
        try {
            DB.inisiasiDB();
            SQL ="INSERT INTO sph VALUES(?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "jasa = ?, "
                + "ppn = ?, "
                + "GrandTotal = ?, "
                + "kdProyek = ?; ";
            DB.setSTMT(SQL);
            DB.insertSQL(DATA, "sph", "noSPH");
            noSurat = txNoSPH.getText()+"/"+currentProyek.getKODE()+"/"+hari[2];
            Model.HandlerComponent.Report jReport  = new Model.HandlerComponent.Report();
            jReport.setFileName("SuratPenawaranHarga");
            //pathLogoHeader, pathWatermark, Kegiatan, Lokasi, Kolom Table
            jReport.setParam("noSurat", noSurat);
            jReport.setParam("pathLogoHeader", replace);
            a = new File("misc/logo.png");
            absolute = a.getCanonicalPath();
            replace = absolute.replace("\\", "\\\\");
            jReport.setParam("pathWatermark", replace);
            jReport.setParam("namaKonsumen", currentKonsumen.getNAMA());
            jReport.setParam("alamatKonsumen", currentKonsumen.getALAMAT());
            jReport.setParam("suratMulai", hari[2]+" "+bulanTerbilang[Integer.parseInt(hari[1])]+" "+hari[0]);
            jReport.setParam("nilaiProyek", String.format("%,.2f", currentProyek.getNILAI()));
            long temp;
            temp = Math.round(currentProyek.getNILAI());
            jReport.setParam("terbilang", angkaToTerbilang(temp)+" Rupiah");
            jReport.setParam("durasiHari", durasiHari(currentProyek.getMULAI(), currentProyek.getSELESAI()));
            jReport.setParam("kodeProyek", currentProyek.getKODE());
            jReport.setParam("deskripsiPekerjaan", currentProyek.getDESKRIPSI().substring(0, 50));
            jReport.printReportEmptyDatasource();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearAll(ActionEvent event) {
       currentKonsumen.clear(); currentProyek.clear();
       txKodeProyek.setText(""); txKodeKonsumen.setText("");
       txGrandTotal.setText(""); txPPn.setText(""); txProfit.setText("");
       lblJasa.setText("0.0"); lblProyek.setText(""); lblKonsumen.setText("");
    }

    @FXML
    private void listProyek(ActionEvent event) throws IOException {
        if(proyek == null) {
            FXMLLoader fLoader = new FXMLLoader(getClass().getResource("/View/MasterProyek.fxml"));
            Parent parent = fLoader.load();
            proyekController = fLoader.<MasterProyekController>getController();
            proyekController.injectController(this);
            proyek = new Scene(parent);            
        }
        if(subStage == null) {
            subStage = new Stage();
            subStage.setTitle("RAB | Data - Master Proyek");
            subStage.resizableProperty().setValue(Boolean.FALSE);
            subStage.setScene(proyek);      
            subStage.show(); 
        } else {
            if(subStage.getScene() != proyek)
                subStage.setScene(proyek);
            subStage.toFront();
            subStage.show();
        }
    }
    public void setDataProyek(Object[] DATA) {
        if(currentProyek == null){
            currentProyek = new MenuLaporanController.Proyek(DATA[0].toString(),DATA[1].toString(),Double.parseDouble(DATA[2].toString()),
                    DATA[3].toString(),DATA[4].toString(),DATA[5].toString(),DATA[6].toString());
            this.Jasa = currentProyek.getNILAI();
            
        } else {
            currentProyek.setKODE(DATA[0].toString());
            currentProyek.setDESKRIPSI(DATA[1].toString());
            currentProyek.setNILAI(Double.parseDouble(DATA[2].toString()));
            currentProyek.setMULAI(DATA[3].toString());
            currentProyek.setSELESAI(DATA[4].toString());
            currentProyek.setKODE_KONSUMEN(DATA[5].toString());
            currentProyek.setWILAYAH(DATA[6].toString());
            this.Jasa = currentProyek.getNILAI();
        }
    }
    public void initData() {
        if(currentProyek != null) {
            txKodeProyek.setText(currentProyek.getKODE());
            lblProyek.setText(currentProyek.getDESKRIPSI());
            lblJasa.setText(String.format("%,.2f", currentProyek.getNILAI()));
            txKodeKonsumen.setText(currentProyek.getKODE_KONSUMEN());
            
        }
    }
    public Long durasiHari(String TglMulai, String TglSelesai) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate date1 = LocalDate.parse(TglMulai, dtf);
            LocalDate date2 = LocalDate.parse(TglSelesai, dtf);
            return ChronoUnit.DAYS.between(date1, date2);
            //System.out.println ("Days: " + daysBetween);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }
    public static String angkaToTerbilang(Long angka){
        
        if(angka < 12)
            return angkaTerbilang[angka.intValue()];
        if(angka >=12 && angka <= 19)
            return angkaTerbilang[angka.intValue() % 10] + " Belas";
        if(angka >= 20 && angka <= 99)
            return angkaToTerbilang(angka / 10) + " Puluh " + angkaTerbilang[angka.intValue() % 10];
        if(angka >= 100 && angka <= 199)
            return "Seratus " + angkaToTerbilang(angka % 100);
        if(angka >= 200 && angka <= 999)
            return angkaToTerbilang(angka / 100) + " Ratus " + angkaToTerbilang(angka % 100);
        if(angka >= 1000 && angka <= 1999)
            return "Seribu " + angkaToTerbilang(angka % 1000);
        if(angka >= 2000 && angka <= 999999)
            return angkaToTerbilang(angka / 1000) + " Ribu " + angkaToTerbilang(angka % 1000);
        if(angka >= 1000000 && angka <= 999999999)
            return angkaToTerbilang(angka / 1000000) + " Juta " + angkaToTerbilang(angka % 1000000);
        if(angka >= 1000000000 && angka <= 999999999999L)
            return angkaToTerbilang(angka / 1000000000) + " Milyar " + angkaToTerbilang(angka % 1000000000);
        if(angka >= 1000000000000L && angka <= 999999999999999L)
            return angkaToTerbilang(angka / 1000000000000L) + " Triliun " + angkaToTerbilang(angka % 1000000000000L);
        if(angka >= 1000000000000000L && angka <= 999999999999999999L)
            return angkaToTerbilang(angka / 1000000000000000L) + " Quadrilyun " + angkaToTerbilang(angka % 1000000000000000L);
            return "";
    }
//    public static class Proyek {
//        private SimpleStringProperty KODE, DESKRIPSI, KODE_KONSUMEN, MULAI, SELESAI, WILAYAH;
//        private SimpleDoubleProperty NILAI;
//
//        public Proyek(String KODE, String DESKRIPSI, Double NILAI, String MULAI, String SELESAI, String KODE_KONSUMEN, String WILAYAH) {
//            this.KODE = new SimpleStringProperty(KODE);
//            this.DESKRIPSI = new SimpleStringProperty(DESKRIPSI);
//            this.KODE_KONSUMEN = new SimpleStringProperty(KODE_KONSUMEN);
//            this.MULAI = new SimpleStringProperty(MULAI);
//            this.SELESAI = new SimpleStringProperty(SELESAI);
//            this.WILAYAH = new SimpleStringProperty(WILAYAH);
//            this.NILAI = new SimpleDoubleProperty(NILAI);
//        }
//        public void clear() {
//            this.KODE = null;
//            this.DESKRIPSI = null;
//            this.KODE_KONSUMEN = null;
//            this.MULAI = null;
//            this.SELESAI = null;
//            this.WILAYAH = null;
//            this.NILAI = null;
//                    
//        }
//        public String getKODE() {
//            return KODE.get();
//        }
//        public void setKODE(String param) {
//            this.KODE.set(param);
//        }
//        public String getDESKRIPSI() {
//            return DESKRIPSI.get();
//        }
//        public void setDESKRIPSI(String param) {
//            this.DESKRIPSI.set(param);
//        }
//        public String getKODE_KONSUMEN() {
//            return KODE_KONSUMEN.get();
//        }
//        public void setKODE_KONSUMEN(String param) {
//            this.KODE_KONSUMEN.set(param); 
//        }
//        public String getMULAI() {
//            return MULAI.get();
//        }
//        public void setMULAI(String param) {
//            this.MULAI.set(param);
//        }
//        public String getSELESAI() {
//            return SELESAI.get();
//        }
//        public void setSELESAI(String param) {
//            this.SELESAI.set(param);
//        }
//        public String getWILAYAH() {
//            return WILAYAH.get();
//        }
//        public void setWILAYAH(String param) {
//            this.WILAYAH.set(param);
//        }
//        public Double getNILAI() {
//            return NILAI.get();
//        }
//        public void setNILAI(Double param) {
//            this.NILAI.set(param);
//        }
//        
//    }
//    
//    public static class Konsumen {
//        private SimpleStringProperty KODE, NAMA, ALAMAT, NO_TELP;
//
//        public Konsumen(String KODE, String NAMA, String ALAMAT, String NO_TELP) {
//            this.KODE = new SimpleStringProperty(KODE);
//            this.NAMA = new SimpleStringProperty(NAMA);
//            this.ALAMAT = new SimpleStringProperty(ALAMAT);
//            this.NO_TELP = new SimpleStringProperty(NO_TELP);
//        }
//        public void clear() {
//            this.KODE = null;
//            this.NAMA = null;
//            this.ALAMAT = null;
//            this.NO_TELP = null;
//        }
//                    
//        public String getKODE() {
//            return KODE.get();
//        }
//        public void setKODE(String param) {
//            this.KODE.set(param);
//        }
//        public String getNAMA() {
//            return NAMA.get();
//        }
//        public void setNAMA(String param) {
//            this.NAMA.set(param);
//        }
//        public String getALAMAT() {
//            return ALAMAT.get();
//        }
//        public void setALAMAT(String param) {
//            this.ALAMAT.set(param);
//        }
//        public String getNO_TELP() {
//            return NO_TELP.get();
//        }
//        public void setNO_TELP(String param) {
//            this.NO_TELP.set(param);
//        }
//        
//    }
}
