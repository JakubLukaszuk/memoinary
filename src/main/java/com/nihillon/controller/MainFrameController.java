package com.nihillon.controller;

import com.nihillon.utils.DialogUtils;
import com.nihillon.utils.NotificationBar;
import com.nihillon.utils.file.LoggerWriter;
import com.nihillon.viewModel.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MainFrameController {

    private final CategoryModel categoryModel;
    private final WordModel wordModel;
    private final SubCategoryModel subCategoryModel;
    private final LoggerWriter logger;
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
    public MainFrameController(CategoryModel categoryModel, WordModel wordModel, SubCategoryModel subCategoryModel )
    {
        this.categoryModel = categoryModel;
        this.wordModel = wordModel;
        this.subCategoryModel = subCategoryModel;
        logger = new LoggerWriter();
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
            logger.writeLog("execetion at initialize", e);
            DialogUtils.errorDialog("error data reading","reading from db error", e);
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

        String tmpIssue = issueTextFiled.getText(), tmpMean = meanTextField.getText();
        if (!StringUtils.isEmpty(tmpIssue) || !StringUtils.isEmpty(tmpMean))
            try {
                wordModel.saveToDataBase(issueTextFiled.getText(),meanTextField.getText(),categoryChoiceBox.getSelectionModel().getSelectedItem(),subCategoryChoiceBox.getSelectionModel().getSelectedItem(),isKnownCheckBox.isSelected() ) ;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                logger.writeLog("execetion at saving word", e);
                DialogUtils.errorDialog("error data writing","writing to db error", e);
            }
            catch (ParseException e)
            {
            e.printStackTrace();
                logger.writeLog("execetion at saving to DB", e);
                DialogUtils.errorDialog("saving to data base error","word is not saved", e);
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
                logger.writeLog("execetion at saving category", e);
                DialogUtils.errorDialog("error data writing","writing to db error", e);
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
                logger.writeLog("execetion at saving subcategory", e);
                DialogUtils.errorDialog("error data writing","writing to db error", e);
            }
        }
    }

    @FXML
    private void deleteSelectedWords(ActionEvent actionEvent) {
        try {
            wordModel.deleteSelectedWords();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.writeLog("execetion at deleating selected words", e);
            DialogUtils.errorDialog("error data deleting from data base","deleting from database error", e);
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
                logger.writeLog("execetion at deleating subCategory", e);
                DialogUtils.errorDialog("error data deleting from data base","deleting from database error", e);
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
                logger.writeLog("execetion at deleating Category", e);
                DialogUtils.errorDialog("error data deleting from data base","deleting from database error", e);
            }
            tableView.refresh();
            try {
                subCategoryModel.deleteByCategory(category);
            } catch (SQLException e) {
                logger.writeLog("execetion at deleating SubCategory from Catrogry", e);
                DialogUtils.errorDialog("error data deleting from data base","deleting from database error", e);
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
            logger.writeLog("exception parse at updating Words in to database", e);
            DialogUtils.errorDialog("error at updating database","update database error", e);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            logger.writeLog("exception sql at updating Words in to database", e);
            DialogUtils.errorDialog("error at updating database","update database error", e);
        }
        tableView.refresh();
    }

    @FXML
    private void serchWords(ActionEvent actionEvent) {
        try {
            wordModel.filtrWords(meanTextField.getText(),issueTextFiled.getText(),categoryChoiceBox.getValue(), subCategoryChoiceBox.getValue());
            tableView.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.writeLog("exception SQL at serching Words", e);
            DialogUtils.errorDialog("error at downloading data from database","download data database error", e);

        } catch (ParseException e) {
            e.printStackTrace();
            logger.writeLog("exception parse at serching Words", e);
            DialogUtils.errorDialog("error at searching data from database","searching data from database error", e);
        }
        //tableView.refresh();
    }

    @FXML
    private
    void setNotifications(ActionEvent actionEvent) {
        final String FXML_NOTIFICATION_DIALOG_FXML = "/fxml/NotificationOptions.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(getClass().getResource(FXML_NOTIFICATION_DIALOG_FXML));

        Parent dialog = null;
        try {
            dialog = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotifiactionOptionController notificationDialogController = fxmlLoader.getController();
        List<WordView> wordViewListTmp = new ArrayList<>();
        for (WordView word: wordModel.getWordWiewList()) {
            if (word.isChecked())
                wordViewListTmp.add(word);
        }
        if (wordViewListTmp.size()>0){
            notificationDialogController.setWordsToDisplay(wordViewListTmp);


            Scene scene =null;
            assert dialog != null;
            scene = new Scene(dialog);


            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }
    }

    @FXML
    private void setOffNotifications(ActionEvent actionEvent) {
        NotificationBar.getInstance().setWorkFlag(false);
    }

    @FXML
    private void clearSubCategoryBox(ActionEvent actionEvent) {
        subCategoryChoiceBox.getSelectionModel().select(null);
    }

    @FXML
    private void clearCategoryBox(ActionEvent actionEvent) {
        categoryChoiceBox.getSelectionModel().select(null);
        subCategoryChoiceBox.getSelectionModel().select(null);
    }
}
