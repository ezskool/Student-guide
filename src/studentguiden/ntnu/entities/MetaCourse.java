package studentguiden.ntnu.entities;

public class MetaCourse {
	private String code, name, courseText, id;
	
	public MetaCourse(String code, String name) {
		this.name = name;
		this.code = code;
		this.courseText = code+" - "+name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the courseText
	 */
	public String getCourseText() {
		return courseText;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
	
}
