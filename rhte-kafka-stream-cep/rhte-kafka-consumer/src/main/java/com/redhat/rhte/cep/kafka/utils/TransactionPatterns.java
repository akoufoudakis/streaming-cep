package com.redhat.rhte.cep.kafka.utils;

import java.util.Calendar;
import java.util.Date;

import org.apache.kafka.streams.kstream.Predicate;

import com.redhat.rhte.cep.kafka.model.CreditCardTransaction;

public class TransactionPatterns {
	
	public static Predicate<String, CreditCardTransaction> bannedCountries = (key, ccTrans) -> ccTrans
			.getCountryOfTransaction().equals("Iran (Islamic Republic of)") ||  ccTrans
			.getCountryOfTransaction().equals("Afghanistan");

	public static Predicate<String, CreditCardTransaction> InvalidHourOfDay = (key,
			ccTrans) -> IsValidTimeOfDay(ccTrans.getPurchaseDate());

	/*
	 * ValueMapper<String, CreditCardTransaction> new ValueMapper<String, Integer> {
	 * Integer apply(String value) { return value.split(" ").length; } }
	 */

	public static boolean IsValidTimeOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		if (hour > 22 && minutes >= 30) {
			return false;
		}
		return true;
	}
}
