package div.rex.seekfood.ord;

public class QRord {

    private String ord_no;
    private String mem_no;
    private String vendor_no;
    private String verif_code;


    public QRord() {
        // TODO Auto-generated constructor stub
    }


    public QRord(String ord_no, String mem_no, String vendor_no, String verif_code) {
        super();
        this.ord_no = ord_no;
        this.mem_no = mem_no;
        this.vendor_no = vendor_no;
        this.verif_code = verif_code;
    }


    public String getOrd_no() {
        return ord_no;
    }


    public void setOrd_no(String ord_no) {
        this.ord_no = ord_no;
    }


    public String getMem_no() {
        return mem_no;
    }


    public void setMem_no(String mem_no) {
        this.mem_no = mem_no;
    }


    public String getVendor_no() {
        return vendor_no;
    }


    public void setVendor_no(String vendor_no) {
        this.vendor_no = vendor_no;
    }


    public String getVerif_code() {
        return verif_code;
    }


    public void setVerif_code(String verif_code) {
        this.verif_code = verif_code;
    }


    @Override
    public String toString() {
        return "ORD [ord_no=" + ord_no + ", mem_no=" + mem_no + ", vendor_no=" + vendor_no + ", verif_code="
                + verif_code + "]";
    }

}