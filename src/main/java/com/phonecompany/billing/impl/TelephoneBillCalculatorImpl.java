package com.phonecompany.billing.impl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import com.phonecompany.billing.TelephoneBillCalculator;
import com.phonecompany.billing.dto.PhoneLog;
import com.phonecompany.billing.util.CsvConverter;

public class TelephoneBillCalculatorImpl implements TelephoneBillCalculator{

	public static final BigDecimal RUSH_HOUR_RATE = BigDecimal.ONE;
	public static final BigDecimal NORMAL_HOUR_RATE = BigDecimal.valueOf(0.5);
	public static final BigDecimal REDUCE_RATE = BigDecimal.valueOf(0.2);
	public static final int NUMBER_OF_MINUTE_BEFORE_REDUCE = 5;
	public static final LocalTime START_RUSH_HOUR = LocalTime.of(8, 00);
	public static final LocalTime END_RUSH_HOUR = LocalTime.of(16, 00);

	public BigDecimal calculate(String phoneLog) {

		List<PhoneLog> phoneLogs = CsvConverter.readPhoneLogs(phoneLog);
		if(phoneLogs.isEmpty()) {
			return BigDecimal.ZERO;
		}
		String mostNumberCalled = findMostCalledNumber(phoneLogs);

		return phoneLogs.stream()
				.filter(phone -> !phone.getPhone().equals(mostNumberCalled))
				.map(phone -> calculateByDuration(phone.getTimeStart(), phone.getTimeEnd()))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

	}

	private static BigDecimal calculateByDuration(LocalDateTime startTime, LocalDateTime endTime) {

		if(Objects.isNull(startTime) || Objects.isNull(endTime) || !startTime.isBefore(endTime)) {
			return BigDecimal.ZERO;
		}
		int minuteCalled = 0;
		BigDecimal amount = BigDecimal.ZERO;
		while(!startTime.isAfter(endTime)) {
			amount = amount.add(calculateByMinute(minuteCalled, endTime));
			minuteCalled++;
			startTime.plusMinutes(1).withSecond(0);
		}
		return amount;
	}

	private static BigDecimal calculateByMinute(int minuteCalled, LocalDateTime minuteCalculation) {
		if(minuteCalled < 5) {
			return getRate(minuteCalculation);
		}
		return getRate(minuteCalculation).subtract(REDUCE_RATE);
	}

	private static BigDecimal getRate(LocalDateTime minuteCalculation) {
		if(Objects.isNull(minuteCalculation)) {
			return BigDecimal.ZERO;
		}
		LocalTime time = minuteCalculation.toLocalTime();

		if(!START_RUSH_HOUR.isAfter(time) && !time.isAfter(END_RUSH_HOUR)) {
			return RUSH_HOUR_RATE;
		}
		return NORMAL_HOUR_RATE;
	}

	private String findMostCalledNumber(List<PhoneLog> logs) {
		if (logs == null || logs.isEmpty()) {
			return null;
		}

		Map<String, Long> phoneInfos = countByPhones(logs);
		LinkedHashMap<String, Long> sortedPhoneInfos = sortByValue(phoneInfos);


		long maxValue = -1;
		String phone = null;
		for (Entry<String, Long> entry : sortedPhoneInfos.entrySet()) {
			if (maxValue == -1) {
				maxValue = entry.getValue();
				phone = entry.getKey();
				break;
			}

			if (entry.getValue() < maxValue) {
				break;
			}

			if (Long.parseLong(entry.getKey()) > Long.parseLong(phone)) {
				phone = entry.getKey();
			}
		}

		return phone;
	}


	private static Map<String, Long> countByPhones(List<PhoneLog> logs) {
		return logs.stream()
				.filter(Objects::nonNull)
				.map(PhoneLog::getPhone)
				.collect(Collectors.groupingBy(phone -> phone, Collectors.counting()));
	}

	private static LinkedHashMap<String, Long> sortByValue(final Map<String, Long> phoneInfos) {
		return phoneInfos.entrySet()
				.stream()
				.sorted((Map.Entry.<String, Long>comparingByValue().reversed()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

}