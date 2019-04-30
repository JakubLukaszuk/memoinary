package com.jlukaszuk.utils;

import com.jlukaszuk.viewModel.WordView;
import dorkbox.notify.Notify;
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

    public  void generateNotifiacations(int interval, int period, List<WordView> selectedItems, String title)
    {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (workFlag) {
                    int index = (int) (Math.random() * (selectedItems.size()));
                    String message = String.format("%s - %s", selectedItems.get(index).getIssue(), selectedItems.get(index).getMean());
                    displayTray(message,title ,period);
                } else {
                    this.cancel();
                }
            }
        }, 0, interval);
    }


    public void displayTray(String message, String title, int toAutoClose) {
        //Obtain only one instance of the SystemTray object
        Notify.create()
                .title(title)
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


