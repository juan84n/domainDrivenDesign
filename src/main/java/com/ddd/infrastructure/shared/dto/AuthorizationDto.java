package com.ddd.infrastructure.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationDto {

	private AccountDto account;
	private List<String> violations;
	
	public AuthorizationDto(@JsonProperty AccountDto account, @JsonProperty List<String> violations) {
		this.account = account;
		this.violations = violations;
	}

	/**
	 * @return the account
	 */
	public AccountDto getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(AccountDto account) {
		this.account = account;
	}

	/**
	 * @return the violations
	 */
	public List<String> getViolations() {
		return violations;
	}

	/**
	 * @param violations the violations to set
	 */
	public void setViolations(List<String> violations) {
		this.violations = violations;
	}
}
