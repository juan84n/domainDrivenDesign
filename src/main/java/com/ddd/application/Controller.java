package com.ddd.application;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ddd.domain.model.Account;
import com.ddd.domain.model.Authorization;
import com.ddd.domain.model.Transaction;
import com.ddd.domain.service.AccountService;
import com.ddd.domain.service.BusinessRules;
import com.ddd.domain.service.TransactionService;
import com.ddd.infrastructure.shared.dto.AccountDto;
import com.ddd.infrastructure.shared.dto.AuthorizationDto;
import com.ddd.infrastructure.shared.dto.TransactionDto;
import com.ddd.infrastructure.shared.transformer.UtilTransform;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Controller {
	
	AccountService accountService;
	
	TransactionService transactionService;
	
	public Controller(AccountService accountService, TransactionService transactionService) {
		
		this.accountService = accountService;
		this.transactionService = transactionService;
	}
	

	/**
	 * Method for validate all transactions in the json list sent from Application.main
	 * @param listJson
	 */
	public void validateAllTransactions(List<String> listJson) {
		listJson.stream().forEach(line -> {
			try {
				ObjectMapper mapper = new ObjectMapper();
				BusinessRules br= new BusinessRules(this.accountService, this.transactionService);
				List<Account> listAccount = accountService.findAllAccounts();
				Account acc = listAccount.stream().findFirst().orElse(null);
				
				if(line.contains("transaction")) {
					if(acc == null) {
						System.out.println(mapper.writeValueAsString(createViolations(br.accountNoInit())));

					}else {
						if(!acc.isActiveCard()) {
							System.out.println(mapper.writeValueAsString(createViolations(br.IsActiveCard())));

						}else {
							TransactionDto transactionDto  = UtilTransform.transformJsonToTransaction(line);
							
							if(!this.validateSingleTransaction(transactionDto, acc)) {
								this.transactionService.saveTransaction(new Transaction(transactionDto.getMerchant(),
										transactionDto.getAmount(), transactionDto.getTime()));
							}
						}
					}
				}
				if(line.contains("account")) {					
					if(acc == null) {
						acc = this.saveAccount(line);
						System.out.println(mapper.writeValueAsString(createViolations(new Authorization(acc,Arrays.asList()))));

					}else {
						System.out.println(mapper.writeValueAsString(createViolations(br.accountAlreadyInit())));

					}
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		});
	}
	
	
	/**
	 * Method that gets a transaction and transform it to an Account object
	 * for being able to save it
	 * 
	 * @param JsonTransaction
	 * @return
	 */
	public Account saveAccount(String JsonTransaction) {
		Account account = null;
		try {
			AccountDto accountDto  = UtilTransform.transformJsonToAccount(JsonTransaction);
			account = new Account(accountDto.isActiveCard(), accountDto.getAvailableLimit());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return this.accountService.saveAccount(account);
		
	}
	
	
	/**
	 * Method that validates a single transaction and validate that transaction with the 
	 * transactions saved in memory.
	 * @param transactionDto
	 * @param acc
	 * @return
	 */
	public boolean validateSingleTransaction(TransactionDto transactionDto, Account acc) {		
			try {
				ObjectMapper mapper = new ObjectMapper();
				BusinessRules br= new BusinessRules(this.accountService, this.transactionService);		
				
				Transaction currentTransaction = new Transaction(transactionDto.getMerchant(), transactionDto.getAmount(),
						transactionDto.getTime());
				
				List<String> listViolations = Stream.of(createViolations(br.insufficientLimit(currentTransaction))
						.getViolations(),createViolations(br.highFrecuency(currentTransaction)).getViolations(),
						createViolations(br.doubledTransaction(currentTransaction)).getViolations())
						.flatMap(x -> x.stream())
				   		.collect(Collectors.toList());
				
				System.out.println(mapper.writeValueAsString(new AuthorizationDto(new AccountDto(acc.isActiveCard(),
						acc.getAvailableLimit()), listViolations)));
				
				return listViolations.size() > 0;

				
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		
		return false;
	}
	
	/**
	 * Method to create violations a put them in an Authorization object
	 * @param auth
	 * @return
	 */
	private AuthorizationDto createViolations(Authorization auth){
		
		if(auth.getAccount() != null) {
			return new AuthorizationDto(new AccountDto(auth.getAccount().isActiveCard(), auth.getAccount().getAvailableLimit())
					, auth.getViolations());
		}

		return new AuthorizationDto(null, auth.getViolations());
	}

}
