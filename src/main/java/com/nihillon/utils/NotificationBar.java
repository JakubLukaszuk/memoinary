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

    private boolean workFlag;

    public void setWorkFlag(boolean workFlag) {
        this.workFlag = workFlag;
    }

    public NotificationBar() {

    }

    public  void generateNotifiacations(int interval, int period, List<WordView> selectedItems)
    {

        if (selectedItems.size()>0) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (workFlag) {
                        int index = (int) (Math.random() * (selectedItems.size()));
                        try {
                            String message = String.format("%s - %s", selectedItems.get(index).getIssue(), selectedItems.get(index).getMean());
                            displayTray(message, period);
                        } catch (AWTException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        this.cancel();
                    }
                }
            }, 0, interval);
        }
        else
        {

        }
    }


    private void displayTray(String message, int toAutoClose) throws AWTException, MalformedURLException {
        //Obtain only one instance of the SystemTray object
        Notify.create()
                .title("ULTIMATE DICTOINARY")
                .text(message)
                .darkStyle()
                .hideAfter(toAutoClose)
                .showWarning();

    }

    public void test(){
        Notify.create()
                .title("ULTIMATE DICTOINARY")
                .text("TEST")
                .darkStyle()
                .showWarning();
    }
}


