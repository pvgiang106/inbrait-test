package com.phonecompany.billing.dto;

import java.time.LocalDateTime;

public class PhoneLog {

	private String phone;

	private LocalDateTime timeStart;

	private LocalDateTime timeEnd;

	public PhoneLog() {
		
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDateTime getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(LocalDateTime timeStart) {
		this.timeStart = timeStart;
	}

	public LocalDateTime getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(LocalDateTime timeEnd) {
		this.timeEnd = timeEnd;
	}

	

}
