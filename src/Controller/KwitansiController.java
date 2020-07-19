/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author helos
 */
public class KwitansiController implements Initializable {
    Double sisaBayar, jumlahBayar, bayar, sisaBayarKwitansi;
    boolean newKwitansi = false;
    String tglSekarang="", tglTerbilang="", SQL="", noKwitansi="";
    java.util.Date sekarang = new java.util.Date();
    Model.Database DB = new Model.Database();
    public ObservableList<SPHData> sph = FXCollections.observableArrayList();
    public ObservableList<SPH> coba = FXCollections.observableArrayList();
    public MenuLaporanController.Proyek currentProyek; public MenuLaporanController.Konsumen currentKonsumen;
    static String[] angkaTerbilang={"","Satu","Dua","Tiga","Empat",
        "Lima","Enam","Tujuh","Delapan","Sembilan","Sepuluh","Sebelas"};
    static String[] bulanTerbilang={"","Januari","Februari","Maret","April","Mei"
            ,"Juni","Juli","Agustus","September","Oktober","November","Desember"};
    public static String angkaShort(Long angka){
        if(angka >= 1 && angka <= 999)
            return angka+"";
        if(angka >= 1000 && angka <= 999999)
            return (angka / 1000)+" K";
        if(angka >= 1000000 && angka <= 999999999)
            return (angka / 1000000)+" Jt";
        if(angka >= 1000000000 && angka <= 999999999999L)
            return (angka / 1000000000)+" M";
        if(angka >= 1000000000000L && angka <= 999999999999999L)
            return (angka / 1000000000000L)+" T";
        if(angka >= 1000000000000000L && angka <= 999999999999999999L)
            return (angka / 1000000000000000L)+" B";
            return "";
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
    @FXML
    private TextField txNoKwitansi;
    @FXML
    private TextField txBayar;
    @FXML
    private Label lblJumlahBayar;
    @FXML
    private Label lblSisaBayar;
    @FXML
    private Label lblProyek;
    @FXML
    private Label lblKonsumen;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnCancel;
    @FXML
    private ComboBox<SPH> cbSPH;
    @FXML
    private TextField txMenerima;
    @FXML
    private TextField txMenyerahkan;
    @FXML
    private Label lblSisaBayar1;
    @FXML
    private Label lblSisaBayar11;
    @FXML
    private Label lblSisaBayarKwitansi;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        tglSekarang = (sekarang.getYear()+1900)+"-"+(sekarang.getMonth()+1)+"-"+sekarang.getDate();
        tglTerbilang = sekarang.getDate()+" "+bulanTerbilang[sekarang.getMonth()+1]+" "+(sekarang.getYear()+1900);
        try {
            DB.setSTMT("SELECT * FROM sph");
            java.sql.ResultSet SET = DB.getSQL();
            int x = 0;
            while(SET.next()) {
                sph.add(new SPHData(SET.getString(1), SET.getDouble(2),
                SET.getInt(3), SET.getDouble(4), SET.getString(5)));
                coba.add(new SPH(SET.getString(1), x));
                x++;
            }
//            cbSPH.setButtonCtCell<SPH> call(ListView<SPH> p) {
//                    return new SPHListCell();
//                }
//            });
            cbSPH.setItems(coba);
            cbSPH.setConverter(new StringConverter<SPH>(){
                @Override
                public String toString(SPH Object) {
                    return Object.getDISPLAY();
                }
                @Override
                public SPH fromString(String string) {
                    return null;
                }
            });
            //cbSPH.getSelectionModel().selectFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        txBayar.textProperty().addListener(new ChangeListener<String>(){
           @Override
           public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if(t1.length() != 0) 
                    bayar = Double.parseDouble(t1);
                else
                    bayar = 0.0;
                hitung();
                System.out.println(t1);
           }
        });
        txNoKwitansi.textProperty().addListener(new ChangeListener<String>(){
           @Override
           public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                try {
                    DB.setSTMT("SELECT * FROM kwitansi WHERE noKwitansi='"+t1+"'");
                    java.sql.ResultSet SET = DB.getSQL();
                    if(SET.first()) {
                        newKwitansi = false;
                        lblSisaBayarKwitansi.setText(String.format("%,.2f",SET.getDouble("sisaBayar")));
                        sisaBayarKwitansi = SET.getDouble("sisaBayar");
                    } else {
                        newKwitansi = true;
                        lblSisaBayarKwitansi.setText(String.format("%,.2f", sph.get(cbSPH.getValue().getVALUE()).getGRANDTOTAL()));
                    }
                } catch (java.sql.SQLException ex) {
                    ex.printStackTrace();
                }
           }
        });
    }    

    public void hitung() {
        if (newKwitansi == true) {
            this.jumlahBayar = sph.get(cbSPH.getSelectionModel().getSelectedIndex()).getGRANDTOTAL();
            this.sisaBayar = jumlahBayar - bayar;
        } else {
            this.sisaBayar = sisaBayarKwitansi - bayar;
        }
        lblSisaBayar.setText(String.format("%,.2f", this.sisaBayar));
    }
    
    @FXML
    private void printKwitansi(ActionEvent event) {
        SQL = "SELECT * FROM kwitansi WHERE noKwitansi='"+txNoKwitansi+"'";
        DB.setSTMT(SQL); java.sql.ResultSet SET;
        Long temp = Long.parseLong(txBayar.getText());
        Object[] DATA1 = new Object[6];
        Object[] DATA2 = new Object[5];
        try {
            DB.inisiasiDB();
            SET = DB.getSQL();
            if(SET.first() == false) {
                // data untuk kwitansi
                DATA1[0] = txNoKwitansi.getText();
                DATA1[1] = sph.get(cbSPH.getValue().getVALUE()).getGRANDTOTAL();
                DATA1[2] = sph.get(cbSPH.getValue().getVALUE()).getGRANDTOTAL();
                DATA1[3] = currentProyek.getKODE();
                DATA1[4] = currentProyek.getKODE_KONSUMEN();
                DATA1[5] = sph.get(cbSPH.getSelectionModel().getSelectedIndex()).getKODE();
                SQL = "INSERT INTO kwitansi VALUES(?, ?, ?, ?, ?, ?)";
                DB.setSTMT(SQL);
                DB.insertSQL(DATA1, "kwitansi", "noKwitansi");
                
                // data untuk riwayat transaksi kwitansi
                DATA2[0] = tglSekarang;
                DATA2[1] = txNoKwitansi.getText();
                DATA2[2] = bayar;
                DATA2[3] = txMenyerahkan.getText();
                DATA2[4] = txMenerima.getText();
                SQL = "INSERT INTO riwayatKwitansi VALUES(?, ?, ?, ?, ?)";
                DB.setSTMT(SQL);
                DB.insertSQL(DATA2, "riwayatKwitansi", "tglKwitansi");
            } else {
                // data untuk riwayat transaksi kwitansi
                DATA2[0] = tglSekarang;
                DATA2[1] = txNoKwitansi.getText();
                DATA2[2] = bayar;
                DATA2[3] = txMenyerahkan.getText();
                DATA2[4] = txMenerima.getText();
                SQL = "INSERT INTO riwayatKwitansi VALUES(?, ?, ?, ?, ?)";
                DB.setSTMT(SQL);
                DB.insertSQL(DATA2, "riwayatKwitansi", "tglKwitansi");
            }
            noKwitansi = txNoKwitansi.getText().substring(0, 2)+"/"+currentProyek.getKODE()+"/"+
                sph.get(cbSPH.getValue().getVALUE()).getKODE()+"/"+txNoKwitansi.getText().substring(2);
            Model.HandlerComponent.Report jReport = new Model.HandlerComponent.Report();
            jReport.setFileName("Kwitansi");
            File a = new File("misc/logo.jpg"); String absolute = a.getCanonicalPath(); String replace = absolute.replace("\\", "\\\\");
            jReport.setParam("pathLogoHeader", replace);
            a = new File("misc/logo.png"); absolute = a.getCanonicalPath(); replace = absolute.replace("\\", "\\\\");
            jReport.setParam("pathWatermark", replace);
            jReport.setParam("noKwitansi", noKwitansi);
            jReport.setParam("menerima",txMenerima.getText());
            jReport.setParam("menyerahkan",txMenyerahkan.getText());
            jReport.setParam("konsumen", currentKonsumen.getNAMA());
            jReport.setParam("deskripsiPembayaran", currentProyek.getDESKRIPSI());
            jReport.setParam("shortTotal",angkaShort(temp));
            jReport.setParam("terbilangTotal", angkaToTerbilang(temp)+"Rupiah");
            jReport.setParam("tglKwitansi", tglTerbilang);
            jReport.printReportEmptyDatasource();
        } catch (java.sql.SQLException | java.io.IOException e) {
            e.printStackTrace();
        }
        
    }

    @FXML
    private void clearAll(ActionEvent event) {
        lblJumlahBayar.setText("0.0");
        lblSisaBayar.setText("0.0");
        lblKonsumen.setText("-");
        lblProyek.setText("-");
    }

    @FXML
    private void getDetailSPH(ActionEvent event) {
        int index = cbSPH.getSelectionModel().getSelectedIndex();
        lblJumlahBayar.setText(String.format("%,.2f", sph.get(index).getGRANDTOTAL()));
        DB.setSTMT("SELECT * FROM proyek WHERE kdProyek='"+sph.get(index).getKODE_PROYEK()+"'");
        try {
            java.sql.ResultSet SET = DB.getSQL();
            while(SET.next()) {
                currentProyek = new MenuLaporanController.Proyek(
                    SET.getString(1), SET.getString(2), SET.getDouble(3), 
                    SET.getString(4), SET.getString(5), SET.getString(6), SET.getString(7));
            }
            lblProyek.setText(currentProyek.getDESKRIPSI());
            DB.setSTMT("SELECT * FROM konsumen WHERE kdKonsumen='"+currentProyek.getKODE_KONSUMEN()+"'");
            SET = DB.getSQL();
            while(SET.next()) {
                currentKonsumen = new MenuLaporanController.Konsumen(
                    SET.getString(1), SET.getString(2), SET.getString(3), SET.getString(4));
            }
            lblKonsumen.setText(currentKonsumen.getNAMA());
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public class SPHData {
        private String KODE, KODE_PROYEK;
        private Integer PPN;
        private Double  GRANDTOTAL, JASA;
        public SPHData(String Kode, Double Jasa, Integer PPn, Double GrandTotal, String Proyek) {
            this.KODE = Kode;
            this.JASA = Jasa;
            this.PPN = PPn;
            this.GRANDTOTAL = GrandTotal;
            this.KODE_PROYEK = Proyek;
        }
        public SPHData() {
            this.KODE = "";
            this.JASA = 0.0;
            this.PPN = 0;
            this.GRANDTOTAL = 0.0;
            this.KODE_PROYEK = "";
        }
        public String getKODE() {
            return KODE;
        }

        public void setKODE(String KODE) {
            this.KODE = KODE;
        }

        public String getKODE_PROYEK() {
            return KODE_PROYEK;
        }

        public void setKODE_PROYEK(String KODE_PROYEK) {
            this.KODE_PROYEK = KODE_PROYEK;
        }

        public Integer getPPN() {
            return PPN;
        }

        public void setPPN(Integer PPN) {
            this.PPN = PPN;
        }

        public Double getGRANDTOTAL() {
            return GRANDTOTAL;
        }

        public void setGRANDTOTAL(Double GRANDTOTAL) {
            this.GRANDTOTAL = GRANDTOTAL;
        }

        public Double getJASA() {
            return JASA;
        }

        public void setJASA(Double JASA) {
            this.JASA = JASA;
        }
    }
    public class SPH {
        private String DISPLAY;
        private Integer VALUE;
        public SPH(String DISPLAY, Integer VALUE) {
            this.DISPLAY = DISPLAY;
            this.VALUE = VALUE;
        }
        public String getDISPLAY() {
            return DISPLAY;
        }

        public void setDISPLAY(String DISPLAY) {
            this.DISPLAY = DISPLAY;
        }

        public Integer getVALUE() {
            return VALUE;
        }

        public void setVALUE(Integer VALUE) {
            this.VALUE = VALUE;
        }
        
    }
}
