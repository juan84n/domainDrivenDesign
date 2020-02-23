package com.ddd.infrastructure.shared.transformer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.ddd.infrastructure.shared.UtilApp;
import com.ddd.infrastructure.shared.dto.AccountDto;
import com.ddd.infrastructure.shared.dto.TransactionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UtilTransform {

	/**
	 * Method for transforming from json to an accountdto
	 * @param json
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public static AccountDto transformJsonToAccount(String json) throws JsonProcessingException, IOException {
	     JsonNode productNode = new ObjectMapper().readTree(json);
	     
	     return new AccountDto(productNode.get("account").get("active-card").asBoolean(),
	    		 productNode.get("account").get("available-limit").asLong());
	}
	
	/**
	 * Method for transforming from json to transactiondto
	 * @param json
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static TransactionDto transformJsonToTransaction(String json) throws JsonProcessingException, IOException, ParseException {
	     JsonNode productNode = new ObjectMapper().readTree(json);
	     Date time = UtilApp.dateMapper(productNode.get("transaction").get("time").asText());
	     
	     return new TransactionDto(productNode.get("transaction").get("merchant").toString(),
	    		 productNode.get("transaction").get("amount").asLong(), time);
	}
	
}
