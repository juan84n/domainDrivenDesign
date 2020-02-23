package com.ddd.domain.model;

import java.util.Date;

public class Transaction {
	private String merchant;
	private long amount;
	private Date time;
	
	public Transaction(String merchant, long amount, Date time) {
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
	 * @return the amount
	 */
	public long getAmount() {
		return amount;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}
}
