package div.rex.seekfood.chat;

import java.io.Serializable;

public class Friend_ListVo implements Serializable {

    private String mem_no;
    private String frie_no;
    private Integer frie_code;

    public String getMem_no() {
        return mem_no;
    }

    public void setMem_no(String mem_no) {
        this.mem_no = mem_no;
    }

    public String getFrie_no() {
        return frie_no;
    }

    public void setFrie_no(String frie_no) {
        this.frie_no = frie_no;
    }

    public Integer getFrie_code() {
        return frie_code;
    }

    public void setFrie_code(Integer frie_code) {
        this.frie_code = frie_code;
    }
}
