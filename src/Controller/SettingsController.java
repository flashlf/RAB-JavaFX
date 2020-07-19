/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

/**
 * FXML Controller class
 *
 * @author REDHAT
 */
public class SettingsController implements Initializable {

    @FXML
    private TextField txUsername;
    @FXML
    private Button btnEditProyek;
    @FXML
    private FontAwesomeIconView CONTROL_ICON_LOCK;
    @FXML
    private TextField txHostname;
    @FXML
    private TextField txPassword;
    @FXML
    private TextField txDatabase;
    @FXML
    private TextField txPort;
    @FXML
    private ToggleButton CONTROL_LOCK;
    @FXML
    private Button CONTROL_RESET;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void toggleSetting(ActionEvent event) {
        if(CONTROL_LOCK.isSelected())  {
            CONTROL_ICON_LOCK.setGlyphName("LOCK");
            txDatabase.setEditable(false);
            txPort.setEditable(false);
            txPassword.setEditable(false);
            txUsername.setEditable(false);
            txHostname.setEditable(false);
            this.txPort.setDisable(true);
            this.txHostname.setDisable(true);
            this.txDatabase.setDisable(true);
            this.txPassword.setDisable(true);
            this.txUsername.setDisable(true);
            CONTROL_RESET.setDisable(true);
        } else {
            CONTROL_ICON_LOCK.setGlyphName("UNLOCK_ALT");
            txDatabase.setEditable(true);
            txPort.setEditable(true);
            txPassword.setEditable(true);
            txUsername.setEditable(true);
            txHostname.setEditable(true);
            this.txPort.setDisable(false);
            this.txHostname.setDisable(false);
            this.txDatabase.setDisable(false);
            this.txPassword.setDisable(false);
            this.txUsername.setDisable(false);
            CONTROL_RESET.setDisable(false);
        }
    }

    @FXML
    private void resetSetting(ActionEvent event) {
        txDatabase.setText("");
        txPort.setText("");
        txPassword.setText("");
        txUsername.setText("");
        txHostname.setText("");
    }
    
}
