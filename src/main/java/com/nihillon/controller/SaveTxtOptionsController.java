package com.nihillon.controller;

import com.nihillon.utils.DialogUtils;
import com.nihillon.utils.file.txt.TextFileHandler;
import com.nihillon.viewModel.WordView;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class SaveTxtOptionsController {


    @FXML
    private CheckBox  meanCheckBox, knowladgeStatusCheckBox;

    private List<WordView> wordsToSave;


    @FXML
    private void save() {
        String savePattern = "{0}";

        if (meanCheckBox.isSelected())
            savePattern += " - {1}";

        if (knowladgeStatusCheckBox.isSelected())
            savePattern += " - {2}";

        Stage stage = (Stage) meanCheckBox.getScene().getWindow();
        File file = DialogUtils.saveFileChooser(stage, "*.txt", "TXT files (*.txt)");
        if (file!=null){
            try {
                TextFileHandler.SaveTxtFile(wordsToSave, savePattern, file);
                stage.close();

            } catch (IOException e) {
                ResourceBundle bundle = ResourceBundle.getBundle("bundles.messagesData");
                DialogUtils.errorDialog(bundle.getString("error.saveErrorMessage"),bundle.getString("error.saveErrorTitle"),e);
            }
        }
    }


    public void setWordsToSave(List<WordView> wordsToSave) {
        this.wordsToSave = wordsToSave;
    }
}
