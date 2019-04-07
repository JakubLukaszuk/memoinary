package com.nihillon;

import com.nihillon.controller.MainFrameController;
import com.nihillon.dao.CommonDao;
import com.nihillon.models.Category;
import com.nihillon.models.SubCategory;
import com.nihillon.models.Word;
import com.nihillon.utils.DbManager;
import com.nihillon.viewModel.CategoryModel;
import com.nihillon.viewModel.CategoryView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.Date;


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
        springContext = SpringApplication.run(App.class);
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(springContext::getBean);
        DbManager.initializeDatabase();

        CommonDao commonDao = new CommonDao();
        Word word = new Word();
        word.setIssue("Zagadnienie!!!!!!!!!!!!!!!");
        word.setKnowledgeStatus(false);
        word.setMean("nie wiem");
        Date date =new Date();
        word.setDateOfAddition(date);
        Category category = new Category();
        category.setCategory("kategoria");
        category.setDateOfAddition(date);
        category.setDescription("description");
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(category);
        subCategory.setSubCategory("podkategoria");
        subCategory.setDescription("deskrypaca");
        subCategory.setDateOfAddition(date);
        word.setCategory(category);
        word.setSubCategory(subCategory);
        commonDao.createOrUpdate( category);
        commonDao.createOrUpdate( subCategory);
        commonDao.createOrUpdate( word);

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
        commonDao.createOrUpdate(subCategory1);
        commonDao.createOrUpdate(word1);

    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        fxmlLoader.setLocation(getClass().getResource("/fxml/main-frame.fxml"));
        rootNode = fxmlLoader.load();



        primaryStage.setTitle("Memoinary");
        primaryStage.setResizable(false);
        Scene scene = new Scene(rootNode);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();

        
    }

    @Override
    public void stop(){
        springContext.stop();
    }
}
