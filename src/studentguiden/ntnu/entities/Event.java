package studentguiden.ntnu.entities;

import java.util.Calendar;

public class Event {
	private String link, title, description, category, guid, day, hour, exactTime;
	private int bannerResource, iconResource;


	public Event(String title, String description, String category, String guid, int bannerResource, int iconResource) {
		this.title = title;
		this.description = description;
		this.category = category;
		this.guid = guid;
		this.bannerResource = bannerResource;
		this.iconResource = iconResource;
	}


	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 * @return the hour
	 */
	public String getHour() {
		return hour;
	}

	/**
	 * @param hour the hour to set
	 */
	public void setHour(String hour) {
		this.hour = hour;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}

	/**String
	 * @param guid the guid to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * @return the bannerResource
	 */
	public int getBannerResource() {
		return bannerResource;
	}

	/**
	 * @param bannerResource the bannerResource to set
	 */
	public void setBannerResource(int bannerResource) {
		this.bannerResource = bannerResource;
	}

	/**
	 * @return the iconResource
	 */
	public int getIconResource() {
		return iconResource;
	}

	/**
	 * @param iconResource the iconResource to set
	 */
	public void setIconResource(int iconResource) {
		this.iconResource = iconResource;
	}
}
