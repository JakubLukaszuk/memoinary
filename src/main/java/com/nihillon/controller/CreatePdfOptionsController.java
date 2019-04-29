package com.nihillon.controller;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.nihillon.utils.DialogUtils;
import com.nihillon.utils.NotificationBar;
import com.nihillon.utils.file.txt.TextFileHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreatePdfOptionsController {

//   private static Object mutex = new Object();
    @FXML
    private ChoiceBox<Integer> fontSizeCheckBox;
    //private  static  volatile CreatePdfOptionsController instance;

    @FXML
    private CheckBox issueCheckBox, meanCheckBox, categoryCheckBox, subCategoryCheckBox, knowladgeStatusCheckBox;
    @FXML
    private RadioButton radioVertically, radioHorizonally;
    private  String savePattern="";
    private File file;
    private Rectangle pageFormat;
    private int fontSize;

//    public static CreatePdfOptionsController getInstance() {
//        CreatePdfOptionsController result = instance;
//        if (result == null) {
//            synchronized (mutex) {
//                result = instance;
//                if (result == null)
//                    instance = result = new CreatePdfOptionsController();
//            }
//        }
//        return result;
//    }


    @FXML
    private void initialize()
    {
        ToggleGroup toggleGroup = new ToggleGroup();
        radioHorizonally.setToggleGroup(toggleGroup);
        radioVertically.setToggleGroup(toggleGroup);
        radioVertically.setSelected(true);
        ObservableList<Integer> fonts = FXCollections.observableArrayList();
        fonts.add(8);
        fonts.add(10);
        fonts.add(12);
        fonts.add(14);
        fontSizeCheckBox.setItems(fonts);
        fontSizeCheckBox.getSelectionModel().selectFirst();
    }

    public void generatePdfData(ActionEvent actionEvent) {

        if (issueCheckBox.isSelected())
            savePattern+="{0}";

        if (meanCheckBox.isSelected())
            savePattern += " - {1}";

        if (categoryCheckBox.isSelected())
            savePattern +=" - {2}";

        if (subCategoryCheckBox.isSelected())
            savePattern +=" - {3}";

        if (knowladgeStatusCheckBox.isSelected())
            savePattern += " - {4}";

        if (radioVertically.isSelected())
            pageFormat = PageSize.A4;
        else
            pageFormat=PageSize.A4.rotate();

        fontSize = fontSizeCheckBox.getValue();

        Stage stage = (Stage) meanCheckBox.getScene().getWindow();
        file = DialogUtils.saveFileChooser(stage, "*.pdf", "PDF files (*.pdf)");
        stage.close();

    }


    public String getSavePattern() {
        return savePattern;
    }

    public File getFile() {
        return file;
    }

    public Rectangle getPageFormat() {
        return pageFormat;
    }

    public int getFontSize() {
        return fontSize;
    }
}
