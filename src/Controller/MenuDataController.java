/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author REDHAT
 */
public class MenuDataController implements Initializable {
    Parent parent;
    FXMLLoader fxLoader;
    // Controller
    MasterUserController userControl;
    MasterKoordinatorController koordinatorControl;
    MasterBahanController bahanControl;
    MasterTenagaController tenagaControl;
    MasterProyekController proyekControl;
    KonsumenController konsumenControl;
    @FXML
    private Button btnMaterial;
    @FXML
    private Button btnTenaga;
    @FXML
    private Button btnUser;
    @FXML
    private Button btnKonsumen;
    @FXML
    private Button btnProyek;
    @FXML
    private Button btnKoordinator;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    @FXML
    private void openMaterial(ActionEvent event) throws java.io.IOException{
        fxLoader = new FXMLLoader(getClass().getResource("/View/MasterBahan.fxml"));
        parent = fxLoader.load(); bahanControl = fxLoader.getController();
        Scene sceneTenaga  = new Scene(parent);
        Stage window = new Stage(); 
        //window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("RAB | Data - MasterBahan");
        window.resizableProperty().setValue(Boolean.FALSE);
        window.setScene(sceneTenaga);
        window.show();
    }

    @FXML
    private void openTenaga(ActionEvent event) throws java.io.IOException{
        fxLoader = new FXMLLoader(getClass().getResource("/View/MasterTenaga.fxml"));
        parent = fxLoader.load(); tenagaControl = fxLoader.getController();
        Stage window = new Stage(); 
        Scene sceneTenaga  = new Scene(parent);
        //window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("RAB | Data - MasterTenaga");
        window.resizableProperty().setValue(Boolean.FALSE);
        window.show();
        window.setScene(sceneTenaga);
    }

    @FXML
    private void openUser(ActionEvent event) throws java.io.IOException{
        fxLoader = new FXMLLoader(getClass().getResource("/View/MasterUser.fxml"));
        parent = fxLoader.load(); userControl = fxLoader.getController();
        userControl.loadData("SELECT * FROM user");
        Scene sceneTenaga  = new Scene(parent);
        Stage window = new Stage(); 
        //window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("RAB | Data - User");
        window.resizableProperty().setValue(Boolean.FALSE);
        window.setScene(sceneTenaga);
        window.show();
    }

    @FXML
    private void openKonsumen(ActionEvent event) throws java.io.IOException{
        fxLoader = new FXMLLoader(getClass().getResource("/View/Konsumen.fxml"));
        parent = fxLoader.load(); konsumenControl = fxLoader.getController();
        Scene sceneTenaga  = new Scene(parent);
        Stage window = new Stage(); 
        //window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("RAB | Data - Konsumen");
        window.resizableProperty().setValue(Boolean.FALSE);
        window.setScene(sceneTenaga);
        window.show();
    }

    @FXML
    private void openProyek(ActionEvent event) throws java.io.IOException{
        fxLoader = new FXMLLoader(getClass().getResource("/View/MasterProyek.fxml"));
        parent = fxLoader.load(); proyekControl = fxLoader.getController();
        Scene sceneTenaga  = new Scene(parent);
        Stage window = new Stage(); 
        //window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("RAB | Data - Proyek");
        window.resizableProperty().setValue(Boolean.FALSE);
        window.setScene(sceneTenaga);
        window.show();
    }

    @FXML
    private void openKoordinator(ActionEvent event) throws IOException {
        fxLoader = new FXMLLoader(getClass().getResource("/View/MasterKoordinator.fxml"));
        parent = fxLoader.load(); koordinatorControl = fxLoader.getController();
        koordinatorControl.loadData("SELECt * From koordinator");
        Scene sceneTenaga  = new Scene(parent);
        Stage window = new Stage(); 
        //window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("RAB | Data - Koordinator");
        window.resizableProperty().setValue(Boolean.FALSE);
        window.setScene(sceneTenaga);
        window.show();
    }
    
}
