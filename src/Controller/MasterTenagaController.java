/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Database;
import Model.HandlerComponent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author REDHAT
 */
public class MasterTenagaController implements Initializable {
    Database DB = new Database();
    HandlerComponent COMP_HANDLER;
    AHSPController AHSP = null;
    @FXML
    private TextField txSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private TableView<ObservableList<Object>> tblTenaga;
    @FXML
    private TextField txNama;
    @FXML
    private TextField txKode;
    @FXML
    private TextField txHarga;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private Label txSatuan;
    @FXML
    public Pane pnlAction;

    /**
     * Initializes the controller class.
     */
    public void injectController(AHSPController Controller) {
        this.AHSP = Controller;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadData();
    }    
    
    private void loadData() {
        try {
            if(COMP_HANDLER == null)
                COMP_HANDLER = new HandlerComponent();
            //java.sql.ResultSet SQL
            tblTenaga = COMP_HANDLER.iniTable(tblTenaga, "SELECT * FROM tenaga", DB);
        } catch(java.sql.SQLException ex) {
            ex.printStackTrace();
        }        
    }
    @FXML
    private void searchData(ActionEvent event) {
        tblTenaga.getColumns().clear();
        if(txSearch.getText().isEmpty())
            tblTenaga = COMP_HANDLER.iniTable(tblTenaga, "SELECT * FROM tenaga", DB);
        else {
            String crite  = txSearch.getText();
            String SQL = "SELECT * FROM tenaga WHERE kdTenaga like '%"+crite+"%'"
            + " or nmTenaga like '%"+crite+"%'"
            + " or satuan like '%"+crite+"%'";
            tblTenaga = COMP_HANDLER.iniTable(tblTenaga, SQL, DB);            
        }
        tblTenaga.refresh();
    }

    @FXML
    private void getData(MouseEvent event) {
        if(event.getClickCount() == 2 && !event.isConsumed()) {
            ObservableList<Object> data = (ObservableList<Object>) tblTenaga.getSelectionModel().getSelectedItem();            
            System.out.println("Get IT DONE BITCHES \n"
                    + "Kolom 1:"+data.get(0)+"\n"
                    + "Kolom 2:"+data.get(1)+"\n"
                    + "Kolom 3:"+data.get(2)+"\n"
                    + "Kolom 4:"+data.get(3));
            txKode.setText(data.get(0).toString());
            txNama.setText(data.get(1).toString());
            txHarga.setText(data.get(3).toString());
            System.out.println(AHSP);
            if(AHSP != null) {
                Double qty = Double.parseDouble(JOptionPane.showInputDialog("Quantity : "));
                AHSP.setTenaga(data.get(1).toString(), data.get(2).toString(), qty, Double.valueOf(data.get(3).toString()), data.get(0).toString());
                tblTenaga.getSelectionModel().clearSelection();
                ((Stage)(((TableView)event.getSource()).getScene().getWindow())).close();
            }
        }
    }

    @FXML
    private void addData(ActionEvent event) {
        ObservableList<Object> row = FXCollections.observableArrayList();
        COMP_HANDLER.OBJ_DATA = new Object[tblTenaga.getColumns().size()];
        COMP_HANDLER.OBJ_DATA[0] = txKode.getText(); row.add(COMP_HANDLER.OBJ_DATA[0]);
        COMP_HANDLER.OBJ_DATA[1] = txNama.getText(); row.add(COMP_HANDLER.OBJ_DATA[1]);
        COMP_HANDLER.OBJ_DATA[2] = "OH"; row.add(COMP_HANDLER.OBJ_DATA[2]);
        COMP_HANDLER.OBJ_DATA[3] = Double.valueOf(txHarga.getText()); row.add(COMP_HANDLER.OBJ_DATA[3]);
        
        try {
            DB.setSTMT("INSERT INTO tenaga VALUES(?, ?, ?, ?)");            
            DB.insertSQL(COMP_HANDLER.OBJ_DATA, "tenaga", "kdTenaga");            
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
        tblTenaga.getItems().add(row);
    }

    @FXML
    private void editData(ActionEvent event) {
        COMP_HANDLER.OBJ_DATA = new Object[5];
        COMP_HANDLER.OBJ_DATA[0] = txKode.getText(); 
        COMP_HANDLER.OBJ_DATA[1] = txNama.getText(); 
        COMP_HANDLER.OBJ_DATA[2] = "OH"; 
        COMP_HANDLER.OBJ_DATA[3] = Double.valueOf(txHarga.getText());
        COMP_HANDLER.OBJ_DATA[4] = txKode.getText();
        try {
            DB.setSTMT("UPDATE tenaga SET kdTenaga=?, nmTenaga=?, satuan=?, harga=? WHERE kdTenaga=?");
            DB.insertSQL(COMP_HANDLER.OBJ_DATA, "tenaga", "kdTenaga");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        tblTenaga.getColumns().clear();
        loadData();
        tblTenaga.refresh();
    }

    @FXML
    private void deleteData(ActionEvent event) {
        if(tblTenaga.getFocusModel().getFocusedIndex() == -1) {
            ObservableList<Object> selected = FXCollections.observableArrayList();
            COMP_HANDLER.OBJ_DATA = new Object[tblTenaga.getColumns().size()];
            COMP_HANDLER.OBJ_DATA[0] = txKode.getText();
            selected.add(COMP_HANDLER.OBJ_DATA);
            selected.forEach(tblTenaga.getItems()::remove);            
        } else {
            tblTenaga.getItems().remove(tblTenaga.getSelectionModel().getSelectedIndex());
        }
        try {
            DB.setSTMT("DELETE FROM tenaga WHERE kdTenaga=?;");
            int del = DB.deleteSQL(txKode.getText());
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
        tblTenaga.getSelectionModel().clearSelection();
    }
    
}
