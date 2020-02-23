package com.ddd.domain.service;

import java.util.List;

import com.ddd.domain.model.Transaction;
import com.ddd.domain.service.dependency.TransactionRepositoryI;

public class TransactionService {

	TransactionRepositoryI transactionR;
	
	public TransactionService(TransactionRepositoryI transactionR) {
		this.transactionR = transactionR;
	}
	
	/**
	 * Method that saves in repository a transaction
	 * @param transaction
	 * @return
	 */
	public Transaction saveTransaction(Transaction transaction) {
		this.transactionR.saveTransaction(transaction);
		
		return transaction;
	}
	
	/**
	 * Method that list all transactions saved in repository
	 * @return
	 */
	public List<Transaction> findAllTransactions(){
		return this.transactionR.findAllTransactions();
	}
}
