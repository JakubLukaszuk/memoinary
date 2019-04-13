package com.nihillon.utils;

import com.nihillon.viewModel.WordView;
import dorkbox.notify.Notify;
import org.springframework.context.annotation.Bean;

import java.awt.*;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationBar {

    private static Object mutex = new Object();
    private  static  volatile NotificationBar instance;

    private boolean workFlag;

    public void setWorkFlag(boolean workFlag) {
        this.workFlag = workFlag;
    }

    public NotificationBar() {

    }

    public static NotificationBar getInstance() {
        NotificationBar result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = new NotificationBar();
            }
        }
        return result;
    }

    public  void generateNotifiacations(int interval, int period, List<WordView> selectedItems)
    {

        if (selectedItems.size()>0) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (workFlag) {
                        int index = (int) (Math.random() * (selectedItems.size()));
                            String message = String.format("%s - %s", selectedItems.get(index).getIssue(), selectedItems.get(index).getMean());
                            displayTray(message, period);
                    } else {
                        this.cancel();
                    }
                }
            }, 0, interval);
        }
        else
        {
            DialogUtils.confirmDialog("Word list must be bigger than 0", "Word list is empty");
        }
    }


    public void displayTray(String message, int toAutoClose) {
        //Obtain only one instance of the SystemTray object
        Notify.create()
                .title("ULTIMATE DICTOINARY")
                .text(message)
                .darkStyle()
                .hideAfter(toAutoClose)
                .showWarning();
    }

    public void init()
    {
        Notify.create().show();
    }


}


