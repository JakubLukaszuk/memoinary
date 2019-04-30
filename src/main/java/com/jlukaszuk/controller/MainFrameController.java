package com.jlukaszuk.controller;

import com.itextpdf.text.DocumentException;
import com.jlukaszuk.App;
import com.jlukaszuk.utils.DialogUtils;
import com.jlukaszuk.utils.NotificationBar;
import com.jlukaszuk.utils.file.LoggerWriter;
import com.jlukaszuk.utils.file.pdf.PdfFileCreator;
import com.jlukaszuk.utils.file.txt.TextFileHandler;
import com.jlukaszuk.viewModel.*;
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
        bundle = ResourceBundle.getBundle("bundles.messagesData");
    }

    @FXML
    private void initialize()
    {
        try {
            wordModel.fillWithData();
            categoryModel.fillWithData();
            subCategoryModel.fillWithData();
        } catch (SQLException | IOException e ) {
            LoggerWriter.writeLog("execetion at initialize", e);
            DialogUtils.errorDialog(bundle.getString("error.download"),
                    bundle.getString("error.downloadTitle"));
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
                                            WordView tmpWord = tableView.getSelectionModel().
                                                    selectedItemProperty().get();
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
                                            WordView tmpWord = tableView.getSelectionModel()
                                                    .selectedItemProperty().get();
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
                                WordView tmpWord = tableView.getSelectionModel()
                                        .selectedItemProperty().get();
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

                        }
                        else
                        {
                            item.setChecked(false);
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

    private void fiiComboBoxesWithData(){
        categoryChoiceBox.setItems(categoryModel.getCategoryViewList());
        categoryChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null)
                subCategoryChoiceBox.setItems(newValue.getSubCategories());
        });

    }

    @FXML
    private void saveWordToDB() {

        String tmpIssue = issueTextFiled.getText(), tmpMean = meanTextField.getText();
        if (!StringUtils.isEmpty(tmpIssue) || !StringUtils.isEmpty(tmpMean))
        {
            try {
                wordModel.saveToDataBase(issueTextFiled.getText(),meanTextField.getText(),categoryChoiceBox.getSelectionModel().getSelectedItem(),subCategoryChoiceBox.getSelectionModel().getSelectedItem(),isKnownCheckBox.isSelected());
                tableView.refresh();
            }
            catch (ParseException | IOException | SQLException e)
            {
                LoggerWriter.writeLog("execetion at saving word", e);
                DialogUtils.errorDialog(bundle.getString("error.save"),
                        bundle.getString("error.saveTitle"));
            }
        }
        else {
            DialogUtils.informDialog(bundle.getString("dialog.empyFields"), bundle.getString("dialog.title.emptyFields"));
        }
        categoryChoiceBox.setValue(null);
        subCategoryChoiceBox.setValue(null);

    }
    @FXML
    private void saveCategoryToDB() {

        String tmpCategoryName = categoryTextField.getText();
        if (!StringUtils.isEmpty(tmpCategoryName)){
            try {
                categoryModel.saveToDataBase("tmp",tmpCategoryName);
                categoryTextField.setText("");
                wordModel.fillWithData();
            } catch (SQLException | IOException  e) {
                LoggerWriter.writeLog("execetion at saving category", e);
                DialogUtils.errorDialog(bundle.getString("error.save")
                        , bundle.getString("error.saveTitle"));
            }
        }
        clearSubCategoryBox();
        clearCategoryBox();

    }

    @FXML
    private void saveSubCategoryToDB() {
        String tmpSubCategoryName = subCategoryTextField.getText();
        CategoryView categoryTmp = categoryChoiceBox.getSelectionModel().getSelectedItem();
        if (!StringUtils.isEmpty(tmpSubCategoryName) && categoryTmp!=null){
            try {
                subCategoryModel.saveToDataBase("descriptionTmp", tmpSubCategoryName, categoryTmp);
                subCategoryTextField.setText("");
                categoryModel.fillWithData();
                wordModel.fillWithData();
            } catch (SQLException | IOException e) {
                LoggerWriter.writeLog("execetion at saving subcategory", e);
                DialogUtils.errorDialog(bundle.getString("error.save"), bundle.getString("error.saveTitle"));
            }
        }
        clearSubCategoryBox();
        clearCategoryBox();
    }

    @FXML
    private void deleteSelectedWords() {
        try {
            wordModel.deleteSelectedWords();
        } catch (SQLException | IOException e) {
            LoggerWriter.writeLog("execetion at deleating selected words", e);
            DialogUtils.errorDialog(bundle.getString("error.delete"), bundle.getString("error.deleteTitle"));
        }
        tableView.refresh();
    }

    @FXML
    private void deleteSubCategory() {
        SubCategoryView subCategory = subCategoryChoiceBox.getSelectionModel().getSelectedItem();
        if (subCategory!=null){
            try {
                wordModel.deleteBySubCategory(subCategory);
                subCategoryModel.deleteFromDataBaseById(subCategory);
            } catch (IOException | SQLException e) {

                LoggerWriter.writeLog("execetion at deleating subCategory", e);
                DialogUtils.errorDialog(bundle.getString("error.delete"),
                        bundle.getString("error.deleteTitle"));
            }
            tableView.refresh();
            subCategoryChoiceBox.getItems().remove(subCategory);
            clearSubCategoryBox();
            clearCategoryBox();
        }
    }

    @FXML
    private void deleteCategory() {
        CategoryView category = categoryChoiceBox.getSelectionModel().getSelectedItem();
        if (category!=null){
            try {
                wordModel.deleteByCategory(category);
            } catch (IOException | SQLException e) {
                LoggerWriter.writeLog("execetion at deleating Category", e);
                DialogUtils.errorDialog(bundle.getString("error.delete"),
                        bundle.getString("error.deleteTitle"));
            }
            tableView.refresh();
            try {
                subCategoryModel.deleteByCategory(category);
            } catch (IOException | SQLException e) {
                LoggerWriter.writeLog("execetion at deleating SubCategory from Catrogry", e);
                DialogUtils.errorDialog(bundle.getString("error.delete"),
                        bundle.getString("error.deleteTitle"));
            }
            category.getSubCategories().forEach(subCategoryView -> subCategoryChoiceBox.getItems().remove(subCategoryView));
            try {
                categoryModel.deleteFromDataBaseById(category);
            } catch (SQLException | IOException e) {
                LoggerWriter.writeLog("execetion at deleating SubCategory from Catrogry", e);
                DialogUtils.errorDialog(bundle.getString("error.delete"),
                        bundle.getString("error.deleteTitle"));
            }
            categoryChoiceBox.getItems().remove(category);
            clearSubCategoryBox();
            clearCategoryBox();
        }
    }

    @FXML
    private void UpdateWordsInDB() {
        try {
            wordModel.updateInDataBase();
        }
        catch (ParseException e)
        {
            LoggerWriter.writeLog("exception parse at updating Words in to database", e);
            DialogUtils.errorDialog(bundle.getString("error.save"),
                    bundle.getString("error.saveTitle"));
        }
        catch (IOException  | SQLException e)
        {
            LoggerWriter.writeLog("exception sql at updating Words in to database", e);
            DialogUtils.errorDialog(bundle.getString("error.save"),
                    bundle.getString("error.saveTitle"));
        }
        tableView.refresh();
    }

    @FXML
    private void serchWords() {
        try {
            wordModel.filtrWords(meanTextField.getText(),issueTextFiled.getText(),categoryChoiceBox.getValue(), subCategoryChoiceBox.getValue());
            tableView.refresh();
        } catch (ParseException e) {
            DialogUtils.errorDialog(bundle.getString("error.serch"),
                    bundle.getString("error.serchTitle"));

            LoggerWriter.writeLog("exception parse at serching Words", e);
        }
        catch (IOException| SQLException e) {
            DialogUtils.errorDialog(bundle.getString("error.download"),
                    bundle.getString("error.downloadTitle"));

            LoggerWriter.writeLog("exception SQL at serching Words", e);
        }
    }

    @FXML
    private
    void setNotifications() {
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
                DialogUtils.errorDialog(bundle.getString("error.notificationMessage"), bundle.getString("errorTitle.notification"));
            }

            NotifiactionOptionController notificationDialogController = fxmlLoader.getController();

            Scene scene =null;
            assert dialog != null;
            scene = new Scene(dialog);
            scene.getStylesheets().add(App.class.getResource("/css/style.css").toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            int intreval = notificationDialogController.getInterval();
            int displayTime = notificationDialogController.getDisplayTime();

            if (intreval > 0 && displayTime > 0 )
                NotificationBar.getInstance().generateNotifiacations(intreval,displayTime,wordViewListTmp,bundle.getString("application.title"));

        }
        else
            DialogUtils.informDialog(bundle.getString("infromation.amutWords"),bundle.getString("tite.emptyList"));
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
        } catch (SQLException | IOException e) {
            LoggerWriter.writeLog("error at reload data",e);
            DialogUtils.errorDialog(bundle.getString("error.reload"), bundle.getString("errorTitle.reload"));
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
                    LoggerWriter.writeLog("error at clear selectedModifed",e);
                    DialogUtils.errorDialog(bundle.getString("error.reload"), bundle.getString("errorTitle.reload"));
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
                LoggerWriter.writeLog("error at clear selectedModifed",e);
                DialogUtils.errorDialog(bundle.getString("error.reload"), bundle.getString("errorTitle.reload"));
            }

            SaveTxtOptionsController saveTxtOptionsController = fxmlLoader.getController();

            Scene scene =null;
            assert dialog != null;
            scene = new Scene(dialog);
            scene.getStylesheets().add(App.class.getResource("/css/style.css").toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            File file = saveTxtOptionsController.getFile();
            if (file!=null){
                try {
                    TextFileHandler.SaveTxtFile(wordViewListTmp, saveTxtOptionsController.getSavePattern(), file);
                    DialogUtils.informDialog(bundle.getString("dialog.txtCreatedMessage"), bundle.getString("dialogTitle.txtFileCreted"));
                } catch (IOException e) {
                    DialogUtils.errorDialog(bundle.getString("error.saveErrorMessage"),
                            bundle.getString("error.saveErrorTitle"));
                    LoggerWriter.writeLog("exception on save txt file ",e);
                }
            }
        }
        else
            DialogUtils.informDialog(bundle.getString("infromation.amutWords"),bundle.getString("tite.emptyList"));

    }

    public void readTxtFile() {
        File file = DialogUtils.fileChooser(tableView.getScene().getWindow(), "*.txt", "TXT files (*.txt)");

        if (file!=null) {
            try {
                List<WordView> wordsList = TextFileHandler.ReadTxtFile(file);
                if (wordsList!=null)
                    Objects.requireNonNull(TextFileHandler.ReadTxtFile(file)).forEach(wordView -> {
                        try {
                            wordModel.saveToDataBase(wordView);
                        } catch (SQLException e) {
                            LoggerWriter.writeLog("SQLexception at readFile",e);
                            DialogUtils.errorDialog(bundle.getString("error.saveErrorMessage"),
                                    "error.saveTitle");
                        }
                    });
                else {
                    DialogUtils.informDialog(bundle.getString("message.badFileFormat"),bundle.getString("title.badFileFormat"));
                }
                wordModel.realoadDataFromDB();
            }
            catch (IOException e)
            {
                DialogUtils.errorDialog(bundle.getString("error.IO.message"),bundle.getString("errorTitle.readFile"));
                LoggerWriter.writeLog("IO exception at txtRead", e);
            }
            catch (SQLException e)
            {
                DialogUtils.errorDialog(bundle.getString("error.save"),bundle.getString("errorTitle.readFile"));
                LoggerWriter.writeLog("SQL exception at txtRead", e);
            }
        }
    }

    @FXML
    private void generatePdfFile() {
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
                LoggerWriter.writeLog("IOException at loading PDF dialog", e);
                DialogUtils.informDialog(bundle.getString("error.documentException"),bundle.getString("error.saveTitle"));
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

            File file = createPdfOptionsController.getFile();

            if (file!=null){
                try {
                    PdfFileCreator.saveToPdf(wordViewListTmp, createPdfOptionsController.getSavePattern(),file, createPdfOptionsController.getPageFormat(), bundle,createPdfOptionsController.getFontSize());
                    DialogUtils.informDialog(bundle.getString("dialog.PdfCreatedMessage"),bundle.getString("dialog.title.PdfCreated"));
                } catch (DocumentException e) {
                    DialogUtils.errorDialog(bundle.getString("error.documentException"),
                            "error.saveTitle");
                    LoggerWriter.writeLog("Document exception on save pdf.",e);
                } catch (FileNotFoundException e) {
                    DialogUtils.errorDialog(bundle.getString("error.fiIeNotFound"),
                            "error.saveTitle");
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
