package com.phonecompany.billing.util;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.phonecompany.billing.dto.PhoneLog;

public final class CsvConverter {

   private static final String BREAK_LINE = System.lineSeparator();
   private static final String COLUMN_SEPARATOR = ",";
   private static final String PHONE_NUMBER_REGEX = "[0-9]+";
   private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

   private CsvConverter() {
       throw new UnsupportedOperationException();
   }

   public static List<PhoneLog> readPhoneLogs(String csv) {
       if (Objects.isNull(csv) || csv.trim().isEmpty()) {
           return Collections.emptyList();
       }

       List<PhoneLog> phoneLogs = new LinkedList<PhoneLog>();
       for (String row : csv.split(BREAK_LINE)) {
           String[] data = row.split(COLUMN_SEPARATOR);
           if (validRow(data)) {
               PhoneLog phoneLog = new PhoneLog();
               phoneLog.setPhone(data[0]);
               phoneLog.setTimeStart(parseDateTime(data[1]));
               phoneLog.setTimeEnd(parseDateTime(data[1]));
               phoneLogs.add(phoneLog);
           }
       }
       return phoneLogs;
   }

   private static LocalDateTime parseDateTime(String dateTimeStr) {
       return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
   }

   private static boolean validRow(String[] data) {
       if (data.length != 3) {
           System.out.println("Data format is not valid");
           return false;
       }

       String phoneNumber = data[0];
       if (!validPhoneNumber(phoneNumber)) {
           System.out.println(String.format("Invalid phone number %s", phoneNumber));
           return false;
       }

       String startTimeStr = data[1];
       if (!validDateTime(startTimeStr)) {
           System.out.println(String.format("Invalid start time %s", startTimeStr));
           return false;
       }

       String endTimeStr = data[2];
       if (!validDateTime(endTimeStr)) {
           System.out.println(String.format("Invalid end time %s", endTimeStr));
           return false;
       }

       return true;
   }

   private static boolean validPhoneNumber(String phoneNumber) {
       return Objects.nonNull(phoneNumber) && phoneNumber.matches(PHONE_NUMBER_REGEX);
   }

   private static boolean validDateTime(String dataTimeStr) {
       try {
           DATE_TIME_FORMATTER.parse(dataTimeStr);
           return true;
       } catch (DateTimeParseException e) {
           return false;
       }
   }

}
