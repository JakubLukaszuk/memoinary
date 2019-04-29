package com.nihillon.controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.nihillon.App;
import com.nihillon.utils.DialogUtils;
import com.nihillon.utils.NotificationBar;
import com.nihillon.utils.file.LoggerWriter;
import com.nihillon.utils.file.pdf.PdfFileCreator;
import com.nihillon.utils.file.txt.TextFileHandler;
import com.nihillon.viewModel.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
public class MainFrameController {

    private final CategoryModel categoryModel;
    private final WordModel wordModel;
    private final SubCategoryModel subCategoryModel;
    private final LoggerWriter logger;
    private ResourceBundle bundle;

    @FXML
    private TextField subCategoryTextField, categoryTextField, meanTextField, issueTextFiled;
    @FXML
    private CheckBox isKnownCheckBox;
    @FXML
    private MenuBar menuBar;
    @FXML
    private TableView<WordView> tableView;
    @FXML
    private TableColumn<WordView, String> issueColumn, meanColumn;
    @FXML
    private TableColumn<WordView, Boolean> statusColumn;
    @FXML
    private TableColumn<WordView, String> categoryColumn, subCategoryColumn;
    @FXML
    private TableColumn<WordView, WordView> selectColumn, updateToColumn;
    @FXML
    private ChoiceBox<CategoryView> categoryChoiceBox;
    @FXML
    private ChoiceBox<SubCategoryView> subCategoryChoiceBox;
    @FXML
    private Button setInSelectedButton;


    @Autowired
    public MainFrameController(CategoryModel categoryModel, WordModel wordModel, SubCategoryModel subCategoryModel )
    {
        this.categoryModel = categoryModel;
        this.wordModel = wordModel;
        this.subCategoryModel = subCategoryModel;
        logger = new LoggerWriter();
        bundle = ResourceBundle.getBundle("bundles.messagesData");
    }

    @FXML
    private void initialize()
    {
        try {
            wordModel.fillWithData();
            categoryModel.fillWithData();
            subCategoryModel.fillWithData();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.writeLog("execetion at initialize", e);
            DialogUtils.errorDialog(bundle.getString("error.download"), bundle.getString("error.downloadTitle"), e);
        }
        fillTableWithData();
        tableView.setItems(wordModel.getWordWiewList());
        fiiComboBoxesWithData();
    }

    private void fillTableWithData() {
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
                    TableRow row = this.getTableRow();

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
                                            tmpWord.setModifed(true);
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

                                            tmpWord.setModifed(true);
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
                    StringProperty itemProperty = new SimpleStringProperty(item.toString().replaceAll("false", bundle.getString("message.known")).replaceAll("true", bundle.getString("message.unknown")));
                    textProperty().bind(itemProperty);

                    setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getClickCount() == 2 ) {
                                WordView tmpWord = tableView.getSelectionModel().selectedItemProperty().get();
                                if (!item)
                                {
                                    itemProperty.setValue("known");
                                    tmpWord.setKnowledgeStatus(true);
                                }
                                else
                                    {
                                        itemProperty.setValue("unknown");
                                        tmpWord.setKnowledgeStatus(false);
                                    }

                                tmpWord.setModifed(true);
                            }
                        }
                    });
                }
            }
        });

        //Category column
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty().get().categoryNameProperty());
        categoryColumn.setCellFactory(cell -> new TableCell<WordView, String>(){
            ChoiceBox<CategoryView> comboBox = new ChoiceBox<>();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty){
                    setGraphic(null);
                }
                else {
                    textProperty().bind(new SimpleStringProperty(item));
                    comboBox.setItems(categoryModel.getCategoryViewList());
                    if (comboBox.getItems().size()<2){
                        comboBox.setDisable(true);
                        if (comboBox.getItems().size()>0)
                        comboBox.setValue(comboBox.getItems().get(0));
                    }
                    else {
                        comboBox.setDisable(false);

                    comboBox.valueProperty().addListener(new ChangeListener<CategoryView>() {
                        @Override
                        public void changed(ObservableValue<? extends CategoryView> observable, CategoryView oldValue, CategoryView newValue) {
                            //System.out.println("observale: "+observable.toString()+"odValue: "+oldValue.toString()+"newValue: "+newValue.toString());
                            WordView tmpWord = tableView.getSelectionModel().selectedItemProperty().get();
                            tmpWord.setCategory(newValue);
                            tmpWord.setSubCategory(null);
                            textProperty().bind(newValue.categoryNameProperty());
                            tmpWord.setModifed(true);
                        }
                    });
                    }

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
                    ChoiceBox<SubCategoryView> comboBox = new ChoiceBox<>();
                    comboBox.valueProperty().addListener(new ChangeListener<SubCategoryView>() {
                        @Override
                        public void changed(ObservableValue<? extends SubCategoryView> observable, SubCategoryView oldValue, SubCategoryView newValue) {

                                WordView tmpWord = tableView.getSelectionModel().selectedItemProperty().get();
                                tmpWord.setSubCategory(newValue);
                                textProperty().bind(new SimpleStringProperty(newValue.getSubCategoryName()));
                                tmpWord.setModifed(true);
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
        //update column
        updateToColumn.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue()));
        updateToColumn.setCellFactory(cell-> new TableCell<WordView, WordView>(){
            @Override
            protected void updateItem(WordView item, boolean empty) {
                super.updateItem(item, empty);
                if (empty){
                    setGraphic(null);
                }
                else {
                    CheckBox checkBox = new CheckBox();
                    checkBox.setDisable(true);
                    checkBox.selectedProperty().bindBidirectional(item.modifedProperty());
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
        {
            try {
                wordModel.saveToDataBase(issueTextFiled.getText(),meanTextField.getText(),categoryChoiceBox.getSelectionModel().getSelectedItem(),subCategoryChoiceBox.getSelectionModel().getSelectedItem(),isKnownCheckBox.isSelected() ) ;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                logger.writeLog("execetion at saving word", e);
                DialogUtils.errorDialog(bundle.getString("error.save"), bundle.getString("error.saveTitle"), e);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
                logger.writeLog("execetion at saving to DB", e);
                DialogUtils.errorDialog(bundle.getString("error.save"), bundle.getString("error.saveTitle"), e);
            }
        }
        else {
            DialogUtils.confirmDialog(bundle.getString("dialog.empyFields"), bundle.getString("dialog.title.emptyFields"));
        }
        categoryChoiceBox.setValue(null);
        subCategoryChoiceBox.setValue(null);

    }
    @FXML
    private void saveCategoryToDB(ActionEvent actionEvent) {

        String tmpCategoryName = categoryTextField.getText();
        if (!StringUtils.isEmpty(tmpCategoryName)){
            try {
                categoryModel.saveToDataBase("tmp",tmpCategoryName);
                categoryTextField.setText("");
                wordModel.fillWithData();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.writeLog("execetion at saving category", e);
                DialogUtils.errorDialog(bundle.getString("error.save"), bundle.getString("error.saveTitle"), e);
            }
        }
        clearSubCategoryBox();
        clearCategoryBox();

    }

    @FXML
    private void saveSubCategoryToDB(ActionEvent actionEvent) {
        String tmpSubCategoryName = subCategoryTextField.getText();
        CategoryView categoryTmp = categoryChoiceBox.getSelectionModel().getSelectedItem();
        if (!StringUtils.isEmpty(tmpSubCategoryName) && categoryTmp!=null){
            try {
                subCategoryModel.saveToDataBase("descriptionTmp", tmpSubCategoryName, categoryTmp);
                subCategoryTextField.setText("");
                categoryModel.fillWithData();
                wordModel.fillWithData();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.writeLog("execetion at saving subcategory", e);
                DialogUtils.errorDialog(bundle.getString("error.save"), bundle.getString("error.saveTitle"), e);
            }
        }
        clearSubCategoryBox();
        clearCategoryBox();
    }

    @FXML
    private void deleteSelectedWords(ActionEvent actionEvent) {
        try {
            wordModel.deleteSelectedWords();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.writeLog("execetion at deleating selected words", e);
            DialogUtils.errorDialog(bundle.getString("error.delete"), bundle.getString("error.deleteTitle"), e);
        }
        tableView.refresh();
    }

    @FXML
    private void deleteSubCategory(ActionEvent actionEvent) {
        SubCategoryView subCategory = subCategoryChoiceBox.getSelectionModel().getSelectedItem();
        if (subCategory!=null){
            try {
                wordModel.deleteBySubCategory(subCategory);
                subCategoryModel.deleteFromDataBaseById(subCategory);
            } catch (SQLException e) {
                e.printStackTrace();
                logger.writeLog("execetion at deleating subCategory", e);
                DialogUtils.errorDialog(bundle.getString("error.delete"), bundle.getString("error.deleteTitle"), e);
            }
            tableView.refresh();
            subCategoryChoiceBox.getItems().remove(subCategory);
            clearSubCategoryBox();
            clearCategoryBox();
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
                DialogUtils.errorDialog(bundle.getString("error.delete"), bundle.getString("error.deleteTitle"), e);
            }
            tableView.refresh();
            try {
                subCategoryModel.deleteByCategory(category);
            } catch (SQLException e) {
                logger.writeLog("execetion at deleating SubCategory from Catrogry", e);
                DialogUtils.errorDialog(bundle.getString("error.delete"), bundle.getString("error.deleteTitle"), e);
                e.printStackTrace();
            }
            category.getSubCategories().forEach(subCategoryView -> subCategoryChoiceBox.getItems().remove(subCategoryView));
            try {
                categoryModel.deleteFromDataBaseById(category);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            categoryChoiceBox.getItems().remove(category);
            clearSubCategoryBox();
            clearCategoryBox();
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
            DialogUtils.errorDialog(bundle.getString("error.save"), bundle.getString("error.saveTitle"), e);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            logger.writeLog("exception sql at updating Words in to database", e);
            DialogUtils.errorDialog(bundle.getString("error.save"), bundle.getString("error.saveTitle"), e);
        }
        tableView.refresh();
    }

    @FXML
    private void serchWords(ActionEvent actionEvent) {
        try {
            wordModel.filtrWords(meanTextField.getText(),issueTextFiled.getText(),categoryChoiceBox.getValue(), subCategoryChoiceBox.getValue());
            tableView.refresh();
        } catch (SQLException e) {
            DialogUtils.errorDialog(bundle.getString("error.download"), bundle.getString("error.downloadTitle"), e);

            logger.writeLog("exception SQL at serching Words", e);

        } catch (ParseException e) {
            DialogUtils.errorDialog(bundle.getString("error.serch"), bundle.getString("error.serchTitle"), e);

            logger.writeLog("exception parse at serching Words", e);
        }
        //tableView.refresh();
    }

    @FXML
    private
    void setNotifications(ActionEvent actionEvent) {
        List<WordView> wordViewListTmp = getSelectedWords();

        if (wordViewListTmp.size()>0){
        final String FXML_NOTIFICATION_DIALOG_FXML = "/fxml/notification-options.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setResources(bundle);

        fxmlLoader.setLocation(getClass().getResource(FXML_NOTIFICATION_DIALOG_FXML));

        Parent dialog = null;
        try {
            dialog = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotifiactionOptionController notificationDialogController = fxmlLoader.getController();
            notificationDialogController.setWordsToDisplay(wordViewListTmp);

            Scene scene =null;
            assert dialog != null;
            scene = new Scene(dialog);
            scene.getStylesheets().add(App.class.getResource("/css/style.css").toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }
        else
            DialogUtils.confirmDialog(bundle.getString("infromation.amutWords"),bundle.getString("tite.emptyList"));
    }

    @FXML
    private void setOffNotifications() {
        NotificationBar.getInstance().setWorkFlag(false);
    }

    @FXML
    private void clearSubCategoryBox() {
        subCategoryChoiceBox.getSelectionModel().select(null);
    }

    @FXML
    private void clearCategoryBox() {
        categoryChoiceBox.getSelectionModel().select(null);
        subCategoryChoiceBox.getSelectionModel().select(null);
        subCategoryChoiceBox.setItems(FXCollections.observableArrayList());
    }

    @FXML
    private void reloadData(){
        try {
            wordModel.realoadDataFromDB();
            tableView.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearSelectedModifed(){
        for (WordView wordView: wordModel.getWordWiewList()) {
            if (wordView.isModifed() && wordView.isChecked())
            {
                try {
                    wordModel.repleaceByViewFromDB(wordView);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void saveToTxtFile() {
        List<WordView> wordViewListTmp = getSelectedWords();

        if (wordViewListTmp.size()>0){
            final String FXML_NOTIFICATION_DIALOG_FXML = "/fxml/save-txt-options.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setResources(bundle);

            fxmlLoader.setLocation(getClass().getResource(FXML_NOTIFICATION_DIALOG_FXML));

            Parent dialog = null;
            try {
                dialog = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            SaveTxtOptionsController saveTxtOptionsController = fxmlLoader.getController();
            saveTxtOptionsController.setWordsToSave(wordViewListTmp);

            Scene scene =null;
            assert dialog != null;
            scene = new Scene(dialog);
            scene.getStylesheets().add(App.class.getResource("/css/style.css").toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }
        else
            DialogUtils.confirmDialog(bundle.getString("infromation.amutWords"),bundle.getString("tite.emptyList"));

    }

    public void readTxtFile(ActionEvent actionEvent) {
        File file = DialogUtils.fileChooser(tableView.getScene().getWindow(), "*.txt", "TXT files (*.txt)");
        System.out.println(file);
        if (file!=null) {
            try {
                List<WordView> wordsList = TextFileHandler.ReadTxtFile(file);
                if (wordsList!=null)
                Objects.requireNonNull(TextFileHandler.ReadTxtFile(file)).forEach(wordView -> {
                    try {
                       wordModel.saveToDataBase(wordView);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                else {
                    DialogUtils.confirmDialog(bundle.getString("message.badFileFormat"),bundle.getString("title.badFileFormat"));
                }
                wordModel.realoadDataFromDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void generatePdfFile(ActionEvent actionEvent) {
        List<WordView> wordViewListTmp = getSelectedWords();

        if (wordViewListTmp.size()>0){
            final String FXML_NOTIFICATION_DIALOG_FXML = "/fxml/create-pdf-options.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setResources(bundle);

            fxmlLoader.setLocation(getClass().getResource(FXML_NOTIFICATION_DIALOG_FXML));

            Parent dialog = null;
            try {
                dialog = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            CreatePdfOptionsController createPdfOptionsController = fxmlLoader.getController();

            Scene scene =null;
            assert dialog != null;
            scene = new Scene(dialog);
            scene.getStylesheets().add(App.class.getResource("/css/style.css").toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            String pattern = createPdfOptionsController.getSavePattern();
            File file = createPdfOptionsController.getFile();
            Rectangle pageSize = createPdfOptionsController.getPageFormat();
            int fontSize = createPdfOptionsController.getFontSize();

            if (file!=null){
                try {
                    PdfFileCreator.saveToPdf(wordViewListTmp, pattern,file, pageSize, bundle, fontSize);
                    DialogUtils.confirmDialog(bundle.getString("dialog.PdfCreatedMessage"),bundle.getString("dialog.title.PdfCreated"));
                } catch (DocumentException e) {
                    DialogUtils.errorDialog(bundle.getString("error.documentException"),"error.saveTitle", e);
                    LoggerWriter.writeLog("Document exception on save pdf.",e);
                } catch (FileNotFoundException e) {
                    DialogUtils.errorDialog(bundle.getString("error.fiIeNotFound"),"error.saveTitle", e);
                    LoggerWriter.writeLog("File not found Exception on pdf save.",e);
                }
            }
        }
    }

    @FXML
    private void selectAllItems() {
        wordModel.getWordWiewList().forEach(wordView -> wordView.setChecked(true));
    }

    @FXML
    private void unSelectAllItems() {
        wordModel.getWordWiewList().forEach(wordView -> wordView.setChecked(false));
    }

    @FXML
    private void selectAllModifedItems() {
        for (WordView wordView: wordModel.getWordWiewList()) {
            if (wordView.isModifed())
                wordView.setChecked(true);
        }
    }

    @FXML
    public void setInAllSelected() {
        for (WordView wordView: wordModel.getWordWiewList()) {
            if (categoryChoiceBox.getValue()!=null) {
                wordView.setCategory(categoryChoiceBox.getValue());
            } else {
                wordView.setCategory(new CategoryView());
            }
            if (subCategoryChoiceBox.getValue()!=null) {
                wordView.setSubCategory(subCategoryChoiceBox.getValue());
            } else {
                wordView.setSubCategory(new SubCategoryView());
            }
            wordView.setChecked(isKnownCheckBox.isSelected());
            wordView.setModifed(true);
            tableView.refresh();
        }
    }

    private List<WordView> getSelectedWords(){
        List<WordView> wordViewList = new ArrayList<>();
        for (WordView word: wordModel.getWordWiewList()) {
            if (word.isChecked())
                wordViewList.add(word);
        }
        return wordViewList;
    }


}
