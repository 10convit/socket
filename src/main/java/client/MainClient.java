package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainClient extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sampleClient.fxml"));
        primaryStage.setTitle("Ung dung chat");
        Scene logchat = new Scene(root, 647.2, 400);
        logchat.getStylesheets().add("/theme1.css");
        primaryStage.setScene(logchat);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
