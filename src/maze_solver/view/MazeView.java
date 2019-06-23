package maze_solver.view;

import javafx.css.Styleable;
import javafx.event.EventDispatchChain;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import maze_solver.model.Cell;
import maze_solver.model.Maze;

import java.util.Set;

public class MazeView extends Canvas {

    private Maze maze;
    private boolean showWalls;
    private boolean showExploredWalls;
    private boolean showRobot;
    private boolean showRobotOrientation;
    private boolean showStartCell;
    private boolean showCurrentCell;
    private boolean showDestinationCell;
    private boolean showVisitedCells;
    private boolean showExploredCells;

    private GraphicsContext gc = this.getGraphicsContext2D();
    private boolean gcTranslationSet = false;
    private double cell_size = 30.0;

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public Maze getMaze() {
        return maze;
    }

    public void setVisibility(boolean showWalls,
                              boolean showExploredWalls,
                              boolean showRobot,
                              boolean showRobotOrientation,
                              boolean showStartCell,
                              boolean showCurrentCell,
                              boolean showDestinationCell,
                              boolean showVisitedCells,
                              boolean showExploredCells) {
        showWalls(showWalls);
        showExploredWalls(showExploredWalls);
        showRobot(showRobot);
        showRobotOrientation(showRobotOrientation);
        showStartCell(showStartCell);
        showCurrentCell(showCurrentCell);
        showDestinationCell(showDestinationCell);
        showVisitedCells(showVisitedCells);
        showExploredCells(showExploredCells);
    }

    @SuppressWarnings("WeakerAccess")
    public void showWalls(boolean showWalls) {
        this.showWalls = showWalls;
    }

    @SuppressWarnings("WeakerAccess")
    public void showExploredWalls(boolean showExploredWalls) {
        this.showExploredWalls = showExploredWalls;
    }

    @SuppressWarnings("WeakerAccess")
    public void showRobot(boolean showRobot) {
        this.showRobot = showRobot;
    }

    @SuppressWarnings("WeakerAccess")
    public void showRobotOrientation(boolean showRobotOrientation) {
        this.showRobotOrientation = showRobotOrientation;
    }

    @SuppressWarnings("WeakerAccess")
    public void showStartCell(boolean showStartCell) {
        this.showStartCell = showStartCell;
    }

    @SuppressWarnings("WeakerAccess")
    public void showCurrentCell(boolean showCurrentCell) {
        this.showCurrentCell = showCurrentCell;
    }

    @SuppressWarnings("WeakerAccess")
    public void showDestinationCell(boolean showDestinationCell) {
        this.showDestinationCell = showDestinationCell;
    }

    @SuppressWarnings("WeakerAccess")
    public void showVisitedCells(boolean showVisitedCells) {
        this.showVisitedCells = showVisitedCells;
    }

    @SuppressWarnings("WeakerAccess")
    public void showExploredCells(boolean showExploredCells) {
        this.showExploredCells = showExploredCells;
    }

    public void setCellSize(double cell_size) {
        this.cell_size = cell_size;
    }

    public void setGraphicContextTranslation() {
        if (!gcTranslationSet) {
            gc.translate(1, 1);
        }
    }

    public void refreshView() {
        clearView();

        if (showExploredCells) {
            drawExploredCells();
        }
        if (showVisitedCells) {
            drawVisitedCells();
        }
        if (showStartCell) {
            drawStartCell();
        }
        if (showDestinationCell) {
            drawDestinationCell();
        }
        if (showCurrentCell) {
            drawCurrentCell();
        }

        if (showWalls && showExploredWalls) {
            drawWalls(Color.RED, 2);
            drawExploredWalls(Color.BLACK, 3);
        }

        if (showWalls && (!showExploredWalls)) {
            drawWalls(Color.BLACK, 1);
        }

        if (!showWalls && showExploredWalls) {
            drawExploredWalls(Color.BLACK, 2);
        }

        if (showRobot) {
//        drawRobot(showRobotOrientation)
        }
    }

    private void clearView() {
////        System.out.println("clearView()");
        double width = cell_size * maze.getLength_X() + 2;
        double height = cell_size * maze.getLength_Y() + 2;
        this.setWidth(width);
        this.setHeight(height);
        gc.clearRect(0, 0, widthProperty().doubleValue(), heightProperty().doubleValue());
    }

    private void drawRect(Cell point, Color color, double padding) {
        gc.setFill(color);
        gc.fillRect(point.getX() * cell_size + padding, point.getY() * cell_size + padding, cell_size - 2 * padding
                , cell_size - 2 * padding);
    }

    private void drawStartCell() {
        if (maze.getStart() != null) {
            drawRect(maze.getStart(), Color.GREENYELLOW, 0);
        }
    }

    private void drawCurrentCell() {
        if (maze.getCurrent() != null) {
            drawRect(maze.getCurrent(), Color.GREEN, cell_size / 30 * 5);
        }
    }

    private void drawDestinationCell() {
        if (maze.getGoal() != null) {
            drawRect(maze.getGoal(), Color.ORANGERED, 0);
        }
    }

    private void drawVisitedCells() {
        drawCellsStatus(Color.GRAY, 0);
    }

    private void drawExploredCells() {
        drawCellsStatus(Color.BEIGE, 0);
    }

    private void drawCellsStatus(Color color, double padding) {
        for (int x = 0; x < maze.getLength_X(); x++) {
            for (int y = 0; y < maze.getLength_Y(); y++) {
                Cell p = new Cell(x, y);
                if (maze.getCell(p).isVisited()) {
                    drawRect(p, color, padding);
                }
            }
        }
    }

    private void drawWalls(Color wallColor, double lineWidth) {
        drawMazeWalls(maze.getWalls(), wallColor, lineWidth);
    }

    private void drawExploredWalls(Color wallColor, double lineWidth) {
        drawMazeWalls(maze.getExploredWalls(), wallColor, lineWidth);
    }

    private void drawMazeWalls(int[][] mazeWalls, Color color, double lineWidth) {
        double startX;
        double startY;
        double stopX;
        double stopY;

        gc.setLineWidth(lineWidth);
        gc.setStroke(color);
        //print horizontal walls
        for (int row_i = 0; row_i <= maze.getLength_Y(); row_i++) {
            for (int coll_i = 0; coll_i < maze.getLength_X(); coll_i++) {

                if (((mazeWalls[0][row_i] & (0b1 << coll_i))>>>coll_i) > 0) {
                    startX = coll_i * cell_size;
                    stopX = (coll_i + 1) * cell_size;
                    startY = row_i * cell_size;
                    stopY = startY;

                    gc.strokeLine(startX, startY, stopX, stopY);
                }
            }
        }

        //print vertical walls
        for (int coll_i = 0; coll_i <= maze.getLength_X(); coll_i++) {
            for (int row_i = 0; row_i < maze.getLength_Y(); row_i++) {

                if (((mazeWalls[1][coll_i] & (0b1 << row_i))>>>row_i) > 0) {
                    startX = coll_i * cell_size;
                    stopX = startX;
                    startY = row_i * cell_size;
                    stopY = (row_i + 1) * cell_size;

                    gc.strokeLine(startX, startY, stopX, stopY);
                }
            }
        }
    }

    public void setStartCellCoordinates(int x, int y) {
        if (maze.getStart() != null) {
            maze.getStart().modifyXY(x, y);
        }
        if(maze.getCurrent() != null){
            maze.getCurrent().modifyXY(x, y);
        }
    }

    public void setEndCellCoordinates(int x, int y) {
        if (maze.getGoal() != null) {
            maze.getGoal().modifyXY(x, y);
        }
    }

    public boolean isStartCell(Cell cell) {
        if (maze.getStart() != null) {
            return maze.getStart().equals(cell);
        }
        return false;
    }

    public boolean isEndCell(Cell cell) {
        if (maze.getGoal() != null) {
            return maze.getGoal().equals(cell);
        }
        return false;
    }

    public int validateMouseX(MouseEvent mouseEvent) {
        int max_x = maze.getLength_X() - 1;
        int x = (int) (mouseEvent.getX() / cell_size);
        if (x < 0) {
            x = 0;
        }
        if (x > max_x) {
            x = max_x;
        }
        return x;
    }

    public int validateMouseY(MouseEvent mouseEvent) {
        int max_y = maze.getLength_Y() - 1;
        int y = (int) (mouseEvent.getY() / cell_size);
        if (y < 0) {
            y = 0;
        }
        if (y > max_y) {
            y = max_y;
        }
        return y;
    }


    public MazeView() {
        super();
    }

    public MazeView(double width, double height) {
        super(width, height);
    }

    @Override
    public GraphicsContext getGraphicsContext2D() {
        return super.getGraphicsContext2D();
    }

    @Override
    public boolean hasProperties() {
        return super.hasProperties();
    }

    @Override
    public void setUserData(Object value) {
        super.setUserData(value);
    }

    @Override
    public Object getUserData() {
        return super.getUserData();
    }

    @Override
    public Node lookup(String selector) {
        return super.lookup(selector);
    }

    @Override
    public Set<Node> lookupAll(String selector) {
        return super.lookupAll(selector);
    }

    @Override
    public void toBack() {
        super.toBack();
    }

    @Override
    public void toFront() {
        super.toFront();
    }

    @Override
    public WritableImage snapshot(SnapshotParameters params, WritableImage image) {
        return super.snapshot(params, image);
    }

    @Override
    public void snapshot(Callback<SnapshotResult, Void> callback, SnapshotParameters params, WritableImage image) {
        super.snapshot(callback, params, image);
    }

    @Override
    public Dragboard startDragAndDrop(TransferMode... transferModes) {
        return super.startDragAndDrop(transferModes);
    }

    @Override
    public void startFullDrag() {
        super.startFullDrag();
    }

    @Override
    public void relocate(double x, double y) {
        super.relocate(x, y);
    }

    @Override
    public boolean isResizable() {
        return super.isResizable();
    }

    @Override
    public Orientation getContentBias() {
        return super.getContentBias();
    }

    @Override
    public double minWidth(double height) {
        return super.minWidth(height);
    }

    @Override
    public double minHeight(double width) {
        return super.minHeight(width);
    }

    @Override
    public double prefWidth(double height) {
        return super.prefWidth(height);
    }

    @Override
    public double prefHeight(double width) {
        return super.prefHeight(width);
    }

    @Override
    public double maxWidth(double height) {
        return super.maxWidth(height);
    }

    @Override
    public double maxHeight(double width) {
        return super.maxHeight(width);
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
    }

    @Override
    public void resizeRelocate(double x, double y, double width, double height) {
        super.resizeRelocate(x, y, width, height);
    }

    @Override
    public double getBaselineOffset() {
        return super.getBaselineOffset();
    }

    @Override
    public double computeAreaInScreen() {
        return super.computeAreaInScreen();
    }

    @Override
    public boolean contains(double localX, double localY) {
        return super.contains(localX, localY);
    }

    @Override
    public boolean contains(Point2D localPoint) {
        return super.contains(localPoint);
    }

    @Override
    public boolean intersects(double localX, double localY, double localWidth, double localHeight) {
        return super.intersects(localX, localY, localWidth, localHeight);
    }

    @Override
    public boolean intersects(Bounds localBounds) {
        return super.intersects(localBounds);
    }

    @Override
    public Point2D screenToLocal(double screenX, double screenY) {
        return super.screenToLocal(screenX, screenY);
    }

    @Override
    public Point2D screenToLocal(Point2D screenPoint) {
        return super.screenToLocal(screenPoint);
    }

    @Override
    public Bounds screenToLocal(Bounds screenBounds) {
        return super.screenToLocal(screenBounds);
    }

    @Override
    public Point2D sceneToLocal(double x, double y, boolean rootScene) {
        return super.sceneToLocal(x, y, rootScene);
    }

    @Override
    public Point2D sceneToLocal(Point2D point, boolean rootScene) {
        return super.sceneToLocal(point, rootScene);
    }

    @Override
    public Bounds sceneToLocal(Bounds bounds, boolean rootScene) {
        return super.sceneToLocal(bounds, rootScene);
    }

    @Override
    public Point2D sceneToLocal(double sceneX, double sceneY) {
        return super.sceneToLocal(sceneX, sceneY);
    }

    @Override
    public Point2D sceneToLocal(Point2D scenePoint) {
        return super.sceneToLocal(scenePoint);
    }

    @Override
    public Point3D sceneToLocal(Point3D scenePoint) {
        return super.sceneToLocal(scenePoint);
    }

    @Override
    public Point3D sceneToLocal(double sceneX, double sceneY, double sceneZ) {
        return super.sceneToLocal(sceneX, sceneY, sceneZ);
    }

    @Override
    public Bounds sceneToLocal(Bounds sceneBounds) {
        return super.sceneToLocal(sceneBounds);
    }

    @Override
    public Point2D localToScreen(double localX, double localY) {
        return super.localToScreen(localX, localY);
    }

    @Override
    public Point2D localToScreen(Point2D localPoint) {
        return super.localToScreen(localPoint);
    }

    @Override
    public Point2D localToScreen(double localX, double localY, double localZ) {
        return super.localToScreen(localX, localY, localZ);
    }

    @Override
    public Point2D localToScreen(Point3D localPoint) {
        return super.localToScreen(localPoint);
    }

    @Override
    public Bounds localToScreen(Bounds localBounds) {
        return super.localToScreen(localBounds);
    }

    @Override
    public Point2D localToScene(double localX, double localY) {
        return super.localToScene(localX, localY);
    }

    @Override
    public Point2D localToScene(Point2D localPoint) {
        return super.localToScene(localPoint);
    }

    @Override
    public Point3D localToScene(Point3D localPoint) {
        return super.localToScene(localPoint);
    }

    @Override
    public Point3D localToScene(double x, double y, double z) {
        return super.localToScene(x, y, z);
    }

    @Override
    public Point3D localToScene(Point3D localPoint, boolean rootScene) {
        return super.localToScene(localPoint, rootScene);
    }

    @Override
    public Point3D localToScene(double x, double y, double z, boolean rootScene) {
        return super.localToScene(x, y, z, rootScene);
    }

    @Override
    public Point2D localToScene(Point2D localPoint, boolean rootScene) {
        return super.localToScene(localPoint, rootScene);
    }

    @Override
    public Point2D localToScene(double x, double y, boolean rootScene) {
        return super.localToScene(x, y, rootScene);
    }

    @Override
    public Bounds localToScene(Bounds localBounds, boolean rootScene) {
        return super.localToScene(localBounds, rootScene);
    }

    @Override
    public Bounds localToScene(Bounds localBounds) {
        return super.localToScene(localBounds);
    }

    @Override
    public Point2D parentToLocal(double parentX, double parentY) {
        return super.parentToLocal(parentX, parentY);
    }

    @Override
    public Point2D parentToLocal(Point2D parentPoint) {
        return super.parentToLocal(parentPoint);
    }

    @Override
    public Point3D parentToLocal(Point3D parentPoint) {
        return super.parentToLocal(parentPoint);
    }

    @Override
    public Point3D parentToLocal(double parentX, double parentY, double parentZ) {
        return super.parentToLocal(parentX, parentY, parentZ);
    }

    @Override
    public Bounds parentToLocal(Bounds parentBounds) {
        return super.parentToLocal(parentBounds);
    }

    @Override
    public Point2D localToParent(double localX, double localY) {
        return super.localToParent(localX, localY);
    }

    @Override
    public Point2D localToParent(Point2D localPoint) {
        return super.localToParent(localPoint);
    }

    @Override
    public Point3D localToParent(Point3D localPoint) {
        return super.localToParent(localPoint);
    }

    @Override
    public Point3D localToParent(double x, double y, double z) {
        return super.localToParent(x, y, z);
    }

    @Override
    public Bounds localToParent(Bounds localBounds) {
        return super.localToParent(localBounds);
    }

    @Override
    public boolean usesMirroring() {
        return super.usesMirroring();
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return super.buildEventDispatchChain(tail);
    }

    @Override
    public String getTypeSelector() {
        return super.getTypeSelector();
    }

    @Override
    public Styleable getStyleableParent() {
        return super.getStyleableParent();
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        return super.queryAccessibleAttribute(attribute, parameters);
    }

    @Override
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        super.executeAccessibleAction(action, parameters);
    }
}
