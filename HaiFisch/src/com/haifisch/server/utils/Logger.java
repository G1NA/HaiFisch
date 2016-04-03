package com.haifisch.server.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.haifisch.server.datamanagement.LocalStorage;

public class Logger {

	private static Logger loggerInstance;
	private String logFile = "logFile.txt";
	private ArrayList<String> messages;
	private LocalStorage store;
	
	public Logger getLogger(){
		
		if(loggerInstance == null)
			loggerInstance = new Logger();
		
		return loggerInstance;
	}
	
	public void setLogFile(String fileName){
		logFile = fileName;
	}
	
	private Logger(){
		messages = new ArrayList<String>();
		store = LocalStorage.getInstance("");
	}
	
	public void log(String message, LogMessageType type){
		
			messages.add(LocalDateTime.now()+" "+type+": "+message+"\n");
		
	}
	
	public boolean flushLogger(){
		LocalStorage store = LocalStorage.getInstance("");
		boolean writeState = store.writeFile(logFile, messages);
		messages.clear();
		return writeState;
	}
	
	
}
