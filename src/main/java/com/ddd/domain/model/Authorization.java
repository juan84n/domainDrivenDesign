package com.ddd.domain.model;

import java.util.List;

public class Authorization {
	Account account;
	List<String> violations;
	
	public Authorization(Account account, List<String> violations) {
		this.account = account;
		this.violations = violations;
				
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @return the violations
	 */
	public List<String> getViolations() {
		return violations;
	}
}
