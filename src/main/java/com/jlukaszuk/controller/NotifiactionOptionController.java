package com.jlukaszuk.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;


public class NotifiactionOptionController {
    @FXML
    private ChoiceBox<Integer> displayTimeComboBox;
    @FXML
    private ChoiceBox<Integer> intervalComboBox;

    private int interval = 0;
    private int displayTime = 0;


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
        displayTimeComboBox.getSelectionModel().selectFirst();
        intervalComboBox.getSelectionModel().selectFirst();
    }



    @FXML
    private void startNotification(ActionEvent actionEvent) {
        interval = intervalComboBox.getValue();
        displayTime = displayTimeComboBox.getValue();

        Stage stage = (Stage) displayTimeComboBox.getScene().getWindow();
        stage.close();
    }


    public int getInterval() {
        return interval;
    }

    public int getDisplayTime() {
        return displayTime;
    }

}
