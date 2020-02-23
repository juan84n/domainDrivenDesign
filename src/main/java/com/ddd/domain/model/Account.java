package com.ddd.domain.model;

public class Account {
	private boolean activeCard;
	private long availableLimit;
	
	
	public Account(boolean activeCard, long availableLimit) {
		this.activeCard = activeCard;
		this.availableLimit = availableLimit;
	}


	/**
	 * @return the activeCard
	 */
	public boolean isActiveCard() {
		return activeCard;
	}


	/**
	 * @return the availableLimit
	 */
	public long getAvailableLimit() {
		return availableLimit;
	}
	
}
