/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
/**
 * FXML Controller class
 *
 * @author helos
 */
public class MasterProyekController implements Initializable {
    Database DB = new Database();
    HandlerComponent COMP_HANDLER;
    MenuLaporanController menuLaporan;
    SuratPenawaranHargaController laporanSPH;
    @FXML
    private TextField txSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private TableView<ObservableList<Object>> tblProyek;
    @FXML
    private TextArea txDeskripsi;
    @FXML
    private DatePicker dtMulai;
    @FXML
    private DatePicker dtSelesai;
    @FXML
    private TextField txNilaiProyek;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private TextField txKode;
    @FXML
    private TextField txWilayah;
    @FXML
    private TextField txKodeKonsnumen;

    /**
     * Initializes the controller class.
     */
    private void loadData() {
        try {
            COMP_HANDLER = new HandlerComponent();
            //java.sql.ResultSet SQL
            tblProyek = COMP_HANDLER.iniTable(tblProyek, "SELECT * FROM proyek", DB);
        } catch(java.sql.SQLException ex) {
            ex.printStackTrace();
        }
        
    }
    public void injectController(MenuLaporanController Controller) {
        this.menuLaporan = Controller;
    }
    public void injectController(SuratPenawaranHargaController Controller) {
        this.laporanSPH = Controller;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadData();
    }    

    @FXML
    private void searchData(ActionEvent event) {
        tblProyek.getColumns().clear();
        if(txSearch.getText().isEmpty())
            tblProyek = COMP_HANDLER.iniTable(tblProyek, "SELECT * FROM proyek", DB);
        else {
            String crite  = txSearch.getText();
            String SQL = "SELECT * FROM proyek WHERE kdProyek like '%"+crite+"%'"
            + " or deksripsi like '%"+crite+"%'"
            + " or wilayah like '%"+crite+"%'";
            
            tblProyek = COMP_HANDLER.iniTable(tblProyek, SQL, DB);            
        }
        tblProyek.refresh();
    }

    @FXML
    private void getData(MouseEvent event) {
        if(event.getClickCount() == 2 && !event.isConsumed()) {
            ObservableList<Object> data = (ObservableList<Object>) tblProyek.getSelectionModel().getSelectedItem();            
            
            if(this.menuLaporan == null && this.laporanSPH == null) {
                System.out.println("Get IT DONE BITCHES \n"
                        + "Kolom 1:"+data.get(0)+"\n"
                        + "Kolom 2:"+data.get(1)+"\n"
                        + "Kolom 3:"+data.get(2)+"\n"
                        + "Kolom 4:"+data.get(3));
                txKode.setText(data.get(0).toString());
                txDeskripsi.setText(data.get(1).toString());
                txNilaiProyek.setText(Double.valueOf(data.get(2).toString()).toString());
                String[] tgl = data.get(3).toString().split("-");
                dtMulai.getEditor().setText(tgl[2]+"/"+tgl[1]+"/"+tgl[0]);
                tgl = data.get(3).toString().split("-");
                dtSelesai.getEditor().setText(tgl[2]+"/"+tgl[1]+"/"+tgl[0]);
                txKodeKonsnumen.setText(data.get(5).toString());
                txWilayah.setText(data.get(6).toString());                
            } else {
                if(this.menuLaporan != null) {
                    menuLaporan.setProyek(data); 
                    menuLaporan.setKonsumen(data.get(5).toString());
                }
                if(this.laporanSPH != null)
                    laporanSPH.setDataProyek(data.toArray());
            }
        }
    }

    @FXML
    private void addData(ActionEvent event) {
        // blom kelar nih...
        System.out.println(dtMulai.getEditor().getText());
        
        ObservableList<Object> row = FXCollections.observableArrayList();
        COMP_HANDLER.OBJ_DATA = new Object[tblProyek.getColumns().size()];
        COMP_HANDLER.OBJ_DATA[0] = txKode.getText(); row.add(COMP_HANDLER.OBJ_DATA[0]);
        COMP_HANDLER.OBJ_DATA[1] = txDeskripsi.getText(); row.add(COMP_HANDLER.OBJ_DATA[1]);
        COMP_HANDLER.OBJ_DATA[2] = Double.valueOf(txNilaiProyek.getText()); row.add(COMP_HANDLER.OBJ_DATA[2]);
        String[] mulai = dtMulai.getEditor().getText().split("/");       
        COMP_HANDLER.OBJ_DATA[3] = mulai[2]+"-"+mulai[1]+"-"+mulai[0]; row.add(COMP_HANDLER.OBJ_DATA[3]);
        mulai = dtSelesai.getEditor().getText().split("/");
        COMP_HANDLER.OBJ_DATA[4] = mulai[2]+"-"+mulai[1]+"-"+mulai[0]; row.add(COMP_HANDLER.OBJ_DATA[4]);
        COMP_HANDLER.OBJ_DATA[5] = txKodeKonsnumen.getText();
        COMP_HANDLER.OBJ_DATA[6] = txWilayah.getText();
        try {
            DB.setSTMT("INSERT INTO proyek VALUES(?, ?, ?, ?, ?, ?, ?)");            
            DB.insertSQL(COMP_HANDLER.OBJ_DATA, "proyek", "kdProyek");            
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
        tblProyek.getItems().add(row);
    }

    @FXML
    private void editData(ActionEvent event) {
        COMP_HANDLER.OBJ_DATA = new Object[8];        
        COMP_HANDLER.OBJ_DATA[0] = txKode.getText(); COMP_HANDLER.OBJ_DATA[7] = txKode.getText(); 
        COMP_HANDLER.OBJ_DATA[1] = txDeskripsi.getText(); 
        COMP_HANDLER.OBJ_DATA[2] = Double.valueOf(txNilaiProyek.getText()); 
        String[] mulai = dtMulai.getEditor().getText().split("/");       
        COMP_HANDLER.OBJ_DATA[3] = mulai[2]+"-"+mulai[1]+"-"+mulai[0]; 
        mulai = dtSelesai.getEditor().getText().split("/");
        COMP_HANDLER.OBJ_DATA[4] = mulai[2]+"-"+mulai[1]+"-"+mulai[0];
        COMP_HANDLER.OBJ_DATA[5] = txKodeKonsnumen.getText();
        COMP_HANDLER.OBJ_DATA[6] = txWilayah.getText();
        try {
            DB.setSTMT("UPDATE proyek SET kdProyek=?, deskripsi=?, nilai=?, tglMulai=?, tglSelesai=?, wilayah=?, kdKonsumen=? WHERE kdProyek=?");
            DB.insertSQL(COMP_HANDLER.OBJ_DATA, "proyek", "kdProyek");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        tblProyek.getColumns().clear();
        loadData();
        tblProyek.refresh();
    }

    @FXML
    private void deleteData(ActionEvent event) {
        if(tblProyek.getFocusModel().getFocusedIndex() == -1) {
            ObservableList<Object> selected = FXCollections.observableArrayList();
            COMP_HANDLER.OBJ_DATA = new Object[tblProyek.getColumns().size()];
            COMP_HANDLER.OBJ_DATA[0] = txKode.getText();
            selected.add(COMP_HANDLER.OBJ_DATA);
            selected.forEach(tblProyek.getItems()::remove);            
        } else {
            tblProyek.getItems().remove(tblProyek.getSelectionModel().getSelectedIndex());
        }
        try {
            DB.setSTMT("DELETE FROM proyek WHERE kdProyek=?;");
            int del = DB.deleteSQL(txKode.getText());
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
        tblProyek.getSelectionModel().clearSelection();
    }
    
}
