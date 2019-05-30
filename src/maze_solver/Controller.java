package maze_solver;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import maze_solver.maze.Maze;
import maze_solver.view.MazeView;


public class Controller {
    @FXML
    TabPane tabPane;
    @FXML
    MazeView canvas_maze_editor;
    @FXML
    Slider slider_maze_editor_cell_size;


    @FXML
    MazeView canvas_simulation;


    @FXML
    AnchorPane simulation_anchor_pane;
    @FXML
    AnchorPane maze_editor_anchor_pane;
    @FXML
    Slider slider_simulation_cell_size;



    private double width;
    private double height;
    private double cell_size = 30.0;

    private static Maze maze;
    private GraphicsContext gc;

//    public Controller() {
//        System.out.println("Controller() called");
//    }

    @FXML
    void initialize() {
        System.out.println("Controller.initialize() called");

        maze = Maze.getInstance();
//        cell_size = 30.0;

        canvas_maze_editor.setMaze(maze);
        canvas_maze_editor.setVisibility(true
                , false
                , false
                , false
                , false
                , false
                , false
                , false
                , false);
        canvas_maze_editor.setGraphicContextTranslation();
        canvas_maze_editor.refreshView();

        canvas_simulation.setMaze(maze);
        canvas_simulation.setVisibility(true
                , true
                , true
                , false
                , true
                , true
                , true
                , true
                , true);
        canvas_simulation.setGraphicContextTranslation();
        canvas_simulation.refreshView();
//        gc = canvas_maze_editor.getGraphicsContext2D();
//        width = cell_size * maze.getLength_X();
//        height = cell_size * maze.getLength_Y();
//        canvas_maze_editor.setWidth(width);
//        canvas_maze_editor.setHeight(height);
//        gc.clearRect(0, 0, width, height);
//        drawMazeWalls(maze.getWalls(), Color.BLACK, 2);


        //gc = canvas_simulation.getGraphicsContext2D();


        tabPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            width = newValue.doubleValue();
//            canvas_simulation.setWidth(width / 2);
            //drawMaze(null);
        });
        tabPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            height = newValue.doubleValue();
//            canvas_simulation.setHeight(height / 2);
            //drawMaze(null);
        });
    }

    public void onSliderSimulationChanged(InputEvent inputEvent) {
        canvas_simulation.setCellSize(slider_simulation_cell_size.getValue());
        canvas_simulation.refreshView();
    }

    public void runSimulation(MouseEvent mouseEvent) {
        System.out.println("run simulation button pressed");

        Runnable BDFS = new Runnable() {
            @Override
            public void run() {
                int counter = 0;
                System.out.println("Counter value: " + counter);
                while (!maze.bestDepthFirstSearch(maze.getCurrent())) {

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            canvas_simulation.refreshView();
//                            drawMaze(null);
                        }
                    });

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        System.out.println("Error: ");
                        System.out.println(e.getMessage());
                    }

                    System.out.println("Counter value: " + counter);
                }
            }
        };

        new Thread(BDFS).start();


    }

    public void onSliderMazeEditorChanged(InputEvent inputEvent) {
        canvas_maze_editor.setCellSize(slider_maze_editor_cell_size.getValue());
        canvas_maze_editor.refreshView();
    }
}
