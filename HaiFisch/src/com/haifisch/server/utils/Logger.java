package com.haifisch.server.utils;

import java.time.LocalDateTime;

import com.haifisch.server.datamanagement.LocalStorage;

public class Logger {

	private static Logger loggerInstance;
	private String logFile = "logFile.txt";
	
	public Logger getLogger(){
		
		if(loggerInstance == null)
			loggerInstance = new Logger();
		
		return loggerInstance;
	}
	
	public void setLogFile(String fileName){
		logFile = fileName;
	}
	
	private Logger(){
	}
	
	public void log(String message){
		
			LocalStorage store = LocalStorage.getInstance("");
			store.writeFile(logFile, LocalDateTime.now()+" "+message+"\n");
		
	}
	
	
}
