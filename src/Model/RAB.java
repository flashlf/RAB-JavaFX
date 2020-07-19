/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import com.itextpdf.kernel.pdf.PdfDocument; import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.font.PdfFont; import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Document; import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.List; import com.itextpdf.layout.element.ListItem;
import com.itextpdf.forms.PdfAcroForm; import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.ColorConstants;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DottedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.text.StyleConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;


/**
 *
 * @author REDHAT
 */
public class RAB {
    public String Dest = "results/MasterMaterial.pdf";
    static String[] bulanTerbilang={"","Januari","Februari","Maret","April","Mei"
            ,"Juni","Juli","Agustus","September","Oktober","November","Desember"};
    static String[] hariTerbilang={"","Minggu","Senin","Selasa","Rabu","Kamis","Jum'at","Sabtu"};
    String kdProyek;
    String s1,s2,s3,s4,s5,s6,s7,imgUrl;
    public Cell h1,h2,h3,h4,h5,h6,h7,
                c1,c2,c3,c4,c5,c6,c7;
    public PdfDocument pdf;
    public PdfFont font;
    public Document doc;
    public Paragraph para;
    public ImageData imgData; public Image img;
    Database ml; 
    Connection conn;
    static File fl;

    protected void initiateDB() {
        try {
            ml = new Database();
            conn = (Connection)Database.inisiasiDB();
            //ml.setDB("RAB");
            //ml.setUSER("root");
            //ml.setPASS("123456");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void setKdProyek(String param) {
        this.kdProyek = param;
    }
    public void initiateDirectory() {
        fl = new File(Dest);
        fl.getParentFile().mkdirs();
    }
    
    protected Paragraph textHeader(Paragraph param, String text) throws IOException {
        PdfFont ft = PdfFontFactory.createFont(FontConstants.HELVETICA);
        param = new Paragraph();
        param.setFont(ft)
            .setBold()
            .setFontSize(24)
            .setTextAlignment(TextAlignment.RIGHT)
            .add(text);
        return param;
    }
    
    protected void initiateDocument() throws IOException{
        // Setting Document Page
            doc = new Document(pdf, PageSize.A4);
            // Setting Font
            font = PdfFontFactory.createFont(FontConstants.HELVETICA);
            // Setting Page Layout
            doc.setMargins(20f, 10f, 30f, 70f);           
            doc.setFont(font);
    }
    
    protected void initiateHeader(Document param) throws IOException, URISyntaxException {
        // Logo
        //BufferedImage imBuff = ImageIO.read(new File(getClass().getResource("/misc/blackwhite.jpg").toURI()));
        //imgUrl = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().toString()+"/misc/blackwhite.jpg";
        //"./src/misc/blackwhite.jpg";
        //imgData = ImageDataFactory.create(getClass().getResource("/misc/logo.jpg"));
        
        imgData = ImageDataFactory.create("misc/logo.jpg");
        //JOptionPane.showMessageDialog(null, getClass().getResource("/misc/logo.jpg").toString());
        img = new Image(imgData).scale(0.15f, 0.15f)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        // Title
        // Paragraph p = textHeader(para, "LUMEN*");
        // Table
        Table headerSection = new Table(new float[]{0.2f, 0.8f})
                .setWidth(483).setFixedLayout().setBorder(Border.NO_BORDER)
                .setMarginBottom(10);
        c1 = new Cell(2, 1).add(img).setBorder(Border.NO_BORDER)
                .setHorizontalAlignment(HorizontalAlignment.RIGHT);
        c2 = new Cell(1, 1).add("PT. SYALAM UTAMA SEJAHTERA").setBorder(Border.NO_BORDER)
                .setFontColor(new DeviceRgb(34,40,49))
                .setBorderBottom(new SolidBorder(1)).setBold()
                .setPaddingTop(10);
        c3 = new Cell(1, 1).add("Jl. Raya Hankam No. 10F, Kel. Jati Warna, Kec. Pondok Melati"
                + ", RT.006/RW.009, Kota Bekasi, Jawa Barat 17415.\n"
                + "Email: syalamutamasejahtera@gmail.com, Telp. (021) 8498 1190").setBorder(Border.NO_BORDER)
                .setFontColor(new DeviceRgb(70,82,100))
                .setFontSize(6f);
        headerSection.addCell(c1); headerSection.addCell(c2); 
        headerSection.addCell(c3);
        param.add(headerSection);
    }
    protected void initiateSignatureDocument(Document param) throws IOException {
        Calendar calendar = Calendar.getInstance();
        String text = "\nJakarta, "+
                hariTerbilang[calendar.get(Calendar.DAY_OF_WEEK)]+" "+
                calendar.get(Calendar.DATE)+" "+
                bulanTerbilang[calendar.get(Calendar.MONTH)]+" "+
                calendar.get(Calendar.YEAR)+"\nMengetahui";
        para = new Paragraph();
        para.setFontSize(10)
            .setTextAlignment(TextAlignment.RIGHT)
            .setMarginRight(30f)
            .add(text);
        param.add(para);
        // PT
        text = "PT. Syalam Utama Sejahtera\n\n\n\n\nMansyur Tidore, S.E., MBA";
        para = new Paragraph();
        para.setFontSize(10)
            .setBold()
            .setTextAlignment(TextAlignment.RIGHT)
            .setMarginRight(30f)
            .add(text);
        param.add(para);        
    }
    protected void setSubHeaderText(String param) {
        para = new Paragraph()
                .add(param)
                .setMultipliedLeading(0.9f)
                .setFont(font)
                .setBold()
                .setFontSize(9f);
    }
    protected void addEmptyCell(Table param, int i) {
        for (int x=0; x < i; x++) {
            param.addCell(new Cell(1,1)
                    .setBorderBottom(new DottedBorder(0.65f)));
        }
    }
    public void genReportMasterMaterial(String destination) throws IOException, SQLException, URISyntaxException {
        // Initialize Database
        initiateDB();
        
        // Initialize Document Generator iText
        pdf = new PdfDocument(new PdfWriter(destination));
        try {
            ResultSet sql;
            initiateDocument();
            // Initialize Element
            initiateHeader(doc);
            // Total unit untuk A4 523unit
            float[] ColumnWidths = new float[]{0.3f, 2.3f, 0.5f, 0.7f};
            Table rc = new Table(ColumnWidths).setFixedLayout()
                    .setBackgroundColor(new DeviceRgb(238,238,238))
                    .setWidth(483);
            // Table Header
            // blank Cell
            //Cell header = new Cell(1,4);
            //rc.addCell(header);
            ml.setSTMT("SELECT deskripsi FROM proyek WHERE kdProyek='"+this.kdProyek+"'");
            sql = ml.getSQL(); sql.next();
            String temp = sql.getString(1);
            String fixtemp = temp.replaceAll("<br>", "\\\n");
            String textHeader = "DAFTAR HARGA SATUAN UPAH & BAHAN\n"+fixtemp;
            
            setSubHeaderText(textHeader);
            Cell header = new Cell(1,4)
                .setTextAlignment(TextAlignment.CENTER)
                .setSpacingRatio(0.5f)
                .setBackgroundColor(new DeviceRgb(34,40,49))
                .setFontColor(new DeviceRgb(255,255,255))    
                .setPaddings(15f, 5f, 10f, 5f)
                .add(para);
            rc.addCell(header);
            
            // blank Cell
            //header = new Cell(1,4);
            //rc.addCell(header);
            // Head Column
            h1 = new Cell(1,1)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold().setFontSize(8f)
                .setBackgroundColor(new DeviceRgb(70,82,100))
                .setFontColor(new DeviceRgb(255,255,255))
                .add("No.");
            h2 = new Cell(1,1)
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(new DeviceRgb(70,82,100))
                .setFontColor(new DeviceRgb(255,255,255))
                .setBold().setFontSize(8f)
                .add("UPAH");
            h3 = new Cell(1,1)
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(new DeviceRgb(70,82,100))
                .setFontColor(new DeviceRgb(255,255,255))
                .setBold().setFontSize(8f)
                .add("SATUAN");
            h4 = new Cell(1,1)
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(new DeviceRgb(70,82,100))
                .setFontColor(new DeviceRgb(255,255,255))
                .setBold().setFontSize(8f)
                .add("HARGA SATUAN");
            
            // Initialize Element Table
            rc.addCell(header); 
            rc.addCell(h1); rc.addCell(h2); rc.addCell(h3); rc.addCell(h4);
            
            // Looping Content Table Tenaga
            int i = 1;
            // Old way
            // Statement stm =  conn.createStatement(); ResultSet sql = stm.executeQuery("SELECT * FROM tenaga");
            
            // master tenaga
            //ml.setSTMT("SELECT * FROM tenaga");
            ml.setSTMT("SELECT * FROM tenaga WHERE kdTenaga "
                    + "in(SELECT kdTenaga from uraian, muraian "
                    + "WHERE uraian.kdUraian = muraian.kdUraian "
                    + "AND muraian.kdProyek = \""+this.kdProyek+"\" )");                        
            
            sql = ml.getSQL();
            while (sql.next()) {
                s1 = i+"";
                s2 = sql.getString(2);
                s3 = sql.getString(3);
                s4 = sql.getString(4);
                if(i % 2 == 0) {
                rc.addCell(new Cell()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(7f)
                    .setBackgroundColor(new DeviceRgb(238,238,238))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setBorderRight(new DottedBorder(0.65f))
                    .setBorderBottom(new DottedBorder(0.65f))
                    .add(s1));
                rc.addCell(new Cell()
                    .setTextAlignment(TextAlignment.LEFT)
                    .setPaddingLeft(7f)
                    .setBackgroundColor(new DeviceRgb(238,238,238))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setBorderRight(new DottedBorder(0.65f))
                    .setBorderBottom(new DottedBorder(0.65f))
                    .setFontSize(7f)
                    .add(s2));
                rc.addCell(new Cell()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(new DeviceRgb(238,238,238))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setBorderRight(new DottedBorder(0.65f))
                    .setBorderBottom(new DottedBorder(0.65f))
                    .setFontSize(7f)
                    .add(s3));
                rc.addCell(new Cell()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBackgroundColor(new DeviceRgb(238,238,238))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setBorderLeft(new DottedBorder(0.65f))
                    .setBorderBottom(new DottedBorder(0.65f))
                    .setPaddingRight(7f)
                    .setFontSize(7f)
                    .add(s4));    
                } else {
                rc.addCell(new Cell()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(7f)
                    .setBackgroundColor(new DeviceRgb(255,255,255))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setBorderRight(new DottedBorder(0.65f))
                    .setBorderBottom(new DottedBorder(0.65f))
                    .add(s1));
                rc.addCell(new Cell()
                    .setTextAlignment(TextAlignment.LEFT)
                    .setPaddingLeft(7f)
                    .setBackgroundColor(new DeviceRgb(255,255,255))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setBorderRight(new DottedBorder(0.65f))
                    .setBorderBottom(new DottedBorder(0.65f))
                    .setFontSize(7f)
                    .add(s2));
                rc.addCell(new Cell()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(new DeviceRgb(255,255,255))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setBorderRight(new DottedBorder(0.65f))
                    .setBorderBottom(new DottedBorder(0.65f))
                    .setFontSize(7f)
                    .add(s3));
                rc.addCell(new Cell()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBackgroundColor(new DeviceRgb(255,255,255))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setBorderLeft(new DottedBorder(0.65f))
                    .setBorderBottom(new DottedBorder(0.65f))
                    .setPaddingRight(7f)
                    .setFontSize(7f)
                    .add(s4));
                }
                    i++;
            }
            // new blank row
            rc.addCell(new Cell(1,4).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
            
            // Head Column
            h1 = new Cell(1,1)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold().setFontSize(8f)
                .setBackgroundColor(new DeviceRgb(70,82,100))
                .setFontColor(new DeviceRgb(255,255,255))
                .add("No.");
            h2 = new Cell(1,1)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold().setFontSize(8f)
                .setBackgroundColor(new DeviceRgb(70,82,100))
                .setFontColor(new DeviceRgb(255,255,255))
                .add("BAHAN");
            h3 = new Cell(1,1)
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(new DeviceRgb(70,82,100))
                .setFontColor(new DeviceRgb(255,255,255))
                .setBold().setFontSize(8f)
                .add("SATUAN");
            h4 = new Cell(1,1)
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(new DeviceRgb(70,82,100))
                .setFontColor(new DeviceRgb(255,255,255))
                .setBold().setFontSize(8f)
                .add("HARGA SATUAN");
            rc.addCell(h1); rc.addCell(h2); rc.addCell(h3); rc.addCell(h4);

            // Looping Content Table Bahan
            i = 1;
            // ini untuk semua  item
            //ml.setSTMT("SELECT * FROM items");
            
            // ini buat item yg diipakee di  proyek tertentu aja
            ml.setSTMT("SELECT * FROM items WHERE kdItems "
                    + "in(SELECT kdItems from uraian, muraian "
                    + "WHERE uraian.kdUraian = muraian.kdUraian "
                    + "AND muraian.kdProyek = \""+this.kdProyek+"\" );");
            sql = ml.getSQL();
            while (sql.next()) {
                s1 = i+"";
                s2 = sql.getString(2)+" "+sql.getString(3);
                s3 = sql.getString(4);
                s4 = sql.getString(5);
                if(i % 2 == 0) {
                rc.addCell(new Cell()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(7f)
                    .setBackgroundColor(new DeviceRgb(238,238,238))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setBorderRight(new DottedBorder(0.65f))
                    .setBorderBottom(new DottedBorder(0.65f))
                    .add(s1));
                rc.addCell(new Cell()
                    .setTextAlignment(TextAlignment.LEFT)
                    .setPaddingLeft(7f)
                    .setBackgroundColor(new DeviceRgb(238,238,238))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setBorderRight(new DottedBorder(0.65f))
                    .setBorderBottom(new DottedBorder(0.65f))
                    .setFontSize(7f)
                    .add(s2));
                rc.addCell(new Cell()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(new DeviceRgb(238,238,238))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setBorderRight(new DottedBorder(0.65f))
                    .setBorderBottom(new DottedBorder(0.65f))
                    .setFontSize(7f)
                    .add(s3));
                rc.addCell(new Cell()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBackgroundColor(new DeviceRgb(238,238,238))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setBorderLeft(new DottedBorder(0.65f))
                    .setBorderBottom(new DottedBorder(0.65f))
                    .setPaddingRight(7f)
                    .setFontSize(7f)
                    .add(s4));    
                } else {
                rc.addCell(new Cell()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(7f)
                    .setBackgroundColor(new DeviceRgb(255,255,255))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setBorderRight(new DottedBorder(0.65f))
                    .setBorderBottom(new DottedBorder(0.65f))
                    .add(s1));
                rc.addCell(new Cell()
                    .setTextAlignment(TextAlignment.LEFT)
                    .setPaddingLeft(7f)
                    .setBackgroundColor(new DeviceRgb(255,255,255))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setBorderRight(new DottedBorder(0.65f))
                    .setBorderBottom(new DottedBorder(0.65f))
                    .setFontSize(7f)
                    .add(s2));
                rc.addCell(new Cell()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(new DeviceRgb(255,255,255))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setBorderRight(new DottedBorder(0.65f))
                    .setBorderBottom(new DottedBorder(0.65f))
                    .setFontSize(7f)
                    .add(s3));
                rc.addCell(new Cell()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBackgroundColor(new DeviceRgb(255,255,255))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setBorderLeft(new DottedBorder(0.65f))
                    .setBorderBottom(new DottedBorder(0.65f))
                    .setPaddingRight(7f)
                    .setFontSize(7f)
                    .add(s4));
                }
                    i++;
            }
           
            // adding element to Document

            //doc.add(p);
            doc.add(rc);
            //doc.setRightMargin(40f);
            initiateSignatureDocument(doc);
            doc.close();
        } catch(IOException|SQLException ex) {
            ex.printStackTrace();
        } 
        
    }
    public void genReportAnalystJob(String destination) throws IOException, SQLException {
        // initialize Connection to DB;
        initiateDB();
        
        // Initialize Document generator iText
        pdf = new PdfDocument(new PdfWriter(destination));
        try {
            ResultSet sql;
            initiateDocument();
            doc.setLeftMargin(20f);
            initiateHeader(doc);
            // Total unit untuk A4 523unit
            // 7 kolom
            float[] ColumnWidths = new float[]{0.2f, 0.3f, 1.7f, 0.4f, 0.4f, 0.4f, 0.4f};
            float[] subColumn = new float[]{0.5f,0.5f,0.3f,2.7f};
            Table wrapper = new Table(ColumnWidths).setFixedLayout()
                    .setWidth(523).setWidthPercent(100);
            
            ml.setSTMT("SELECT deskripsi FROM proyek WHERE kdProyek='"+this.kdProyek+"'");
            sql = ml.getSQL(); sql.next();
            String temp = sql.getString(1);
            String fixtemp = temp.replaceAll("<br>", "\\\n");
            String test = "DAFTAR ANALISA HARGA SATUAN UPAH & BAHAN\n"+fixtemp;
            setSubHeaderText(test);
            Cell header = new Cell(1,7)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setSpacingRatio(0.5f)
                    .setBackgroundColor(new DeviceRgb(34,40,49))
                    .setFontColor(new DeviceRgb(255,255,255))  
                    .setPaddings(15f, 5f, 10f, 5f)
                    .add(para);
            wrapper.addCell(header);
            
            // setting dan penambahan field header
            h1 = new Cell(1,1).setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER).setBold()
                    .setBackgroundColor(new DeviceRgb(70,82,100))
                    .setFontColor(new DeviceRgb(255,255,255))
                    .setFontSize(8f).add("NO."); wrapper.addCell(h1); 
            h2 = new Cell(1,1).setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER).setBold()
                    .setBackgroundColor(new DeviceRgb(70,82,100))
                    .setFontColor(new DeviceRgb(255,255,255))
                    .setFontSize(8f).add("ANALISA"); wrapper.addCell(h2);
            h3 = new Cell(1,1).setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER).setBold()
                    .setBackgroundColor(new DeviceRgb(70,82,100))
                    .setFontColor(new DeviceRgb(255,255,255))
                    .setFontSize(8f).add("URAIAN PEKERJAAN"); wrapper.addCell(h3);
            h4 = new Cell(1,1).setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER).setBold()
                    .setBackgroundColor(new DeviceRgb(70,82,100))
                    .setFontColor(new DeviceRgb(255,255,255))
                    .setFontSize(8f).add("HARGA\nSATUAN\nRp."); wrapper.addCell(h4);
            h5 = new Cell(1,1).setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER).setBold()
                    .setBackgroundColor(new DeviceRgb(70,82,100))
                    .setFontColor(new DeviceRgb(255,255,255))
                    .setFontSize(8f).add("UPAH\nRp."); wrapper.addCell(h5);
            h6 = new Cell(1,1).setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER).setBold()
                    .setBackgroundColor(new DeviceRgb(70,82,100))
                    .setFontColor(new DeviceRgb(255,255,255))
                    .setFontSize(8f).add("BAHAN\nRp."); wrapper.addCell(h6);
            h7 = new Cell(1,1).setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER).setBold()
                    .setBackgroundColor(new DeviceRgb(70,82,100))
                    .setFontColor(new DeviceRgb(255,255,255))
                    .setFontSize(8f).add("JUMLAH\nRp."); wrapper.addCell(h7);
                    
            
            int i = 0;
            ml.setSTMT("SELECT * FROM vwperuraianfix WHERE kdProyek='"+this.kdProyek+"'");
            System.out.println("vwperuraian kdProyek : "+this.kdProyek);
            sql = ml.getSQL();
            while(sql.next()) {
                s1 = (i+1)+"";
                s2 = sql.getString(1); // kdUraian
                s3 = sql.getString(2); // Deskripsi
                s4 = sql.getString(3); // Bahan
                double tCostTenaga=0.0, tCostBahan=0.0;
                String[] kodeBahan=null, qtyBahan=null;
                String sqlBahan = null;
                
                s5 = sql.getString(5); // Tenaga
                String[] kodeTenaga=null, qtyTenaga=null;
                String sqlTenaga = null;
                Boolean first = true;
                
                // setting field content
                wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(7f).add(s1).setBold()
                        .setBackgroundColor(new DeviceRgb(238,238,238))
                        .setFontColor(new DeviceRgb(34,40,49))
                        .setBorderBottom(new DottedBorder(0.65f)));
                wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(7f).add(s2).setBold()
                        .setBackgroundColor(new DeviceRgb(238,238,238))
                        .setFontColor(new DeviceRgb(34,40,49))
                        .setBorderBottom(new DottedBorder(0.65f)));
                wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(7f).add(s3).setBold()
                        .setBackgroundColor(new DeviceRgb(238,238,238))
                        .setFontColor(new DeviceRgb(34,40,49))
                        .setBorderBottom(new DottedBorder(0.65f)));
                wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.RIGHT).add("")
                        .setFontSize(7f)
                        .setBackgroundColor(new DeviceRgb(238,238,238))
                        .setFontColor(new DeviceRgb(34,40,49))
                        .setBorderBottom(new DottedBorder(0.65f)));
                wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.RIGHT).add("")
                        .setFontSize(7f)
                        .setBackgroundColor(new DeviceRgb(238,238,238))
                        .setFontColor(new DeviceRgb(34,40,49))
                        .setBorderBottom(new DottedBorder(0.65f)));
                wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.RIGHT).add("")
                        .setFontSize(7f)
                        .setBackgroundColor(new DeviceRgb(238,238,238))
                        .setFontColor(new DeviceRgb(34,40,49))
                        .setBorderBottom(new DottedBorder(0.65f)));
                wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.RIGHT).add("")
                        .setFontSize(7f)
                        .setBackgroundColor(new DeviceRgb(238,238,238))
                        .setFontColor(new DeviceRgb(34,40,49))
                        .setBorderBottom(new DottedBorder(0.65f)));
                 
                // klo ada bahan
                if(s4 != null) {
                    kodeBahan = ml.serializeString(s4);
                    qtyBahan = ml.serializeString(sql.getString(4));
                    sqlBahan = "SELECT * FROM items WHERE kdItems in(";
                    for(int x=0; x < kodeBahan.length; x++) {
                        if(x == (kodeBahan.length-1)) {
                            sqlBahan += "'"+kodeBahan[x]+"') \nORDER BY CASE kdItems\n";
                        } else {
                            sqlBahan += "'"+kodeBahan[x]+"',";
                        }
                    }
                    for(int x=0; x < qtyBahan.length; x++) {
                        if(x == (qtyBahan.length-1)) {
                            sqlBahan += "\tWHEN '"+kodeBahan[x]+"' THEN "+(x+1)+" \r\nEND";
                        } else {
                            sqlBahan += "\tWHEN '"+kodeBahan[x]+"' THEN "+(x+1)+"\n";                            
                        }
                    }
                    System.out.println(sqlBahan);
                    
                    ResultSet sql2 = ml.getSQL(sqlBahan);
                    int y =0;
                    while(sql2.next()) {
                        Table subWrapper = new Table(UnitValue.createPercentArray(subColumn))
                        .useAllAvailableWidth().setBorder(Border.NO_BORDER)
                        .setPadding(0f).setMargin(0f).setFixedLayout();
                        
                        wrapper.addCell(new Cell(1,1)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.RIGHT).add("")
                            .setFontSize(7f)
                            .setBorderBottom(new DottedBorder(0.65f)));
                        wrapper.addCell(new Cell(1,1)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.RIGHT).add("")
                            .setFontSize(7f)
                            .setBorderBottom(new DottedBorder(0.65f)));
                        // =============================
                        // SubWrapper Bahan
                        // ==============================
                        if(first == true) {
                            subWrapper.addCell(new Cell(1,1)
                                .setFontSize(7f).setTextAlignment(TextAlignment.LEFT)
                                .setBorder(Border.NO_BORDER)
                                .setBorderRight(new DottedBorder(0.65f)).add("Bahan").setBold());
                            first = false;
                        } else if(first == false){
                            subWrapper.addCell(new Cell(1,1)
                            .setFontSize(7f).setTextAlignment(TextAlignment.LEFT)
                            .setBorder(Border.NO_BORDER)
                            .setBorderRight(new DottedBorder(0.65f)).add("").setBold());
                        }

                        subWrapper.addCell(new Cell(1,1) // Jumlah Satuan
                            .setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)
                            .setBorder(Border.NO_BORDER)
                            .setBorderRight(new DottedBorder(0.65f)).add(qtyBahan[y]));
                        subWrapper.addCell(new Cell(1,1) // Satuan Unit
                            .setFontSize(7f).setTextAlignment(TextAlignment.CENTER)
                            .setBorder(Border.NO_BORDER)
                            .setBorderRight(new DottedBorder(0.65f)).add(sql2.getString(4)));
                        subWrapper.addCell(new Cell(1,1) // Nama Bahan
                            .setFontSize(7f).setTextAlignment(TextAlignment.LEFT)
                            .setBorder(Border.NO_BORDER).add(sql2.getString(2)+" "+sql2.getString(3)));
                        
                        wrapper.addCell(new Cell(1,1)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.LEFT)
                            .setFontSize(7f).add(subWrapper)
                            .setBorderBottom(new DottedBorder(0.65f)));

                        double subTotalBahan = Double.valueOf(qtyBahan[y]) * sql2.getDouble(5);
                        wrapper.addCell(new Cell(1,1)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.RIGHT).add(String.format("%,.2f", sql2.getDouble(5)))
                            .setFontSize(7f)
                            .setBorderBottom(new DottedBorder(0.65f)));
                        wrapper.addCell(new Cell(1,1)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setFontSize(7f)
                            .setBorderBottom(new DottedBorder(0.65f)));
                        wrapper.addCell(new Cell(1,1)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.RIGHT).add(String.format("%,.2f", subTotalBahan))
                            .setFontSize(7f)
                            .setBorderBottom(new DottedBorder(0.65f)));
                        wrapper.addCell(new Cell(1,1)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.RIGHT).add("")
                            .setFontSize(7f)
                            .setBorderBottom(new DottedBorder(0.65f)));
                        tCostBahan += subTotalBahan;
                        y++;
                    }
                    first = true;
                }
                // klo ada tenaga 
                if(s5 != null) {
                    kodeTenaga = ml.serializeString(s5);
                    qtyTenaga = ml.serializeString(sql.getString(6));
                    for(int x=0; x< qtyTenaga.length; x++) {
                        System.out.print(qtyTenaga[x]+" ");
                    }
                    System.out.println();
                    sqlTenaga = "SELECT * FROM tenaga WHERE kdTenaga in(";
                    for(int x=0; x < kodeTenaga.length; x++) {
                        if(x == (kodeTenaga.length-1)) {
                            sqlTenaga += "'"+kodeTenaga[x]+"') \nORDER BY CASE kdTenaga\n";
                        } else {
                            sqlTenaga += "'"+kodeTenaga[x]+"',";
                        }
                    }
                    for(int x=0; x < qtyTenaga.length; x++) {
                        if(x == (qtyTenaga.length-1)) {
                            sqlTenaga += "\tWHEN '"+kodeTenaga[x]+"' THEN "+(x+1)+" \r\nEND";
                        } else {
                            sqlTenaga += "\tWHEN '"+kodeTenaga[x]+"' THEN "+(x+1)+"\n";                            
                        }
                    }
                    System.out.println(sqlTenaga);
                    ResultSet sql2 = ml.getSQL(sqlTenaga);
                    int y =0;
                    while(sql2.next()) {
                        Table subWrapper = new Table(UnitValue.createPercentArray(subColumn))
                        .useAllAvailableWidth().setBorder(Border.NO_BORDER)
                        .setPadding(0f).setMargin(0f).setFixedLayout();
                        addEmptyCell(wrapper, 2);
                        // =============================
                        // SubWrapper Tenaga
                        // =============================
                        if(first == true) {
                            subWrapper.addCell(new Cell(1,1)
                                .setFontSize(7f).setTextAlignment(TextAlignment.LEFT)
                                .setBorder(Border.NO_BORDER)
                                .setBorderRight(new DottedBorder(0.65f)).add("Tenaga").setBold());
                            first = false;
                        } else if(first == false){
                            subWrapper.addCell(new Cell(1,1)
                            .setFontSize(7f).setTextAlignment(TextAlignment.LEFT)
                            .setBorder(Border.NO_BORDER)
                            .setBorderRight(new DottedBorder(0.65f)).add("").setBold());
                        }

                        subWrapper.addCell(new Cell(1,1)
                            .setFontSize(7f).setTextAlignment(TextAlignment.RIGHT)
                            .setBorder(Border.NO_BORDER)
                            .setBorderRight(new DottedBorder(0.65f)).add(qtyTenaga[y]));
                        subWrapper.addCell(new Cell(1,1)
                            .setFontSize(7f).setTextAlignment(TextAlignment.CENTER)
                            .setBorder(Border.NO_BORDER)
                            .setBorderRight(new DottedBorder(0.65f)).add(sql2.getString(3)));
                        subWrapper.addCell(new Cell(1,1)
                            .setFontSize(7f).setTextAlignment(TextAlignment.LEFT)
                            .setBorder(Border.NO_BORDER).add(sql2.getString(2)));
                        
                        wrapper.addCell(new Cell(1,1)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.LEFT)
                            .setFontSize(7f).add(subWrapper)
                            .setBorderBottom(new DottedBorder(0.65f)));

                        double subTotalTenaga = Double.valueOf(qtyTenaga[y]) * sql2.getDouble(4);
                        wrapper.addCell(new Cell(1,1)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.RIGHT).add(String.format("%,.2f", sql2.getDouble(4)))
                            .setFontSize(7f)
                            .setBorderBottom(new DottedBorder(0.65f)));
                        wrapper.addCell(new Cell(1,1)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.RIGHT).add(String.format("%,.2f", subTotalTenaga))
                            .setFontSize(7f)
                            .setBorderBottom(new DottedBorder(0.65f)));
                        wrapper.addCell(new Cell(1,1)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.RIGHT).add("")
                            .setFontSize(7f)
                            .setBorderBottom(new DottedBorder(0.65f)));
                        wrapper.addCell(new Cell(1,1)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.RIGHT).add("")
                            .setFontSize(7f)
                            .setBorderBottom(new DottedBorder(0.65f)));
                        tCostTenaga += subTotalTenaga;
                        y++;
                    }
                    wrapper.addCell(new Cell(1,4));
                    wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setBackgroundColor(new DeviceRgb(70,82,100))
                        .setFontColor(new DeviceRgb(255,255,255))
                        .setTextAlignment(TextAlignment.RIGHT).add(String.format("%,.2f", tCostTenaga))
                        .setFontSize(7f).setBold().setBorderTop(new SolidBorder(1f))
                        .setBorderBottom(new DottedBorder(0.65f)));
                    wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setBackgroundColor(new DeviceRgb(70,82,100))
                        .setFontColor(new DeviceRgb(255,255,255))
                        .setTextAlignment(TextAlignment.RIGHT).add(String.format("%,.2f", tCostBahan))
                        .setFontSize(7f).setBold().setBorderTop(new SolidBorder(1f))
                        .setBorderBottom(new DottedBorder(0.65f)));
                    wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setBackgroundColor(new DeviceRgb(70,82,100))
                        .setFontColor(new DeviceRgb(255,255,255))
                        .setTextAlignment(TextAlignment.RIGHT).add(String.format("%,.2f", (tCostTenaga+tCostBahan)))
                         .setFontSize(7f).setBold().setBorderTop(new SolidBorder(1f))
                        .setBorderBottom(new DottedBorder(0.65f)));
                    i++;
                }
                wrapper.addCell(new Cell(1,7)
                    .setBackgroundColor(new DeviceRgb(214,90,49)));
            }
            //addEmptyCell(wrapper, 2);
            
            //wrapper.addCell(subWrapper.setMargin(0f).setPadding(0f));
                    
            doc.add(wrapper); 
            initiateSignatureDocument(doc);
            doc.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void genReportKalkulasi(String destination) throws IOException,SQLException,URISyntaxException {
        // Initialize Database
        initiateDB();
        double[] subCost = new double[100];
        // Initialize Document Generator iText [136,159,195]
        pdf = new PdfDocument(new PdfWriter(destination));
        try {
            ResultSet SQL;
            initiateDocument();
            doc.setLeftMargin(20f);
            doc.setBackgroundColor(new DeviceRgb(136,159,195));
            initiateHeader(doc);
            float[] ColumnWidths = new float[]{0.2f, 1.7f, 0.3f, 0.3f, 0.2f, 0.4f, 0.4f};
            Table wrapper = new Table(ColumnWidths).setFixedLayout()
                    .setWidth(523).setWidthPercent(100)
                    .setBackgroundColor(new DeviceRgb(238,238,238));
            ml.setSTMT("SELECT deskripsi FROM proyek WHERE kdProyek='"+this.kdProyek+"'");
            SQL = ml.getSQL(); SQL.next();
            String temp = SQL.getString(1);
            String fixtemp = temp.replaceAll("<br>", "\\\n");
            String test = "PRA-KALKULASI :\n"+fixtemp;
                
            setSubHeaderText(test);
            Cell header = new Cell(1,7)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setSpacingRatio(0.5f)
                    .setBackgroundColor(new DeviceRgb(34,40,49))
                    .setFontColor(new DeviceRgb(255,255,255))
                    .setPaddings(15f, 5f, 10f, 5f)
                    .add(para);
            wrapper.addCell(header);
            
            // setting dan penambahan field header [70,82,100]
            h1 = new Cell(1,1).setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER).setBold()
                    .setBackgroundColor(new DeviceRgb(70,82,100))
                    .setFontColor(new DeviceRgb(255,255,255))
                    .setFontSize(8f).add("NO."); wrapper.addCell(h1); 
            h2 = new Cell(1,1).setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER).setBold()
                    .setBackgroundColor(new DeviceRgb(70,82,100))
                    .setFontColor(new DeviceRgb(255,255,255))
                    .setFontSize(8f).add("URAIAN PEKERJAAN"); wrapper.addCell(h2);
            h3 = new Cell(1,1).setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER).setBold()
                    .setBackgroundColor(new DeviceRgb(70,82,100))
                    .setFontColor(new DeviceRgb(255,255,255))
                    .setFontSize(8f).add("ANL"); wrapper.addCell(h3);
            h4 = new Cell(1,1).setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER).setBold()
                    .setBackgroundColor(new DeviceRgb(70,82,100))
                    .setFontColor(new DeviceRgb(255,255,255))
                    .setFontSize(8f).add("VOLUME"); wrapper.addCell(h4);
            h5 = new Cell(1,1).setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER).setBold()
                    .setBackgroundColor(new DeviceRgb(70,82,100))
                    .setFontColor(new DeviceRgb(255,255,255))
                    .setRotationAngle(80.0)
                    .setFontSize(8f).add("SATUAN"); wrapper.addCell(h5);
            h6 = new Cell(1,1).setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER).setBold()
                    .setBackgroundColor(new DeviceRgb(70,82,100))
                    .setFontColor(new DeviceRgb(255,255,255))
                    .setFontSize(8f).add("HARGA\nSATUAN Rp."); wrapper.addCell(h6);
            h7 = new Cell(1,1).setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER).setBold()
                    .setBackgroundColor(new DeviceRgb(70,82,100))
                    .setFontColor(new DeviceRgb(255,255,255))
                    .setFontSize(8f).add("JUMLAH HARGA\nRp."); wrapper.addCell(h7);
                    
            int i = 0;
            String query = "SELECT mkalkulasi.kdKalkulasi, mkalkulasi.Deskripsi "
                    + "FROM mkalkulasi WHERE kdProyek='"+this.kdProyek+"'";
            System.out.println(this.kdProyek);
            ml.setSTMT(query);
            SQL = ml.getSQL();
            while(SQL.next()){
                s1 = (i+1)+"";
                s2 = SQL.getString(1); // kdKalkulasi
                s3 = SQL.getString(2); // Deskripsi
                double tCost=0.0;
                String sqlSub = "SELECT kalkulasi.kdKalkulasi ,`muraian`.`Deskripsi`, `kalkulasi`.`kdUraian`, `kalkulasi`.`volume`,\n" +
                                  " `kalkulasi`.`satuan`, (`costperuraian`.`CostBahan`+`costperuraian`.`CostTenaga`) AS hSatuan\n" +
                                  "FROM muraian, kalkulasi, costperuraian\n" +
                                  "WHERE muraian.kdUraian = kalkulasi.kdUraian \n" +
                                  " AND kalkulasi.kdUraian = costperuraian.kdUraian\n" +
                                  " AND kalkulasi.kdKalkulasi = '"+s2+"'";
                ResultSet SQL2 = ml.getSQL(sqlSub);
                int y=0;
                
                //setting content
                wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(7f).add(s1).setBold());
                wrapper.addCell(new Cell(1,6)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(7f).add(s3).setBold());
                
                while(SQL2.next()){
                    tCost += (SQL2.getDouble(6) * SQL2.getDouble(4));
                    wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(7f).add((++y)+")")
                        .setBorderBottom(new DottedBorder(0.65f)));
                    wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(7f).add(SQL2.getString(2))
                        .setBorderBottom(new DottedBorder(0.65f)));
                    wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(7f).add(SQL2.getString(3))
                        .setBorderBottom(new DottedBorder(0.65f)));
                    wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(7f).add(String.format("%,.2f", SQL2.getDouble(4)))
                        .setBorderBottom(new DottedBorder(0.65f)));
                    wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(7f).add(SQL2.getString(5))
                        .setBorderBottom(new DottedBorder(0.65f)));
                    wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setFontSize(7f).add(String.format("%,.2f", SQL2.getDouble(6)))
                        .setBorderBottom(new DottedBorder(0.65f)));
                    wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setFontSize(7f).add(String.format("%,.2f", (SQL2.getDouble(6) * SQL2.getDouble(4)) ))
                        .setBorderBottom(new DottedBorder(0.65f)));
                }
                subCost[i] = tCost;
                wrapper.addCell(new Cell(1,6)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(new DeviceRgb(255,255,255))
                        .setFontColor(new DeviceRgb(34,40,49))
                        .setFontSize(7f).add("JUMLAH "+(i+1)).setBold());
                wrapper.addCell(new Cell(1,1)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setBackgroundColor(new DeviceRgb(255,255,255))
                        .setFontColor(new DeviceRgb(34,40,49))
                        .setFontSize(7f).add(String.format("%,.2f", (subCost[i]) )).setBold());
                wrapper.addCell(new Cell(1,7).setBackgroundColor(new DeviceRgb(214,90,49)));
                i++;
                
            }
            // rekapitulasi
            wrapper.addCell(new Cell(1,7)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(new DeviceRgb(70,82,100))
                .setFontColor(new DeviceRgb(255,255,255))
                .setFontSize(7f).add("REKAPITULASI : ").setBold());
                
            SQL = ml.getSQL();
            int x = 1;
            double totalCost = 0.0;
            while(SQL.next()) {
                wrapper.addCell(new Cell(1,1)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(new DeviceRgb(255,255,255))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setFontSize(7f).add(x+"").setBold());
                wrapper.addCell(new Cell(1,5)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setBackgroundColor(new DeviceRgb(255,255,255))
                    .setFontColor(new DeviceRgb(34,40,49))
                    .setFontSize(7f).add(SQL.getString(2)).setBold());
                wrapper.addCell(new Cell(1,1)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBackgroundColor(new DeviceRgb(255,255,255))
                    .setFontColor(new DeviceRgb(214,90,49))
                    .setFontSize(7f).add(String.format("%,.2f", (subCost[x-1]) )).setBold());
                totalCost += subCost[x-1];
                x++;
            }
            
            wrapper.addCell(new Cell(1,6)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(new DeviceRgb(70,82,100))
                .setFontColor(new DeviceRgb(255,255,255))
                .setFontSize(7f).add("JUMLAH SELURUH POS : ").setBold());
            wrapper.addCell(new Cell(1,1)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.RIGHT)
                .setBackgroundColor(new DeviceRgb(70,82,100))
                .setFontColor(new DeviceRgb(255,255,255))
                .setFontSize(7f).add(String.format("%,.2f", totalCost )).setBold());
            wrapper.addCell(new Cell(1,6)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(new DeviceRgb(70,82,100))
                .setFontColor(new DeviceRgb(255,255,255))
                .setFontSize(7f).add("DIBULATKAN MENJADI : ").setBold());
            wrapper.addCell(new Cell(1,1)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.RIGHT)
                .setBackgroundColor(new DeviceRgb(70,82,100))
                .setFontColor(new DeviceRgb(255,255,255))
                .setFontSize(7f).add(String.format("%,.2f", (double)Math.round(totalCost) )).setBold());
            doc.add(wrapper); 
            initiateSignatureDocument(doc);
            doc.close();
        } catch (IOException|SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected void manipulatePdf(String src, String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        int n = pdfDoc.getNumberOfPages();
        for (int i = 1; i <= n; i++) {
            doc.showTextAligned(new Paragraph(String.format("%s of %s", i, n)).setFontSize(7f),
                    580, 10, i, TextAlignment.RIGHT, VerticalAlignment.BOTTOM, 0);
                    //559
        }
        doc.close();
    }
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public void main(int param) throws IOException, SQLException {
        //mm = MainMenu.getInstance();
        //this.kdProyek = mm.kdProyek;
        this.Dest = "output/"+this.kdProyek+"-MasterMaterial.pdf";
    // Initialize Directory File
        initiateDirectory();
        
    // Open File using Desktop
        Desktop desktop = Desktop.getDesktop();
        try {
            switch (param){
                case 1 :
                    genReportMasterMaterial("output/"+this.kdProyek+"-MasterMaterial.pdf");
                    manipulatePdf("output/"+this.kdProyek+"-MasterMaterial.pdf", "output/"+this.kdProyek+"-MasterMaterial-fix.pdf");
                    fl = new File("output/"+this.kdProyek+"-MasterMaterial-fix.pdf");
                    if (fl.exists()) desktop.open(fl);
                    break;
                case 2 :
                    genReportAnalystJob("output/"+this.kdProyek+"-MasterAnalyst.pdf");
                    manipulatePdf("output/"+this.kdProyek+"-MasterAnalyst.pdf", "output/"+this.kdProyek+"-MasterAnalyst-fix.pdf");
                    fl = new File("output/"+this.kdProyek+"-MasterAnalyst-fix.pdf");
                    if (fl.exists()) desktop.open(fl);
                    break;
                case 3 :
                    genReportKalkulasi("output/"+this.kdProyek+"-PraKalkulasi.pdf");
                    manipulatePdf("output/"+this.kdProyek+"-PraKalkulasi.pdf", "output/"+this.kdProyek+"-PraKalkulasi-fix.pdf");
                    fl = new File("output/"+this.kdProyek+"-PraKalkulasi-fix.pdf");
                    if (fl.exists()) desktop.open(fl);
                    break;
                default:
                    genReportMasterMaterial("output/"+this.kdProyek+"-MasterMaterial.pdf");
                    manipulatePdf("output/"+this.kdProyek+"-MasterMaterial.pdf", "output/"+this.kdProyek+"-MasterMaterial-fix.pdf");
                    
                    genReportAnalystJob("output/"+this.kdProyek+"-MasterAnalyst.pdf");
                    manipulatePdf("output/"+this.kdProyek+"-MasterAnalyst.pdf", "output/"+this.kdProyek+"-MasterAnalyst-fix.pdf");
                    
                    genReportKalkulasi("output/"+this.kdProyek+"-PraKalkulasi.pdf");
                    manipulatePdf("output/"+this.kdProyek+"-PraKalkulasi.pdf", "output/"+this.kdProyek+"-PraKalkulasi-fix.pdf");
                    fl = new File("output/"+this.kdProyek+"-MasterMaterial-fix.pdf");
                    if (fl.exists()) desktop.open(fl);
                    fl = new File("output/"+this.kdProyek+"-MasterAnalyst-fix.pdf");
                    if (fl.exists()) desktop.open(fl);
                    fl = new File("output/"+this.kdProyek+"-PraKalkulasi-fix.pdf");
                    if (fl.exists()) desktop.open(fl);
                    break;       
            }
        } catch (Exception ex) {
            //System.setErr(new PrintStream(new FileOutputStream(System.getProperty("test")+"/error.log")));
            ex.printStackTrace();
            //JOptionPane.showMessageDialog(null, ex.getMessage());

        }
    // Generate Report using JasperReport
    /*
        try {
            Model ml = new Model();
            ml.setDB("RAB");
            ml.setUSER("root");
            ml.setPASS("123456");
            Connection conn = Model.inisiasiDB();
            String fName = "src/rab/Laporan.jrxml";
            String fFill = "src/rab/Laporan.jasper";
            JasperCompileManager.compileReport(fName);
            Map param = new HashMap();
            JasperFillManager.fillReport(fFill,param,conn);
            JasperPrint jp = JasperFillManager.fillReport(fFill,param,conn);
            JasperViewer.viewReport(jp, false);
        } catch(SQLException | JRException e) {
            System.out.println(e.toString());
        }
    */    
        
    }
    
}
