package com.nubank.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.ddd.domain.model.Account;
import com.ddd.domain.model.Transaction;
import com.ddd.domain.service.AccountService;
import com.ddd.domain.service.BusinessRules;
import com.ddd.domain.service.TransactionService;
import com.ddd.infrastructure.persistence.AccountRepositoryMemory;
import com.ddd.infrastructure.persistence.TransactionRepositoryMemory;
import com.ddd.infrastructure.shared.UtilApp;


public class BusinessRulesTest {
	AccountService accountService = new AccountService(new AccountRepositoryMemory());
	TransactionService transactionService = new TransactionService(new TransactionRepositoryMemory());
	
	@Test
	public void accountNotInitViolationTest() {
				
		BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
			
		assertEquals(BusinessRules.ACCOUNT_NO_INITIALIZED, br.accountNoInit().getViolations().stream().findFirst().orElse(null));
	}
	
	@Test
	public void accountNotInitEmptyViolationTest() {
		
		Account acc = new Account(true, 100);
		
		this.accountService.saveAccount(acc);
		
		BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
		
		assertEquals(null, br.accountNoInit().getViolations().stream().findFirst().orElse(null));
	}
	
	@Test
	public void isActiveCardViolationTest() {
		
		Account acc = new Account(false, 100);
		
		this.accountService.saveAccount(acc);
		
		BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
		
		assertEquals(BusinessRules.CARD_NOT_ACTIVE, br.IsActiveCard().getViolations().stream().findFirst().orElse(null));
	}
	
	@Test
	public void isActiveCardEmptyViolationTest() {
		
		Account acc = new Account(true, 100);
		
		this.accountService.saveAccount(acc);
		
		BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
		
		assertEquals(null, br.IsActiveCard().getViolations().stream().findFirst().orElse(null));
	}
	
	@Test
	public void accountAlreadyInitViolationTest() {
		
		Account acc = new Account(false, 100);
		
		this.accountService.saveAccount(acc);
		
		BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
		
		assertEquals(BusinessRules.ACCOUNT_ALREADY_INIT, br.accountAlreadyInit().getViolations().stream().findFirst().orElse(null));
	}
	
	@Test
	public void accountAlreadyInitEmptyViolationTest() {
		
		Account acc = new Account(false, 100);
		
		this.accountService.saveAccount(acc);
		
		BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
		
		assertEquals(BusinessRules.ACCOUNT_ALREADY_INIT, br.accountAlreadyInit().getViolations().stream().findFirst().orElse(null));
	}
	
	@Test
	public void insufficientLimitViolationTest() throws ParseException {
		
		Account acc = new Account(true, 80);
		
		this.accountService.saveAccount(acc);
		
		Transaction[] transactionsArr = {new Transaction("Burger King",20, UtilApp.dateMapper("2019-02-13T10:00:00.000Z"))};
		
		Arrays.asList(transactionsArr).stream().forEach(transaction -> {
			this.transactionService.saveTransaction(transaction);
		});
		
		Transaction currentTransaction = new Transaction("Burger King",100, UtilApp.dateMapper("2019-02-13T10:00:00.000Z"));
			
		BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
		
		assertEquals(BusinessRules.INSUFFICIENT_LIMIT, br.insufficientLimit(currentTransaction)
					.getViolations().stream().findFirst().orElse(null));
			
	}
	
	@Test
	public void insufficientLimitEmptyViolationTest() throws ParseException {
		
		Account acc = new Account(true, 150);
		
		this.accountService.saveAccount(acc);
		
		Transaction[] transactionsArr = {new Transaction("Burger King",20, UtilApp.dateMapper("2019-02-13T10:00:00.000Z"))};
		
		Arrays.asList(transactionsArr).stream().forEach(transaction -> {
			this.transactionService.saveTransaction(transaction);
		});
		
		Transaction currentTransaction = new Transaction("Burger King",100, UtilApp.dateMapper("2019-02-13T10:00:00.000Z"));
			
		BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
		
		assertEquals(null, br.insufficientLimit(currentTransaction)
					.getViolations().stream().findFirst().orElse(null));
			
	}
	
	@Test
	public void highFrecuencyViolationTest() throws ParseException {

			
		Account acc = new Account(true, 150);
		
		this.accountService.saveAccount(acc);
		
		Transaction[] transactionsArr = {new Transaction("Burger King",20, UtilApp.dateMapper("2019-02-13T10:00:00.000Z")),
				new Transaction("Mcdonalds",20, UtilApp.dateMapper("2019-02-13T10:01:30.000Z")),
				new Transaction("Papas johns",20, UtilApp.dateMapper("2019-02-13T10:01:10.000Z"))};
		
		Arrays.asList(transactionsArr).stream().forEach(transaction -> {
			this.transactionService.saveTransaction(transaction);
		});
		
		Transaction currentTransaction = new Transaction("Burger King",100, UtilApp.dateMapper("2019-02-13T10:01:00.000Z"));
			
		BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
		
		assertEquals(BusinessRules.HIGH_FRECUENCY, br.highFrecuency(currentTransaction)
					.getViolations().stream().findFirst().orElse(null));
	}
	
	@Test
	public void highFrecuencyEmptyViolationTest() throws ParseException {

			
		Account acc = new Account(true, 150);
		
		this.accountService.saveAccount(acc);
		
		Transaction[] transactionsArr = {new Transaction("Burger King",20, UtilApp.dateMapper("2019-02-13T10:00:00.000Z")),
				new Transaction("Mcdonalds",20, UtilApp.dateMapper("2019-02-13T10:01:30.000Z")),
				new Transaction("Papas johns",20, UtilApp.dateMapper("2019-02-13T11:01:10.000Z"))};
		
		Arrays.asList(transactionsArr).stream().forEach(transaction -> {
			this.transactionService.saveTransaction(transaction);
		});
		
		Transaction currentTransaction = new Transaction("Burger King",100, UtilApp.dateMapper("2019-02-13T10:01:00.000Z"));
			
		BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
		
		assertEquals(null, br.highFrecuency(currentTransaction)
					.getViolations().stream().findFirst().orElse(null));
	}
	
	@Test
	public void doubledTransactionViolationTest() throws ParseException {
			
		Account acc = new Account(true, 150);
		
		this.accountService.saveAccount(acc);
		
		Transaction[] transactionsArr = {new Transaction("Mcdonalds",20, UtilApp.dateMapper("2019-02-13T10:00:00.000Z")),
				new Transaction("Mcdonalds",20, UtilApp.dateMapper("2019-02-13T10:01:30.000Z"))};
		
		Arrays.asList(transactionsArr).stream().forEach(transaction -> {
			this.transactionService.saveTransaction(transaction);
		});
		
		Transaction currentTransaction = new Transaction("Mcdonalds",20, UtilApp.dateMapper("2019-02-13T10:01:00.000Z"));
			
		BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
			
		assertEquals(BusinessRules.DOUBLED_TRANSACTION, br.doubledTransaction(currentTransaction)
					.getViolations().stream().findFirst().orElse(null));
			
	}
	
	@Test
	public void doubledTransactionEmptyViolationTest() throws ParseException {
			
		Account acc = new Account(true, 150);
		
		this.accountService.saveAccount(acc);
		
		Transaction[] transactionsArr = {new Transaction("Mcdonalds",20, UtilApp.dateMapper("2019-02-13T10:00:00.000Z")),
				new Transaction("Mcdonalds",20, UtilApp.dateMapper("2019-02-13T10:01:30.000Z"))};
		
		Arrays.asList(transactionsArr).stream().forEach(transaction -> {
			this.transactionService.saveTransaction(transaction);
		});
		
		Transaction currentTransaction = new Transaction("Mcdonalds",50, UtilApp.dateMapper("2019-02-13T10:01:00.000Z"));
			
		BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
			
		assertEquals(null, br.doubledTransaction(currentTransaction)
					.getViolations().stream().findFirst().orElse(null));
			
	}
}
