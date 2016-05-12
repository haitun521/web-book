package com.haitun.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	private static SimpleDateFormat sf = null;
	

	public Utils(String pattern) {
		sf = new SimpleDateFormat(pattern);
		
	}

	public Date String2Date(String s) throws ParseException {

		if (s.length() > 6) {
			s = s.substring(0, 6);
		}
		return sf.parse(s);
	}

	public String Date2String(Date date) throws ParseException {
		return sf.format(date);
	}

}
