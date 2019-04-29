package div.rex.seekfood.fav_res;

import java.io.Serializable;

public class Fav_ResVo implements Serializable {

	private String mem_no;
	private String vendor_no;

	public Fav_ResVo() {
	}

	
	
	public Fav_ResVo(String mem_no, String vendor_no) {
		super();
		this.mem_no = mem_no;
		this.vendor_no = vendor_no;
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

	@Override
	public String toString() {
		return "Fav_ResVo [mem_no=" + mem_no + ", vendor_no=" + vendor_no + "]";
	}

}
