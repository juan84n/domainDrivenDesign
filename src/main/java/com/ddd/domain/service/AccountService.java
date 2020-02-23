package com.ddd.domain.service;

import java.util.List;

import com.ddd.domain.model.Account;
import com.ddd.domain.service.dependency.AccountRepositoryI;

public class AccountService {

	AccountRepositoryI accountR;
	
	public AccountService(AccountRepositoryI accountR) {
		this.accountR = accountR;
	}
	
	/**
	 * Method that goes to repository for an account
	 * @param account
	 * @return
	 */
	public Account saveAccount(Account account) {
		this.accountR.saveAccount(account);
		
		return account;
	}
	
	/**
	 * Method that goes to repository for all accounts
	 * @return
	 */
	public List<Account> findAllAccounts(){
		return this.accountR.findAllAccounts();
	}
	
}
