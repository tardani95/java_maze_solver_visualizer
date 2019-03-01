package maze_solver;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import maze_solver.maze.Maze;

import static maze_solver.maze.Maze.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
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
