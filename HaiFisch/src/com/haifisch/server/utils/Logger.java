package com.haifisch.server.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

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
		File log = new File(logFile);
		if(!log.exists()){
			try {
				log.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void write(String message) throws IOException{ //---> kalitera na valw try-catch?? 
		Path path = Paths.get(logFile);
		//--> dn 8imamai an me kati apo auta edw dimiourgeitai to arxeio an dn iparxei....
		//--> an paizei kati tetoio apla svise tis grammes 36-43...
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
			writer.write(LocalDateTime.now()+" "+message+"\n");
		}
	}
	
	
}
