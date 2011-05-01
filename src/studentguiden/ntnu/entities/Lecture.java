package studentguiden.ntnu.entities;

import java.util.ArrayList;

public class Lecture {
	private String  activityDescription, day, start, end, room, roomCode;
	private ArrayList<String> studyProgrammes;
	private int dayNumber;
	private String[] weeks;


	/**
	 * @return the weeks
	 */
	public String[] getWeeks() {
		return weeks;
	}

	public String getWeeksText() {
		if(weeks.length==1) {
			return weeks[0];
		} else if(weeks.length >2) {
			return weeks[0]+"-"+weeks[weeks.length-1];
		}else {
			return "";
		}
	}
	/**
	 * @param weeks splits the weeks on each "," separating them and creates an array of weeks
	 */
	public void setWeeks(String weeks) {
		setWeeks(weeks.split(","));
	}

	public void setWeeks(String[] weeks) {
		this.weeks = weeks;
	}
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
	 * @return the dayNumber - 1 for sunday, 2 for monday..
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

}
