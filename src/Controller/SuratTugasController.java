/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.table.DefaultTableModel;

/**
 * FXML Controller class
 *
 * @author REDHAT
 */
public class SuratTugasController implements Initializable {
    private Stage subStage;
    private Scene koordinator;    
    private MasterKoordinatorController koordinatorController;
    private MenuLaporanController laporanController;
    public String SQL = "", listData="", noSurat="", Kegiatan="", Lokasi="";
    int noDataTable = 1;    
    String[] headerTable = {"No","Kode","Nama","Jabatan","Telp"};
    Model.Database DB = new Model.Database();
    DefaultTableModel tabmodel = new DefaultTableModel(null, headerTable);
    @FXML
    private DatePicker dtMulai;
    @FXML
    private DatePicker dtSelesai;
    @FXML
    private TextField txKode;
    @FXML
    private TableView<listKoordinator> tblListKoordinator;
    @FXML
    private Label lblTglSurat;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnCancel;
    @FXML
    public Label lblKodeProyek;
    @FXML
    public Label lblKodeKonsumen;
    @FXML
    private Button btnPrint;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setColumnTable();
        java.util.Date sekarang = new java.util.Date();
        lblTglSurat.setText((sekarang.getYear()+1900)+"/"+(sekarang.getMonth()+1)+"/"+sekarang.getDate());
        txKode.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                try {
                    DB.inisiasiDB();
                    DB.setSTMT("SELECT * FROM st WHERE kodeSt ='"+txKode.getText()+"'");
                    java.sql.ResultSet SET = DB.getSQL();
                    if(SET.next()) {
                        tblListKoordinator.getColumns().clear();
                        setColumnTable();
                        String list = SET.getString(4).replace("|", "-");
                        String[] data = list.split("-");
                        String[] mulai = SET.getString(5).split("-");
                        dtMulai.getEditor().setText(mulai[2]+"/"+mulai[1]+"/"+mulai[0]);
                        mulai = SET.getString(6).split("-");
                        dtSelesai.getEditor().setText(mulai[2]+"/"+mulai[1]+"/"+mulai[0]);
                        SQL = "SELECT * FROM koordinator WHERE kdKoordinator in(";
                        for(int x=0; x<data.length;  x++)  {
                            if(x == (data.length-1))
                                SQL += "'"+data[x]+"') \n ORDER BY CASE kdKoordinator\n";
                            else  
                                SQL += "'"+data[x]+"',";
                        }
                        for(int x=0; x< data.length; x++){
                            if(x == (data.length-1)){
                                SQL += "\tWHEN '"+data[x]+"' THEN "+(x+1)+" \r\nEND";
                            } else {
                                SQL += "\tWHEN '"+data[x]+"' THEN "+(x+1)+" \n";
                            }
                        }
                        DB.setSTMT(SQL); SET = DB.getSQL();
                        int y = 1;
                        while(SET.next()) {
                            listKoordinator temp = new listKoordinator(y, SET.getString(1), SET.getString(2), SET.getString(3), SET.getString(4));
                            Object[] temp1 = new Object[5];
                            temp1[0] = y; temp1[1] = SET.getString(1); temp1[2] = SET.getString(3);
                            temp1[3] = SET.getString(3); temp1[4] = SET.getString(4);
                            tabmodel.addRow(temp1);
                            y++;
                            tblListKoordinator.getItems().add(temp);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }    
    public void injectController(MasterKoordinatorController Controller) {
        this.koordinatorController = Controller;
    }
    public void injectController(MenuLaporanController Controller) {
        this.laporanController = Controller;
    }
    @FXML
    private void getData(MouseEvent event) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        System.out.println(saltStr);
    }

    @FXML
    private void addData(ActionEvent event) throws IOException {
        if( subStage == null ) {
            FXMLLoader fLoader = new FXMLLoader(getClass().getResource("/View/MasterKoordinator.fxml"));
            Parent parent = fLoader.load();
            koordinatorController = fLoader.getController();
            koordinatorController.injectController(this);            
            koordinatorController.loadData("SELECT * FROM koordinator");
            koordinator = new Scene(parent);   
            subStage = new Stage();
            subStage.setTitle("RAB | Data - Koordinator");
            subStage.resizableProperty().setValue(Boolean.FALSE);
            subStage.setScene(koordinator);
            subStage.show();
        } else  {
            subStage.toFront();
            subStage.show();
        }
    }


    @FXML
    private void deleteData(ActionEvent event) {
        try {
            if(tblListKoordinator.getFocusModel().getFocusedIndex() == -1) {
                System.out.println("Seleksi Table Dulu lah");
            } else {
                tabmodel.removeRow(tblListKoordinator.getSelectionModel().getSelectedIndex());
                tblListKoordinator.getItems().remove(tblListKoordinator.getSelectionModel().getSelectedIndex());
                serializeKoordinator();
                SQL = "UPDATE st SET listKoordinator='"+this.listData+"' WHERE kodeSt=?";
                DB.setSTMT(SQL);
                int del = DB.deleteSQL(txKode.getText());
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tblListKoordinator.getSelectionModel().clearSelection();
    }

    @FXML
    private void printST(ActionEvent event) {
        // Save to DB
        serializeKoordinator();
        Object[] DATA = new Object[12];
        DATA[0] = txKode.getText();
        DATA[1] = lblTglSurat.getText();
        DATA[2] = lblKodeProyek.getText();
        DATA[3] = this.listData;
        String[] mulai = dtMulai.getEditor().getText().split("/");
        DATA[4] = mulai[2]+"-"+mulai[1]+"-"+mulai[0];
        mulai = dtSelesai.getEditor().getText().split("/");
        DATA[5] = mulai[2]+"-"+mulai[1]+"-"+mulai[0];        
        DATA[6] = lblKodeKonsumen.getText();
        DATA[7] = DATA[3];
        DATA[8] = DATA[2];
        DATA[9] = DATA[6];
        DATA[10] = DATA[4];
        DATA[11] = DATA[5];
        try {
            DB.inisiasiDB();
            SQL ="INSERT INTO st VALUES(?, ?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "listKoordinator = ?, "
                + "kdProyek = ?, "
                + "kdKonsumen = ?, "
                + "tglMulaiSt = ?, "
                + "tglSelesaiSt = ?;";
            DB.setSTMT(SQL);
            DB.insertSQL(DATA, "st", "kodeSt");
            noSurat = lblKodeProyek.getText()+"/"+txKode.getText()+"/"+lblKodeKonsumen.getText();
            Model.HandlerComponent.Report jReport  = new Model.HandlerComponent.Report();
            jReport.setFileName("SuratTugas");
            //pathLogoHeader, pathWatermark, Kegiatan, Lokasi, Kolom Table
            jReport.setParam("noSurat", noSurat);
            File a = new File("misc/logo.jpg");
            String absolute = a.getCanonicalPath();
            String replace = absolute.replace("\\", "\\\\");
            jReport.setParam("pathLogoHeader", replace);
            a = new File("misc/logo.png");
            absolute = a.getCanonicalPath();
            replace = absolute.replace("\\", "\\\\");
            jReport.setParam("pathWatermark", replace);
            jReport.setParam("Kegiatan", this.Kegiatan);
            jReport.setParam("Lokasi", this.Lokasi);
            jReport.setParam("tglMulai", dtMulai.getEditor().getText());
            jReport.setParam("tglSelesai", dtSelesai.getEditor().getText());
            jReport.printReportDataTable(tabmodel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearAll(ActionEvent event) {
        tblListKoordinator.getColumns().clear();
        setColumnTable();
        txKode.setText("");
        dtMulai.getEditor().setText("");
        dtSelesai.getEditor().setText("");
    }
    public void getFromKoordinator(MasterKoordinatorController.Koordinator Data) {
        listKoordinator temp = new listKoordinator(noDataTable, Data.getKode(), Data.getNama(), Data.getJabatan(), Data.getTelp());
        Object[] data = new Object[5];
        data[0] = noDataTable;
        data[1] = Data.getKode();
        data[2] = Data.getNama();
        data[3] = Data.getJabatan();
        data[4] = Data.getTelp();
        tblListKoordinator.getItems().add(temp);
        tabmodel.addRow(data);
        this.noDataTable++;
    }
    
    void serializeKoordinator() {
        for(int i=0; i<tblListKoordinator.getItems().size(); i++) {
            if(i == (tblListKoordinator.getItems().size()-1))
                this.listData += tblListKoordinator.getItems().get(i).getKode();
            else
                this.listData += tblListKoordinator.getItems().get(i).getKode()+"|";

        }
    }
    public void setColumnTable() {
        TableColumn col1, col2, col3, col4, col5;
        col5 = new TableColumn("No.");
        col5.setMaxWidth(30);
        col5.setCellValueFactory(new PropertyValueFactory<MasterKoordinatorController.Koordinator, Integer>("No"));
        
        col1 = new TableColumn("Kode");
        col1.setMaxWidth(40);
        col1.setCellValueFactory(new PropertyValueFactory<MasterKoordinatorController.Koordinator, String>("Kode"));
        
        col2 = new TableColumn("Nama");
        col2.setMinWidth(150);
        col2.setCellValueFactory(new PropertyValueFactory<MasterKoordinatorController.Koordinator, String>("Nama"));
                
        col3 = new TableColumn("Jabatan");
        col3.setMinWidth(150);
        col3.setCellValueFactory(new PropertyValueFactory<MasterKoordinatorController.Koordinator, String>("Jabatan"));
        
        col4 = new TableColumn("Telp");
        col4.setMinWidth(75);
        col4.setCellValueFactory(new PropertyValueFactory<MasterKoordinatorController.Koordinator, String>("Telp"));
        tblListKoordinator.getColumns().addAll(col5, col1, col2, col3, col4);
    }

    @FXML
    private void searchST(KeyEvent event) {
        
    }
    public static class listKoordinator {
        private final SimpleIntegerProperty No;
        private final SimpleStringProperty Kode, Nama, Jabatan, Telp;
        
        private listKoordinator(Integer NO, String KODE, String NAMA, String JABATAN, String TELP) {
            this.No = new SimpleIntegerProperty(NO);
            this.Kode = new SimpleStringProperty(KODE);
            this.Jabatan = new SimpleStringProperty(JABATAN);
            this.Nama = new SimpleStringProperty(NAMA);
            this.Telp = new SimpleStringProperty(TELP);
        }
        
        public Integer getNo() {
            return No.get();
        }
        public String getKode() {
            return Kode.get();
        }
        public String getNama() {
            return Nama.get();
        }
        public String getJabatan()  {
            return Jabatan.get();
        }
        public String getTelp() {
            return Telp.get();
        }
        
        public void setNo(Integer NO) {
            this.No.set(NO);
        }
        public void setKode(String KODE) {
            this.Kode.set(KODE);
        }
        public void setNama(String NAMA) {
            this.Nama.set(NAMA);
        }
        public void setJabatan(String JABATAN) {
            this.Jabatan.set(JABATAN);           
        }
        public void setTelp(String TELP) {
            this.Telp.set(TELP);
        }
    }
}
