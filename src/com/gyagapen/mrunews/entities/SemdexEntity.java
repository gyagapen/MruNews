package com.gyagapen.mrunews.entities;

public class SemdexEntity {
	
	private String name;
	private String nominal;
	private String lastClosingPrice;
	private String latestPrice;
	
	public SemdexEntity() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNominal() {
		return nominal;
	}

	public void setNominal(String nominal) {
		this.nominal = nominal;
	}

	public String getLastClosingPrice() {
		return lastClosingPrice;
	}

	public void setLastClosingPrice(String lastClosingPrice) {
		this.lastClosingPrice = lastClosingPrice;
	}

	public String getLatestPrice() {
		return latestPrice;
	}

	public void setLatestPrice(String latest) {
		this.latestPrice = latest;
	}
	

}
