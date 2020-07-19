/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author REDHAT
 */
public class MenuLaporanController implements Initializable {
    private Stage subStage;
    private Scene suratSPH, suratTugas, proyek, kwitansi;    
    private MasterProyekController proyekController;
    private SuratTugasController tugasController;
    private SuratPenawaranHargaController SPHController;
    public Proyek currentProyek; public Konsumen currentKonsumen;
    String Kegiatan="", Lokasi="", SQL="";
    Model.Database DB = new Model.Database();
    Model.RAB reportRAB;
    @FXML
    private TextArea txDeskripsi;
    @FXML
    private Button btnAHSP;
    @FXML
    private Button btnRAB;
    @FXML
    private Button btnSuratTugas;
    @FXML
    private Button btnSPH;
    @FXML
    private Button btnKwitansi;
    @FXML
    private Button btnBoQ;
    @FXML
    private Button btnPrintAll;
    @FXML
    public TextField txProyekLaporan;
    @FXML
    private Button btnListProyek;
    @FXML
    private TextArea txKonsumen;
    @FXML
    private Label txKodeKonsumen;
    @FXML
    private Button btnRiwayatKwitansi;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txProyekLaporan.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1){
                System.out.println("Cobalah");
            }
        });
    }    
    
    
    public void injectController(MasterProyekController Controller) {
        this.proyekController = Controller;
    }
    @FXML
    private void printAHSP(ActionEvent event) {
        if(reportRAB == null)
            reportRAB = new Model.RAB();
        reportRAB.setKdProyek(this.txProyekLaporan.getText());
        try {
            reportRAB.main(2);
        } catch(java.io.IOException | java.sql.SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void printRAB(ActionEvent event) {
        if(reportRAB == null)
            reportRAB = new Model.RAB();
        reportRAB.setKdProyek(this.txProyekLaporan.getText());
        try {
            reportRAB.main(3);
        } catch(java.io.IOException | java.sql.SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void printSuratTugas(ActionEvent event) throws java.io.IOException{
        File a = new File("misc/logo.jpg");
        String absolute = a.getCanonicalPath();
        String replace = absolute.replace("\\", "\\\\");
        System.out.println(replace);
        if(suratTugas == null) {
            FXMLLoader fLoader = new FXMLLoader(getClass().getResource("/View/SuratTugas.fxml"));
            Parent parent = fLoader.load();
            tugasController = fLoader.<SuratTugasController>getController();
            tugasController.injectController(this);
            tugasController.Kegiatan = this.Kegiatan;
            tugasController.Lokasi = this.Lokasi;
            suratTugas = new Scene(parent);
            if(subStage == null)
                subStage = new Stage();
            subStage.setTitle("RAB | Laporan - Surat Tugas");
            subStage.setScene(suratTugas);
            subStage.resizableProperty().setValue(Boolean.FALSE);
            subStage.setScene(suratTugas);      
            subStage.show();
        }
        if(subStage == null) {            
            subStage = new Stage();
            subStage.setTitle("RAB | Laporan - Surat Tugas");
            subStage.resizableProperty().setValue(Boolean.FALSE);
            subStage.setScene(suratTugas);      
            subStage.show(); 
        } else {
            if(subStage.getScene() != suratTugas)
                subStage.setScene(suratTugas);
            subStage.toFront();
            subStage.show();
        }
    }

    @FXML
    private void printSPH(ActionEvent event) throws IOException {
        if(SPHController != null) {
            SPHController.currentKonsumen = this.currentKonsumen;
            SPHController.currentProyek = this.currentProyek;
            SPHController.initData();
        }
        if(suratSPH == null) {
            FXMLLoader fLoader = new FXMLLoader(getClass().getResource("/View/SuratPenawaranHarga.fxml"));
            Parent parent = fLoader.load();
            SPHController = fLoader.<SuratPenawaranHargaController>getController();
            SPHController.injectController(this);
            SPHController.currentKonsumen = this.currentKonsumen;
            SPHController.currentProyek = this.currentProyek;
            SPHController.initData();
            suratSPH = new Scene(parent);
            if(subStage == null)
                subStage = new Stage();
            subStage.setTitle("RAB | Laporan - Surat Penawaran Harga");
            subStage.setScene(suratSPH);
            subStage.resizableProperty().setValue(Boolean.FALSE);
            subStage.setScene(suratSPH);      
            subStage.show();
        }
        if(subStage == null) {            
            subStage = new Stage();
            subStage.setTitle("RAB | Laporan - Surat Penawaran Harga");
            subStage.resizableProperty().setValue(Boolean.FALSE);
            subStage.setScene(suratSPH);      
            subStage.show(); 
        } else {
            SPHController.currentKonsumen = this.currentKonsumen;
            SPHController.currentProyek = this.currentProyek;
            if(subStage.getScene() != suratSPH)
                subStage.setScene(suratSPH);
            subStage.toFront();
            subStage.show();
        }
    }

    @FXML
    private void printKwitansi(ActionEvent event) throws IOException {
        if(kwitansi == null) {
            FXMLLoader fLoader = new FXMLLoader(getClass().getResource("/View/Kwitansi.fxml"));
            Parent parent = fLoader.load();
            kwitansi = new Scene(parent);
            if(subStage == null)
                subStage = new Stage();
            subStage.setTitle("RAB | Laporan - Kwitansi");
            subStage.setScene(kwitansi);
            subStage.resizableProperty().setValue(Boolean.FALSE);
            subStage.setScene(kwitansi);      
            subStage.show();
        }
        if(subStage == null) {            
            subStage = new Stage();
            subStage.setTitle("RAB | Laporan - Kwitansi");
            subStage.resizableProperty().setValue(Boolean.FALSE);
            subStage.setScene(kwitansi);      
            subStage.show(); 
        } else {
            if(subStage.getScene() != kwitansi)
                subStage.setScene(kwitansi);
            subStage.toFront();
            subStage.show();
        }
    }

    @FXML
    private void printBoQ(ActionEvent event) {
        if(reportRAB == null)
            reportRAB = new Model.RAB();
        reportRAB.setKdProyek(this.txProyekLaporan.getText());
        try {
            reportRAB.main(1);
        } catch(java.io.IOException | java.sql.SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void printAll(ActionEvent event) {
        if(reportRAB == null)
            reportRAB = new Model.RAB();
        reportRAB.setKdProyek(this.txProyekLaporan.getText());
        try {
            reportRAB.main(0);
        } catch(java.io.IOException | java.sql.SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void changeProyek(ActionEvent event) throws IOException {
        if(proyek == null) {
            FXMLLoader fLoader = new FXMLLoader(getClass().getResource("/View/MasterProyek.fxml"));
            Parent parent = fLoader.load();
            proyekController = fLoader.<MasterProyekController>getController();
            proyekController.injectController(this);
            proyek = new Scene(parent);            
        }
        if(subStage == null) {
            subStage = new Stage();
            subStage.setTitle("RAB | Kalkulasi - Detail AHSP");
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
    public void setProyek(ObservableList<Object> DATA) {
        if(currentProyek == null)
            currentProyek = new MenuLaporanController.Proyek(DATA.get(0).toString(),DATA.get(1).toString(),Double.parseDouble(DATA.get(2).toString()),
                DATA.get(3).toString(),DATA.get(4).toString(),DATA.get(5).toString(),DATA.get(6).toString());
        else {
            currentProyek.setKODE(DATA.get(0).toString());
            currentProyek.setDESKRIPSI(DATA.get(1).toString());
            currentProyek.setNILAI(Double.parseDouble(DATA.get(2).toString()));
            currentProyek.setMULAI(DATA.get(3).toString());
            currentProyek.setSELESAI(DATA.get(4).toString());
            currentProyek.setKODE_KONSUMEN(DATA.get(5).toString());
            currentProyek.setWILAYAH(DATA.get(6).toString());
        }
        this.txProyekLaporan.setText(DATA.get(0).toString());
        this.txDeskripsi.setText(DATA.get(1).toString().replace("<br>", " "));
        this.Kegiatan = this.txDeskripsi.getText();
        this.Lokasi = DATA.get(6).toString();
        try{
            DB.inisiasiDB();
            DB.setSTMT("SELECT nmKonsumen FROM konsumen WHERE kdKonsumen='"+DATA.get(5).toString()+"'");
            java.sql.ResultSet SET = DB.getSQL();
            SET.next();
            this.txKonsumen.setText(SET.getString(1));
        } catch (java.sql.SQLException ex) {
            
        }
    }
    public void setKonsumen(String KODE){
        SQL = "SELECT * FROM konsumen WHERE kdKonsumen='"+KODE+"'";
        try {
            DB.inisiasiDB();
            DB.setSTMT(SQL);
            java.sql.ResultSet SET = DB.getSQL();
            while(SET.next()) {
                if(currentKonsumen == null)
                    currentKonsumen = new Konsumen(SET.getString(1),SET.getString(2),SET.getString(3),SET.getString(4));
                else {
                    currentKonsumen.setKODE(SET.getString(1));
                    currentKonsumen.setNAMA(SET.getString(2));
                    currentKonsumen.setALAMAT(SET.getString(3));
                    currentKonsumen.setNO_TELP(SET.getString(4));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void printRiwayatKwitansi(ActionEvent event) throws IOException {
        Model.HandlerComponent.Report jReport = new Model.HandlerComponent.Report();
        File a = new File("misc/logo.jpg");
        String absolute = a.getCanonicalPath();
        String replace = absolute.replace("\\", "\\\\");
        jReport.setFileName("HeadRiwayatKwitansi");
        jReport.setParam("pathLogoHeader",replace);
        a = new File("misc/logo.png"); absolute = a.getCanonicalPath(); replace = absolute.replace("\\", "\\\\");
        jReport.setParam("pathWatermark", replace);
        a = new File("src/View/RiwayatKwitansi.jasper"); absolute = a.getCanonicalPath(); replace = absolute.replace("\\", "\\\\");
        jReport.setParam("pathSubReport", replace);
        jReport.printReport();
    }
    
    public static class Proyek {
        private SimpleStringProperty KODE, DESKRIPSI, KODE_KONSUMEN, MULAI, SELESAI, WILAYAH;
        private SimpleDoubleProperty NILAI;

        public Proyek(String KODE, String DESKRIPSI, Double NILAI, String MULAI, String SELESAI, String KODE_KONSUMEN, String WILAYAH) {
            this.KODE = new SimpleStringProperty(KODE);
            this.DESKRIPSI = new SimpleStringProperty(DESKRIPSI);
            this.KODE_KONSUMEN = new SimpleStringProperty(KODE_KONSUMEN);
            this.MULAI = new SimpleStringProperty(MULAI);
            this.SELESAI = new SimpleStringProperty(SELESAI);
            this.WILAYAH = new SimpleStringProperty(WILAYAH);
            this.NILAI = new SimpleDoubleProperty(NILAI);

        }
        public void clear() {
            this.KODE = null;
            this.DESKRIPSI = null;
            this.KODE_KONSUMEN = null;
            this.MULAI = null;
            this.SELESAI = null;
            this.WILAYAH = null;
            this.NILAI = null;
                    
        }
        public String getKODE() {
            return KODE.get();
        }
        public void setKODE(String param) {
            this.KODE.set(param);
        }
        public String getDESKRIPSI() {
            return DESKRIPSI.get();
        }
        public void setDESKRIPSI(String param) {
            this.DESKRIPSI.set(param);
        }
        public String getKODE_KONSUMEN() {
            return KODE_KONSUMEN.get();
        }
        public void setKODE_KONSUMEN(String param) {
            this.KODE_KONSUMEN.set(param); 
        }
        public String getMULAI() {
            return MULAI.get();
        }
        public void setMULAI(String param) {
            this.MULAI.set(param);
        }
        public String getSELESAI() {
            return SELESAI.get();
        }
        public void setSELESAI(String param) {
            this.SELESAI.set(param);
        }
        public String getWILAYAH() {
            return WILAYAH.get();
        }
        public void setWILAYAH(String param) {
            this.WILAYAH.set(param);
        }
        public Double getNILAI() {
            return NILAI.get();
        }
        public void setNILAI(Double param) {
            this.NILAI.set(param);
        }
        
    }
    
    public static class Konsumen {
        private SimpleStringProperty KODE, NAMA, ALAMAT, NO_TELP;

        public Konsumen(String KODE, String NAMA, String ALAMAT, String NO_TELP) {
            this.KODE = new SimpleStringProperty(KODE);
            this.NAMA = new SimpleStringProperty(NAMA);
            this.ALAMAT = new SimpleStringProperty(ALAMAT);
            this.NO_TELP = new SimpleStringProperty(NO_TELP);
        }
        public void clear() {
            this.KODE = null;
            this.NAMA = null;
            this.ALAMAT = null;
            this.NO_TELP = null;
        }
                    
        public String getKODE() {
            return KODE.get();
        }
        public void setKODE(String param) {
            this.KODE.set(param);
        }
        public String getNAMA() {
            return NAMA.get();
        }
        public void setNAMA(String param) {
            this.NAMA.set(param);
        }
        public String getALAMAT() {
            return ALAMAT.get();
        }
        public void setALAMAT(String param) {
            this.ALAMAT.set(param);
        }
        public String getNO_TELP() {
            return NO_TELP.get();
        }
        public void setNO_TELP(String param) {
            this.NO_TELP.set(param);
        }
        
    }
}
