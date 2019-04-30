package com.jlukaszuk.utils.file;



import com.jlukaszuk.utils.DialogUtils;

import java.io.IOException;
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
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("logs/memoinaryLog.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            logger.info(info+"\nstacktrace: "+ Arrays.toString(exception.getStackTrace())+"\nmessage: "+exception.getMessage()+"\nclass: "+exception.getClass()+"\ntoString: "+exception.toString()+"\n");

        } catch (SecurityException e) {
            e.printStackTrace();
            DialogUtils.errorDialog("Change permision for this user for /logs/memoinaryLog.log to log errors data", "Directory permision error");
        } catch (IOException e) {
            DialogUtils.errorDialog("", "Input Error");
            e.printStackTrace();
        }
    }

}
