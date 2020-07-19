/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import Model.Database;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
/**
 * FXML Controller class
 *
 * @author REDHAT
 */
public class HeaderProyekController implements Initializable {
    Database DB = new Database();
    protected String SORT_TYPE = "ASC";
    @FXML
    private ComboBox<kolomProyek> cbUrutData;
    @FXML
    private ToggleButton btnAsc;
    @FXML
    private ToggleGroup TipePengurutan;
    @FXML
    private Label lblProyekSelesai;
    @FXML
    private Label lblProyekBelum;
    @FXML
    private Label lblTotalProyek;
    @FXML
    private Button btnSort;
    @FXML
    private AnchorPane headerProyek;
    private DashboardController mainControl;
    @FXML
    private MaterialDesignIconView CONTROL_ASC_ICON;
    @FXML
    private Label lblKdProyekTerbaru;
    @FXML
    private TextField txKriteriaFilter;
    @FXML
    private Label lblDeskripsiProyekTerbaru;
    @FXML
    private Label lblNilaiProyekTerbaru;
    @FXML
    private Label lblKdProyekFokus;
    @FXML
    private Label lblDeskripsiProyekFokus;
    @FXML
    private Label lblNilaiProyekFokus;

    public static String angkaShort(Long angka){
        if(angka >= 1 && angka <= 999)
            return angka+"";
        if(angka >= 1000 && angka <= 999999)
            return (angka / 1000)+" K";
        if(angka >= 1000000 && angka <= 999999999)
            return (angka / 1000000)+" Juta";
        if(angka >= 1000000000 && angka <= 999999999999L)
            return (angka / 1000000000)+" Milyar";
        if(angka >= 1000000000000L && angka <= 999999999999999L)
            return (angka / 1000000000000L)+" Triliun";
        if(angka >= 1000000000000000L && angka <= 999999999999999999L)
            return (angka / 1000000000000000L)+" Billiun";
            return "";
    }
    public void setFokusProyek(Object[] Data) {
        lblKdProyekFokus.setText(Data[0].toString());
        lblDeskripsiProyekFokus.setText(Data[1].toString());        
        lblNilaiProyekFokus.setText(angkaShort(Long.parseLong(Data[2].toString())));
    }
    @FXML
    private void tipeUrut(ActionEvent event) {
        if(btnAsc.isSelected()){
            btnAsc.setText("DESC");
            CONTROL_ASC_ICON.setGlyphName("SORT_DESCENDING");
            SORT_TYPE = "DESC";
        }else{
            btnAsc.setText("ASC");
            CONTROL_ASC_ICON.setGlyphName("SORT_ASCENDING");
            SORT_TYPE = "ASC";
        }
    }
    
    public class kolomProyek {
        private String displayKolom, dbKolom;
        
        public kolomProyek(String display, String kolomDB) {
            this.displayKolom = display;
            this.dbKolom = kolomDB;
        }
        public String getDisplayKolom() {
            return displayKolom;
        }

        public void setDisplayKolom(String displayKolom) {
            this.displayKolom = displayKolom;
        }

        public String getDbKolom() {
            return dbKolom;
        }

        public void setDbKolom(String dbKolom) {
            this.dbKolom = dbKolom;
        }
        
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cbUrutData.setItems(FXCollections.observableArrayList(
            new kolomProyek("Kode Proyek","kdProyek"),
            new kolomProyek("Detil Proyek","deskripsi"),
            new kolomProyek("Nilai Proyek","nilai"),
            new kolomProyek("Tanggal Mulai","tglMulai"),
            new kolomProyek("Tanggal Selesai","tglSelesai")));
        cbUrutData.setConverter(new StringConverter<kolomProyek>(){
            @Override
            public String toString(kolomProyek Object) {
                return Object.getDisplayKolom();
            }
            @Override
            public kolomProyek fromString(String string) {
                return null;
            }
        });
        setData();
    }    
    public void injectMainController(DashboardController Controller){
        this.mainControl = Controller;
    }
    public void setData() {
        try {
            DB.inisiasiDB();
            String SQL="SELECT COUNT(*) AS totalPROYEK, kdProyek,deskripsi,nilai FROM proyek ORDER BY tglMulai DESC";            
            java.sql.ResultSet RSET = DB.getSQL(SQL);
            RSET.next();
            lblTotalProyek.setText(RSET.getString(1));
            lblDeskripsiProyekTerbaru.setText(RSET.getString(3));
            lblKdProyekTerbaru.setText(RSET.getString(2));
            lblNilaiProyekTerbaru.setText(angkaShort(RSET.getLong(4)));
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void sortData(ActionEvent event) {
        try {
            DB.inisiasiDB();
            String SQL;
            String kriteria = txKriteriaFilter.getText(), orderby = cbUrutData.getItems().get(cbUrutData.getSelectionModel().getSelectedIndex()).dbKolom;
            if(kriteria.length()==0) 
                SQL = "SELECT * FROM PROYEK ORDER BY "+orderby+" "+SORT_TYPE;
            else {
                if(!orderby.equalsIgnoreCase("nilai"))
                    SQL = "SELECT * FROM PROYEK WHERE "+orderby+"='"+kriteria+"'"
                      +" ORDER BY "+orderby+" "+SORT_TYPE;
                else
                    SQL = "SELECT * FROM PROYEK WHERE "+orderby+"=>"+Integer.parseInt(kriteria)
                      +" ORDER BY "+orderby+" "+SORT_TYPE;
            }
            java.sql.ResultSet RSET = DB.getSQL(SQL);
            mainControl.getVBOX_PANEL().getChildren().clear();
            RSET.last();
            Node[] nodes = new Node[RSET.getRow()];
            RSET.beforeFirst();
            for(int i=0; i<nodes.length; i++) {
                try{
                    Object[] HasilSQL = new Object[5];
                    RSET.next();
                    HasilSQL[0] = RSET.getString(1); HasilSQL[1] = RSET.getString(2).replaceAll("<br>", "\n"); 
                    HasilSQL[2] = RSET.getLong(3); HasilSQL[3] = RSET.getString(4);
                    HasilSQL[4] = RSET.getString(5);                    
                    FXMLLoader fLoader = new FXMLLoader(getClass().getResource("/View/ThumbnailProyek.fxml"));
                    nodes[i]  = (Node) fLoader.load();
                    ThumbnailProyekController ProyekController = fLoader.<ThumbnailProyekController>getController();
                    ProyekController.injectController(this);
                    ProyekController.injectController(mainControl);
                    ProyekController.setData(HasilSQL);
                    mainControl.getVBOX_PANEL().getChildren().add(nodes[i]);
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }            
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
    }
    
}
