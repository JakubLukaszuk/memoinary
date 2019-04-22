package com.nihillon.controller;

import com.nihillon.viewModel.WordView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.FileHandler;

public class SaveTxtOptionsController {

    @FXML
    private CheckBox categoryCheckBox, subCategoryCheckBox, knowladgeStatusCheckBox;

    private List<WordView> wordsToSave;


    @FXML
    private void save() {
        String savePattern = "{0} - {1}";

        if (categoryCheckBox.isSelected())
            savePattern += " - {2}";
        if (subCategoryCheckBox.isSelected())
            savePattern += " - {3}";
        if (knowladgeStatusCheckBox.isSelected())
            savePattern += " - {4}";


    }


    public void setWordsToSave(List<WordView> wordsToSave) {
        this.wordsToSave = wordsToSave;
    }
}
