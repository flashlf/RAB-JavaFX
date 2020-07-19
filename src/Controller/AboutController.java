/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author REDHAT
 */
public class AboutController implements Initializable {

    @FXML
    private FontAwesomeIconView githubLink;
    @FXML
    private Label linkFB;
    @FXML
    private Label linkGithub;
    @FXML
    private Label linkLinkedIn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void openSourceProject(MouseEvent event) {
    }

    @FXML
    private void openFacebook(MouseEvent event) {
    }

    @FXML
    private void openGithub(MouseEvent event) {
    }

    @FXML
    private void openLinkedIn(MouseEvent event) {
    }
    
}
