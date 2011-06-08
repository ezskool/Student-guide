package studentguiden.ntnu.entities;

import java.util.ArrayList;
import java.util.List;

import studentguiden.ntnu.misc.Util;

public class Canteen {
	private String name, url;
	private int pictureResource;
	private boolean checked;
	private ArrayList<Dinner> dinners;

	public Canteen(String name, int res, String URI) {
		this.name = name;
		this.pictureResource = res;
		this.url = URI;
		this.dinners = new ArrayList<Dinner>();
	}
	
	/**
	 * @return the place
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the place to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the selected
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.checked = selected;
	}

	/**
	 * @return the pictureResource
	 */
	public int getPictureResource() {
		return pictureResource;
	}

	/**
	 * @param pictureResource the pictureResource to set
	 */
	public void setPictureResource(int pictureResource) {
		this.pictureResource = pictureResource;
	}

	/**
	 * @return the uRI
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param uRI the uRI to set
	 */
	public void setUrl(String uRI) {
		url = uRI;
	}
	
	public ArrayList<Dinner> getDinners() {
		return this.dinners;
	}
	
	public void addDinner(String description, String price, boolean vegetarian, boolean lactoseFree, boolean glutenFree) {
		dinners.add(new Dinner(description, price, vegetarian, lactoseFree, glutenFree));
	}
	
	public void addDinner(Dinner dinner) {
		dinners.add(dinner);
	}
	
	public void addDinners(List<Dinner> dinners) {
		Util.log("adding "+dinners.size()+" dinners to canteen: "+name);
		this.dinners.addAll(dinners);
	}
	
	
	
}
