package studentguiden.ntnu.entities;

public class Dinner {
	private String day, description, price;
	private boolean glutenFree, lactoseFree, Vegetarian;
	
	
	public Dinner() {}
	
	public Dinner(String description, String price, boolean vegetarian, boolean glutenFree, boolean lactoseFree) {
		this.description = description;
		this.price = price;
		this.glutenFree = glutenFree;
		this.lactoseFree = lactoseFree;
		this.Vegetarian = vegetarian;
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
	 * @return the content
	 */
	public String getContent() {
		return description;
	}


	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.description = content;
	}


	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}


	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}


	/**
	 * @return the glutenFree
	 */
	public boolean isGlutenFree() {
		return glutenFree;
	}


	/**
	 * @param glutenFree the glutenFree to set
	 */
	public void setGlutenFree(boolean glutenFree) {
		this.glutenFree = glutenFree;
	}


	/**
	 * @return the lactoseFree
	 */
	public boolean isLactoseFree() {
		return lactoseFree;
	}


	/**
	 * @param lactoseFree the lactoseFree to set
	 */
	public void setLactoseFree(boolean lactoseFree) {
		this.lactoseFree = lactoseFree;
	}


	/**
	 * @return the vegetarian
	 */
	public boolean isVegetarian() {
		return Vegetarian;
	}


	/**
	 * @param vegetarian the vegetarian to set
	 */
	public void setVegetarian(boolean vegetarian) {
		Vegetarian = vegetarian;
	}
	
	
}
