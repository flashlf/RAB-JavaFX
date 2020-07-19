/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import Model.Database;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author REDHAT
 */
public class DashboardController implements Initializable {
    Database DB = new Database();
    public MenuLaporanController.Proyek currentProyek; public MenuLaporanController.Konsumen currentKonsumen;
    private KalkulasiController kalkulasiControl;
    private Scene aboutScene;
    private Stage aboutStage;
    public Object[] fokusProyek =  new Object[3];
    @FXML
    private Label txtCostProyek;
    @FXML
    private Label txtTerbilang;
    @FXML
    private Label tglMulai;
    @FXML
    private Label kodeProyek;
    @FXML
    private Label detailProyek;
    @FXML
    private Label txtTotalProyek;
    @FXML
    private VBox DASHBOARD_LEFT_PANEL;
    @FXML
    private ToggleButton menuDashboard;
    @FXML
    private ToggleButton menuProyek;
    @FXML
    private ToggleButton menuData;
    @FXML
    private ToggleButton menuKalkulasi;
    @FXML
    private ToggleButton menuLaporan;
    @FXML
    private ToggleButton menuSettings;
    @FXML
    private Button menuAbout;
    @FXML
    private Button btnLogout;
    @FXML
    private VBox DASHBOARD_RIGHT_PANEL;
    @FXML
    private HBox DASHBOARD_HEADER;
    @FXML
    private ScrollPane DASHBOARD_CONTENT;
    @FXML
    private VBox VBOX_PANEL;
    @FXML
    private ToggleGroup groupDashboard;
    @FXML
    private HeaderProyekController hProyek;
    @FXML
    private FXMLMainController main;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    public void injectController(FXMLMainController Controller){
        this.main = Controller;
        if(null == main.currentUser.getLevel()) {
            menuSettings.setDisable(false);
        } else switch (main.currentUser.getLevel()) {
            case 1:
                menuSettings.setDisable(true);
                break;
            case 2:
                menuLaporan.setDisable(false);
                break;
            default:
                menuSettings.setDisable(false);
                break;
        }
    }
    private void changePanel(String FXML_NAME) {
        try {
            VBOX_PANEL.getChildren().clear();
            Node nodes = (Node)FXMLLoader.load(getClass().getResource("/View/"+FXML_NAME+".fxml"));
            VBOX_PANEL.getChildren().add(nodes);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    private void changePanel(String FXML_NAME, KalkulasiController Controller) {
        try {
            VBOX_PANEL.getChildren().clear();
            FXMLLoader fLoader = new FXMLLoader(getClass().getResource("/View/"+FXML_NAME+".fxml"));
            Node nodes = (Node) fLoader.load();
            Controller = fLoader.<KalkulasiController>getController();
            if(currentProyek != null)
                Controller.kodeProyek = currentProyek.getKODE();
            Controller.loadData();
            VBOX_PANEL.getChildren().add(nodes);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    public void loadSettingPanel(javafx.event.ActionEvent e) {
        changePanel("Settings");
    }
    @FXML
    public void loadDataPanel(javafx.event.ActionEvent e) {
        changePanel("MenuData");
    }
    @FXML
    public void loadLaporanPanel(javafx.event.ActionEvent e) {
        changePanel("MenuLaporan");
    }
    @FXML
    public void loadProyekPanel(javafx.event.ActionEvent e) {
        try {
            //set header
            DASHBOARD_HEADER.getChildren().clear();
            FXMLLoader fLoader = new FXMLLoader(getClass().getResource("/View/HeaderProyek.fxml"));
            Node[] hnode = new Node[1];
            hnode[0] = (Node) fLoader.load();
            hProyek = fLoader.<HeaderProyekController>getController();
            hProyek.injectMainController(this);
            DASHBOARD_HEADER.getChildren().add(hnode[0]);
            //set fokusproyek pada headerproyek
            if(this.fokusProyek[0] != null)
                hProyek.setFokusProyek(fokusProyek);
            //set konten
            DB.inisiasiDB();
            String SQL = "SELECT * FROM proyek";
            java.sql.ResultSet RSET =  DB.getSQL(SQL);
            VBOX_PANEL.getChildren().clear();
            RSET.last();
            Node[] nodes = new Node[RSET.getRow()];
            RSET.beforeFirst();
            for(int i=0; i<nodes.length; i++) {
                try{
                    Object[] HasilSQL = new Object[7];
                    RSET.next();
                    DB.setSTMT("SELECT * FROM konsumen where kdKonsumen='"+RSET.getString(6)+"' ");
                    java.sql.ResultSet KONSUMEN = DB.getSQL(); KONSUMEN.next();
                    HasilSQL[0] = RSET.getString(1); HasilSQL[1] = RSET.getString(2).replaceAll("<br>", "\n"); 
                    HasilSQL[2] = RSET.getLong(3); HasilSQL[3] = RSET.getString(4);
                    HasilSQL[4] = RSET.getString(5); HasilSQL[5] = RSET.getString(6);
                    HasilSQL[6] = RSET.getString(7);
                    fLoader = new FXMLLoader(getClass().getResource("/View/ThumbnailProyek.fxml"));
                    nodes[i]  = (Node) fLoader.load();
                    ThumbnailProyekController ProyekController = fLoader.<ThumbnailProyekController>getController();
                    ProyekController.injectController(this);
                    ProyekController.injectController(hProyek);
                    ProyekController.setData(HasilSQL);
                    ProyekController.setPOJOProyek(HasilSQL);
                    HasilSQL[0] = KONSUMEN.getString(1);
                    HasilSQL[1] = KONSUMEN.getString(2);
                    HasilSQL[2] = KONSUMEN.getString(3);
                    HasilSQL[3] = KONSUMEN.getString(4);
                    ProyekController.setPOJOKonsumen(HasilSQL);
                    VBOX_PANEL.getChildren().add(nodes[i]);
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }catch (java.sql.SQLException | java.io.IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void loadKalkulasiPanel(ActionEvent event) {
        changePanel("Kalkulasi", kalkulasiControl);
    }

    @FXML
    private void loadAboutPanel(ActionEvent event) throws IOException {
        if(aboutStage == null) {
            FXMLLoader fLoader = new FXMLLoader(getClass().getResource("/View/About.fxml"));
            Parent parent = fLoader.load();
            aboutScene  = new Scene(parent);   
            aboutStage = new Stage();
            aboutStage.setTitle("RAB | About");
            aboutStage.resizableProperty().setValue(Boolean.FALSE);
            aboutStage.setScene(aboutScene);
            aboutStage.show();
        } else {
            aboutStage.toFront();
            aboutStage.show();
        }
    }

    @FXML
    private void Logout(ActionEvent event) throws java.io.IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FXMLMain.fxml"));
        Stage mainStage = (Stage) btnLogout.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        mainStage.setScene(scene);
        mainStage.centerOnScreen();
    }

    public VBox getVBOX_PANEL() {
        return VBOX_PANEL;
    }

    public void setVBOX_PANEL(VBox VBOX_PANEL) {
        this.VBOX_PANEL = VBOX_PANEL;
    }
}
