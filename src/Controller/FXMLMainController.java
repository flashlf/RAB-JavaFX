/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author REDHAT
 */
public class FXMLMainController implements Initializable {
    private Model.Database DB;
    private final ObservableList<User> ListUser;
    public User currentUser;
    private boolean loggedIn = false;
    private DashboardController dashboardController;
    private Label label;
    @FXML
    private Label lblForgot;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblRegister;
    @FXML
    private TextField txUsername;
    @FXML
    private PasswordField txPassword;

    public FXMLMainController() {
        this.ListUser = FXCollections.observableArrayList();
        this.DB = new Model.Database();
    }
    
    @FXML
    private void initLogin(ActionEvent event) {
        try {
            if(currentUser == null)
                currentUser = new User("",txUsername.getText(),txPassword.getText(),1);
            else {
                currentUser.setUsername(txUsername.getText());
                currentUser.setPassword(txPassword.getText());
            }
                
            for(User compare : this.ListUser) {
                if(currentUser.getUsername().equals(compare.getUsername()))
                    if(currentUser.getPassword().equals(compare.getPassword())) {
                        currentUser.setId(compare.getId());
                        currentUser.setLevel(compare.getLevel());
                        this.loggedIn = true;
                        break;
                    }
            }
            if(loggedIn){
                System.out.println("Clicked Login Button\nSwitching Stage now"
                        + "\nWelcome User "+currentUser.getUsername());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Dashboard.fxml"));
                Parent parent = loader.load();
                dashboardController = loader.<DashboardController>getController();
                dashboardController.injectController(this);
                Stage mainStage = (Stage) btnLogin.getScene().getWindow();
                Scene scene = new Scene(parent);
                mainStage.setScene(scene);
                mainStage.centerOnScreen();
            } else {
                System.out.println("WHO ARE YOU?");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            DB.inisiasiDB();
            if(this.ListUser.size() < 1) {
                DB.setSTMT("SELECT * FROM user");        
                java.sql.ResultSet SET = DB.getSQL();
                while(SET.next()) {                    
                    this.ListUser.add(new User(SET.getString(1),SET.getString(2),SET.getString(3),SET.getInt(4)));
                }

            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
    
    public static class User {
        // properti kelas
        private final SimpleStringProperty ID, USERNAME, PASSWORD;
        private final SimpleIntegerProperty LEVEL;
        private final int HASH;
        // Konstruktor
        public User() {
            this.ID = new SimpleStringProperty("NULL");
            this.USERNAME = new SimpleStringProperty("NULL");
            this.PASSWORD = new SimpleStringProperty("NULL");
            this.LEVEL = new SimpleIntegerProperty(1);
            this.HASH = 432 *  1337;
        } 
        public User(String Id, String Username, String Password, Integer Level) {
            this.ID = new SimpleStringProperty(Id);
            this.USERNAME = new SimpleStringProperty(Username);
            this.PASSWORD = new SimpleStringProperty(Password);
            this.LEVEL = new SimpleIntegerProperty(Level);
            this.HASH = 432 *  1337;
        } 
        
        // metod getter dan setter
        public void setLevel(Integer param) {
            this.LEVEL.set(param);
        }
        public void setId(String param)  {
            this.ID.set(param);
        }
        public void setUsername(String param) {
            this.USERNAME.set(param);
        }
        public void setPassword(String param) {
            this.PASSWORD.set(param);
        }
        
        public Integer getLevel() {
            return LEVEL.get();
        }
        public String getId(){
            return ID.get();
        }
        public String getPassword(){
            return PASSWORD.get();
        }
        public String getUsername(){
            return USERNAME.get();
        }
        @Override
        public int hashCode(){
            return HASH;
        }
        @Override
        public boolean equals(Object obj) {
            return !(obj == null || getClass() != obj.getClass());
        }
    }
}
