/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author helos
 */
public class MasterUserController implements Initializable {
    Model.Database DB = new Model.Database();
    Object[] OBJ_DATA;
    String SQL;
    @FXML
    private TextField txSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private TableView<User> tblUser;
    @FXML
    private TextField txUsername;
    @FXML
    private TextField txID;
    @FXML
    private TextField txPassword;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private ComboBox<Level> cbLevel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setColumnTable();
        try {
            cbLevel.setItems(FXCollections.observableArrayList(
            new Level("Administrator",0),
            new Level("Staff",1),
            new Level("Direktur",2)
                    ));
            cbLevel.setConverter(new StringConverter<Level>(){
                @Override
                public String toString(Level Object) {
                    return Object.getDISPLAY();
                }
                @Override
                public Level fromString(String string) {
                    return null;
                }
            });
            loadData("SELECT * FROM user");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
            
    }    

    @FXML
    private void searchData(ActionEvent event) {
        if(txSearch.getText().isEmpty()) {
            tblUser.getColumns().clear();
            setColumnTable();
            loadData("SELECT * FROM user");
        } else {
            tblUser.getColumns().clear();
            setColumnTable();
            String crite = txSearch.getText();
            String SQL = "SELECT * FROM user WHERE ID like '%"+crite+"%'"
            + " or Username like '%"+crite+"%'"
            + " or Password like '%"+crite+"%'";
            loadData(SQL);
        }
        tblUser.refresh();
    }

    @FXML
    private void getData(MouseEvent event) {
        if(event.getClickCount() == 2 && !event.isConsumed()) {
            User temp = tblUser.getSelectionModel().getSelectedItem();
//            if(laporanController != null) {
                txID.setText(temp.getID());
                txUsername.setText(temp.getUSERNAME());
                txPassword.setText(temp.getPASSWORD());
                switch(temp.getLEVEL()) {
                    case 0 :
                        cbLevel.getSelectionModel().select(0);
                        break;
                    case 1 :
                        cbLevel.getSelectionModel().select(1);
                        break;
                    case 2 :
                        cbLevel.getSelectionModel().select(2);
                        break;
                    default :
                        System.out.println("Error getting level data user");
                }
//            } else {
//                this.suratTugas.getFromKoordinator(temp);
//            }
            tblUser.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void addData(ActionEvent event) {
        User temp = new User(txID.getText(), txUsername.getText(), txPassword.getText(), cbLevel.getSelectionModel().getSelectedItem().getVALUE());
        OBJ_DATA = new Object[4];
        OBJ_DATA[0] = temp.getID();
        OBJ_DATA[1] = temp.getUSERNAME();
        OBJ_DATA[2] = temp.getPASSWORD();
        OBJ_DATA[3] = temp.getLEVEL();
        //add to table
        tblUser.getItems().add(temp);
        try { // save to DB
            DB.inisiasiDB();
            DB.setSTMT("INSERT INTO user VALUES(?, ?, ?, ?)");            
            DB.insertSQL(OBJ_DATA, "user", "ID");  
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void editData(ActionEvent event) {
        boolean find = false;
        SQL = "UPDATE user SET Username=?, Password=?, Level=? WHERE ID=?";
        User temp = new User(txID.getText(), txUsername.getText(), txPassword.getText(), cbLevel.getSelectionModel().getSelectedItem().getVALUE());
        ObservableList<User> contain = tblUser.getItems();
        OBJ_DATA = new Object[4];
        OBJ_DATA[3] = temp.getID();
        OBJ_DATA[0] = temp.getUSERNAME();
        OBJ_DATA[1] = temp.getPASSWORD();
        OBJ_DATA[2] = cbLevel.getSelectionModel().getSelectedItem().getVALUE();
        if(tblUser.getSelectionModel().getSelectedIndex() >= 0) {
            int i = tblUser.getSelectionModel().getSelectedIndex();
            tblUser.getItems().set(i, temp);
        } else {            
            int x = 0;
            for(User item : contain) {
                if(item.getID().equalsIgnoreCase(temp.getID())) {
                    find = true; break;                    
                }
                x++;
            }
            if(find) {
                tblUser.getItems().set(x, temp);                
            }
        }
        try {
            DB.setSTMT(SQL);
            DB.insertSQL(OBJ_DATA, "user", "ID");
        } catch(java.sql.SQLException ex) {
            ex.printStackTrace();
        } finally {
            tblUser.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void deleteData(ActionEvent event) throws SQLException {
        boolean find = false;
        SQL = "DELETE FROM user WHERE ID= ?";
        if(tblUser.getItems().size()>0) {
            User temp = new User(txID.getText(), txUsername.getText(), txPassword.getText(), cbLevel.getSelectionModel().getSelectedItem().getVALUE());
            ObservableList<User> contain = tblUser.getItems();
            if(tblUser.getSelectionModel().getSelectedIndex() >= 0) {
                DB.deleteSQL(tblUser.getItems().get(tblUser.getSelectionModel().getSelectedIndex()).getID());
                tblUser.getItems().remove(tblUser.getSelectionModel().getSelectedIndex());                
            } else  {
                int x = 0;
                for(User item : contain) {
                    if(item.getID().equalsIgnoreCase(temp.getID())) {
                        find = true; break;                    
                    }
                    x++;
                }
                if(find) {
                    tblUser.getItems().remove(x);                
                    DB.deleteSQL(temp.getID());
                }
            }
        }
    }
    
    public void loadData(String SQL){
        ObservableList<User> CONT_MAIN = FXCollections.observableArrayList();
        try {
            DB.inisiasiDB();
            DB.setSTMT(SQL);
            java.sql.ResultSet SET = DB.getSQL();
            while(SET.next()) {
                CONT_MAIN.add(new User(SET.getString(1), SET.getString(2), SET.getString(3), SET.getInt(4)));
            }
            tblUser.setItems(CONT_MAIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setColumnTable(){
        TableColumn col1, col2, col3, col4;
        col1 = new TableColumn("ID");
        col1.setMinWidth(50);
        col1.setCellValueFactory(new PropertyValueFactory<User, String>("ID"));
        
        col2 = new TableColumn("Username");
        col2.setMinWidth(100);
        col2.setCellValueFactory(new PropertyValueFactory<User, String>("USERNAME"));
        
        col3 = new TableColumn("Password");
        col3.setMinWidth(100);
        col3.setCellValueFactory(new PropertyValueFactory<User, String>("PASSWORD"));
        
        col4 = new TableColumn("Level");
        col4.setMinWidth(30);
        col4.setCellValueFactory(new PropertyValueFactory<User, Integer>("LEVEL"));
        tblUser.getColumns().addAll(col1, col2, col3, col4);
    }
    
    public static class User {
        // properti kelas
        private final SimpleStringProperty ID, USERNAME, PASSWORD;
        private final SimpleIntegerProperty LEVEL;
        //private final int HASH;
        // Konstruktor
        public User() {
            this.ID = new SimpleStringProperty("NULL");
            this.USERNAME = new SimpleStringProperty("NULL");
            this.PASSWORD = new SimpleStringProperty("NULL");
            this.LEVEL = new SimpleIntegerProperty(1);
           // this.HASH = 432 *  1337;
        } 
        public User(String Id, String Username, String Password, Integer Level) {
            this.ID = new SimpleStringProperty(Id);
            this.USERNAME = new SimpleStringProperty(Username);
            this.PASSWORD = new SimpleStringProperty(Password);
            this.LEVEL = new SimpleIntegerProperty(Level);
           // this.HASH = 432 *  1337;
        } 
        
        // metod getter dan setter
        public void setLEVEL(Integer param) {
            this.LEVEL.set(param);
        }
        public void setID(String param)  {
            this.ID.set(param);
        }
        public void setUSERNAME(String param) {
            this.USERNAME.set(param);
        }
        public void setPASSWORD(String param) {
            this.PASSWORD.set(param);
        }
        
        public Integer getLEVEL() {
            return LEVEL.get();
        }
        public String getID(){
            return ID.get();
        }
        public String getPASSWORD(){
            return PASSWORD.get();
        }
        public String getUSERNAME(){
            return USERNAME.get();
        }
//        @Override
//        public int hashCode(){
//            return HASH;
//        }
//        @Override
//        public boolean equals(Object obj) {
//            return !(obj == null || getClass() != obj.getClass());
//        }
    }
    public class Level {
        private String DISPLAY;
        private Integer VALUE;
        public Level(String DISPLAY, Integer VALUE) {
            this.DISPLAY = DISPLAY;
            this.VALUE = VALUE;
        }
        public String getDISPLAY() {
            return DISPLAY;
        }

        public void setDISPLAY(String DISPLAY) {
            this.DISPLAY = DISPLAY;
        }

        public Integer getVALUE() {
            return VALUE;
        }

        public void setVALUE(Integer VALUE) {
            this.VALUE = VALUE;
        }
        
    }
}
