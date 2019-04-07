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

import java.sql.SQLException;
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
    public MainFrameController(CategoryModel categoryModel, WordModel wordModel, SubCategoryModel subCategoryModel)
    {
        this.categoryModel = categoryModel;
        this.wordModel = wordModel;
        this.subCategoryModel = subCategoryModel;
    }

    @FXML
    private void initialize()
    {
//
//        categoryModel = new CategoryModel();
//        wordModel = new WordModel();
//        subCategoryModel = new SubCategoryModel();
        try {
            wordModel.fillWithData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        categoryModel.fillWithData();
        subCategoryModel.fillWithData();
        fillTableWithData();
        tableView.setItems(wordModel.getWordWiewList());
        fiiComboBoxesWithData();

    }

    public void fillTableWithData() {
        //issue coulmn
        issueColumn.setCellValueFactory(cellData -> cellData.getValue().issueProperty());
        issueColumn.setCellFactory(cell-> new TableCell<WordView, String>()
        {
            TextField textField = new TextField();
            @Override
            protected void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);
                if (empty)
                {
                    setGraphic(null);
                }
                else{
                    textProperty().bind(new SimpleStringProperty(item));
                    textField.setText(item);
                    int index = this.getIndex();

                    setOnMouseClicked(new EventHandler<MouseEvent>()
                    {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getClickCount() == 2 ) {
                                setGraphic(textField);
                                textField.setOnAction(new EventHandler<ActionEvent>()
                                {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        String tmp = textField.getText();
                                        if (!tmp.equals(item))
                                        {
                                            tableView.getSelectionModel().select(index); //block missclicks
                                            WordView tmpWord = tableView.getSelectionModel().selectedItemProperty().get();
                                            tmpWord.setIssue(tmp);

                                            if (!wordModel.getModifed().contains(tmpWord))
                                            {
                                                wordModel.getModifed().add(tmpWord);
                                            }
                                        }
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
                else
                    {
                    textProperty().bind(new SimpleStringProperty(item));
                    textField.setText(item);
                    int index = this.getIndex();

                    setOnMouseClicked(new EventHandler<MouseEvent>()
                    {
                        @Override
                        public void handle(MouseEvent event)
                        {
                            if (event.getClickCount() == 2 )
                            {
                                setGraphic(textField);
                                textField.setOnAction(new EventHandler<ActionEvent>()
                                {
                                    @Override
                                    public void handle(ActionEvent event)
                                    {
                                        String tmp = textField.getText();
                                        if (!tmp.equals(item))
                                        {
                                            tableView.getSelectionModel().select(index); //block missclicks
                                            WordView tmpWord = tableView.getSelectionModel().selectedItemProperty().get();
                                            tmpWord.setMean(tmp);

                                            if (!wordModel.getModifed().contains(tmpWord))
                                            {
                                                wordModel.getModifed().add(tmpWord);
                                            }
                                        }
                                        setGraphic(null);
                                    }
                                });

                            }
                        }
                    });
                }
            }
        });
        //knowladgeStatusColumn
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
                                WordView tmpWord = tableView.getSelectionModel().selectedItemProperty().get();
                                if (!item)
                                {
                                    textProperty().setValue("known");
                                    tmpWord.setKnowledgeStatus(true);
                                }
                                else
                                    {
                                        textProperty().setValue("unknown");
                                        tmpWord.setKnowledgeStatus(false);
                                    }

                                if (!wordModel.getModifed().contains(tmpWord))
                                {
                                    wordModel.getModifed().add(tmpWord);
                                }
                            }
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
                            //System.out.println("observale: "+observable.toString()+"odValue: "+oldValue.toString()+"newValue: "+newValue.toString());
                            WordView tmpWord = tableView.getSelectionModel().selectedItemProperty().get();
                            tmpWord.setCategory(newValue);
                            tmpWord.setSubCategory(null);
                            textProperty().bind(newValue.categoryNameProperty());

                            if (!wordModel.getModifed().contains(tmpWord))
                            {
                                wordModel.getModifed().add(tmpWord);
                            }
                        }
                    });

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

                                WordView tmpWord = tableView.getSelectionModel().selectedItemProperty().get();
                                System.out.println("subkategoria "+newValue+"}"+newValue.getCategoryId());
                                tmpWord.setSubCategory(newValue);
                                textProperty().bind(new SimpleStringProperty(newValue.getSubCategoryName()));

                                if (!wordModel.getModifed().contains(tmpWord))
                                {
                                    wordModel.getModifed().add(tmpWord);
                                }
                        }
                    });

                    setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getClickCount() == 2 ) {
                                //System.out.println(tableView.getSelectionModel().getSelectedItem().toString());
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
            @Override
            protected void updateItem(WordView item, boolean empty) {
                super.updateItem(item, empty);
                if (empty){
                    setGraphic(null);
                }
                else {
                    CheckBox checkBox = new CheckBox();
                    checkBox.selectedProperty().bindBidirectional(item.checkedProperty());
                    checkBox.setOnAction(event -> {
                        if (checkBox.isSelected())
                        {
                            item.setChecked(true);
                            //System.out.println("item:"+item);
                            //wordModel.setSelectedItems();
                        }
                        else
                        {
                            item.setChecked(false);
                            //wordModel.setSelectedItems();
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
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            catch (ParseException e)
            {
            e.printStackTrace();
        }
    }
    @FXML
    private void saveCategoryToDB(ActionEvent actionEvent) {

        String tmpCategoryName = categoryTextField.getText();
        if (!StringUtils.isEmpty(tmpCategoryName)){
            categoryModel.saveToDataBase("tmp",tmpCategoryName);
            categoryTextField.setText("");
            try {
                wordModel.fillWithData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
            try {
                wordModel.fillWithData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void deleteSelectedWords(ActionEvent actionEvent) {
        try {
            wordModel.deleteSelectedWords();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.refresh();
    }

    @FXML
    private void deleteSubCategory(ActionEvent actionEvent) {
        SubCategoryView subCategory = subCategoryChoiceBox.getSelectionModel().getSelectedItem();
        if (subCategory!=null){
            try {
                wordModel.deleteBySubCategory(subCategory);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tableView.refresh();
            subCategoryModel.deleteFromDataBaseById(subCategory);
            subCategoryChoiceBox.getItems().remove(subCategory);
        }
    }

    @FXML
    private void deleteCategory(ActionEvent actionEvent) {
        CategoryView category = categoryChoiceBox.getSelectionModel().getSelectedItem();
        if (category!=null){
            try {
                wordModel.deleteByCategory(category);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tableView.refresh();
            try {
                subCategoryModel.deleteByCategory(category);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            category.getSubCategories().forEach(subCategoryView -> subCategoryChoiceBox.getItems().remove(subCategoryView));
            categoryModel.deleteFromDataBaseById(category);
            categoryChoiceBox.getItems().remove(category);
        }
    }

    @FXML
    private void UpdateWordsInDB(ActionEvent actionEvent) {
        try {
            wordModel.updateInDataBase();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        tableView.refresh();
    }
}
