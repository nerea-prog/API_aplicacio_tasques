package com.ra12.projecte1.logging;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class CustomLogging {
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = LOG_DIR + "/aplicacio.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss");

    public void logError(String className, String method, String error, Exception exception){
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format("[ERROR] %s - Class: %s - Method: %s - Message: %s", timestamp, className, method, error);
        if (exception != null){
            logEntry += " - Exception: " + exception.getMessage();
        }
        writeToFile(logEntry);
    }

    public void  logInfo(String className, String method, String info){
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format("[INFO] %s - Class: %s - Method: %s - Message: %s", timestamp, className, method, info);
        writeToFile(logEntry);
    }

    private void writeToFile(String message) {
        Path logPath = Paths.get(LOG_FILE);
        try{
            Files.createDirectories(Paths.get(LOG_DIR));
            try (BufferedWriter bw = Files.newBufferedWriter(logPath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)){
                bw.write(message);
                bw.newLine();
            }
        } catch (IOException e){
            System.err.println("ERROR escrivint al fitxer de log: " + e.getMessage());
        }
    }
}