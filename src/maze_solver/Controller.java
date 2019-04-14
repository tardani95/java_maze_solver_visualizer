package maze_solver;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import maze_solver.maze.Maze;
import maze_solver.maze.Point2D;


public class Controller {

    @FXML
    TabPane tabPane;
    @FXML
    AnchorPane simulation_anchor_pane;
    @FXML
    Slider slider_cell_size;
    @FXML
    Canvas canvas;

    private double width;
    private double height;
    private double cell_size;

    private static Maze maze;
    private GraphicsContext gc;

    public Controller() {
        System.out.println("Controller() called");
    }

    @FXML
    void initialize() {
        System.out.println("Initialize() called");

        gc = canvas.getGraphicsContext2D();
        maze = Maze.getInstance();

        cell_size = 30.0;

        tabPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            width = newValue.doubleValue();
            canvas.setWidth(width / 2);
            drawMaze(null);
        });
        tabPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            height = newValue.doubleValue();
            canvas.setHeight(height / 2);
            drawMaze(null);
        });
    }

    public void onSliderChanged(MouseEvent mouseEvent) {
        cell_size = slider_cell_size.getValue();
        System.out.println("Cell size changed to " + String.valueOf(cell_size) + "px");
        drawMaze(null);
    }

    public void runSimulation(MouseEvent mouseEvent) {
        System.out.println("run simulation button pressed");
    }

    public void drawMaze(MouseEvent mouseEvent) {
        System.out.println("drawMaze() called");

        double width = cell_size * maze.getCols();
        double height = cell_size * maze.getRows();

        canvas.setWidth(width);
        canvas.setHeight(height);
        gc.clearRect(0, 0, width, height);

        drawMazeWalls(maze.getWalls(), Color.RED, 1);

        drawMazeWalls(maze.getExploredWalls(), Color.BLACK, 2);

        drawPoint(maze.getStart(), Color.GREENYELLOW,5);
        drawPoint(maze.getEnd(), Color.ORANGERED,5);
//        drawMazeOutline();
    }

    private void drawPoint(Point2D point, Color color, double padding) {
        gc.setFill(color);
        gc.fillRect(point.x * cell_size + padding, point.y * cell_size + padding, cell_size - 2* padding
                , cell_size - 2* padding);
    }

    private void drawMazeWalls(int[][] mazeWalls, Color color, double lineWidth) {
        double startX;
        double startY;
        double stopX;
        double stopY;

        gc.setLineWidth(lineWidth);
        gc.setStroke(color);
        //print horizontal walls
        for (int row_i = 0; row_i < maze.getRows() + 1; row_i++) {
            for (int coll_i = 0; coll_i < maze.getCols(); coll_i++) {

                if ((mazeWalls[0][row_i] & (0b1 << coll_i)) > 0) {
                    startX = coll_i * cell_size;
                    stopX = (coll_i + 1) * cell_size;
                    startY = row_i * cell_size;
                    stopY = startY;

                    gc.strokeLine(startX, startY, stopX, stopY);
                }
            }
        }

        //print vertical walls
        for (int coll_i = 0; coll_i < maze.getCols() + 1; coll_i++) {
            for (int row_i = 0; row_i < maze.getRows(); row_i++) {

                if ((mazeWalls[1][coll_i] & (0b1 << row_i)) > 0) {
                    startX = coll_i * cell_size;
                    stopX = startX;
                    startY = row_i * cell_size;
                    stopY = (row_i + 1) * cell_size;

                    gc.strokeLine(startX, startY, stopX, stopY);
                }
            }
        }
    }

    private void drawMazeOutline() {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.strokeRect(0, 0, maze.getCols() * cell_size, maze.getRows() * cell_size);
    }
}
