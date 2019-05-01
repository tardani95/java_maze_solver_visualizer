package maze_solver.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class MazeView extends Canvas {

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


    //    public MazeView(double width, double height) {
//        super(width, height);
//    }


}
