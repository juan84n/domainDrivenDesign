package com.ddd.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import com.ddd.domain.model.Transaction;
import com.ddd.domain.service.dependency.TransactionRepositoryI;

public class TransactionRepositoryMemory implements TransactionRepositoryI {

	List<Transaction> listTransaction = new ArrayList<>();
	
	/**
	 *method to save a transaction in memory
	 */
	@Override
	public void saveTransaction(Transaction transaction) {
		listTransaction.add(transaction);
		
	}

	/**
	 *Method to list all transactions in memory
	 */
	@Override
	public List<Transaction> findAllTransactions() {
		return listTransaction;
	}

}
