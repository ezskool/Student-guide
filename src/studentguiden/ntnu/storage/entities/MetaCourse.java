package studentguiden.ntnu.storage.entities;

import java.io.Serializable;


public class MetaCourse implements Serializable{

	private static final long serialVersionUID = -1761189591586938961L;

	private String code;
	
	private String name_no;
	private String name_en;

	public MetaCourse() {}
	
	public MetaCourse(String code, String name_no, String name_en) {
		this.code = code;
		this.name_no = name_no;
		this.name_en = name_en;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name_no
	 */
	public String getName_no() {
		return name_no;
	}

	/**
	 * @param nameNo the name_no to set
	 */
	public void setName_no(String nameNo) {
		name_no = nameNo;
	}

	/**
	 * @return the name_en
	 */
	public String getName_en() {
		return name_en;
	}
	
	//TODO: return en hvis en, no hvis no
	public String getName() {
		return name_no;
	}

	/**
	 * @param nameEn the name_en to set
	 */
	public void setName_en(String nameEn) {
		name_en = nameEn;
	}
	
	public String getCourseText() {
		return code+name_no+name_en;
	}
}
