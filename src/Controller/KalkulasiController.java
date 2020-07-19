/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author REDHAT
 */
public class KalkulasiController implements Initializable {
    private MasterAHSPController masterAHSP;
    Model.Database DB =  new Model.Database();
    private Stage subStage;
    private String SQL = "";
    public String kodeProyek = "B101"; //ambil data darimana kek
    @FXML
    private TableView<mainKalkulasi> tblKalkulasi;
    private Label txKodeProyek;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private TextField txKodeKalkulasi;
    @FXML
    private TableView<subKalkulasi> tblSubKalkulasi;
    @FXML
    private TextField txKodeAHSP;
    @FXML
    private Button btnListAHSP;
    @FXML
    private ComboBox<satuanUnit> cbSatuan;
    @FXML
    private TextArea txDeskripsiAHSP;
    @FXML
    private TextField txKoefisien;
    @FXML
    private Button btnAddSub;
    @FXML
    private Button btnEditSub;
    @FXML
    private Button btnDelSub;
    @FXML
    private Label lblKodeProyek;
    @FXML
    private TextArea txDeskripsiKalkulasi;
    @FXML
    private Label lblKodeKalkulasi;
    @FXML
    private TextField txCostAHSP;
    @FXML
    public Label lblSubCostAHSP;

    @FXML
    private void getMainKalkulasi(MouseEvent event) {
        if(event.getClickCount() == 2 && !event.isConsumed()) {            
            txKodeKalkulasi.setText(tblKalkulasi.getSelectionModel().getSelectedItem().getKode());
            txDeskripsiKalkulasi.setText(tblKalkulasi.getSelectionModel().getSelectedItem().getDeskripsi());
            lblKodeKalkulasi.setText(txKodeKalkulasi.getText());
        }
    }

    @FXML
    private void addMain(ActionEvent event) {
        if(txKodeKalkulasi.getText().length() !=0 && lblKodeProyek.getText().length() != 0) {
            SQL = "INSERT INTO mkalkulasi VALUES (?, ?, ?, ?)";
            Object[] data = new Object[4];
            data[0] = txKodeKalkulasi.getText();
            data[1] = lblKodeProyek.getText();
            data[2] = txDeskripsiKalkulasi.getText();
            data[3] = 0;
            //Add to Table
            mainKalkulasi temp = new mainKalkulasi(data[0].toString(), data[1].toString(), data[2].toString());
            tblKalkulasi.getItems().add(temp);
            //save to DB
            DB.setSTMT(SQL);
            try {
                DB.insertSQL(data, "mkalkulasi", "kdKalkulasi");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            
    }

    @FXML
    private void editMain(ActionEvent event) {
        SQL = "UPDATE mkalkulasi SET deskripsi=? WHERE kdKalkulasi=? AND kdProyek=?";
        Object[] data = new Object[3];
        data[0] = txDeskripsiKalkulasi.getText();
        data[1] = txKodeKalkulasi.getText();
        data[2] = lblKodeProyek.getText();
        if(tblKalkulasi.getSelectionModel().getSelectedIndex() < 0) {
            boolean find = false;
            mainKalkulasi temp = new mainKalkulasi(txKodeKalkulasi.getText(), lblKodeProyek.getText(), txDeskripsiKalkulasi.getText());
            ObservableList<mainKalkulasi> contain = tblKalkulasi.getItems();
            int x = 0;
            for(mainKalkulasi item : contain) {
                if(item.getKode().equalsIgnoreCase(temp.getKode())) {
                    find = true; break;                    
                }
                x++;
            }
            if(find) {
                tblKalkulasi.getItems().set(x, temp);
                try {
                    DB.setSTMT(SQL); DB.insertSQL(data, "mkalkulasi", "kdKalkulasi");                                    
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    tblKalkulasi.getSelectionModel().clearSelection();
                }
            }
        } else {
            int i = tblKalkulasi.getSelectionModel().getSelectedIndex(); // index di table
            try {
                DB.setSTMT(SQL); DB.insertSQL(data, "mkalkulasi", "kdKalkulasi");                
                tblKalkulasi.getItems().get(i).setDeskripsi(data[0].toString());
                tblKalkulasi.getItems().get(i).setKode(data[1].toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                tblKalkulasi.getSelectionModel().clearSelection();
            }
            
        }
    }

    @FXML
    private void delMain(ActionEvent event) throws java.sql.SQLException{
        if(tblKalkulasi.getSelectionModel().getSelectedIndex() < 0) {
            System.out.println("Pilih dulu lah di table");
        } else {
            SQL = "DELETE FROM mKalkulasi WHERE kdKalkulasi = ?";
            int i = tblKalkulasi.getSelectionModel().getSelectedIndex(); // index di table
            DB.setSTMT(SQL);
            String kriteria = txKodeKalkulasi.getText();
            if(txKodeKalkulasi.getText().length() < 1)
                kriteria = tblKalkulasi.getItems().get(i).getKode();                
            DB.deleteSQL(kriteria);
            tblKalkulasi.getItems().remove(i);
            tblKalkulasi.getSelectionModel().clearSelection();
        }
        
    }

    @FXML
    private void getSubKalkulasi(MouseEvent event) {
        if(event.getClickCount() == 2 && !event.isConsumed()) {  
            txKodeAHSP.setText(tblSubKalkulasi.getSelectionModel().getSelectedItem().getKode());
            txDeskripsiAHSP.setText(tblSubKalkulasi.getSelectionModel().getSelectedItem().getDeskripsi());
            txKoefisien.setText(String.valueOf(tblSubKalkulasi.getSelectionModel().getSelectedItem().getVolume()));
            Double temp = tblSubKalkulasi.getSelectionModel().getSelectedItem().getCost();
            lblSubCostAHSP.setText(String.format("%.0f", temp));
            switch(tblSubKalkulasi.getSelectionModel().getSelectedItem().getUnit()) {
                case "ltr" :
                    cbSatuan.getSelectionModel().select(0);
                    break;
                case "lbr" :
                    cbSatuan.getSelectionModel().select(1);
                    break;
                case "m" :
                    cbSatuan.getSelectionModel().select(2);
                    break;
                case "m²" :
                    cbSatuan.getSelectionModel().select(3);
                    break;
                case "m³" :
                    cbSatuan.getSelectionModel().select(4);
                    break;
                case "kg" :
                    cbSatuan.getSelectionModel().select(5);
                    break;
                case "btg" :
                    cbSatuan.getSelectionModel().select(6);
                    break;
                default:
                    System.out.println("Error Satuan Unit : "+tblSubKalkulasi.getSelectionModel().getSelectedItem().getUnit());
                    break;
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    
    /**
     * P.O.J.O used in Combo-Box @cbSatuan
     */
    public class satuanUnit {
        private String DISPLAY, VALUE;
        public satuanUnit(String DISPLAY, String VALUE) {
            this.DISPLAY = DISPLAY;
            this.VALUE = VALUE;
        }
        public String getDISPLAY() {
            return DISPLAY;
        }

        public void setDISPLAY(String DISPLAY) {
            this.DISPLAY = DISPLAY;
        }

        public String getVALUE() {
            return VALUE;
        }

        public void setVALUE(String VALUE) {
            this.VALUE = VALUE;
        }
        
    }
    /**
     * P.O.J.O used in Table-View @tblKalkulasi
     */
    public static class mainKalkulasi {
        // Property Class
        private final SimpleStringProperty Kode;
        private final SimpleStringProperty KodeProyek;
        private final SimpleStringProperty Deskripsi;
        private final SimpleIntegerProperty subKalkulasi; // gatau lupa ini apaan di DATABASE
        
        // Constructor        
        public mainKalkulasi(String KODE, String KODE_PROYEK, String DESKRIPSI) {
            this.Kode = new SimpleStringProperty(KODE);
            this.KodeProyek = new SimpleStringProperty(KODE_PROYEK);
            this.Deskripsi = new SimpleStringProperty(DESKRIPSI);
            this.subKalkulasi = new SimpleIntegerProperty(0);
        }
        
        // Getter Setter
        public String getKode(){
            return Kode.get();
        }
        public void setKode(String KODE){
            Kode.set(KODE);
        }
        public String getKodeProyek(){
            return KodeProyek.get();
        }
        public void setKodeProyek(String KODE_PROYEK){
            KodeProyek.set(KODE_PROYEK);
        }
        public String getDeskripsi(){
            return Deskripsi.get();
        }
        public void setDeskripsi(String DESKRIPSI){
            Deskripsi.set(DESKRIPSI);
        }
        public Integer getSubKalkulasi(){
            return subKalkulasi.get();
        }
        public void setSubKalkulasi(Integer SUB_KALKULASI){
            subKalkulasi.set(SUB_KALKULASI);
        }
    }
    
    public static class subKalkulasi {
        private final SimpleStringProperty Kode;
        private final SimpleStringProperty Deskripsi;
        private final SimpleStringProperty Unit;
        private final SimpleDoubleProperty Volume;
        private final SimpleDoubleProperty Cost;
        
        public subKalkulasi(String KODE, String DESKRIPSI, String UNIT, Double VOLUME, Double COST){
            this.Kode = new SimpleStringProperty(KODE);
            this.Deskripsi = new SimpleStringProperty(DESKRIPSI);
            this.Unit = new SimpleStringProperty(UNIT);
            this.Volume = new SimpleDoubleProperty(VOLUME);
            this.Cost = new SimpleDoubleProperty(COST);
        }
        
        public String getKode(){
            return Kode.get();
        }
        public void setKode(String KODE){
            this.Kode.set(KODE);
        }
        public String getDeskripsi(){
            return Deskripsi.get();
        }
        public void setDeskripsi(String DESKRIPSI){
            this.Deskripsi.set(DESKRIPSI);
        }
        public String getUnit(){
            return Unit.get();
        }
        public void setUnit(String UNIT){
            this.Unit.set(UNIT);
        }
        public double getVolume(){
            return Volume.get();
        }
        public void setVolume(double VOLUME){
            this.Volume.set(VOLUME);
        }
        public double getCost(){
            return Cost.get();
        }
        public void setCost(double COST) {
            this.Cost.set(COST);
        }
    }
    public void setTableSub(){
        TableColumn col1, col2, col3, col4, col5;
        col1 = new TableColumn("Kode");
        col1.setMinWidth(30);
        col1.setCellValueFactory(new PropertyValueFactory<subKalkulasi, String>("Kode"));
        
        col2 = new TableColumn("Deskripsi");
        col2.setMinWidth(100);
        col2.setCellValueFactory(new PropertyValueFactory<subKalkulasi, String>("Deskripsi"));
        
        col3 = new TableColumn("Unit");
        col3.setMinWidth(20);
        col3.setCellValueFactory(new PropertyValueFactory<subKalkulasi, String>("Unit"));
        
        col4 = new TableColumn("Volume");
        col4.setMaxWidth(50);
        col4.setCellValueFactory(new PropertyValueFactory<subKalkulasi, Double>("Volume"));
        
        col5 = new TableColumn("Cost");
        col5.setVisible(false);
        col5.setCellValueFactory(new PropertyValueFactory<subKalkulasi, Double>("Cost"));
        tblSubKalkulasi.getColumns().addAll(col1, col2, col3, col4, col5);
    }
    
    public void setTableUtama(){
        TableColumn col1, col2, col3, col4;
        col1 = new TableColumn("Kode");
        col1.setMinWidth(30);
        col1.setCellValueFactory(new PropertyValueFactory<mainKalkulasi, String>("Kode"));
        
        col2 = new TableColumn("Proyek");
        col2.setMaxWidth(0);
        col2.setVisible(false);
        col2.setCellValueFactory(new PropertyValueFactory<mainKalkulasi, String>("KodeProyek"));
        
        col3 = new TableColumn("Deskripsi");
        col3.setMinWidth(200);
        col3.setCellValueFactory(new PropertyValueFactory<mainKalkulasi, String>("Deskripsi"));
        
        col4 = new TableColumn("SubKalkulasi");
        col4.setMaxWidth(0);
        col4.setVisible(false);
        col4.setCellValueFactory(new PropertyValueFactory<mainKalkulasi, String>("subKalkulasi"));
        tblKalkulasi.getColumns().addAll(col1, col2, col3, col4);
    }
    
    void setComboSatuan() {
        cbSatuan.setItems(FXCollections.observableArrayList(
            new satuanUnit("Liter","ltr"),
            new satuanUnit("Lembar","lbr"),
            new satuanUnit("Meter","m"),
            new satuanUnit("Meter²","m²"),
            new satuanUnit("Meter³","m³"),
            new satuanUnit("Kilogram","kg"),
            new satuanUnit("Batang","btg")
            ));
            cbSatuan.setConverter(new StringConverter<satuanUnit>(){
                @Override
                public String toString(satuanUnit Object) {
                    return Object.getDISPLAY();
                }
                @Override
                public satuanUnit fromString(String string) {
                    return null;
                }
            });
    }
    public void loadData() {
        if(kodeProyek == null)
            kodeProyek = "B101";
        ObservableList<mainKalkulasi> CONT_MAIN = FXCollections.observableArrayList();
        try {
            DB.inisiasiDB();
            DB.setSTMT("SELECT * FROM mkalkulasi "
                + "WHERE kdProyek='"+kodeProyek+"' "
                + "ORDER BY kdKalkulasi ASC");
            java.sql.ResultSet SET = DB.getSQL();
            while(SET.next()){
                CONT_MAIN.add(new mainKalkulasi(SET.getString(1), SET.getString(2), SET.getString(3)));                
            }
            tblKalkulasi.setItems(CONT_MAIN);
        } catch(Exception ex)  {
            ex.printStackTrace();
        }
    }
    public void loadDataSub() {
        ObservableList<subKalkulasi> CONT_SUB = FXCollections.observableArrayList();
        try {
            // kode, unit, deskripsi, volume, cost
            DB.inisiasiDB();
            DB.setSTMT("SELECT `kalkulasi`.`kdUraian`,`muraian`.`Deskripsi`, `kalkulasi`.`satuan`, `kalkulasi`.`volume`, "
                    + "(SELECT (CostBahan+CostTenaga) FROM costperuraian WHERE costperuraian.kdUraian = kalkulasi.kdUraian) AS TOTAL "
                    + "FROM kalkulasi, muraian WHERE kalkulasi.kdUraian = muraian.kdUraian "
                    + "AND kdKalkulasi ='"+lblKodeKalkulasi.getText()+"'");
            java.sql.ResultSet SET = DB.getSQL();
            while(SET.next()){
                CONT_SUB.add(new subKalkulasi(SET.getString(1), SET.getString(2), SET.getString(3), SET.getDouble(4), SET.getDouble(5)));                
            }
            tblSubKalkulasi.setItems(CONT_SUB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setComboSatuan();
        setTableSub(); setTableUtama();
        //loadData();
        lblKodeKalkulasi.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {                
                System.out.println("lblKodeKalkulasi diganti jadi => "+t1);
                clearSub(); setTableSub();
                loadDataSub();
            }
        });
        txKodeAHSP.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if(txKodeAHSP.getText().length()!=0) {
                    
                } else {
                    System.out.println("lblKodeKalkulasi diganti jadi => "+t1);
                }
                
            }
        });
        txKoefisien.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if(txKoefisien.getText().length() != 0) 
                    try {
                        Double temp = Double.valueOf(txKoefisien.getText());
                        Double temp1 = Double.valueOf(lblSubCostAHSP.getText());
                        txCostAHSP.setText(String.format("%,.2f", (temp*temp1)));
                    } catch (Exception ex)  {
                        ex.printStackTrace();
                    }                    
                System.out.println("Kosong Brooo");
            }
        });
    }    



    @FXML
    private void addSub(ActionEvent event) {
        try {
            SQL = "INSERT INTO kalkulasi VALUES (?,?,?,?)";
            Object[] data = new Object[4];
            data[0] = lblKodeKalkulasi.getText();
            data[1] = txKodeAHSP.getText();
            data[2] = cbSatuan.getSelectionModel().getSelectedItem().VALUE;
            data[3] = Double.valueOf(txKoefisien.getText());
            tblSubKalkulasi.getItems().add(new subKalkulasi(
                data[1].toString(), 
                txDeskripsiAHSP.getText(), 
                data[2].toString(), 
                Double.valueOf(txKoefisien.getText()), 
                Double.valueOf(lblSubCostAHSP.getText())) );
            DB.setSTMT(SQL);
            DB.insertSQL(data, "kalkulasi", "kdKalkulasi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editSub(ActionEvent event) {
        if(tblSubKalkulasi.getSelectionModel().getSelectedIndex() < 0) {
            System.out.println("Pilih dulu lah di table");
        } else {
            int i = tblSubKalkulasi.getSelectionModel().getSelectedIndex(); // index di table
            SQL = "UPDATE kalkulasi SET volume=?, satuan=? WHERE kdKalkulasi=? AND kdUraian=?";
            Object[] data = new Object[4];
            data[0] = Double.valueOf(txKoefisien.getText());
            data[1] = cbSatuan.getSelectionModel().getSelectedItem().VALUE;
            data[2] = txKodeKalkulasi.getText();
            data[3] = lblKodeKalkulasi.getText();
            try {
                DB.setSTMT(SQL); DB.insertSQL(data, "mkalkulasi", "kdKalkulasi");                
                tblSubKalkulasi.getItems().get(i).setVolume(Double.valueOf(txKoefisien.getText()));
                tblSubKalkulasi.getItems().get(i).setUnit(data[1].toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                tblKalkulasi.getSelectionModel().clearSelection();
            }
            
        }
    }

    @FXML
    private void delSub(ActionEvent event) throws java.sql.SQLException{
        if(tblSubKalkulasi.getSelectionModel().getSelectedIndex() < 0) {
            System.out.println("Pilih dulu lah di table");
        } else {
            int i = tblSubKalkulasi.getSelectionModel().getSelectedIndex(); // index di table
            String kriteria = txKodeAHSP.getText();
            if(txKodeAHSP.getText().length() < 1)
                kriteria = tblSubKalkulasi.getItems().get(i).getKode();                
            SQL = "DELETE FROM kalkulasi WHERE kdKalkulasi = ? AND kdUraian ='"+kriteria+"'";
            DB.setSTMT(SQL);
            DB.deleteSQL(lblKodeKalkulasi.getText());
            tblSubKalkulasi.getItems().remove(i);
            tblSubKalkulasi.getSelectionModel().clearSelection();
        }
    }
    public void clearSub() {
        tblSubKalkulasi.getColumns().clear();
        txKodeAHSP.setText("");
        txDeskripsiAHSP.setText("");
        lblSubCostAHSP.setText("0.0");  
        txKoefisien.setText("0.0");
    }
    @FXML
    private void listAHSP(ActionEvent event) throws java.io.IOException, SQLException{
        if(subStage == null) {
            FXMLLoader fLoader = new FXMLLoader(getClass().getResource("/View/MasterAHSP.fxml"));
            Parent parent = fLoader.load();
            masterAHSP = fLoader.<MasterAHSPController>getController();
            masterAHSP.injectController(this);
            masterAHSP.txKodeProyek.setText(this.kodeProyek);  
            masterAHSP.btnLookupKalkulasi.setDisable(false);
            masterAHSP.txKodeProyek.setEditable(false);
            Scene subScene = new Scene(parent);
            subStage = new Stage();
            subStage.setTitle("RAB | Kalkulasi - Detail AHSP");
            subStage.resizableProperty().setValue(Boolean.FALSE);
            subStage.setScene(subScene);      
            subStage.show();
            masterAHSP.searchData(event);
        } else {            
            subStage.toFront();
            subStage.show();
        }
    }
    public void getSubAHSP(Object[] Data) {
        Double temp = Double.parseDouble(Data[2].toString());
        lblSubCostAHSP.setText(String.format("%.0f", temp));
        txKodeAHSP.setText(Data[0].toString());
        txDeskripsiAHSP.setText(Data[1].toString());
    }
    
}
