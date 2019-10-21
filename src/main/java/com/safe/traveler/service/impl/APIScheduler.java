package com.safe.traveler.service.impl;

import java.net.SocketTimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class APIScheduler {
	@Autowired
	APIConnector api = null;
	
	@Scheduled(fixedDelay = 1000 * 60 * 60)
	public void executeAPIschedule() throws SocketTimeoutException {
		api.executeWarningSchedule();
		api.executeAccidentSchedule();
	}
}
