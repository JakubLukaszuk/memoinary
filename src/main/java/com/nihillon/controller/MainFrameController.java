package com.nihillon.controller;

import com.nihillon.viewModel.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;

@Component
public class MainFrameController {

    private final CategoryModel categoryModel;
    private final WordModel wordModel;
    private final SubCategoryModel subCategoryModel;
    @FXML
    private TextField subCategoryTextField;
    @FXML
    private TextField categoryTextField;
    @FXML
    private CheckBox isKnownCheckBox;
    @FXML
    private TextField meanTextField;
    @FXML
    private TextField issueTextFiled;
    @FXML
    private MenuBar menuBar;
    @FXML
    private TableView<WordView> tableView;
    @FXML
    private TableColumn<WordView, String> issueColumn;
    @FXML
    private TableColumn<WordView, String> meanColumn;
    @FXML
    private TableColumn<WordView, Boolean> statusColumn;
    @FXML
    private TableColumn<WordView, String> categoryColumn;
    @FXML
    private TableColumn<WordView, String> subCategoryColumn;
    @FXML
    private TableColumn<WordView, WordView> selectColumn;
    @FXML
    private ChoiceBox<CategoryView> categoryChoiceBox;
    @FXML
    private ChoiceBox<SubCategoryView> subCategoryChoiceBox;



    @Autowired
    public MainFrameController(CategoryModel categoryModel, WordModel wordModel, SubCategoryModel subCategoryModel) {
        this.categoryModel = categoryModel;
        this.wordModel = wordModel;
        this.subCategoryModel = subCategoryModel;
    }

    @FXML
    private void initialize() {
//
//        categoryModel = new CategoryModel();
//        wordModel = new WordModel();
//        subCategoryModel = new SubCategoryModel();
        wordModel.fillWithData();
        categoryModel.fillWithData();
        subCategoryModel.fillWithData();
        fillTableWithData();
        tableView.setItems(wordModel.getWordWiewList());
        fiiComboBoxesWithData();
    }

    public void fillTableWithData() {
        //issue coulmn
        issueColumn.setCellValueFactory(cellData -> cellData.getValue().issueProperty());
        issueColumn.setCellFactory(cell-> new TableCell<WordView, String>(){
            TextField textField = new TextField();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty){
                    setGraphic(null);
                }
                else{
                    textProperty().bind(new SimpleStringProperty(item));
                    textField.setText(item);

                    setOnMouseClicked(new EventHandler<MouseEvent>(){
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getClickCount() == 2 ) {
                                setGraphic(textField);
                                textField.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        wordModel.setSelectedWord(tableView.getSelectionModel().selectedItemProperty().get());
                                        wordModel.getSelectedWord().setMean(textField.getText());
                                        setGraphic(null);
                                    }
                                });
                            }
                        }
                    });


                }
            }
        });
        //mean column
        meanColumn.setCellValueFactory(cellData -> cellData.getValue().meanProperty());
        meanColumn.setCellFactory(cell->new TableCell<WordView, String>(){
            TextField textField = new TextField();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty){
                    setGraphic(null);
                }
                else {
                    Tooltip tooltip =new Tooltip("XDDD");
                    setTooltip(tooltip);
                    textProperty().bind(new SimpleStringProperty(item));


                    textField.setText(item);

                    setOnMouseClicked(new EventHandler<MouseEvent>(){
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getClickCount() == 2 ) {
                                setGraphic(textField);
                                textField.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        setGraphic(null);
                                    }
                                });


                            }
                        }
                    });


                }

            }
        });
        //statusColumn
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().knowledgeStatusProperty());
        statusColumn.setCellFactory(cell->new TableCell<WordView, Boolean>(){


            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty){
                    setGraphic(null);
                }
                else {
                    if (item)
                        textProperty().setValue("known");
                    else
                        textProperty().setValue("unknown");

                    setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getClickCount() == 2 ) {
                                        if (!item)
                                            textProperty().setValue("known");
                                        else
                                            textProperty().setValue("unknown");

                            }
                            tableView.getSelectionModel().getSelectedItem();
                        }
                    });
                }
            }
        });

        //Category column
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty().get().categoryNameProperty());
        categoryColumn.setCellFactory(cell -> new TableCell<WordView, String>(){
            ComboBox<CategoryView> comboBox = new ComboBox<>();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty){
                    setGraphic(null);
                }
                else {
                    textProperty().bind(new SimpleStringProperty(item));
                    comboBox.setItems(categoryModel.getCategoryViewList());
                    comboBox.valueProperty().addListener(new ChangeListener<CategoryView>() {
                        @Override
                        public void changed(ObservableValue<? extends CategoryView> observable, CategoryView oldValue, CategoryView newValue) {
                            System.out.println("observale: "+observable+"odValue: "+oldValue+"newValue: "+newValue);

                        }
                    }
                    );

                    setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getClickCount() == 2 ) {
                                setGraphic(comboBox);
                                comboBox.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        setGraphic(null);
                                    }
                                });
                            }
                                tableView.getSelectionModel().getSelectedItem();
                        }
                    });
                }
            }
        });
        //SubCategory Column
        subCategoryColumn.setCellValueFactory(cellData -> cellData.getValue().subCategoryProperty().get().subCategoryNameProperty());
        subCategoryColumn.setCellFactory(cell -> new TableCell<WordView, String>(){
            ComboBox comboBox = new ComboBox();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty){
                    setGraphic(null);
                }
                else {
                    textProperty().bind(new SimpleStringProperty(item));
                    ComboBox<SubCategoryView> comboBox = new ComboBox<>();
                    comboBox.valueProperty().addListener(new ChangeListener<SubCategoryView>() {
                        @Override
                        public void changed(ObservableValue<? extends SubCategoryView> observable, SubCategoryView oldValue, SubCategoryView newValue) {
                        }
                    });

                    setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getClickCount() == 2 ) {
                                System.out.println(tableView.getSelectionModel().getSelectedItem().toString());
                                comboBox.setItems(tableView.getSelectionModel().getSelectedItem().categoryProperty().getValue().getSubCategories());
                                setGraphic(comboBox);
                                comboBox.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        setGraphic(null);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        //selection column
        selectColumn.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue()));
        selectColumn.setCellFactory(cell-> new TableCell<WordView, WordView>(){
            CheckBox checkBox = new CheckBox();
            @Override
            protected void updateItem(WordView item, boolean empty) {
                super.updateItem(item, empty);
                if (empty){
                    setGraphic(null);
                }
                else {
                    checkBox.selectedProperty().bindBidirectional(item.checkedProperty());
                    checkBox.setOnAction(event -> {
                        if (checkBox.isSelected())
                        {
                            item.setChecked(true);
                            wordModel.setSelectedItems();
                        }
                        else
                        {
                            item.setChecked(false);
                            wordModel.setSelectedItems();
                        }
                    });
                    setGraphic(checkBox);
                    setAlignment(Pos.CENTER);
                }
            }
        });
    }

    public void fiiComboBoxesWithData(){
        categoryChoiceBox.setItems(categoryModel.getCategoryViewList());
        categoryChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null)
            subCategoryChoiceBox.setItems(newValue.getSubCategories());
        });

    }

    @FXML
    private void saveWordToDB(ActionEvent actionEvent) {
        try {
            wordModel.saveToDataBase(issueTextFiled.getText(),meanTextField.getText(),categoryChoiceBox.getSelectionModel().getSelectedItem(),subCategoryChoiceBox.getSelectionModel().getSelectedItem(),isKnownCheckBox.isSelected() ) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void saveCategoryToDB(ActionEvent actionEvent) {

        String tmpCategoryName = categoryTextField.getText();
        if (!StringUtils.isEmpty(tmpCategoryName)){
            categoryModel.saveToDataBase("tmp",tmpCategoryName);
            categoryTextField.setText("");
            wordModel.fillWithData();
        }
    }

    @FXML
    private void saveSubCategoryToDB(ActionEvent actionEvent) {
        String tmpSubCategoryName = subCategoryTextField.getText();
        CategoryView categoryTmp = categoryChoiceBox.getSelectionModel().getSelectedItem();
        if (!StringUtils.isEmpty(tmpSubCategoryName) && categoryTmp!=null){
               subCategoryModel.saveToDataBase("descriptionTmp", tmpSubCategoryName, categoryTmp);
               subCategoryTextField.setText("");
               categoryModel.fillWithData();
               wordModel.fillWithData();
        }
    }
}
