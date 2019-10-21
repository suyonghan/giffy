package com.safe.traveler.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LogService {
	private Logger log;

	public LogService() {
		log = LogManager.getLogger("case4");
	}

	public void writelog(int logType, boolean isGet, String name, String msg) {
		String body;
		if (isGet)
			body = "GET : " + name + ", " + msg;
		else
			body = "POST : " + name + ", " + msg;
		switch (logType) {
		case 1:
			log.info(body);
			break;
		case 2:
			log.error(body);
			break;
		default:
			break;
		}
	}

	public void writelog(int logType, String name, String msg) {
		String body;
		body = name + ", " + msg;
		switch (logType) {
		case 1:
			log.info(body);
			break;
		case 2:
			log.error(body);
			break;
		default:
			break;
		}
	}
}
