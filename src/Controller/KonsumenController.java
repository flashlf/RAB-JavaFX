/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import Model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
/**
 * FXML Controller class
 *
 * @author REDHAT
 */
public class KonsumenController implements Initializable {
    Database DB = new Database();
    HandlerComponent COMP_HANDLER;
    @FXML
    private TableView<ObservableList<Object>> tblKonsumen;
    @FXML
    private TextArea txAlamat;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDel;
    @FXML
    private TextField txKode;
    @FXML
    private TextField txNama;
    @FXML
    private TextField txNomer;
    @FXML
    private TextField txSearch;
    @FXML
    private Button btnSearch;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadData();
    }    
    
    private void loadData() {
        try {
            if(COMP_HANDLER == null)
                COMP_HANDLER = new HandlerComponent();
            //java.sql.ResultSet SQL
            tblKonsumen = COMP_HANDLER.iniTable(tblKonsumen, "SELECT * FROM konsumen", DB);
        } catch(java.sql.SQLException ex) {
            ex.printStackTrace();
        }
    }
        
    @FXML
    private void getData(MouseEvent event) {
        if(event.getClickCount() == 2 && !event.isConsumed()) {
            ObservableList<Object> data = (ObservableList<Object>) tblKonsumen.getSelectionModel().getSelectedItem();            
            System.out.println("Get IT DONE BITCHES \n"
                    + "Kolom 1:"+data.get(0)+"\n"
                    + "Kolom 2:"+data.get(1)+"\n"
                    + "Kolom 3:"+data.get(2)+"\n"
                    + "Kolom 4:"+data.get(3));
            txKode.setText(data.get(0).toString());
            txNama.setText(data.get(1).toString());            
            txAlamat.setText(data.get(2).toString());
            txNomer.setText(data.get(3).toString());
            tblKonsumen.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void addData(ActionEvent event) {
        ObservableList<Object> row = FXCollections.observableArrayList();
        COMP_HANDLER.OBJ_DATA = new Object[tblKonsumen.getColumns().size()];
        COMP_HANDLER.OBJ_DATA[0] = txKode.getText(); row.add(COMP_HANDLER.OBJ_DATA[0]);
        COMP_HANDLER.OBJ_DATA[1] = txNama.getText(); row.add(COMP_HANDLER.OBJ_DATA[1]);        
        COMP_HANDLER.OBJ_DATA[2] = txAlamat.getText(); row.add(COMP_HANDLER.OBJ_DATA[2]);
        COMP_HANDLER.OBJ_DATA[3] = txNomer.getText(); row.add(COMP_HANDLER.OBJ_DATA[3]);
        try {
            DB.setSTMT("INSERT INTO proyek VALUES(?, ?, ?, ?)");            
            DB.insertSQL(COMP_HANDLER.OBJ_DATA, "konsumen", "kdKonsumen");            
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
        tblKonsumen.getItems().add(row);
    }

    @FXML
    private void editData(ActionEvent event) {
        COMP_HANDLER.OBJ_DATA = new Object[5];        
        COMP_HANDLER.OBJ_DATA[0] = txKode.getText(); COMP_HANDLER.OBJ_DATA[4] = txKode.getText();         
        COMP_HANDLER.OBJ_DATA[1] = txNama.getText();    
        COMP_HANDLER.OBJ_DATA[2] = txAlamat.getText();
        COMP_HANDLER.OBJ_DATA[3] = txNomer.getText();
        try {
            DB.setSTMT("UPDATE konsumen SET kdKonsumen=?, nmKonsumen=?, alamat=?, noTelp=? WHERE kdKonsumen=?");
            DB.insertSQL(COMP_HANDLER.OBJ_DATA, "konsumen", "kdKonsumen");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        tblKonsumen.getColumns().clear();
        loadData();
        tblKonsumen.refresh();
    }

    @FXML
    private void deleteData(ActionEvent event) {
        if(tblKonsumen.getFocusModel().getFocusedIndex() == -1) {
            ObservableList<Object> selected = FXCollections.observableArrayList();
            COMP_HANDLER.OBJ_DATA = new Object[tblKonsumen.getColumns().size()];
            COMP_HANDLER.OBJ_DATA[0] = txKode.getText();
            selected.add(COMP_HANDLER.OBJ_DATA);
            selected.forEach(tblKonsumen.getItems()::remove);            
        } else {
            tblKonsumen.getItems().remove(tblKonsumen.getSelectionModel().getSelectedIndex());
        }
        try {
            DB.setSTMT("DELETE FROM konsumen WHERE kdKonsumen=?;");
            int del = DB.deleteSQL(txKode.getText());
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
        tblKonsumen.getSelectionModel().clearSelection();
    }

    @FXML
    private void searchData(ActionEvent event) {
        tblKonsumen.getColumns().clear();
        if(txSearch.getText().isEmpty())
            tblKonsumen = COMP_HANDLER.iniTable(tblKonsumen, "SELECT * FROM konsumen", DB);
        else {
            String crite  = txSearch.getText();
            String SQL = "SELECT * FROM konsumen WHERE kdKonsumen like '%"+crite+"%'"
            + " or nmKonsumen like '%"+crite+"%'"
            + " or alamat like '%"+crite+"%'"
            + " or noTelp like '%"+crite+"%'";
            
            tblKonsumen = COMP_HANDLER.iniTable(tblKonsumen, SQL, DB);            
        }
        tblKonsumen.refresh();
    }
    
}
