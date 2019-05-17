package div.rex.seekfood.ord;

public class OrdVO implements java.io.Serializable {
    private String ord_no;
    private String mem_no;
    private String vendor_no;
    private String tbl_no;
    private Integer party_size;
    private String share_mem_no1;
    private String share_mem_no2;
    private Integer share_amount;
//    private java.sql.Timestamp ord_time;
//    private java.sql.Date booking_date;
    private String booking_time;
    private String notes;
    private Integer total;
    private String arrival_time;
    private String finish_time;
    private String verif_code;
    private Integer status;

    public OrdVO() {
        super();
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

    public String getTbl_no() {
        return tbl_no;
    }

    public void setTbl_no(String tbl_no) {
        this.tbl_no = tbl_no;
    }

    public Integer getParty_size() {
        return party_size;
    }

    public void setParty_size(Integer party_size) {
        this.party_size = party_size;
    }

    public String getShare_mem_no1() {
        return share_mem_no1;
    }

    public void setShare_mem_no1(String share_mem_no1) {
        this.share_mem_no1 = share_mem_no1;
    }

    public String getShare_mem_no2() {
        return share_mem_no2;
    }

    public void setShare_mem_no2(String share_mem_no2) {
        this.share_mem_no2 = share_mem_no2;
    }

    public Integer getShare_amount() {
        return share_amount;
    }

    public void setShare_amount(Integer share_amount) {
        this.share_amount = share_amount;
    }

//    public java.sql.Timestamp getOrd_time() {
//        return ord_time;
//    }
//
//    public void setOrd_time(java.sql.Timestamp ord_time) {
//        this.ord_time = ord_time;
//    }

//    public java.sql.Date getBooking_date() {
//        return booking_date;
//    }
//
//    public void setBooking_date(java.sql.Date booking_date) {
//        this.booking_date = booking_date;
//    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public String getVerif_code() {
        return verif_code;
    }

    public void setVerif_code(String verif_code) {
        this.verif_code = verif_code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
