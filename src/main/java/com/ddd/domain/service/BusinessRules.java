package com.ddd.domain.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ddd.domain.model.Account;
import com.ddd.domain.model.Authorization;
import com.ddd.domain.model.Transaction;

public class BusinessRules {
	public static final String ACCOUNT_NO_INITIALIZED = "account-not-initialized";
	public static final String CARD_NOT_ACTIVE = "card-not-active";
	public static final String INSUFFICIENT_LIMIT = "insufficient-limit";
	public static final String HIGH_FRECUENCY = "high-frequency-small-interval";
	public static final String DOUBLED_TRANSACTION = "doubled-transaction";
	public static final String ACCOUNT_ALREADY_INIT = "account-already-initialized";
	
	
	AccountService accountServices;
	TransactionService transactionServices;
	
	public BusinessRules(AccountService accountService, TransactionService transactionService) {
		this.accountServices = accountService;
		this.transactionServices = transactionService;
	}
	
	/**
	 * Method to validate if an account wasn't initialized
	 * @param acc
	 * @return
	 */
	public Authorization accountNoInit() {
		Account acc = this.accountServices.findAllAccounts().stream().findFirst().orElse(null);
		if(acc == null) {
			String[] violations = {BusinessRules.ACCOUNT_NO_INITIALIZED};
			return new Authorization(null, Arrays.asList(violations));
		}
		 return new Authorization(acc, Arrays.asList());

	}
	
	/**
	 * method to validate if the account card is active
	 * @param acc
	 * @return
	 */
	public Authorization IsActiveCard() {
		Account acc = this.accountServices.findAllAccounts().stream().findFirst().orElse(null);
		if(acc != null && !acc.isActiveCard()) {
			String[] violations = {BusinessRules.CARD_NOT_ACTIVE};
			return new Authorization(acc, Arrays.asList(violations));
		}
		 return new Authorization(acc, Arrays.asList());
	}
	
	/**
	 * Method to validate if an account already exists
	 * @param acc
	 * @return
	 */
	public Authorization accountAlreadyInit() {
		Account acc = this.accountServices.findAllAccounts().stream().findFirst().orElse(null);
		
		if(acc != null) {
			String[] violations = {BusinessRules.ACCOUNT_ALREADY_INIT};
			return new Authorization(acc, Arrays.asList(violations));
		}
		 return new Authorization(acc, Arrays.asList());
		
	}
	
	/**
	 * Method to validate if the current transaction value is over the account available limit.
	 * @param listTransactions
	 * @param currentTransaction
	 * @param acc
	 * @return
	 */
	public Authorization insufficientLimit(Transaction currentTransaction) {
		Account acc = this.accountServices.findAllAccounts().stream().findFirst().orElse(null);
		List<Transaction> listTransactions = this.transactionServices.findAllTransactions();
		long amountSpent = 0;
		for(Transaction transaction : listTransactions) {
			amountSpent += transaction.getAmount();
		}
		
		amountSpent += currentTransaction.getAmount();
		
		if(acc != null && amountSpent > acc.getAvailableLimit()) {
			String[] violations = {BusinessRules.INSUFFICIENT_LIMIT};
			return new Authorization(acc, Arrays.asList(violations));
		}
		 return new Authorization(acc, Arrays.asList());
	}
	
	/**
	 * method to validate if more than 3 transactions were sent in less 2 minutes.
	 * @param listTransactions
	 * @param currentTransaction
	 * @param acc
	 * @return
	 */
	public Authorization highFrecuency(Transaction currentTransaction) {
		Account acc = this.accountServices.findAllAccounts().stream().findFirst().orElse(null);
		List<Transaction> listTransactions = this.transactionServices.findAllTransactions();
		int countTransactions = 1;
		for(Transaction transaction : listTransactions) {
			if(this.compareDates(transaction.getTime(), currentTransaction.getTime())) {
				countTransactions++;
			}
		}
		
		if(countTransactions > 3) {
			String[] violations = {BusinessRules.HIGH_FRECUENCY};
			return new Authorization(acc, Arrays.asList(violations));
		}
		 return new Authorization(acc, Arrays.asList());
	}
	
	/**
	 * method to validate if two transactions with the same information was sent in less 2 minutes
	 * @param listTransactions
	 * @param currentTransaction
	 * @param acc
	 * @return
	 */
	public Authorization doubledTransaction(Transaction currentTransaction) {
		Account acc = this.accountServices.findAllAccounts().stream().findFirst().orElse(null);
		List<Transaction> listTransactions = this.transactionServices.findAllTransactions();
		int countTransactions = 1;
		for(Transaction transaction : listTransactions) {
			if(this.compareDates(transaction.getTime(), currentTransaction.getTime()) && 
					transaction.getMerchant().equals(currentTransaction.getMerchant()) &&
					transaction.getAmount() == currentTransaction.getAmount()) {
				countTransactions++;
			}
		}
		String[] violations = {BusinessRules.DOUBLED_TRANSACTION};
		if(countTransactions >= 2) {
			return new Authorization(acc, Arrays.asList(violations));
		}
			
		return new Authorization(acc, Arrays.asList());
	}
	
	/**
	 * Method to compare 2 dates and get the diff in minutes of them.
	 * @param date1
	 * @param date2
	 * @return
	 */
	private boolean compareDates(Date date1, Date date2) {
		
		long diff = date2.getTime() - date1.getTime();
		long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
		
		Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        int day1 = cal1.get(Calendar.DATE);
        int day2 = cal1.get(Calendar.DATE);
        
        int month1 = cal1.get(Calendar.MONTH);
        int month2 = cal1.get(Calendar.MONTH);
        
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal1.get(Calendar.YEAR);
		
        if(day1==day2 && month1== month2 && year1==year2) {
        	long diffMinutesAbs = Math.abs(diffMinutes);
        	return diffMinutesAbs <= 2;
        }
        
        return false;
			
	}
	
	
	

}
