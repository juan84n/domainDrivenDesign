package com.ddd.infrastructure.shared.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionDto {
	private String merchant;
	private long amount;
	private Date time;
	
	public TransactionDto(@JsonProperty("merchant")String merchant,
			@JsonProperty("amount")long amount, @JsonProperty("time")Date time) {
		this.merchant = merchant;
		this.amount = amount;
		this.time = time;
	}

	/**
	 * @return the merchant
	 */
	public String getMerchant() {
		return merchant;
	}

	/**
	 * @param merchant the merchant to set
	 */
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	/**
	 * @return the amount
	 */
	public long getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(long amount) {
		this.amount = amount;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}
}
