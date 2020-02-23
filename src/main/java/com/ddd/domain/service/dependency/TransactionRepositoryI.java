package com.ddd.domain.service.dependency;

import java.util.List;

import com.ddd.domain.model.Transaction;

public interface TransactionRepositoryI {
	public void saveTransaction(Transaction transaction);
	public List<Transaction> findAllTransactions();
}
