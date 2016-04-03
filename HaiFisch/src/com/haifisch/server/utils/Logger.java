package com.haifisch.server.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.haifisch.server.datamanagement.LocalStorage;

public class Logger {

	private static Logger loggerInstance; // Logger instance
	private String logFile = "logFile.txt"; // the log file
	private ArrayList<String> messages; // messages stored to be written in log file
	private LocalStorage store; // the local storage
	
	/**
	 * @return the Logger instance
	 * */
	public Logger getLogger(){
		
		if(loggerInstance == null)
			loggerInstance = new Logger();
		
		return loggerInstance;
	}
	
	/**
	 * changes the logger's file
	 * @param fileName the new file name 
	 * */
	public void setLogFile(String fileName){
		logFile = fileName;
	}
	
	/**
	 * constructor
	 * */
	private Logger(){
		messages = new ArrayList<String>();
		store = LocalStorage.getInstance("");
	}
	
	/**
	 * Logs a new message in the Logger. The message is stored until the Logger is flushed.
	 * @param message the message to be logged
	 * @param type the message type
	 * */
	public void log(String message, LogMessageType type){
		
			messages.add(LocalDateTime.now()+" "+type+": "+message+"\n");
		
	}
	
	/**
	 * Flushes the messages stored in Logger in the Logger's file
	 * @return true if the messages were successfully written in the file,
	 * false otherwise
	 * */
	public boolean flushLogger(){
		boolean writeState = store.writeFile(logFile, messages);
		messages.clear();
		return writeState;
	}
	
	
}
