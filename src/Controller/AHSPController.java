/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
public class AHSPController implements Initializable {
    Model.Database DB = new Model.Database();
    MasterBahanController material;
    MasterTenagaController tenaga;
    Scene sceneTenaga,sceneMaterial;
    Stage subStage, subStage2;
    Model.AnlKerja anlKerja;
    ArrayList<Model.AnlKerja.Analisa> analisa;
    protected JAXBContext jContext; protected Unmarshaller um; protected Marshaller msr;
    MasterAHSPController masterAHSP;    
    subAHSP BAHAN,TENAGA; Integer bIndex = 0, tIndex = 0;
    String listBahan="", listTenaga="";
    Double costBahan,costTenaga;    
    @FXML
    public TableView<subAHSP> tblListBahan;
    @FXML
    private Button btnAddBahan;
    @FXML
    private Button btnDelBahan;
    @FXML
    public TableView<subAHSP> tblListTenaga;
    @FXML
    private Button btnAddTenaga;
    @FXML
    private Button btnDelTenaga;
    @FXML
    private TextField txKodeAnalisa;
    @FXML
    private TextField txUraianPekerjaan;
    @FXML
    private TextField txTotalCost;
    @FXML
    private Label txSubTotalTenaga;
    @FXML
    private Label txSubTotalBahan;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    
    public void getInstance(){
        if(material == null)  {
            material = new MasterBahanController();
            material.injectController(this);
        }
        if(tenaga == null) {
            tenaga = new MasterTenagaController();
            tenaga.injectController(this);
        }
    }
    public void injectController(MasterAHSPController Controller){
        this.masterAHSP = Controller;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getInstance();
        //setColumnTable();
    }    

    public void setColumnTable(){
        TableColumn noCol = new TableColumn("No");
        noCol.setMaxWidth(30);
        noCol.setCellValueFactory(new PropertyValueFactory<subAHSP,Integer>("No"));
        
        TableColumn namaCol = new TableColumn("Nama");
        namaCol.setMinWidth(100);
        namaCol.setCellValueFactory(new PropertyValueFactory<subAHSP,String>("Nama"));
        
        TableColumn unitCol = new TableColumn("Unit");
        unitCol.setMinWidth(30);
        unitCol.setCellValueFactory(new PropertyValueFactory<subAHSP,String>("Unit"));
        
        TableColumn qtyCol = new TableColumn("Qty");
        qtyCol.setMinWidth(50);
        qtyCol.setCellValueFactory(new PropertyValueFactory<subAHSP,Double>("Qty"));        
        
        TableColumn costCol = new TableColumn("Cost");
        costCol.setMinWidth(100);
        costCol.setCellValueFactory(new PropertyValueFactory<subAHSP,Double>("Cost"));
        
        TableColumn kodeCol = new TableColumn("Kode");
        kodeCol.setVisible(false);
        kodeCol.setCellValueFactory(new PropertyValueFactory<subAHSP,Double>("Kode"));
        tblListTenaga.getColumns().addAll(noCol, namaCol, unitCol, qtyCol, costCol, kodeCol);
        
        noCol = new TableColumn("No");
        noCol.setMaxWidth(30);
        noCol.setCellValueFactory(new PropertyValueFactory<subAHSP,Integer>("No"));
        
        namaCol = new TableColumn("Nama");
        namaCol.setMinWidth(100);
        namaCol.setCellValueFactory(new PropertyValueFactory<subAHSP,String>("Nama"));
        
        unitCol = new TableColumn("Unit");
        unitCol.setMinWidth(30);
        unitCol.setCellValueFactory(new PropertyValueFactory<subAHSP,String>("Unit"));
        
        qtyCol = new TableColumn("Qty");
        qtyCol.setMinWidth(50);
        qtyCol.setCellValueFactory(new PropertyValueFactory<subAHSP,Double>("Qty"));        
        
        costCol = new TableColumn("Cost");
        costCol.setMinWidth(100);
        costCol.setCellValueFactory(new PropertyValueFactory<subAHSP,Double>("Cost"));
        
        kodeCol = new TableColumn("Kode");
        kodeCol.setVisible(false);
        kodeCol.setCellValueFactory(new PropertyValueFactory<subAHSP,Double>("Kode"));
        tblListBahan.getColumns().addAll(noCol, namaCol, unitCol, qtyCol, costCol, kodeCol);
    }
    public void loadDataXML() throws java.sql.SQLException, JAXBException, FileNotFoundException{
        // too many bitches are going on here.
        ObservableList<subAHSP> CONT_BAHAN = FXCollections.observableArrayList();
        ObservableList<subAHSP> CONT_TENAGA = FXCollections.observableArrayList();
        // inisiasi awal subTotal tiap tabel;
        costBahan = 0.0; costTenaga = 0.0;
        DB.inisiasiDB();
        this.anlKerja = loadXML(masterAHSP.kodeProyek);
        analisa = anlKerja.getAnalisa();
        txKodeAnalisa.setText(analisa.get(masterAHSP.indexList).getKode()); 
        txUraianPekerjaan.setText(analisa.get(masterAHSP.indexList).getUraian());
        txTotalCost.setText(String.format("%,.2f", analisa.get(masterAHSP.indexList).getJumlah()));
        ArrayList<Model.AnlKerja.Analisa.Material.Item> items = analisa.get(masterAHSP.indexList).getMaterial().getItem();
        for(Model.AnlKerja.Analisa.Material.Item cob : items) {            
            String[] temp = cob.valueToArray();
            if(cob.getType().equals("Tenaga")) {
                if(!"".equalsIgnoreCase(cob.getValue())) {
                    //tenaga
                    String sql = "SELECT nmTenaga, satuan, harga FROM tenaga WHERE kdTenaga in(";
                    for(int x=0; x< (temp.length/2); x++){
                        if(x == (temp.length/2-1)){
                            sql += "'"+temp[x]+"') \n ORDER BY CASE kdTenaga\n";
                        } else {
                            sql += "'"+temp[x]+"',";
                        }
                    }
                    for(int x=0; x< (temp.length/2); x++){
                        if(x == (temp.length/2-1)){
                            sql += "\tWHEN '"+temp[x]+"' THEN "+(x+1)+" \r\nEND";
                        } else {
                            sql += "\tWHEN '"+temp[x]+"' THEN "+(x+1)+" \n";
                        }
                    }
                    //System.out.println("\nSQL untuk items =======\n"+sql+"\n=======================\n");
                    DB.setSTMT(sql); java.sql.ResultSet SET1 = DB.getSQL();
                    int z = temp.length /2;
                    int y = 1;
                    while(SET1.next()) {                        
                        //ambil data
                        TENAGA = new subAHSP(y, SET1.getString(1), SET1.getString(2), Double.parseDouble(temp[z]) , SET1.getDouble(3), temp[y-1]);
                        CONT_TENAGA.add(TENAGA);
                        costTenaga += Double.parseDouble(temp[z]) * SET1.getDouble(3);
                        y++; z++;
                        this.tIndex = y-1;
                    }
                }
            } else {
                if(!"".equalsIgnoreCase(cob.getValue())) {
                    // material
                    String sql = "SELECT nama, deskripsi, satuan, harga FROM items WHERE kdItems in(";
                    for(int x=0; x< (temp.length/2); x++){
                        if(x == (temp.length/2-1)){
                            sql += "'"+temp[x]+"') \n ORDER BY CASE kdItems\n";
                        } else {
                            sql += "'"+temp[x]+"',";
                        }
                    }
                    for(int x=0; x< (temp.length/2); x++){
                        if(x == (temp.length/2-1)){
                            sql += "\tWHEN '"+temp[x]+"' THEN "+(x+1)+" \r\nEND";
                        } else {
                            sql += "\tWHEN '"+temp[x]+"' THEN "+(x+1)+" \n";
                        }
                    }
                    //System.out.println("\nSQL untuk items =======\n"+sql+"\n=======================\n");
                    DB.setSTMT(sql); java.sql.ResultSet SET = DB.getSQL();
                    int z = temp.length /2;
                    int y = 1;
                    while(SET.next()) {
                        //ambil data
                        BAHAN = new subAHSP(y, SET.getString(1)+" "+SET.getString(2), SET.getString(3), Double.parseDouble(temp[z]) , SET.getDouble(4), temp[y-1]);
                        CONT_BAHAN.add(BAHAN);
                        costBahan += Double.parseDouble(temp[z]) * SET.getDouble(4);
                        y++; z++;
                        this.bIndex = y-1;
                    }
                }
            }
            this.tblListTenaga.setItems(CONT_TENAGA);
            txSubTotalTenaga.setText(String.format("%,.2f", costTenaga));
            this.tblListBahan.setItems(CONT_BAHAN);
            txSubTotalBahan.setText(String.format("%,.2f", costBahan));
        }
    }
    @FXML
    private void addBahan(ActionEvent event) throws java.io.IOException{
        if( subStage == null ) {
            FXMLLoader fLoader = new FXMLLoader(getClass().getResource("/View/MasterBahan.fxml"));
            Parent parent = fLoader.load();
            material = fLoader.getController();
            material.injectController(this);
            material.pnlAction.setVisible(false);
            sceneMaterial = new Scene(parent);   
            subStage = new Stage();
            subStage.setTitle("RAB | Data - MasterBahan");
            subStage.resizableProperty().setValue(Boolean.FALSE);
            subStage.setScene(sceneMaterial);
            subStage.setHeight(475-178);
            subStage.show();
        } else  {
            subStage.toFront();
            subStage.show();
        }             
    }

    @FXML
    private void delBahan(ActionEvent event) {
        tblListBahan.getItems().remove(tblListBahan.getSelectionModel().getSelectedIndex());
        --this.bIndex;
        for(int x=0; x<this.bIndex; x++) {
           tblListBahan.getItems().get(x).setNo(x+1);           
        }
        hitungUlangBiaya();
    }
    public void setBahan(String NAMA, String UNIT, Double QTY, Double COST, String KODE) {
        tblListBahan.refresh();
        System.out.println(this.bIndex);
        BAHAN = new subAHSP((++this.bIndex), NAMA, UNIT, QTY, COST, KODE);
        tblListBahan.getItems().add(BAHAN);
        hitungUlangBiaya();
    }

    @FXML
    private void addTenaga(ActionEvent event) throws java.io.IOException{
        if( subStage2 == null ) {
            FXMLLoader fLoader = new FXMLLoader(getClass().getResource("/View/MasterTenaga.fxml"));
            Parent parent = fLoader.load();
            tenaga = fLoader.getController();
            tenaga.injectController(this);
            tenaga.pnlAction.setVisible(false);
            sceneTenaga  = new Scene(parent);   
            subStage2 = new Stage();
            subStage2.setTitle("RAB | Data - MasterTenaga");
            subStage2.resizableProperty().setValue(Boolean.FALSE);
            subStage2.setScene(sceneTenaga);
            subStage2.setHeight(475-178);
            subStage2.show();
        } else  {
            subStage2.toFront();
            subStage2.show();
        }
    }

    @FXML
    private void delTenaga(ActionEvent event) {
        System.out.println(this.tIndex);
        tblListTenaga.getItems().remove(tblListTenaga.getSelectionModel().getSelectedIndex());
        --this.tIndex;        
        System.out.println(this.tIndex);
        for(int x=0; x<this.tIndex; x++) {
           tblListTenaga.getItems().get(x).setNo(x+1);           
        }
        hitungUlangBiaya();
    }
    public void setTenaga(String NAMA, String UNIT, Double QTY, Double COST, String KODE) {        
        tblListTenaga.refresh();
        System.out.println(this.tIndex);
        TENAGA = new subAHSP((++this.tIndex), NAMA, UNIT, QTY, COST, KODE);
        tblListTenaga.getItems().add(TENAGA);
        hitungUlangBiaya();
    }
    Model.AnlKerja loadXML(String KodeProyek) throws JAXBException, FileNotFoundException {
        jContext = JAXBContext.newInstance(Model.AnlKerja.class);
        um = jContext.createUnmarshaller();
        Model.AnlKerja temp = (Model.AnlKerja) um.unmarshal(new FileReader("output/"+KodeProyek+"-ANL.xml"));
        return temp;
    }

    @FXML
    private void saveAHSP(ActionEvent event) {
        File file = new File("output/"+masterAHSP.kodeProyek+"-ANL.xml");        
        try {
            if (file.exists() && file.isFile()){ 
                editXML(this.anlKerja);
                masterAHSP.getAHSPFromSub(0, txKodeAnalisa.getText(), txUraianPekerjaan.getText(), (costBahan+costTenaga));
                saveToDB();                
            } else {
                Model.AnlKerja temp = loadXML(masterAHSP.kodeProyek);
                ArrayList<Model.AnlKerja.Analisa> list = temp.getAnalisa();
                
                ArrayList<Model.AnlKerja.Analisa.Material.Item> item = new ArrayList<>();
                ArrayList<Model.AnlKerja.Analisa.Material> mtr = new ArrayList<>();
                Model.AnlKerja.Analisa.Material.Item it = new Model.AnlKerja.Analisa.Material.Item();
                Model.AnlKerja.Analisa.Material.Item it2 = new Model.AnlKerja.Analisa.Material.Item();
                
                it.setType("Tenaga");
                it.setValue(listTenaga);
                item.add(it);
                                
                it2.setType("Bahan");
                it2.setValue(listBahan);
                item.add(it2);
                Model.AnlKerja.Analisa.Material mat = new Model.AnlKerja.Analisa.Material();
                mat.setItem(item);
                mtr.add(mat);
                
                Model.AnlKerja.Analisa ana = new Model.AnlKerja.Analisa();
                int number= temp.getAnalisa().get(temp.getAnalisa().size()-1).getNo()+1;
                ana.setNo(number);
                ana.setKode(txKodeAnalisa.getText());
                ana.setUraian(txUraianPekerjaan.getText());
                ana.setMaterial(mat);
                ana.setJumlah(costBahan+costTenaga);
                list.add(ana);
                
                msr = jContext.createMarshaller();
                msr.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                // Tulis ke System.out
                msr.marshal(temp, System.out);

                // tulis file XML
                msr.marshal(temp, new File("output/"+masterAHSP.kodeProyek+"-ANL.xml"));
                // tambah ke table MasterAHSP
                
                masterAHSP.getAHSPFromSub(number, txKodeAnalisa.getText(), txUraianPekerjaan.getText(), (costBahan+costTenaga));
                
                saveToDB();
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void editXML(Model.AnlKerja param) throws FileNotFoundException, JAXBException {
        param = loadXML(masterAHSP.kodeProyek);
        System.out.println("Bahan : "+this.listBahan+"\nTenaga : "+this.listTenaga);
        serializeKode();
        System.out.println("Bahan : "+this.listBahan+"\nTenaga : "+this.listTenaga);
        ArrayList<Model.AnlKerja.Analisa> list = param.getAnalisa();
        list.get(masterAHSP.indexList).setUraian(txUraianPekerjaan.getText());
        list.get(masterAHSP.indexList).setKode(txKodeAnalisa.getText());
        list.get(masterAHSP.indexList).getMaterial().getItem().get(0).setValue(listTenaga);
        list.get(masterAHSP.indexList).getMaterial().getItem().get(1).setValue(listBahan);            
        list.get(masterAHSP.indexList).setJumlah(costBahan+costTenaga);
        msr = jContext.createMarshaller();
        msr.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // Tulis ke System.out
        msr.marshal(param, System.out);

        // tulis file XML
        msr.marshal(param, new File("output/"+masterAHSP.kodeProyek+"-ANL.xml"));
        JOptionPane.showMessageDialog(null, "Edit Data Berhasil\n"
                + "Kode :"+txKodeAnalisa.getText()
                +"\nAnalisa :"+txUraianPekerjaan.getText(), "Success", 1);
    }
    public void saveToDB() throws java.sql.SQLException{
        String SQL="SELECT COUNT(kdProyek) as totalProyek FROM proyek WHERE kdProyek='"+masterAHSP.kodeProyek+"'";
        ResultSet rs = DB.getSQL(SQL); rs.next();
        Object[] DATA;
        if(rs.getInt(1) == 0) {
            // cek dulu apakah proyek sudah ada di DB;
            JOptionPane.showMessageDialog(null, "Data Proyek Belum ada, silahkan input data Proyek", "Warning", 1);
        } else {
            SQL = "SELECT * FROM muraian WHERE kdUraian='"+txKodeAnalisa.getText()
                 +"' AND kdProyek='"+masterAHSP.kodeProyek+"'";
            rs = DB.getSQL(SQL);
            if(rs.next() == false) {
                // data muraian belom ada, maka insert
                SQL = "INSERT INTO muraian VALUES (?,?,?)";
                DB.setSTMT(SQL);
                DATA = new Object[3];
                DATA[0] = txKodeAnalisa.getText();
                DATA[1] = masterAHSP.kodeProyek; 
                DATA[2] = txUraianPekerjaan.getText();
                DB.insertSQL(DATA, "muraian", "kdUraian");
            } else {
                // kalo data muraian udah ada, maka update
                SQL = "UPDATE muraian SET Deskripsi=? , kdUraian= ? WHERE "
                    + "kdUraian=? AND kdProyek=?";
                DB.setSTMT(SQL);
                DATA = new Object[4];
                DATA[0] = txUraianPekerjaan.getText();
                DATA[1] = txKodeAnalisa.getText();
                DATA[2] = txKodeAnalisa.getText();
                DATA[3] = masterAHSP.kodeProyek;
                DB.insertSQL(DATA, "muraian", "kdUraian");
            }
            DB.setSTMT("DELETE FROM uraian WHERE kdUraian=?;");
            int del = DB.deleteSQL(txKodeAnalisa.getText());
            SQL = "INSERT INTO uraian VALUES (?,?,?,?,?)";            
            int z;
            if(tIndex>bIndex){z = tIndex;} else {z = bIndex;}
            Connection conn = (Connection) DB.inisiasiDBnoStatic();
            java.sql.PreparedStatement prep = conn.prepareStatement(SQL);
            for (int i = 0; i < z; i++) {
                prep.setString(1,txKodeAnalisa.getText());
                if(i < (bIndex)) {
                    prep.setString(2, tblListBahan.getItems().get(i).getKode());
                    prep.setDouble(3, tblListBahan.getItems().get(i).getQty());                    
                } else {
                    prep.setNull(2, Types.VARCHAR);
                    prep.setNull(3, Types.DOUBLE);                     
                }
                if(i < (tIndex)) {
                    prep.setString(4, tblListTenaga.getItems().get(i).getKode());
                    prep.setDouble(5, tblListTenaga.getItems().get(i).getQty());
                } else {
                    prep.setNull(4, Types.VARCHAR);
                    prep.setNull(5, Types.DOUBLE);                    
                }
                prep.executeUpdate();
            }
            
        }
        //conn.close //CLOSE KAH?
    }
    void serializeKode() {
        //bahan dulu
        if(tblListBahan.getItems().size()>0) {
            for(int i=0; i<bIndex; i++){ //Kode
                this.listBahan += tblListBahan.getItems().get(i).getKode()+"|";
            }
            for(int i=0; i<bIndex; i++){ //Quantity
                if(i == (bIndex-1))
                    this.listBahan += tblListBahan.getItems().get(i).getQty().toString();
                else
                    this.listBahan += tblListBahan.getItems().get(i).getQty().toString()+"|";
            }            
        }
        
        //Tenaga
        if(tblListTenaga.getItems().size()>0) {
            for(int i=0; i<tIndex; i++){ //Kode
                this.listTenaga += tblListTenaga.getItems().get(i).getKode()+"|";
            }
            for(int i=0; i<tIndex; i++){ //Quantity
                if(i == (tIndex-1))
                    this.listTenaga += tblListTenaga.getItems().get(i).getQty().toString();
                else
                    this.listTenaga += tblListTenaga.getItems().get(i).getQty().toString()+"|";
            }
        }
            
        System.out.println("Bahan : "+this.listBahan+"\nTenaga : "+this.listTenaga);
    }
    @FXML
    private void cancelAll(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void refreshList(ActionEvent event) {
        hitungUlangBiaya();
    }   
    void hitungUlangBiaya() {
        costBahan = 0.0; costTenaga = 0.0;
        if(tblListBahan.getItems().size()>0) {
            for(int i=0; i<bIndex; i++)
                costBahan += (tblListBahan.getItems().get(i).getQty() * tblListBahan.getItems().get(i).getCost());
        }
        
        if(tblListTenaga.getItems().size()>0) {
            for(int i=0; i<tIndex; i++)
                costTenaga += (tblListTenaga.getItems().get(i).getQty() * tblListTenaga.getItems().get(i).getCost());
        }
        txSubTotalBahan.setText(String.format("%,.2f", costBahan));
        txSubTotalTenaga.setText(String.format("%,.2f", costTenaga));
        txTotalCost.setText(String.format("%,.2f", (costTenaga+costBahan)));
    }
    @FXML
    private void editQtyBahan(MouseEvent event) {
        if(event.getClickCount() == 2 && !event.isConsumed()) {
            Double qty = Double.parseDouble(JOptionPane.showInputDialog("Jumlah Quantity untuk item\n"+
                    tblListBahan.getSelectionModel().getSelectedItem().getNama()+" : "));
            if(qty > 0) {
                BAHAN = tblListBahan.getItems().get(tblListBahan.getSelectionModel().getSelectedIndex());
                BAHAN.setQty(qty);
                tblListBahan.getItems().set(tblListBahan.getSelectionModel().getSelectedIndex(), BAHAN);
                tblListBahan.getSelectionModel().clearSelection();
                hitungUlangBiaya();                
            }
        }   
    }

    @FXML
    private void editQtyTenaga(MouseEvent event) {
        if(event.getClickCount() == 2 && !event.isConsumed()) {
            Double qty = Double.parseDouble(JOptionPane.showInputDialog("Jumlah Quantity untuk item\n"+
                    tblListTenaga.getSelectionModel().getSelectedItem().getNama()+" : "));
            if(qty >0) {
                TENAGA = tblListTenaga.getItems().get(tblListTenaga.getSelectionModel().getSelectedIndex());
                TENAGA.setQty(qty);
                tblListTenaga.getItems().set(tblListTenaga.getSelectionModel().getSelectedIndex(), TENAGA);
                tblListTenaga.getSelectionModel().clearSelection();
                hitungUlangBiaya();
            }
        }
    }
    public static class subAHSP {
        //no,nama,unit,Qty,Cost
        private final SimpleIntegerProperty No;
        private final SimpleStringProperty Nama;
        private final SimpleStringProperty Unit;
        private final SimpleDoubleProperty Qty;
        private final SimpleDoubleProperty Cost;
        private final SimpleStringProperty Kode;
        
        private subAHSP(Integer NO, String NAMA, String UNIT, Double QTY, Double COST, String KODE){
            this.No = new SimpleIntegerProperty(NO);
            this.Nama = new SimpleStringProperty(NAMA);
            this.Unit = new SimpleStringProperty(UNIT);
            this.Qty = new SimpleDoubleProperty(QTY);
            this.Cost = new SimpleDoubleProperty(COST);
            this.Kode = new SimpleStringProperty(KODE);
        }

        public Integer getNo() {
            return No.get();
        }
        public void setNo(Integer NO){
            No.set(NO);
        }
        
        public String getNama() {
            return Nama.get();
        }
        public void setNama(String NAMA){
            Nama.set(NAMA);
        }

        public String getUnit() {
            return Unit.get();
        }
        public void setUnit(String UNIT){
            Unit.set(UNIT);
        }

        public Double getQty() {
            return Qty.get();
        }
        public void setQty(Double QTY){
            Qty.set(QTY);
        }

        public double getCost() {
            return Cost.get();
        }
        public void setCost(Double COST){
            Cost.set(COST);
        }
        public String getKode() {
            return Kode.get();
        }
        public void setKode(String KODE) {
            Kode.set(KODE);
        }
    }
}
