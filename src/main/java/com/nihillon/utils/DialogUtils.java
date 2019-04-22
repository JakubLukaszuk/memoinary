package com.nihillon.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import org.springframework.util.StringUtils;

public class DialogUtils {
    private static Alert alert;

    public DialogUtils() {

    }

    public static void confirmDialog(String message, String title)
    {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.getDialogPane().getStylesheets().add(DialogUtils.class.getResource("/css/style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog");
        alert.showAndWait();

    }

    public static void errorDialog(String message, String title, Exception exception)
    {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(DialogUtils.class.getResource("/css/style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog");
        alert.showAndWait();
    }


}
