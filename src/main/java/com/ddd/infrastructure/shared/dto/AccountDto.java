package com.ddd.infrastructure.shared.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountDto {
	
	private boolean activeCard;
	private long availableLimit;
	
	
	public AccountDto(@JsonProperty("active-card")boolean activeCard,
			@JsonProperty("available-limit")long availableLimit) {
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
	 * @param activeCard the activeCard to set
	 */
	public void setActiveCard(boolean activeCard) {
		this.activeCard = activeCard;
	}


	/**
	 * @return the availableLimit
	 */
	public long getAvailableLimit() {
		return availableLimit;
	}


	/**
	 * @param availableLimit the availableLimit to set
	 */
	public void setAvailableLimit(long availableLimit) {
		this.availableLimit = availableLimit;
	}
}
