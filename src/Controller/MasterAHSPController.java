/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * FXML Controller class
 *
 * @author helos
 */
public class MasterAHSPController implements Initializable {
    Model.Database DB;
    public String kodeProyek = "B101";
    public Integer indexList = 0, tblSize = 0;
    private Stage subStage;
    private KalkulasiController kalkulasiController;
    private AHSPController subController;
    protected JAXBContext jContext; protected Unmarshaller um; protected Marshaller msr;
    protected Model.AnlKerja anlKerja;
    protected ArrayList<Model.AnlKerja.Analisa> list;
    private final ObservableList<AHSP> dummyData =
        FXCollections.observableArrayList(
            new AHSP(1,"B.1","1 m³ Galian Tanah Dalam s/d 1 m manual",122414.025),
            new AHSP(2,"C.3","1 m³ Pasang Pondasi Batu gunung 1 : 5",1097823.725),
            new AHSP(3,"G.2","1 m³ Beton Mutu K175",1583785.999),
            new AHSP(4,"G.4","1 m³ Beton Mutu K225",1621225.999),
            new AHSP(5,"D.4a","1 m² Dinding batako padat uk. 9X15x30 cm  ad. 1 : 4",136918.635),
            new AHSP(6,"G.7","Pembesian 1 kg Dengan Besi polos",15472.9598)
        );
    @FXML
    private TableView<AHSP> tblSubKalkulasi;
    @FXML
    public Button btnAdd;
    @FXML
    public Button btnDelete;
    @FXML
    public TextField txKodeProyek;
    @FXML
    private TextArea txMasterKalkulasi;
    @FXML
    private Button btnSearch;
    @FXML
    public Button btnLookupKalkulasi;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Setting TableCustom dulu
        setColumnTable();
        //tblSubKalkulasi.setItems(dummyData);
    }    
    public void injectController(KalkulasiController Controller) {
        this.kalkulasiController = Controller;
    }
    private void setColumnTable() {
        TableColumn noCol = new TableColumn("No");
        noCol.setMaxWidth(30); 
        noCol.setCellValueFactory(new PropertyValueFactory<AHSP,String>("No"));
        TableColumn kodeCol = new TableColumn("Kode");
        kodeCol.setMinWidth(50);
        kodeCol.setCellValueFactory(new PropertyValueFactory<AHSP,String>("Kode"));
        TableColumn uraianCol = new TableColumn("Uraian");
        uraianCol.setMinWidth(200); 
        uraianCol.setCellValueFactory(new PropertyValueFactory<AHSP,String>("Uraian"));
        TableColumn costCol = new TableColumn("Total Cost");
        costCol.setMinWidth(150); 
        costCol.setCellValueFactory(new PropertyValueFactory<AHSP,Double>("Cost"));        
        tblSubKalkulasi.getColumns().addAll(noCol, kodeCol, uraianCol, costCol);
    }
    public void openAHSP() throws IOException {
        AHSP data = tblSubKalkulasi.getSelectionModel().getSelectedItem();            
        System.out.println("Get IT DONE BITCHES \n"
            + "Kolom 1:"+data.getDataInfo());                
        this.indexList = data.getNo()-1;
        // Open AHSP
        if(subStage == null) {
            FXMLLoader fLoader = new FXMLLoader(getClass().getResource("/View/AHSP.fxml"));
            Parent parent = fLoader.load();
            subController = fLoader.<AHSPController>getController();
            subController.injectController(this);
            try {
                subController.setColumnTable();
                subController.loadDataXML();                    
            } catch (Exception e) {
                e.printStackTrace();
            }
            Scene subScene  = new Scene(parent);   
            subStage = new Stage();
            subStage.setTitle("RAB | Data - MasterBahan");
            subStage.resizableProperty().setValue(Boolean.FALSE);
            subStage.setScene(subScene);      
            subStage.show();
        } else {
            try {
                subController.tblListBahan.getColumns().clear();
                subController.tblListTenaga.getColumns().clear();
                subController.setColumnTable();
                subController.loadDataXML();
                subController.tblListBahan.refresh();
                subController.tblListTenaga.refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
            subStage.toFront();
            subStage.show();
        }
    }
    @FXML
    private void editData(MouseEvent event) throws java.io.IOException{
        if(event.getClickCount() == 2 && !event.isConsumed()) {
            if(kalkulasiController == null) {
                openAHSP();                
            } else {
                System.out.println("COME HERE ITS NOT DONE YET");
                Object[] DATA = new Object[3];
                DATA[0] = tblSubKalkulasi.getSelectionModel().getSelectedItem().getKode();
                DATA[1] = tblSubKalkulasi.getSelectionModel().getSelectedItem().getUraian();
                DATA[2] = tblSubKalkulasi.getSelectionModel().getSelectedItem().getCost();
                
                Stage thisStage = ((Stage)(((TableView)event.getSource()).getScene().getWindow()));
                Alert.AlertType typeDialog = Alert.AlertType.CONFIRMATION;
                Alert dialog = new Alert(typeDialog, "Info");
                dialog.initModality(Modality.WINDOW_MODAL);
                dialog.initOwner(thisStage);
                dialog.getDialogPane().setContentText("Apakah Anda ingin memilih kode : "+
                    DATA[0].toString()+"\n sebagai data yang akan dikalkulasikan ?\n*Note : tekan cancel untuk mengedit data ini");
                dialog.getDialogPane().setHeaderText("Konfirmasi Aksi");
                Optional<ButtonType> result = dialog.showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK) {
                    kalkulasiController.getSubAHSP(DATA);
                    thisStage.close();
                } else {
                    openAHSP();
                }
                    
                //int ch = JOptionPane.showConfirmDialog(null,"Data akan Dihapus ?","Hapus Data",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
                //if (ch == 0){
                    
                //}
                // buka dari kalkulasi
            }
        }
    }

    @FXML
    private void addAHSP(ActionEvent event) {
        try {
            anlKerja = loadXML(kodeProyek);                
            list = anlKerja.getAnalisa();                    

            ArrayList<Model.AnlKerja.Analisa.Material.Item> item = new ArrayList<>();
            ArrayList<Model.AnlKerja.Analisa.Material> mtr = new ArrayList<>();
            Model.AnlKerja.Analisa.Material.Item it = new Model.AnlKerja.Analisa.Material.Item();
            Model.AnlKerja.Analisa.Material.Item it2 = new Model.AnlKerja.Analisa.Material.Item();

            it.setType("Tenaga");
            it.setValue("");
            item.add(it);

            it2.setType("Bahan");
            it2.setValue("");
            item.add(it2);
            Model.AnlKerja.Analisa.Material mat = new Model.AnlKerja.Analisa.Material();
            mat.setItem(item);
            mtr.add(mat);

            Model.AnlKerja.Analisa ana = new Model.AnlKerja.Analisa();
            
            ana.setNo(tblSize+1);
            ana.setKode("");
            ana.setUraian("");
            ana.setMaterial(mat);
            ana.setJumlah(0.0);
            list.add(ana);

            msr = jContext.createMarshaller();
            msr.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // Tulis ke System.out
            msr.marshal(anlKerja, System.out);

            // tulis file XML
            msr.marshal(anlKerja, new File("output/"+kodeProyek+"-ANL.xml"));
            
            AHSP e = new AHSP(tblSize+1, "", "", 0.0);
            tblSubKalkulasi.getItems().add(e);
            if(subStage == null) {
                FXMLLoader fLoader = new FXMLLoader(getClass().getResource("/View/AHSP.fxml"));
                Parent parent = fLoader.load();
                subController = fLoader.<AHSPController>getController();
                subController.injectController(this);
                subController.setColumnTable();
                subController.loadDataXML();                    
                Scene subScene  = new Scene(parent);   
                subStage = new Stage();
                subStage.setTitle("RAB | Data - MasterBahan");
                subStage.resizableProperty().setValue(Boolean.FALSE);
                subStage.setScene(subScene);      
                subStage.show();
            } else {

                subController.tblListBahan.getColumns().clear();
                subController.tblListTenaga.getColumns().clear();
                subController.setColumnTable();
                subController.loadDataXML();
                subController.tblListBahan.refresh();
                subController.tblListTenaga.refresh();

                subStage.toFront();
                subStage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteAHSP(ActionEvent event) {
        if(tblSubKalkulasi.getItems().size() >  0) {
            // Delete Data in XML
            this.indexList = tblSubKalkulasi.getSelectionModel().getSelectedIndex();
            int noAHSP = tblSubKalkulasi.getItems().get(indexList).getNo();
            tblSubKalkulasi.getItems().remove(tblSubKalkulasi.getSelectionModel().getSelectedIndex());
            --tblSize;
            list.remove(noAHSP-1);
            int x=1;
            for(Model.AnlKerja.Analisa analisa : list) {
                analisa.setNo(x);
                x++;
            }
            for(int i=1; i<=tblSize; i++) {
                tblSubKalkulasi.getItems().get(i-1).setNo(i);
            }            
            System.out.println("DELETED ========\nTable Size : "+tblSize);
            try {
                msr = jContext.createMarshaller();
                msr.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                // Tulis ke System.out
                msr.marshal(this.anlKerja, System.out);

                // tulis file XML
                msr.marshal(this.anlKerja, new File("output/"+this.kodeProyek+"-ANL.xml"));
                
                // Delete Data in DB
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            tblSubKalkulasi.getSelectionModel().clearSelection();            
        }
            
    }
    public void getAHSPFromSub(Integer NO, String KODE, String URAIAN, Double COST) {
        AHSP temp = new AHSP(NO, KODE, URAIAN, COST);
        System.out.println("NO : "+NO+"\nKODE : "+KODE+"\nURAIAN : "+URAIAN+"\n COST : "+COST);
        tblSubKalkulasi.getItems().get(indexList).setUraian(URAIAN);
        tblSubKalkulasi.getItems().get(indexList).setKode(KODE);
        tblSubKalkulasi.getItems().get(indexList).setCost(COST);
        tblSubKalkulasi.refresh();
        tblSize++;
        tblSubKalkulasi.getSelectionModel().clearSelection();
    }
    @FXML
    public void searchData(ActionEvent event) throws SQLException {
        this.kodeProyek = txKodeProyek.getText();
        if(DB == null)
            DB = new Model.Database();
        DB.setSTMT("SELECT proyek.deskripsi, proyek.wilayah, konsumen.nmKonsumen FROM proyek,konsumen WHERE proyek.kdProyek='"+kodeProyek+"'"
                + " AND proyek.kdKonsumen = konsumen.kdKonsumen");
        File file = new File("output/"+kodeProyek+"-ANL.xml");
        try {
            java.sql.ResultSet RSET = DB.getSQL();
            if(RSET.next() == false) {
                System.out.println("Data Blom Ada Boy, input dulu dah, ntar baru balik kesini lagi");
            } else {                
                txMasterKalkulasi.setText("Deskripsi Proyek : "+RSET.getString(1).replaceAll("<br>", "\n")
                        +"\nClient : "+RSET.getString(3)+"\n"
                        + "Wilayah : "+RSET.getString(2));
                if(file.exists() == false) {
                    //anlKerja = loadXML(kodeProyek);                
                    //list = anlKerja.getAnalisa();
                    anlKerja = new Model.AnlKerja();
                    list = new ArrayList<>();
                    
                    jContext = JAXBContext.newInstance(Model.AnlKerja.class);                    
                    ArrayList<Model.AnlKerja.Analisa.Material.Item> item = new ArrayList<>();
                    ArrayList<Model.AnlKerja.Analisa.Material> mtr = new ArrayList<>();
                    Model.AnlKerja.Analisa.Material.Item it = new Model.AnlKerja.Analisa.Material.Item();
                    Model.AnlKerja.Analisa.Material.Item it2 = new Model.AnlKerja.Analisa.Material.Item();
                    
                    it.setType("Tenaga");
                    it.setValue("");
                    item.add(it);

                    it2.setType("Bahan");
                    it2.setValue("");
                    item.add(it2);
                    Model.AnlKerja.Analisa.Material mat = new Model.AnlKerja.Analisa.Material();
                    mat.setItem(item);
                    mtr.add(mat);

                    Model.AnlKerja.Analisa ana = new Model.AnlKerja.Analisa();
                    //int number= anlKerja.getAnalisa().get(anlKerja.getAnalisa().size()-1).getNo()+1;
                    ana.setNo(1);
                    ana.setKode("");
                    ana.setUraian("");
                    ana.setMaterial(mat);
                    ana.setJumlah(0.0);
                    list.add(ana);
                    
                    anlKerja.setAnalisa(list);
                    
                    msr = jContext.createMarshaller();
                    msr.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                    // Tulis ke System.out
                    msr.marshal(anlKerja, System.out);

                    // tulis file XML
                    msr.marshal(anlKerja, new File("output/"+kodeProyek+"-ANL.xml"));
                    System.out.println("Masa ga bisa???");
                } else if(file.exists()) {
                    anlKerja = loadXML(kodeProyek);
                    list = anlKerja.getAnalisa();
                    tblSubKalkulasi.getItems().clear();
                    for(Model.AnlKerja.Analisa analis : list) {
                        AHSP e = new AHSP(analis.getNo(), analis.getKode(), analis.getUraian(), analis.getJumlah());
                        tblSubKalkulasi.getItems().add(e);
                        tblSize++; indexList++;
                    }
                    tblSubKalkulasi.refresh();
                }
            }
                
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    Model.AnlKerja loadXML(String KodeProyek) throws JAXBException, FileNotFoundException {
        jContext = JAXBContext.newInstance(Model.AnlKerja.class);
        um = jContext.createUnmarshaller();
        Model.AnlKerja temp = (Model.AnlKerja) um.unmarshal(new FileReader("output/"+KodeProyek+"-ANL.xml"));
        return temp;
    }

    @FXML
    private void lookUpKalkulasi(ActionEvent event) {
        Object[] DATA = new Object[3];
        DATA[0] = tblSubKalkulasi.getSelectionModel().getSelectedItem().getKode();
        DATA[1] = tblSubKalkulasi.getSelectionModel().getSelectedItem().getUraian();
        DATA[2] = tblSubKalkulasi.getSelectionModel().getSelectedItem().getCost();
        kalkulasiController.getSubAHSP(DATA);
    }
    
    public static class AHSP {
        private final SimpleIntegerProperty No;
        private final SimpleStringProperty Kode;
        private final SimpleStringProperty Uraian;
        private final SimpleDoubleProperty Cost;
        
        private AHSP(Integer NO, String KODE, String URAIAN, Double COST)  {            
            this.Cost = new SimpleDoubleProperty(COST);
            this.No = new SimpleIntegerProperty(NO);
            this.Uraian = new SimpleStringProperty(URAIAN);
            this.Kode = new SimpleStringProperty(KODE);
        }

        public Integer getNo() {
            return No.get();
        }
        public void setNo(Integer NO) {
            No.set(NO); 
        }
        public String getKode() {
            return Kode.get();
        }
        public void setKode(String KODE) {
            Kode.set(KODE);
        }
        public String getUraian() {
            return Uraian.get();
        }
        public void setUraian(String URAIAN) {
            Uraian.set(URAIAN);
        }
        public double getCost() {
            return Cost.get();
        }
        public void setCost(Double COST) {
            Cost.set(COST);
        }
        public String getDataInfo() {            
            return "["+No.get()+", "+Kode.get()+", "+Uraian.get()+", "+Cost.get()+"]";
        }
    }
}
