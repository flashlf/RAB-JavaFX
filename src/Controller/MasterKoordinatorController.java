/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author REDHAT
 */
public class MasterKoordinatorController implements Initializable {
    Model.Database DB = new Model.Database();    
    MenuLaporanController laporanController;
    SuratTugasController suratTugas;
    Object[] OBJ_DATA;
    String SQL;
    @FXML
    private TextField txKode;
    @FXML
    private TextField txTelepon;
    @FXML    
    private TextField txJabatan;
    @FXML
    private TextField txNama;
    @FXML
    private TextField txSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private TableView<Koordinator> tblKoordinator;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setColumnTable();        
    }    

    public void injectController(MenuLaporanController Controller) {
        this.laporanController = Controller;
    }
    public void injectController(SuratTugasController Controller) {
        this.suratTugas = Controller;
    }
    
    public void loadData(String SQL) {
        ObservableList<Koordinator> CONT_MAIN  =  FXCollections.observableArrayList();
        try {
            DB.inisiasiDB();
            DB.setSTMT(SQL);
            java.sql.ResultSet SET = DB.getSQL();
            while(SET.next()) {
                CONT_MAIN.add(new Koordinator(SET.getString(1), SET.getString(2), SET.getString(3), SET.getString(4)));                
            }
            tblKoordinator.setItems(CONT_MAIN);
        } catch  (java.sql.SQLException ex)  {
            ex.printStackTrace();
        }
    }
    @FXML
    private void searchData(ActionEvent event) throws SQLException {
        if(txSearch.getText().isEmpty()) {
            tblKoordinator.getColumns().clear();
            setColumnTable();
            loadData("SELECT * FROM koordinator");
        } else {
            tblKoordinator.getColumns().clear();
            setColumnTable();
            String crite  = txSearch.getText();
            String SQL = "SELECT * FROM koordinator WHERE kdKoordinator like '%"+crite+"%'"
            + " or nmKoordinator like '%"+crite+"%'"
            + " or noTelp like '%"+crite+"%'"
            + " or jabatan like '%"+crite+"%'";
            loadData(SQL);
        }
        tblKoordinator.refresh();
    }

    @FXML
    private void getData(MouseEvent event) {
        if(event.getClickCount() == 2 && !event.isConsumed()) {
            Koordinator temp = tblKoordinator.getSelectionModel().getSelectedItem();
            if(laporanController != null) {
                txKode.setText(temp.getKode());
                txJabatan.setText(temp.getJabatan());
                txTelepon.setText(temp.getTelp());
                txNama.setText(temp.getNama());
            } else {
                this.suratTugas.getFromKoordinator(temp);
            }
            tblKoordinator.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void addData(ActionEvent event) {
        Koordinator temp = new Koordinator(txKode.getText(), txNama.getText(), txJabatan.getText(), txTelepon.getText());
        OBJ_DATA = new Object[4];
        OBJ_DATA[0] = temp.getKode();
        OBJ_DATA[1] = temp.getNama();
        OBJ_DATA[2] = temp.getJabatan();
        OBJ_DATA[3] = temp.getTelp();
        //add to table
        tblKoordinator.getItems().add(temp);
        try { // save to DB
            DB.inisiasiDB();
            DB.setSTMT("INSERT INTO koordinator VALUES(?, ?, ?, ?)");            
            DB.insertSQL(OBJ_DATA, "koordinator", "kdKoordinator");  
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void editData(ActionEvent event) {
        boolean find = false;
        SQL = "UPDATE koordinator SET nmKoordinator=?, alamat=?, noTelp=? WHERE kdKoordinator=?";
        Koordinator temp = new Koordinator(txKode.getText(), txNama.getText(), txJabatan.getText(), txTelepon.getText());
        ObservableList<Koordinator> contain = tblKoordinator.getItems();
        OBJ_DATA = new Object[4];
        OBJ_DATA[3] = temp.getKode();
        OBJ_DATA[0] = temp.getNama();
        OBJ_DATA[1] = temp.getJabatan();
        OBJ_DATA[2] = temp.getTelp();
        if(tblKoordinator.getSelectionModel().getSelectedIndex() >= 0) {
            int i = tblKoordinator.getSelectionModel().getSelectedIndex();
            tblKoordinator.getItems().set(i, temp);
        } else {            
            int x = 0;
            for(Koordinator item : contain) {
                if(item.getKode().equalsIgnoreCase(temp.getKode())) {
                    find = true; break;                    
                }
                x++;
            }
            if(find) {
                tblKoordinator.getItems().set(x, temp);                
            }
        }
        try {
            DB.setSTMT(SQL);
            DB.insertSQL(OBJ_DATA, "koordinator", "kdKoordinator");
        } catch(java.sql.SQLException ex) {
            ex.printStackTrace();
        } finally {
            tblKoordinator.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void deleteData(ActionEvent event) throws SQLException {
        boolean find = false;
        SQL = "DELETE FROM koordinator WHERE kdKoordinator= ?";
        if(tblKoordinator.getItems().size()>0) {
            Koordinator temp = new Koordinator(txKode.getText(), txNama.getText(), txJabatan.getText(), txTelepon.getText());
            ObservableList<Koordinator> contain = tblKoordinator.getItems();
            if(tblKoordinator.getSelectionModel().getSelectedIndex() >= 0) {
                DB.deleteSQL(tblKoordinator.getItems().get(tblKoordinator.getSelectionModel().getSelectedIndex()).getKode());
                tblKoordinator.getItems().remove(tblKoordinator.getSelectionModel().getSelectedIndex());                
            } else  {
                int x = 0;
                for(Koordinator item : contain) {
                    if(item.getKode().equalsIgnoreCase(temp.getKode())) {
                        find = true; break;                    
                    }
                    x++;
                }
                if(find) {
                    tblKoordinator.getItems().remove(x);                
                    DB.deleteSQL(temp.getKode());
                }
            }
        }
    }
    
    public void setColumnTable() {
        TableColumn col1, col2, col3, col4;
        
        col1 = new TableColumn("Kode");
        col1.setMaxWidth(40);
        col1.setCellValueFactory(new PropertyValueFactory<Koordinator, String>("Kode"));
        
        col2 = new TableColumn("Nama");
        col2.setMinWidth(150);
        col2.setCellValueFactory(new PropertyValueFactory<Koordinator, String>("Nama"));
                
        col3 = new TableColumn("Jabatan");
        col3.setMinWidth(200);
        col3.setCellValueFactory(new PropertyValueFactory<Koordinator, String>("Jabatan"));
        
        col4 = new TableColumn("Telp");
        col4.setMinWidth(75);
        col4.setCellValueFactory(new PropertyValueFactory<Koordinator, String>("Telp"));
        tblKoordinator.getColumns().addAll(col1, col2, col3, col4);
    }
    
    public static class Koordinator {
        // kd, nama, alamat, telp
        private final SimpleStringProperty Kode, Nama, Alamat, Telp;
        
        private Koordinator(String KODE, String NAMA, String JABATAN, String TELP) {
            this.Kode = new SimpleStringProperty(KODE);
            this.Alamat = new SimpleStringProperty(JABATAN);
            this.Nama = new SimpleStringProperty(NAMA);
            this.Telp = new SimpleStringProperty(TELP);
        }
        
        public String getKode() {
            return Kode.get();
        }
        public String getNama() {
            return Nama.get();
        }
        public String getJabatan()  {
            return Alamat.get();
        }
        public String getTelp() {
            return Telp.get();
        }
        
        public void setKode(String KODE) {
            this.Kode.set(KODE);
        }
        public void setNama(String NAMA) {
            this.Nama.set(NAMA);
        }
        public void setJabatan(String JABATAN) {
            this.Alamat.set(JABATAN);           
        }
        public void setTelp(String TELP) {
            this.Telp.set(TELP);
        }
    }
}
