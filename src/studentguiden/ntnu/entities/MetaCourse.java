package studentguiden.ntnu.entities;

public class MetaCourse {
	private String code, name, courseText;
	
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
	
	
	
	
}
