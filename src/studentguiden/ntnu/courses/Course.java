package studentguiden.ntnu.courses;

public class Course {
	private String code, name, courseText;

	public Course(String code, String name) {
		this.code = code;
		this.name = name;
		this.courseText = code+" - "+name;
	}
	
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}		
	
	public String getCourseText() {
		return courseText;
	}
}
