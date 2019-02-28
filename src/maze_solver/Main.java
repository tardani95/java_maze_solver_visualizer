package maze_solver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import maze_solver.maze.Maze;

import static maze_solver.maze.Maze.*;

public class Main extends Application {

    public static Maze labyrinth;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {

        labyrinth = new Maze();
        System.out.println("Point [" + labyrinth.getCurrent().x + "," + labyrinth.getCurrent().y + "] has top wall: " + labyrinth.getWall(labyrinth.getCurrent(), TOP_WALL));
        System.out.println("Point [" + labyrinth.getCurrent().x + "," + labyrinth.getCurrent().y + "] has left wall: " + labyrinth.getWall(labyrinth.getCurrent(), LEFT_WALL));
        System.out.println("Point [" + labyrinth.getCurrent().x + "," + labyrinth.getCurrent().y + "] has bottom wall: " + labyrinth.getWall(labyrinth.getCurrent(), BOTTOM_WALL));
        System.out.println("Point [" + labyrinth.getCurrent().x + "," + labyrinth.getCurrent().y + "] has right wall: " + labyrinth.getWall(labyrinth.getCurrent(), RIGHT_WALL));
        launch(args);
    }
}
