/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "analisa"
})
@XmlRootElement(name = "AnlKerja")
public class AnlKerja {

    @XmlElement(name = "Analisa")
    protected ArrayList<AnlKerja.Analisa> analisa;

    public ArrayList<AnlKerja.Analisa> getAnalisa() {
        if (analisa == null) {
            analisa = new ArrayList<>();
        }
        return this.analisa;
    }
    public void setAnalisa(ArrayList<AnlKerja.Analisa> param){
        this.analisa = param;
    }
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "kode",
        "uraian",
        "material",
        "jumlah"
    })
    public static class Analisa {

        @XmlElement(name = "Kode", required = true)
        protected String kode;
        @XmlElement(name = "Uraian", required = true)
        protected String uraian;
        @XmlElement(name = "Material", required = true)
        protected AnlKerja.Analisa.Material material;
        @XmlElement(name = "Jumlah", required = true)
        protected double jumlah;
        @XmlAttribute(name = "no")
        protected int no;
        
        public String getKode() {
            return kode;
        }

        public void setKode(String value) {
            this.kode = value;
        }

        public String getUraian() {
            return uraian;
        }

        public void setUraian(String value) {
            this.uraian = value;
        }

        public AnlKerja.Analisa.Material getMaterial() {
            return material;
        }

        public void setMaterial(AnlKerja.Analisa.Material value) {
            this.material = value;
        }

        public double getJumlah() {
            return jumlah;
        }

        public void setJumlah(double value) {
            this.jumlah = value;
        }

        public int getNo() {
            return no;
        }

        public void setNo(int value) {
            this.no = value;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "item"
        })
        public static class Material {

            protected ArrayList<AnlKerja.Analisa.Material.Item> item;
            public ArrayList<AnlKerja.Analisa.Material.Item> getItem() {
                if (item == null) {
                    item = new ArrayList<>();
                }
                return this.item;
            }
            public void setItem(ArrayList<AnlKerja.Analisa.Material.Item> param) {
                this.item = param;
            }
            public ArrayList<AnlKerja.Analisa.Material.Item> getItem(Boolean param){
                return this.item;
            }
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "value"
            })
            public static class Item {

                @XmlValue
                protected String value;
                @XmlAttribute(name = "type")
                protected String type;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
                public String[] valueToArray() {
                    String data = this.getValue();
                    String[] output = data.split(Pattern.quote("|"));
                    return output;
                }
                public void setValue(String[] items){
                    int temp = items.length;
                    for (int x=0; x<temp; x++) {
                        if(x == (temp-1)){
                            this.value += items[x];
                        } else {
                            this.value += items[x]+"|";
                        }
                    }
                }
                public String getType() {
                    return type;
                }

                public void setType(String value) {
                    this.type = value;
                }

            }

        }

    }

}
