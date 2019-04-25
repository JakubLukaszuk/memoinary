package com.nihillon.utils.file;



import com.nihillon.utils.DialogUtils;
import org.springframework.beans.factory.annotation.Autowired;

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

        dirHandler.crateDirIfNotExist("logs/");
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("logs/memoinaryLog.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any \
            //
            // messages
            logger.info(info+"\nstacktrace: "+ Arrays.toString(exception.getStackTrace())+"\nmessage: "+exception.getMessage()+"\nclass: "+exception.getClass()+"\ntoString: "+exception.toString());

        } catch (SecurityException e) {
            e.printStackTrace();
            DialogUtils.errorDialog("Change permision for this user for /logs/memoinaryLog.log to log errors data", "Directory permision error", e);
        } catch (IOException e) {
            DialogUtils.errorDialog("", "Input Error", e);
            e.printStackTrace();
        }
    }

}
