package com.safe.traveler.service.impl;

import java.util.HashMap;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;

public class TextMining {
	@Autowired
	LogService log;
	RConnection rc;
	HashMap<String, String> risk;

	public TextMining() {
		try {
			rc = new RConnection();
//			rc.eval("install.packages(\"KoNLP\")");
			rc.eval("library(KoNLP)");
			rc.eval("useSejongDic()");
		} catch (RserveException e) {
			// TODO Auto-generated catch block
			log.writelog(2, "set TextMining enviroment", e.getMessage());
		}
	}

	public void setRisk(HashMap<String, String> risk) {
		this.risk = risk;
	}

	public String classifyText(String title, String content) {
		String result = null;
		try {
			rc.eval("title <- '" + title + "'");
			rc.eval("title <- sapply(title, extractNoun, USE.NAMES = F)");
			REXP tResult = rc.eval("title");
			String[] temp = tResult.asStrings();
			for (int i = 0; i < temp.length; i++) {
				if (risk.containsKey(temp[i]))
					return risk.get(temp[i]);
			}
			if (result == null)
				System.out.println(title);
		} catch (RserveException e) {
			// TODO Auto-generated catch block
			System.out.println("에러가 난 곳은 여기야 : " + title);
//			e.printStackTrace();
		} catch (REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
