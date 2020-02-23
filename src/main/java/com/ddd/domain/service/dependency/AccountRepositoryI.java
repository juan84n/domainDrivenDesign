package com.ddd.domain.service.dependency;

import java.util.List;

import com.ddd.domain.model.Account;

public interface AccountRepositoryI {
	public void saveAccount(Account account);
	public List<Account> findAllAccounts();

}
