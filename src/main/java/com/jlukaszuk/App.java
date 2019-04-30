package com.jlukaszuk;

import com.jlukaszuk.utils.DbManager;
import com.jlukaszuk.utils.DialogUtils;
import com.jlukaszuk.utils.NotificationBar;
import com.jlukaszuk.utils.file.LoggerWriter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SpringBootApplication
public class App extends Application {

    private ConfigurableApplicationContext springContext;
    private FXMLLoader fxmlLoader;
    private ResourceBundle bundle;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init(){
        NotificationBar.getInstance().init();
        bundle = ResourceBundle.getBundle("bundles.messagesData");
        springContext = SpringApplication.run(App.class);
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(springContext::getBean);
        try {
            DbManager.initializeDatabase();
        } catch (SQLException | IOException e) {
            DialogUtils.errorDialog(bundle.getString("error.download"),bundle.getString("error.downloadTitle"));
            LoggerWriter.writeLog("initilaziation error", e);
        }


    }


    @Override
    public void start(Stage primaryStage) throws IOException {

        fxmlLoader.setLocation(getClass().getResource("/fxml/main-frame.fxml"));
        fxmlLoader.setResources(bundle);
        Parent rootNode = fxmlLoader.load();
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
