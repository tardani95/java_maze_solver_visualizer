package maze_solver;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import maze_solver.maze.Maze;
import maze_solver.maze.MyPoint2D;
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

    private enum SelectedCell {
        none,
        start_cell,
        end_cell
    }

    private SelectedCell selectedCell = SelectedCell.none;
//    private MazeView mazeView;

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
                , true
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

        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                switch (newValue.getId()) {
                    case "tab_maze_editor":
                        canvas_maze_editor.refreshView();
                        break;
                    case "tab_simulation":
                        canvas_simulation.refreshView();
                        break;
                    default:
                        break;
                }
            }
        });

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

    private void onDragDetected(MazeView mazeView, MouseEvent mouseEvent) {
        selectedCell = SelectedCell.none;
        int x = mazeView.validateMouseX(mouseEvent);
        int y = mazeView.validateMouseY(mouseEvent);
        MyPoint2D cell = new MyPoint2D(x, y);
        if (mazeView.isStartCell(cell)) {
            selectedCell = SelectedCell.start_cell;
        }

        if (mazeView.isEndCell(cell)) {
            selectedCell = SelectedCell.end_cell;
        }
    }

    public void onDragDetectedMazeEditor(MouseEvent mouseEvent) {
        onDragDetected(canvas_maze_editor, mouseEvent);

    }

    public void onDragDetectedSimulation(MouseEvent mouseEvent) {
        onDragDetected(canvas_simulation, mouseEvent);
    }

    private void onDrag(MazeView mazeView, MouseEvent mouseEvent) {
        int x = mazeView.validateMouseX(mouseEvent);
        int y = mazeView.validateMouseY(mouseEvent);
        switch (selectedCell) {
            case start_cell:
                if (mazeView.isEndCell(new MyPoint2D(x, y))) {
                    // do nothing
                } else {
                    mazeView.setStartCellCoordinates(x, y);
                    System.out.print("Start set to: ");
                    System.out.println(x + "," + y);
                }

                break;
            case end_cell:
                if (mazeView.isStartCell(new MyPoint2D(x, y))) {
                    // do nothing
                } else {
                    mazeView.setEndCellCoordinates(x, y);
                    System.out.print("End set to: ");
                    System.out.println(x + "," + y);
                }
                break;
            default:
                break;
        }
        mazeView.refreshView();
    }

    public void onDragMazeEditor(MouseEvent mouseEvent) {
        onDrag(canvas_maze_editor, mouseEvent);
    }

    public void onDragSimulation(MouseEvent mouseEvent) {
        onDrag(canvas_simulation, mouseEvent);
    }

}
