package maze_solver;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import maze_solver.model.Cell;
import maze_solver.model.Maze;
import maze_solver.view.MazeView;

import java.text.DecimalFormat;
import java.text.ParsePosition;

import static maze_solver.model.Maze.MAX_MAZE_SIZE;


public class Controller {

    @FXML
    JFXTextField tf_x_size;

    @FXML
    JFXTextField tf_y_size;

    @FXML
    Button btn_generate_maze;


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

    public void onInputTextChanged(InputMethodEvent inputMethodEvent) {
        System.out.println(inputMethodEvent.getSource());
    }

    public void onTfKeyTyped(KeyEvent keyEvent) {
//        Object o = keyEvent.getSource();
//        JFXTextField textField;
//        if (o instanceof JFXTextField) {
//            textField = (JFXTextField) o;
//            String newText = textField.getText();
//            if (newText != null && !newText.equals("")) {
//                System.out.println(newText);
//                if (Integer.valueOf(newText) > MAX_MAZE_SIZE) {
//                    textField.setText(String.valueOf(MAX_MAZE_SIZE));
//                }
//            }
//
//        }
    }

    private enum SelectedCell {
        none,
        start_cell,
        end_cell
    }

    private volatile SelectedCell selectedCell = SelectedCell.none;
//    private MazeView mazeView;

//    public Controller() {
//        System.out.println("Controller() called");
//    }

    @FXML
    void initialize() {
        System.out.println("Controller.initialize() called");

        maze = new Maze(6, 6);
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

        canvas_simulation.setMaze(new Maze());
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

        tf_x_size.textProperty().addListener(newTextFieldInputValidationChangeListener(tf_x_size));
        tf_y_size.textProperty().addListener(newTextFieldInputValidationChangeListener(tf_y_size));
    }

    private ChangeListener<String> newTextFieldInputValidationChangeListener(TextField textField) {
        return new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null && !newValue.equals("")) {
                    if (!(newValue.matches("\\d+(\\.\\d+)?"))) {
                        tf_x_size.textProperty().setValue(oldValue);
                    } else {
                        if (Integer.valueOf(newValue) > MAX_MAZE_SIZE) {
                            textField.textProperty().setValue(String.valueOf(MAX_MAZE_SIZE));
                        }
                    }
                }
            }
        };
    }

    private TextFormatter newTextFieldFormatter() {
        return new TextFormatter<>(
                change -> {
                    if (change.getControlNewText().isEmpty()) {
                        return change;
                    }

                    ParsePosition parsePosition = new ParsePosition(0);
                    Object object = new DecimalFormat("#0").parse(change.getControlNewText(), parsePosition);

                    if (object == null || parsePosition.getIndex() < change.getControlNewText().length()) {
                        return null;
                    } else {
                        return change;
                    }
                }
        );
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
//                System.out.println("Counter value: " + counter);
//                maze.getCurrent().modifyXY(maze.getStart());
                maze = canvas_simulation.getMaze();
                while (!maze.bestDepthFirstSearch(maze.getNext())) {

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
                    counter++;
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        canvas_simulation.refreshView();
//                            drawMaze(null);
                    }
                });
            }
        };
        new Thread(BDFS).start();
    }


    public void onBtnGenerateMazePressed(ActionEvent actionEvent) {
        Runnable generateMaze = new Runnable() {
            @Override
            public void run() {
                int counter = 0;
                canvas_maze_editor.setMaze(new Maze(Integer.valueOf(tf_x_size.textProperty().getValue()),Integer.valueOf(tf_y_size.textProperty().getValue())));
                maze = canvas_maze_editor.getMaze();
                maze.setCurrent(maze.getGenerationStart());
                maze.getCell(maze.getCurrent()).setVisited();

                while (!maze.randomizedDepthFirstSearch()) {

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            canvas_maze_editor.refreshView();
//                            drawMaze(null);
                        }
                    });

                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        System.out.println("Error: ");
                        System.out.println(e.getMessage());
                    }

                    System.out.println("Counter value: " + counter);
                    counter++;
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        canvas_maze_editor.refreshView();
//                            drawMaze(null);
                    }
                });

                System.out.println("Generation done!");
            }
        };

        new Thread(generateMaze).start();
    }

    public void onSliderMazeEditorChanged(InputEvent inputEvent) {
        canvas_maze_editor.setCellSize(slider_maze_editor_cell_size.getValue());
        canvas_maze_editor.refreshView();
    }

    private void onDragDetected(MazeView mazeView, MouseEvent mouseEvent) {
        selectedCell = SelectedCell.none;
        int x = mazeView.validateMouseX(mouseEvent);
        int y = mazeView.validateMouseY(mouseEvent);
        Cell cell = new Cell(x, y);
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
                if (mazeView.isEndCell(new Cell(x, y))) {
                    // do nothing
                } else {
                    mazeView.setStartCellCoordinates(x, y);
                    System.out.print("Start set to: ");
                    System.out.println(x + "," + y);
                }

                break;
            case end_cell:
                if (mazeView.isStartCell(new Cell(x, y))) {
                    // do nothing
                } else {
                    mazeView.setEndCellCoordinates(x, y);
                    System.out.print("End set to: ");
                    System.out.println(x + "," + y);
                }
                break;
            case none:
                System.out.println("none is selected");
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
