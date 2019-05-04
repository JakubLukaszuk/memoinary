package com.jlukaszuk.utils.file;



import com.jlukaszuk.utils.DialogUtils;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerWriter {

    public static void writeLog(String info, Exception exception) {
        Logger logger = Logger.getLogger("memoinaryLogs");
        FileHandler fh;
        DirHandler dirHandler = new DirHandler();
        logger.setUseParentHandlers(false);
        dirHandler.crateDirIfNotExist("logs/");
        DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");

        try {
            fh = new FileHandler("logs/memoinaryLog_"+timeStampPattern.format(java.time.LocalDateTime.now())+".log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            logger.info(info+System.getProperty("line.separator")+
                    "stacktrace: "+ Arrays.toString(exception.getStackTrace())+
                    System.getProperty("line.separator")+"message: "+exception.getMessage()+
                    System.getProperty("line.separator")+"class: "+exception.getClass()+
                    System.getProperty("line.separator")+"toString: "+exception.toString());

        } catch (SecurityException e) {
            e.printStackTrace();
            DialogUtils.errorDialog("Change permision for this user for /logs/memoinaryLog.log to log errors data", "Directory permision error");
        } catch (IOException e) {
            DialogUtils.errorDialog("", "Input Error");
            e.printStackTrace();
        }
    }

}
