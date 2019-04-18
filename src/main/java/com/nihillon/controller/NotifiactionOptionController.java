package com.nihillon.controller;

import com.nihillon.utils.NotificationBar;
import com.nihillon.viewModel.WordView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NotifiactionOptionController {
    @FXML
    private ChoiceBox<Integer> displayTimeComboBox;
    @FXML
    private ChoiceBox<Integer> intervalComboBox;

    private List<WordView> wordsToDisplay;

    @FXML
    private void initialize() {
        initComboBoxes();
}


    private void initComboBoxes()
    {
        List<Integer> showTimeList = new ArrayList<>();
        showTimeList.add(3);
        showTimeList.add(4);
        showTimeList.add(5);

        List<Integer> nextShowTimeList = new ArrayList<>();
        nextShowTimeList.add(5);
        nextShowTimeList.add(8);
        nextShowTimeList.add(10);
        nextShowTimeList.add(15);
        nextShowTimeList.add(20);
        nextShowTimeList.add(30);
        nextShowTimeList.add(60);
        nextShowTimeList.add(120);
        nextShowTimeList.add(180);
        nextShowTimeList.add(320);
        nextShowTimeList.add(440);
        nextShowTimeList.add(500);


        displayTimeComboBox.setItems(FXCollections.observableArrayList(showTimeList));
        intervalComboBox.setItems( FXCollections.observableArrayList(nextShowTimeList));
    }



    @FXML
    private void startNotification(ActionEvent actionEvent) {
        NotificationBar.getInstance().setWorkFlag(true);
        NotificationBar.getInstance().generateNotifiacations((int)intervalComboBox.getSelectionModel().getSelectedItem()*1000,
                (int)displayTimeComboBox.getSelectionModel().getSelectedItem()*1000, wordsToDisplay,
                ResourceBundle.getBundle("bundles.messagesData").getString("application.title").toUpperCase());
        Stage stage = (Stage) displayTimeComboBox.getScene().getWindow();
        stage.close();
    }

    public List<WordView> getWordsToDisplay() {
        return wordsToDisplay;
    }

    public void setWordsToDisplay(List<WordView> wordsToDisplay) {
        this.wordsToDisplay = wordsToDisplay;
    }
}
