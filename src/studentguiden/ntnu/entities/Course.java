package studentguiden.ntnu.entities;

import java.util.ArrayList;

public class Course {
	private String code, name, courseText, credit, studyLevel, courseType, goals, description, prerequisites;
	private boolean taughtInSpring, taughtInAutumn;
	private ArrayList<Lecture> lectureList;

	
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}		
	
	
	
	/**
	 * @return the lectureList
	 */
	public ArrayList<Lecture> getLectureList() {
		return lectureList;
	}

	public String getCourseText() {
		return courseText;
	}
	
	public void addLecture(Lecture lecture) {
		if(lectureList== null) {
			lectureList = new ArrayList<Lecture>();
		}
		lectureList.add(lecture);
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
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param courseText the courseText to set
	 */
	public void setCourseText(String courseText) {
		this.courseText = courseText;
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
	
	
}
