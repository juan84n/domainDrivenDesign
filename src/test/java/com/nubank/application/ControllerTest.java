package com.nubank.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.ddd.application.Controller;
import com.ddd.domain.model.Transaction;
import com.ddd.domain.service.AccountService;
import com.ddd.domain.service.BusinessRules;
import com.ddd.domain.service.TransactionService;
import com.ddd.infrastructure.persistence.AccountRepositoryMemory;
import com.ddd.infrastructure.persistence.TransactionRepositoryMemory;
import com.ddd.infrastructure.shared.UtilApp;

public class ControllerTest {

	AccountService accountService = new AccountService(new AccountRepositoryMemory());
	TransactionService transactionService = new TransactionService(new TransactionRepositoryMemory());
	Controller accoCtr = new Controller(accountService,transactionService);
	
	@Test
	public void accountNotInitTest() {
		String[] jsonArray = {"{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 20, \"time\":\"2019-02-13T10:00:00.000Z\"}}"};
				//"{\"account\": {\"active-card\": true, \"available-limit\": 100}}"};
		
		List<String> listJson = Arrays.asList(jsonArray);
		
		accoCtr.validateAllTransactions(listJson);
				
		BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
			
		assertEquals(BusinessRules.ACCOUNT_NO_INITIALIZED, br.accountNoInit().getViolations().stream().findFirst().orElse(null));
	}
	
	@Test
	public void isActiveCardTest() {
		String[] jsonArray = {"{\"account\": {\"active-card\": false, \"available-limit\": 100}}",
				"{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 20, \"time\":\"2019-02-13T10:00:00.000Z\"}}"};
		
		List<String> listJson = Arrays.asList(jsonArray);
		
		accoCtr.validateAllTransactions(listJson);
				
		BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
		
		assertEquals(BusinessRules.CARD_NOT_ACTIVE, br.IsActiveCard().getViolations().stream().findFirst().orElse(null));
	}
	
	@Test
	public void accountAlreadyInitTest() {
		String[] jsonArray = {"{\"account\": {\"active-card\": false, \"available-limit\": 100}}",
				"{\"account\": {\"active-card\": false, \"available-limit\": 100}}"};
		
		List<String> listJson = Arrays.asList(jsonArray);
		
		accoCtr.validateAllTransactions(listJson);
				
		BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
		
		assertEquals(BusinessRules.ACCOUNT_ALREADY_INIT, br.accountAlreadyInit().getViolations().stream().findFirst().orElse(null));
	}
	
	@Test
	public void insufficientLimitTest() {
		
		try {
			String[] jsonArray = {"{\"account\": {\"active-card\": true, \"available-limit\": 100}}",
					"{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 80, \"time\":\"2019-02-13T10:00:00.000Z\"}}",
					"{\"transaction\": {\"merchant\": \"Mcdonalds\", \"amount\": 40, \"time\":\"2019-02-13T10:00:00.000Z\"}}"};
			
			List<String> listJson = Arrays.asList(jsonArray);
				
			Transaction currentTransaction = new Transaction("Papas john",50, UtilApp.dateMapper("2019-02-13T10:00:00.000Z"));
			
			accoCtr.validateAllTransactions(listJson);
						
			BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
			
			assertEquals(BusinessRules.INSUFFICIENT_LIMIT, br.insufficientLimit(currentTransaction)
					.getViolations().stream().findFirst().orElse(null));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void highFrecuencyTest() {
		
		try {
			String[] jsonArray = {"{\"account\": {\"active-card\": true, \"available-limit\": 100}}",
					"{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 10, \"time\":\"2019-02-13T10:00:00.000Z\"}}",
					"{\"transaction\": {\"merchant\": \"Mcdonalds\", \"amount\": 10, \"time\":\"2019-02-13T10:00:00.000Z\"}}",
					"{\"transaction\": {\"merchant\": \"Mcdonalds\", \"amount\": 20, \"time\":\"2019-02-13T10:00:00.000Z\"}}"};
			
			List<String> listJson = Arrays.asList(jsonArray);
			
			accoCtr.validateAllTransactions(listJson);
			
			Transaction currentTransaction = new Transaction("Papas john",10, UtilApp.dateMapper("2019-02-13T10:00:00.000Z"));
			
			BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
			
			assertEquals(BusinessRules.HIGH_FRECUENCY, br.highFrecuency(currentTransaction)
					.getViolations().stream().findFirst().orElse(null));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}	
	}
	
	@Test
	public void doubledTransactionTest() {
		
		try {
			String[] jsonArray = {"{\"account\": {\"active-card\": true, \"available-limit\": 100}}",
					"{\"transaction\": {\"merchant\": \"Mcdonalds\", \"amount\": 10, \"time\":\"2019-02-13T10:00:00.000Z\"}}",
					"{\"transaction\": {\"merchant\": \"Mcdonalds\", \"amount\": 10, \"time\":\"2019-02-13T10:00:00.000Z\"}}"};
			
			
			List<String> listJson = Arrays.asList(jsonArray);
			 
			accoCtr.validateAllTransactions(listJson);
			
			Transaction currentTransaction = new Transaction("\"Mcdonalds\"",10, UtilApp.dateMapper("2019-02-13T10:00:00.000Z"));
									
			BusinessRules br = new BusinessRules(this.accountService, this.transactionService);
			
			assertEquals(BusinessRules.DOUBLED_TRANSACTION, br.doubledTransaction(currentTransaction)
					.getViolations().stream().findFirst().orElse(null));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}	
	}
}
