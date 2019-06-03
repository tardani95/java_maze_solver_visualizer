package maze_solver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main_layout.fxml"));
        primaryStage.setTitle("Maze Visualizer");
        Scene myScene = new Scene(root, 1024, 576);
        primaryStage.setScene(myScene);
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(576);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
