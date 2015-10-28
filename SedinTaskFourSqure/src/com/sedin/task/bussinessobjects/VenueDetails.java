package com.sedin.task.bussinessobjects;

public class VenueDetails {
	private String name;
	private String city;
	private String lat="0.0";
	private String lang="0.0";
	private String address="";
	private boolean isFavorate;
	public boolean isFavorate() {
		return isFavorate;
	}

	public void setFavorate(boolean isFavorate) {
		this.isFavorate = isFavorate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	

	

	private String category;

	public VenueDetails() {
		this.name = "";
		this.city = "";
		this.setCategory("");
	}

	public String getCity() {
		if (city.length() > 0) {
			return city;
		}
		return city;
	}

	public void setCity(String city) {
		if (city != null) {
			this.city = city.replaceAll("\\(", "").replaceAll("\\)", "");
			;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
