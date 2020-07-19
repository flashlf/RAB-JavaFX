/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author REDHAT
 */
public class ThumbnailProyekController implements Initializable {
    public MenuLaporanController.Proyek Proyek; public MenuLaporanController.Konsumen Konsumen;
    static String[] angkaTerbilang={"","Satu","Dua","Tiga","Empat",
        "Lima","Enam","Tujuh","Delapan","Sembilan","Sepuluh","Sebelas"};
    static String[] bulanTerbilang={"","JAN","FEB","MAR","APR","MEI"
            ,"JUN","JUL","AUG","SEP","OKT","NOV","DES"};
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
    public static String angkaShort(Long angka){
        if(angka >= 1 && angka <= 999)
            return angka+"";
        if(angka >= 1000 && angka <= 999999)
            return (angka / 1000)+" K";
        if(angka >= 1000000 && angka <= 999999999)
            return (angka / 1000000)+" Jt";
        if(angka >= 1000000000 && angka <= 999999999999L)
            return (angka / 1000000000)+" Mil";
        if(angka >= 1000000000000L && angka <= 999999999999999L)
            return (angka / 1000000000000L)+" T";
        if(angka >= 1000000000000000L && angka <= 999999999999999999L)
            return (angka / 1000000000000000L)+" B";
            return "";
    }
    private long nilai=0;
    private String hStart, bStart, tStart, hEnd, bEnd, tEnd, kodeProyek, detailProyek;
    
    @FXML
    private HBox DASHBOARD_HEADER2;
    @FXML
    private Label lblKodeProyek;
    @FXML
    private Label lblDetailProyek;
    @FXML
    private Label hariMulai;
    @FXML
    private Label bulanMulai;
    @FXML
    private Label tahunMulai;
    @FXML
    private Label hariSelesai;
    @FXML
    private Label bulanSelesai;
    @FXML
    private Label tahunSelesai;
    @FXML
    private Label lblStatusProyek;
    @FXML
    private Button btnEditProyek;
    @FXML
    private Button btnSelesaiProyek;
    @FXML
    private Label lblCostProyek;
    @FXML
    private Label lblTerbilang;
    @FXML
    private Button btnPinProyek;
    
    private HeaderProyekController hProyek;
    private DashboardController Dashboard;
    /**
     * Initializes the controller class.
     */
    
    
    public void injectController(HeaderProyekController Controller) {
        this.hProyek = Controller;
    }
    public void injectController(DashboardController Controller) {
        this.Dashboard = Controller;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void setData(Object[] Data) {
        setKodeProyek(Data[0].toString());
        lblKodeProyek.setText(getKodeProyek());
        
        setDetailProyek(Data[1].toString());
        lblDetailProyek.setText(getDetailProyek());
        
        String[] mulai = Data[3].toString().split("-");
        settStart(mulai[0]); sethStart(mulai[2]); setbStart(bulanTerbilang[Integer.parseInt(mulai[1])]);
        tahunMulai.setText(gettStart());
        bulanMulai.setText(getbStart());
        hariMulai.setText(gethStart());
        
        String[] selesai = Data[4].toString().split("-");
        settEnd(selesai[0]); sethEnd(selesai[2]); setbEnd(bulanTerbilang[Integer.parseInt(selesai[1])]);
        tahunSelesai.setText(gettEnd());
        bulanSelesai.setText(getbEnd());
        hariSelesai.setText(gethEnd());
        
        Long temp = (Long) Data[2]; setNilai(temp);
        lblCostProyek.setText(angkaShort(getNilai()));
        lblTerbilang.setText(angkaToTerbilang(getNilai())+" Rupiah");
    }

    public long getNilai() {
        return nilai;
    }

    public void setNilai(long nilai) {
        this.nilai = nilai;
    }

    public String gethStart() {
        return hStart;
    }

    public void sethStart(String hStart) {
        this.hStart = hStart;
    }

    public String getbStart() {
        return bStart;
    }

    public void setbStart(String bStart) {
        this.bStart = bStart;
    }

    public String gettStart() {
        return tStart;
    }

    public void settStart(String tStart) {
        this.tStart = tStart;
    }

    public String gethEnd() {
        return hEnd;
    }

    public void sethEnd(String hEnd) {
        this.hEnd = hEnd;
    }

    public String getbEnd() {
        return bEnd;
    }

    public void setbEnd(String bEnd) {
        this.bEnd = bEnd;
    }

    public String gettEnd() {
        return tEnd;
    }

    public void settEnd(String tEnd) {
        this.tEnd = tEnd;
    }

    public String getKodeProyek() {
        return kodeProyek;
    }

    public void setKodeProyek(String kodeProyek) {
        this.kodeProyek = kodeProyek;
    }

    public String getDetailProyek() {
        return detailProyek;
    }

    public void setDetailProyek(String detailProyek) {
        this.detailProyek = detailProyek;
    }

    @FXML
    private void proyekSelesai(ActionEvent event) {
        lblStatusProyek.setText("SELESAI");
    }

    @FXML
    private void fokusProyek(ActionEvent event) {
        Object[] dataFokus = new Object[3];
        dataFokus[0] = lblKodeProyek.getText();
        dataFokus[1] = lblDetailProyek.getText();
        dataFokus[2] = nilai;
        hProyek.setFokusProyek(dataFokus);
        Dashboard.fokusProyek[0] = dataFokus[0];
        Dashboard.fokusProyek[1] = dataFokus[1];
        Dashboard.fokusProyek[2] = dataFokus[2];
        Dashboard.currentProyek = this.Proyek;
        Dashboard.currentKonsumen = this.Konsumen;
    }
    public void setPOJOProyek(Object[] DATA) {
        this.Proyek = new MenuLaporanController.Proyek(DATA[0].toString(), DATA[1].toString(),
        Double.parseDouble(DATA[2].toString()), DATA[3].toString(), DATA[4].toString(),
        DATA[5].toString(), DATA[6].toString());
    }
    public void setPOJOKonsumen(Object[] DATA) {
        this.Konsumen = new MenuLaporanController.Konsumen(DATA[0].toString(), 
            DATA[1].toString(), DATA[2].toString(), DATA[3].toString());
    }
}
