package com.ddd.infrastructure.shared;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilApp {

	/**
	 * Method to parse a date
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date dateMapper(String date) throws ParseException {
		
	     DateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSS");
	     Date time = dateFormat.parse(date.replace("Z",""));
	     
	     return time;
	}
}
