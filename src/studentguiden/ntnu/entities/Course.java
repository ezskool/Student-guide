package studentguiden.ntnu.entities;

import java.io.Serializable;
import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "my_courses")
public class Course implements Serializable{
	
	private static final long serialVersionUID = 6539649775644656485L;

	@DatabaseField(id = true)
	private String code;
	
	@DatabaseField
	private String name_no;
	
	@DatabaseField
	private String name_en;
	
	private String credit, studyLevel, courseType, goals, description, prerequisites, timespan;
	private boolean taughtInSpring, taughtInAutumn;
	private ArrayList<Lecture> lectureList;
	
	public Course() {}
	
	public Course(String code, String name_no, String name_en) {
		this.code = code;
		this.name_no = name_no;
		this.name_en = name_en;
	}
	
	/**Returns name, with correct language based on phone selection
	 * 
	 * @return
	 */
	public String getName(){
		//TODO: fix riktig språk
		return name_no;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the norwegian name for the course
	 * @param name
	 */
	public void setNameNo(String name) {
		this.name_no = name;
	}
	
	/**
	 * Sets the english name for the course
	 * @param name
	 */
	public void setNameEn(String name) {
		this.name_en = name;
	}
	
	/**
	 * @return the name
	 */
	public String getNameNo() {
		return name_no;
	}

	/**
	 * @return the name_no
	 */
	public String getName_no() {
		return name_no;
	}

	/**
	 * @return the name_en
	 */
	public String getName_en() {
		return name_en;
	}
	
	public String getCourseText() {
		return getCode()+" - "+getName();
	}

	public void addLecture(Lecture lecture) {		
		getLectureList().add(lecture);
	}

	/**
	 * @return the credit
	 */
	public String getCredit() {
		return credit;
	}

	/**
	 * @param credit the credit to set
	 */
	public void setCredit(String credit) {
		this.credit = credit;
	}

	/**
	 * @return the studyLevel
	 */
	public String getStudyLevel() {
		return studyLevel;
	}

	/**
	 * @param studyLevel the studyLevel to set
	 */
	public void setStudyLevel(String studyLevel) {
		this.studyLevel = studyLevel;
	}

	/**
	 * @return the courseType
	 */
	public String getCourseType() {
		return courseType;
	}

	/**
	 * @param courseType the courseType to set
	 */
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	/**
	 * @return the goals
	 */
	public String getGoals() {
		return goals;
	}

	/**
	 * @param goals the goals to set
	 */
	public void setGoals(String goals) {
		this.goals = goals;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the prerequisites
	 */
	public String getPrerequisites() {
		return prerequisites;
	}

	/**
	 * @param prerequisites the prerequisites to set
	 */
	public void setPrerequisites(String prerequisites) {
		this.prerequisites = prerequisites;
	}

	/**
	 * @return the timespan
	 */
	public String getTimespan() {
		return timespan;
	}

	/**
	 * @param timespan the timespan to set
	 */
	public void setTimespan(String timespan) {
		this.timespan = timespan;
	}

	/**
	 * @return the taughtInSpring
	 */
	public boolean isTaughtInSpring() {
		return taughtInSpring;
	}

	/**
	 * @param taughtInSpring the taughtInSpring to set
	 */
	public void setTaughtInSpring(boolean taughtInSpring) {
		this.taughtInSpring = taughtInSpring;
	}

	/**
	 * @return the taughtInAutumn
	 */
	public boolean isTaughtInAutumn() {
		return taughtInAutumn;
	}

	/**
	 * @param taughtInAutumn the taughtInAutumn to set
	 */
	public void setTaughtInAutumn(boolean taughtInAutumn) {
		this.taughtInAutumn = taughtInAutumn;
	}

	/** 
	 * @return the lectureList - lazily created.
	 */
	public ArrayList<Lecture> getLectureList() {
		if(lectureList == null) {
			lectureList = new ArrayList<Lecture>();
		}
		return lectureList;
	}

	/**
	 * @param lectureList the lectureList to set
	 */
	public void setLectureList(ArrayList<Lecture> lectureList) {
		this.lectureList = lectureList;
	}

	/**
	 * @param nameNo the name_no to set
	 */
	public void setName_no(String nameNo) {
		this.name_no = nameNo;
	}

	/**
	 * @param nameEn the name_en to set
	 */
	public void setName_en(String nameEn) {
		this.name_en = nameEn;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
