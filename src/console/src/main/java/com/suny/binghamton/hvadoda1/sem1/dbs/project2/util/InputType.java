package com.suny.binghamton.hvadoda1.sem1.dbs.project2.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public enum InputType {
	Integer,
	Double,
	String,
	Date,
	;
	public Object parse(String s) {
		switch (this) {
		case Integer:
			return java.lang.Integer.parseInt(s);
		case Double:
			return java.lang.Double.parseDouble(s);
		case Date:
			try {
				return new SimpleDateFormat("dd-MMM-yy").parse(s);
			} catch (ParseException e) {
				e.printStackTrace();
				return s;
			}
		default: return s;
		}
	}
}
