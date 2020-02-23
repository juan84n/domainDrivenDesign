package com.ddd.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import com.ddd.domain.model.Account;
import com.ddd.domain.service.dependency.AccountRepositoryI;

public class AccountRepositoryMemory implements AccountRepositoryI {
	
	List<Account> listAccount = new ArrayList<>();

	/**
	 *Method to save an Account in memory
	 */
	@Override
	public void saveAccount(Account account) {
		listAccount.add(account);
		
	}

	/**
	 *Method to list all accounts in memory
	 */
	@Override
	public List<Account> findAllAccounts() {
		return listAccount;
	}


}
