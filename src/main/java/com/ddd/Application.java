package com.ddd;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ddd.application.Controller;
import com.ddd.domain.service.AccountService;
import com.ddd.domain.service.TransactionService;
import com.ddd.infrastructure.persistence.AccountRepositoryMemory;
import com.ddd.infrastructure.persistence.TransactionRepositoryMemory;

public class Application {

	public static void main(String[] args) {		
		/*
		 * Get the information from arguments sent through console and read it
		 * */
		String path = args[0];
	    File jsonFile = new File(path);
	    		
		try (Stream<String> lines = Files.lines(Paths.get(jsonFile.getPath()))) 
		{
			
			List<String> listJson = lines.collect(Collectors.toList());
			
			Controller accoCtr = new Controller(new AccountService(new AccountRepositoryMemory()),
					new TransactionService(new TransactionRepositoryMemory()));
			
			accoCtr.validateAllTransactions(listJson);
		 
		} 
		catch (IOException e) {
		    e.printStackTrace();
		}
		
	}

}
