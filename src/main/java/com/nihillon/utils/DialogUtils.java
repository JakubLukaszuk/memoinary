package com.nihillon.utils;

import javafx.scene.control.Alert;
import org.springframework.util.StringUtils;

public class DialogUtils {
    private static Alert alert;

    public static void confirmDialog(String message, String title)
    {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public static void errorDialog(String message, String title, Exception exception)
    {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText("Error type: "+exception.getClass()+"\n infromation will be logged");
        alert.showAndWait();
    }


}
