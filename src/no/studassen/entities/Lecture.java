package no.studassen.entities;

import java.util.ArrayList;

import no.studassen.misc.Util;

public class Lecture {

	private int id;
	
	private String  activityDescription, day, start, end, room, roomCode, courseCode, weeks, color;
	
	//TODO: add databasefield(egen tabell?) for studyprogrammes
	private ArrayList<String> studyProgrammes;
	
	private int dayNumber;
	
	public Lecture(){}

	public Lecture(String code, String start, String end, String day, int dayNumber, String weeks, String room, String roomCode, String activityDescription, String color) {
		this.start = start;
		this.end = end;
		this.day = day;
		this.room = room;
		this.courseCode = code;
		this.weeks = weeks;
		this.dayNumber = dayNumber;
		this.activityDescription = activityDescription;
		this.roomCode = roomCode;
		this.color = color;
	}

//	public String[] retrieveWeeks() {
//		String[] temp = weeks.split("-");
//		if(temp.length >1) {
//			
//			int duration = Integer.parseInt(temp[1])-Integer.parseInt(temp[0]);
//			Util.log("lecture has a duration of "+duration+". this is parsed from "+weeks);
//			String[] out = new String[duration+1];
//			for (int i = 0; i < duration+1; i++) {
//				out[i] = Integer.toString(i+Integer.parseInt(temp[0]));
//			}
//			return out;
//		}
//		return new String[] {""};
//	}
	
	
	
	public String[] retrieveWeeks() {
		return weeks.split(",");
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
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
		String[] weekList = weeks.split(",");
		Util.log("----- length: "+weekList.length);
		if(weekList.length==1) {
			return weekList[0];
		} else if(weekList.length >1) {
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
