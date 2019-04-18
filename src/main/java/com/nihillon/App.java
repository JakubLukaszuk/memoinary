package com.nihillon;

import com.nihillon.dao.CommonDao;
import com.nihillon.models.Category;
import com.nihillon.models.SubCategory;
import com.nihillon.models.Word;
import com.nihillon.utils.DbManager;
import com.nihillon.utils.NotificationBar;
import com.nihillon.utils.file.LoggerWriter;
import dorkbox.notify.Notify;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;


@SpringBootApplication
public class App extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;
    private FXMLLoader fxmlLoader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init(){
        NotificationBar.getInstance().init();
        springContext = SpringApplication.run(App.class);
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(springContext::getBean);
        DbManager.initializeDatabase();

        CommonDao commonDao = new CommonDao();
        Word word = new Word();
        word.setIssue("Zagadnienie!!!!!!!!!!!!!!dasdsadasdsadsa!");
        word.setKnowledgeStatus(false);
        word.setMean("nie wiefsddddddddddddddddddddddddddddm");
        Date date =new Date();
        word.setDateOfAddition(date);
        Category category = new Category();
        category.setCategory("kategorisdffffffffffffffffffa");
        category.setDateOfAddition(date);
        category.setDescription("description");
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(category);
        subCategory.setSubCategory("podkatsdfffffffffffffffffffffffffffegoria");
        subCategory.setDescription("deskrypaca");
        subCategory.setDateOfAddition(date);
        word.setCategory(category);
        word.setSubCategory(subCategory);
        try {
            commonDao.createOrUpdate( category);
            commonDao.createOrUpdate( subCategory);
            commonDao.createOrUpdate( word);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        Word word1 = new Word();
        word1.setIssue("zagadnienie2");
        word1.setKnowledgeStatus(true);
        word1.setMean("XD");
        word1.setDateOfAddition(date);
        word1.setCategory(category);
        SubCategory subCategory1 = new SubCategory();
        subCategory1.setSubCategory("sub2");
        subCategory1.setDateOfAddition(date);
        subCategory1.setCategory(category);
        word1.setSubCategory(subCategory1);
        subCategory1.setDescription("xd");
        try {
            commonDao.createOrUpdate(subCategory1);
            commonDao.createOrUpdate(word1);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void start(Stage primaryStage) throws IOException {

        fxmlLoader.setLocation(getClass().getResource("/fxml/main-frame.fxml"));
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.messagesData");
        fxmlLoader.setResources(bundle);
        rootNode = fxmlLoader.load();
        NotificationBar.getInstance().displayTray(bundle.getString("message.invitation"), bundle.getString("application.title").toUpperCase(), 3000);

        primaryStage.setResizable(false);
        Scene scene = new Scene(rootNode);
        primaryStage.setTitle(bundle.getString("application.title"));
        primaryStage.setScene(scene);
        scene.getStylesheets().add(App.class.getResource("/css/style.css").toExternalForm());
        primaryStage.sizeToScene();
        primaryStage.show();
    }



    @Override
    public void stop(){
        springContext.stop();
        System.exit(0);
    }
}
