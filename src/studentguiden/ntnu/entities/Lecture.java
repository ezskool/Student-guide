package studentguiden.ntnu.entities;

import java.io.Serializable;
import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import studentguiden.ntnu.misc.Util;

@DatabaseTable(tableName="my_lectures")
public class Lecture implements Serializable {
	private static final long serialVersionUID = 3599770702142308960L;

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField
	private String  activityDescription, day, start, end, room, roomCode, courseCode, weeks;
	
	//TODO: add databasefield(egen tabell?) for studyprogrammes
	private ArrayList<String> studyProgrammes;
	
	@DatabaseField
	private int dayNumber;
	
	public Lecture(){}

	public Lecture(String code, String start, String end, String day, int dayNumber, String weeks, String room) {
		this.start = start;
		this.end = end;
		this.day = day;
		this.room = room;
		this.courseCode = code;
		this.weeks = weeks;
		this.dayNumber = dayNumber;
	}

	public String[] retrieveWeeks() {
		String[] temp = weeks.split("-");
		if(temp.length >1) {
			
			int duration = Integer.parseInt(temp[1])-Integer.parseInt(temp[0]);
			Util.log("lecture has a duration of "+duration+". this is parsed from "+weeks);
			String[] out = new String[duration+1];
			for (int i = 0; i < duration+1; i++) {
				out[i] = Integer.toString(i+Integer.parseInt(temp[0]));
			}
			return out;
		}
		return new String[] {""};
	}

	/**
	 * 
	 * @return id
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * @return the courseCode
	 */
	public String getCourseCode() {
		return courseCode;
	}

	/**
	 * @param courseCode the courseCode to set
	 */
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	/**
	 * @return the weeks
	 */
	public String getWeeks() {
		return weeks;
	}

	public String getWeeksText() {
		String[] weekList = retrieveWeeks();
		if(weekList.length==1) {
			return weekList[0];
		} else if(weekList.length >2) {
			return weekList[0]+"-"+weekList[weekList.length-1];
		}else {
			return "";
		}
	}
//	/**
//	 * @param weeks splits the weeks on each "," separating them and creates an array of weeks
//	 */
//	public void setWeeks(String weeks) {
//		setWeeks(weeks.split(","));
//	}
//
//	public void setWeeks(String weeks) {
//		this.weeks = weeks;
//	}
	/**
	 * @return the activityDescription
	 */
	public String getActivityDescription() {
		return activityDescription;
	}
	/**
	 * @param activityDescription the activityDescription to set
	 */
	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}
	/**
	 * @return the day
	 */
	public String getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(String day) {
		this.day = day;
	}
	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}
	/**
	 * @return the end
	 */
	public String getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}
	/**
	 * @return the room
	 */
	public String getRoom() {
		return room;
	}
	/**
	 * @param room the room to set
	 */
	public void setRoom(String room) {
		this.room = room;
	}
	/**
	 * @return the roomCode
	 */
	public String getRoomCode() {
		return roomCode;
	}
	/**
	 * @param roomCode the roomCode to set
	 */
	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}
	/**
	 * @return the studyProgrammes
	 */
	public ArrayList<String> getStudyProgrammes() {
		return studyProgrammes;
	}
	/**
	 * @param String add the  study program
	 */
	public void addStudyProgramm(String programCode) {
		studyProgrammes.add(programCode);
	}
	/**
	 * @return the dayNumber - 0 for monday, 1 tueday, 2 wednesday, 3 thursday, 4 friday
	 */
	public int getDayNumber() {
		return dayNumber;
	}
	/**
	 * @param dayNumber the dayNumber to set
	 */
	public void setDayNumber(int dayNumber) {
		this.dayNumber = dayNumber;
	}

	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}

}
