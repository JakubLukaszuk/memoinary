package com.jlukaszuk.controller;

import com.jlukaszuk.utils.DialogUtils;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import java.io.File;

public class SaveTxtOptionsController {


    @FXML
    private CheckBox  meanCheckBox, knowladgeStatusCheckBox;

    private File file;
    private String savePattern = "{0}";


    @FXML
    private void save() {


        if (meanCheckBox.isSelected())
            savePattern += " - {1}";

        if (knowladgeStatusCheckBox.isSelected())
            savePattern += " - {2}";

        Stage stage = (Stage) meanCheckBox.getScene().getWindow();
        file = DialogUtils.saveFileChooser(stage, "*.txt", "TXT files (*.txt)");
        ((Stage) meanCheckBox.getScene().getWindow()).close();
    }



    public File getFile() {
        return file;
    }

    public String getSavePattern() {
        return savePattern;
    }
}


