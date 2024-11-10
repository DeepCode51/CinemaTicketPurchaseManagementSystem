package com.movie.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {

	public static void main(String[] args) throws ParseException {
		/*SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time1 = "2020-01-01 16:18:47";
		String time = "1577808000000";
		System.out.println(sf.parse(time1).getTime());*/
		Date date = new Date();
		System.out.println(date.getTime());
		Calendar calendar = Calendar.getInstance();
		System.out.println(calendar.getTimeInMillis());
	}
}
