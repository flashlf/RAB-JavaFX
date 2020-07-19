/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author REDHAT
 */
public class MasterBahanController implements Initializable {
    Database DB = new Database();
    AHSPController AHSP;
    HandlerComponent COMP_HANDLER;
    @FXML
    private TextField txSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private TableView<ObservableList<Object>> tblBahan;
    @FXML
    private TextField txNama;
    @FXML
    private TextField txKode;
    @FXML
    private TextField txDeskripsi;
    @FXML
    private TextField txHarga;
    @FXML
    private ComboBox<satuanUnit> cbSatuan;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    public Pane pnlAction;

    
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
     * Initializes the controller class.
     */
    public void injectController(AHSPController Controller) {
        this.AHSP = Controller;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            cbSatuan.setItems(FXCollections.observableArrayList(
            new satuanUnit("Liter","ltr"),
            new satuanUnit("Lembar","lbr"),
            new satuanUnit("Meter","m"),
            new satuanUnit("Meter²","m²"),
            new satuanUnit("Meter³","m³"),
            new satuanUnit("Kilogram","kg"),
            new satuanUnit("Batang","btg"),
            new satuanUnit("Jam","OH")
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
            
            if(COMP_HANDLER == null)
                COMP_HANDLER = new HandlerComponent();
            //java.sql.ResultSet SQL
            tblBahan = COMP_HANDLER.iniTable(tblBahan, "SELECT * FROM items", DB);
        } catch(java.sql.SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void loadData() {
        try {
            if(COMP_HANDLER == null)
                COMP_HANDLER = new HandlerComponent();
            //java.sql.ResultSet SQL
            tblBahan = COMP_HANDLER.iniTable(tblBahan, "SELECT * FROM items", DB);
        } catch(java.sql.SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void getData(MouseEvent event) {
        if(event.getClickCount() == 2 && !event.isConsumed()) {
            ObservableList<Object> data = (ObservableList<Object>) tblBahan.getSelectionModel().getSelectedItem();            
            System.out.println("Get IT DONE BITCHES \n"
                    + "Kolom 1:"+data.get(0)+"\n"
                    + "Kolom 2:"+data.get(1)+"\n"
                    + "Kolom 3:"+data.get(2)+"\n"
                    + "Kolom 4:"+data.get(3)+"\n"
                    + "Kolom 5:"+data.get(4)); 
            txKode.setText(data.get(0).toString());
            txNama.setText(data.get(1).toString());
            txDeskripsi.setText(data.get(2).toString());
            switch(data.get(3).toString()) {
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
                    System.out.println("Error Satuan Unit : "+data.get(3).toString());
                    break;
            }
            txHarga.setText(data.get(4).toString());
            if(AHSP != null) {
                Double qty = Double.parseDouble(JOptionPane.showInputDialog("Quantity : "));
                AHSP.setBahan((data.get(1).toString()+" "+data.get(2).toString()), data.get(3).toString(), qty, Double.valueOf(data.get(4).toString()), data.get(0).toString());
                tblBahan.getSelectionModel().clearSelection();
                ((Stage)(((TableView)event.getSource()).getScene().getWindow())).close();
            }
            tblBahan.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void searchData(ActionEvent event) {
        tblBahan.getColumns().clear();
        if(txSearch.getText().isEmpty())
            tblBahan = COMP_HANDLER.iniTable(tblBahan, "SELECT * FROM items", DB);
        else {
            String crite  = txSearch.getText();
            String SQL = "SELECT * FROM items WHERE kdItems like '%"+crite+"%'"
            + " or nama like '%"+crite+"%' or deskripsi like '%"+crite+"%'"
            + " or satuan like '%"+crite+"%'";
            tblBahan = COMP_HANDLER.iniTable(tblBahan, SQL, DB);            
        }
        tblBahan.refresh();
    }

    @FXML
    private void addData(ActionEvent event) {
        ObservableList<Object> row = FXCollections.observableArrayList();
        COMP_HANDLER.OBJ_DATA = new Object[tblBahan.getColumns().size()];
        COMP_HANDLER.OBJ_DATA[0] = txKode.getText(); row.add(COMP_HANDLER.OBJ_DATA[0]);
        COMP_HANDLER.OBJ_DATA[1] = txNama.getText(); row.add(COMP_HANDLER.OBJ_DATA[1]);
        COMP_HANDLER.OBJ_DATA[2] = txDeskripsi.getText(); row.add(COMP_HANDLER.OBJ_DATA[2]);  
        COMP_HANDLER.OBJ_DATA[3] = cbSatuan.getSelectionModel().getSelectedItem().getVALUE(); row.add(COMP_HANDLER.OBJ_DATA[3]);
        COMP_HANDLER.OBJ_DATA[4] = Double.valueOf(txHarga.getText()); row.add(COMP_HANDLER.OBJ_DATA[4]);
        try {
            DB.setSTMT("INSERT INTO items VALUES(?, ?, ?, ?)");            
            DB.insertSQL(COMP_HANDLER.OBJ_DATA, "items", "kdItems");            
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
        tblBahan.getItems().add(row);
    }

    @FXML
    private void editData(ActionEvent event) {
        COMP_HANDLER.OBJ_DATA = new Object[6];        
        COMP_HANDLER.OBJ_DATA[0] = txKode.getText(); COMP_HANDLER.OBJ_DATA[5] = txKode.getText();         
        COMP_HANDLER.OBJ_DATA[1] = txNama.getText();    
        COMP_HANDLER.OBJ_DATA[2] = txDeskripsi.getText();
        COMP_HANDLER.OBJ_DATA[3] = cbSatuan.getSelectionModel().getSelectedItem().getVALUE();
        COMP_HANDLER.OBJ_DATA[4] = Double.valueOf(txHarga.getText());
        try {
            DB.setSTMT("UPDATE items SET kdItems=?, nama=?, deskripsi=?, satuan=?, harga=? WHERE kdItems=?");
            DB.insertSQL(COMP_HANDLER.OBJ_DATA, "konsumen", "kdKonsumen");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        tblBahan.getColumns().clear();
        loadData();
        tblBahan.refresh();
    }

    @FXML
    private void deleteData(ActionEvent event) {
        if(tblBahan.getFocusModel().getFocusedIndex() == -1) {
            ObservableList<Object> selected = FXCollections.observableArrayList();
            COMP_HANDLER.OBJ_DATA = new Object[tblBahan.getColumns().size()];
            COMP_HANDLER.OBJ_DATA[0] = txKode.getText();
            selected.add(COMP_HANDLER.OBJ_DATA);
            selected.forEach(tblBahan.getItems()::remove);            
        } else {
            tblBahan.getItems().remove(tblBahan.getSelectionModel().getSelectedIndex());
        }
        try {
            DB.setSTMT("DELETE FROM items WHERE kdItems=?;");
            int del = DB.deleteSQL(txKode.getText());
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
        tblBahan.getSelectionModel().clearSelection();
    }
    
}
