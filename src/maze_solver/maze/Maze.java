package maze_solver.maze;

public class Maze {

    private static final int RIGHT_WALL = 0;
    private static final int TOP_WALL = 1;
    private static final int LEFT_WALL = 2;
    private static final int BOTTOM_WALL = 4;
    private static final int X_DIR = 0;
    private static final int Y_DIR = 1;
    int cols;
    int rows;
    Point2D start;
    Point2D end;
    Point2D current;

    int[][] maze_walls;
    int[][] explored_maze_walls;

    Cell[][] cell;

    public Maze(){
        this(5,4,new Point2D(0,0), new Point2D(3,4),null,new Cell[5][4]);
        initMazeWalls();
        System.out.println("initial maze created");
        current = start;
        System.out.println("current point set to start point");
    }

    public Maze(int cols, int rows, Point2D start, Point2D end, Point2D current, Cell[][] cell) {
        this.cols = cols;
        this.rows = rows;
        this.start = start;
        this.end = end;
        this.current = current;
        this.cell = cell;
    }

    public void initMazeWalls(){
        int max_size = Integer.max(cols,rows) +1;
        maze_walls = new int[2][max_size];
        explored_maze_walls = new int[2][max_size];

        /* X DIR walls */
        explored_maze_walls[X_DIR][0] = 0b11111;
        explored_maze_walls[X_DIR][4] = 0b11111;
        maze_walls[X_DIR][0] = 0b11111;
        maze_walls[X_DIR][1] = 0b01110;
        maze_walls[X_DIR][2] = 0b00100;
        maze_walls[X_DIR][3] = 0b10010;
        maze_walls[X_DIR][4] = 0b11111;

        /* Y DIR walls */
        explored_maze_walls[Y_DIR][0] = 0b1111;
        explored_maze_walls[Y_DIR][5] = 0b1111;
        maze_walls[Y_DIR][5] = 0b1111;
        maze_walls[Y_DIR][4] = 0b0010;
        maze_walls[Y_DIR][3] = 0b0100;
        maze_walls[Y_DIR][2] = 0b0010;
        maze_walls[Y_DIR][1] = 0b0111;
        maze_walls[Y_DIR][0] = 0b1111;
    }

    public boolean getWall(int x, int y, int wall_type){
        switch (wall_type){
            case LEFT_WALL:{
                return (maze_walls[Y_DIR][x]>>y == 0b1);
            }
            case RIGHT_WALL:{
                return (maze_walls[Y_DIR][x+1]>>y == 0b1);
            }
            case BOTTOM_WALL:{
                return (maze_walls[X_DIR][y]>>x == 0b1);
            }
            case TOP_WALL:{
                return (maze_walls[X_DIR][y+1]>>x == 0b1);
            }
            default:
                System.err.println("no wall type was given.");
                return false;
        }
    }
}
