package com.jlukaszuk.utils;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;


import java.io.File;

public class DialogUtils {
    private static Alert alert;

    public DialogUtils() {

    }

    public static void informDialog(String message, String title)
    {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.getDialogPane().getStylesheets().add(DialogUtils.class.getResource("/css/style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog");
        alert.getDialogPane().setMaxWidth(600);
        alert.showAndWait();
    }

    public static void errorDialog(String message, String title )
    {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(DialogUtils.class.getResource("/css/style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog");
        alert.showAndWait();
    }

    public static File saveFileChooser(Window stage, String extenstion, String extenctionDescription)
    {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(extenctionDescription, extenstion);
        fileChooser.getExtensionFilters().add(extFilter);

        return fileChooser.showSaveDialog(stage);
    }

    public static File fileChooser(Window stage, String extenstion, String extenctionDescription)
    {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(extenctionDescription, extenstion);
        fileChooser.getExtensionFilters().add(extFilter);

        return fileChooser.showOpenDialog(stage);
    }


}
