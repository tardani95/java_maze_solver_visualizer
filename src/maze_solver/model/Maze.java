package maze_solver.model;

import java.util.Random;

public class Maze {
    /**
     * maximum maze side length in micromouse competition
     * [0:32)
     */
    public static final int MAX_MAZE_SIZE = 32;
    private static final int X_DIR = 0;
    private static final int Y_DIR = 1;
    private static final int DIR_SIZE = 2;
    private int length_X;
    private int length_Y;

    private StartCell start;
    private StartCell generationStart;
    private GoalCell goal;
    private CurrentCell current;

    private int[][] walls;
    private int[][] explored_maze_walls;
    private int[][] explored_maze_walls_for_view;

    private Cell[][] cells;

    /*=============CONSTRUCTORS=============*/

    /**
     * Default Constructor
     * It generates a 5x4 maze.
     */
    public Maze() {
        initDefaultMaze();
    }

    /**
     * Default Constructor for maze generation
     *
     * @param cols            Column number (<=@value {@link #MAX_MAZE_SIZE})
     * @param rows            Row number (<=@value {@link #MAX_MAZE_SIZE})
     * @param generationStart StartCell from where the algorithm starts the generation
     * @param wallsEmpty      True to generate only sidewalls for the maze
     */
    public Maze(int cols, int rows, StartCell generationStart, boolean wallsEmpty) {
        if (cols > MAX_MAZE_SIZE || rows > MAX_MAZE_SIZE) {
            cols = MAX_MAZE_SIZE;
            rows = MAX_MAZE_SIZE;
        }
        this.length_X = cols;
        this.length_Y = rows;

        if (generationStart == null || (generationStart.x > MAX_MAZE_SIZE || generationStart.y > MAX_MAZE_SIZE)) {
            Random rand = new Random();
            this.generationStart = new StartCell(rand.nextInt(cols), rand.nextInt(rows));
        } else {
            this.generationStart = generationStart;
        }
        this.walls = wallsEmpty ? newWallArray(cols, rows) : newWallArrayWithAllWalls(cols, rows);
        this.explored_maze_walls = newWallArrayWithAllWalls(cols, rows);
        this.explored_maze_walls_for_view = newWallArray(cols, rows);
        this.cells = newCellArray();
    }

    /**
     * Simplified Constructor for maze generation
     * It randomizes the start cell for generation and sets all walls.
     *
     * @param cols Column number (<=@value {@link #MAX_MAZE_SIZE})
     * @param rows Row number (<=@value {@link #MAX_MAZE_SIZE})
     */
    public Maze(int cols, int rows) {
        this(cols, rows, null, false);
    }

    /*=============END OF CONSTRUCTORS=============*/

    public boolean getWall(int[][] walls, Cell c, WallType wall_type) {
        return getWall(walls, c.x, c.y, wall_type);
    }

    public boolean getWall(int[][] walls, int x, int y, WallType wall_type) {
        switch (wall_type) {
            case LEFT_WALL: {
                return ((walls[Y_DIR][x] >> y & 0b1) == 0b1);
            }
            case RIGHT_WALL: {
                return ((walls[Y_DIR][x + 1] >> y & 0b1) == 0b1);
            }
            case BOTTOM_WALL: {
                return ((walls[X_DIR][y] >> x & 0b1) == 0b1);
            }
            case TOP_WALL: {
                return ((walls[X_DIR][y + 1] >> x & 0b1) == 0b1);
            }
            default:
                System.err.println("no wall type was given.");
                return false;
        }
    }

    public boolean setWall(int[][] walls, Cell c, WallType wall_type) {
        return setWall(walls, c.x, c.y, wall_type);
    }

    public boolean setWall(int[][] walls, int x, int y, WallType wall_type) {
        switch (wall_type) {
            case LEFT_WALL: {
                walls[Y_DIR][x] |= (0b1 << y);
            }
            break;
            case RIGHT_WALL: {
                walls[Y_DIR][x + 1] |= (0b1 << y);

            }
            break;
            case BOTTOM_WALL: {
                walls[X_DIR][y] |= (0b1 << x);
            }
            break;
            case TOP_WALL: {
                walls[X_DIR][y + 1] |= (0b1 << x);
            }
            break;
            default:
                System.err.println("invalid wall type");
                return false;
        }
        return true;
    }

    public Cell getStart() {
        return start;
    }

    public Cell getGoal() {
        return goal;
    }

    public Cell getCurrent() {
        return current;
    }

    public int getLength_X() {
        return length_X;
    }

    public int getLength_Y() {
        return length_Y;
    }

    public int[][] getWalls() {
        return walls;
    }

    public int[][] getExploredWalls() {
        return explored_maze_walls;
    }

    public Cell getCell(Cell onCoordinate) {
        return cells[onCoordinate.x][onCoordinate.y];
    }

    public void pathFind(StartCell start, GoalCell end, int type, Heuristic heuristic) {
        if (start != null) {
            this.start = start;
        }
        if (end != null) {
            this.goal = end;
        }

        calcDistances(end, heuristic);
        initCost();

        switch (type) {
            case 0: {
                bestDepthFirstSearch(start);
            }
            break;
            case 1: {
                System.out.println("A* not implemented");
            }
            break;
            default:
                System.err.println("no such a path finding method");
                break;
        }
    }

    public void calcDistances(Cell p, Heuristic heuristic) {
        switch (heuristic) {
            case EUCLIDEAN: {
                System.out.println("Euler heuristic distance calculation");


            }
            break;
            case MANHATTAN: {
                System.out.println("Manhattam heuristic distance calculation");
            }
            break;
            default:
                System.err.println("not implemented heuristic");
                return;
        }
        for (int i = 0; i < length_X; i++) {
            for (int j = 0; j < length_Y; j++) {
                cells[i][j].setDestination_distance((int) p.distanceTo(i, j, heuristic));
            }
        }
        System.out.println("distances calculated");

    }

    public void initCost() {
        for (int i = 0; i < length_X; i++) {
            for (int j = 0; j < length_Y; j++) {
                cells[i][j].setCost(Integer.MAX_VALUE);
            }
        }
        System.out.println("initial cost set to integer max value");
    }

    public boolean bestDepthFirstSearch(Cell myPoint2D) {
        return bestDepthFirstSearch(myPoint2D.x, myPoint2D.y);
    }

    public boolean bestDepthFirstSearch(int x, int y) {
        current.x = x;
        current.y = y;

        // set actual node as visited
        cells[current.x][current.y].setVisited();

        // if goal point reached then terminate
        if (current.equals(goal)) {
            return true;
        }

        // discover nearby walls
        discoverWalls(current);

        // calculates the following best move
        Cell nextPosition = calcNextPosition(current);

        // set next move cells parent
        if (!cells[nextPosition.x][nextPosition.y].isVisited()) {
            cells[nextPosition.x][nextPosition.y].setParent(cells[current.x][current.y]);
        }

        /*try*/
        current.x = nextPosition.x;
        current.y = nextPosition.y;
        return false;

        /*goal of try*/
//        return bestDepthFirstSearch(nextPosition);
    }


    public void discoverWalls(Cell current) {
        for (WallType wallType : WallType.values()) {
            if (getWall(walls, current, wallType)) {
                setWall(explored_maze_walls, current, wallType);
            }
        }
    }

    private Cell calcNextPosition(Cell current) {
        if (current == null) {
            return null;
        }
        if (current.equals(goal)) {
            return goal;
        }

        double currentCost = cells[current.x][current.y].getCost();
        double min_distance = 10000;

        Cell nextPosition = null;
        Cell p = null;

        for (WallType wallType : WallType.values()) {
            if (!getWall(explored_maze_walls, current, wallType)) {
                switch (wallType) {
                    //TODO - check equality operator with doubles
                    case RIGHT_WALL: {
                        p = new Cell(current.x + 1, current.y);
                        if (cells[p.x][p.y].isVisited()) {
                            p = null;
                        }
                    }
                    break;
                    case TOP_WALL: {
                        p = new Cell(current.x, current.y + 1);
                        if (cells[p.x][p.y].isVisited()) {
                            p = null;
                        }
                    }
                    break;
                    case LEFT_WALL: {
                        if (current.x > 0) {
                            p = new Cell(current.x - 1, current.y);
                            if (cells[p.x][p.y].isVisited()) {
                                p = null;
                            }
                        } else {
                            continue;
                        }
                    }
                    break;
                    case BOTTOM_WALL: {
                        if (current.y > 0) {
                            p = new Cell(current.x, current.y - 1);
                            if (cells[p.x][p.y].isVisited()) {
                                p = null;
                            }
                        } else {
                            continue;
                        }
                    }
                    break;
                    default:
                        break;
                }
                if (p != null) {
                    if (cells[p.x][p.y].getCost() > (currentCost + 1)) {
                        cells[p.x][p.y].setCost(currentCost + 1);
                        cells[p.x][p.y].setParent(cells[current.x][current.y]);
                    }

                    if (cells[p.x][p.y].getDestination_distance() < min_distance) {
                        nextPosition = p;
                        min_distance = cells[p.x][p.y].getDestination_distance();
                    }
                }
            }
        }

        if (nextPosition == null) {
            return cells[current.x][current.y].getParent();
        }


        return nextPosition;
    }

    /*******GETTERS AND SETTERS*******/

    private int getWallsMaxSize(int cols, int rows) {
        return Integer.max(cols, rows) + 1;
    }

    /*******OVERRIDDEN FUNCTIONS*******/


    /*******FUNCTIONS*******/

    private void initDefaultMazeWalls() {
        int max_size = getWallsMaxSize(length_X, length_Y);
        walls = new int[2][max_size];
        explored_maze_walls = new int[2][max_size];

        /* X DIR walls */
        explored_maze_walls[X_DIR][0] = 0b11111;
        explored_maze_walls[X_DIR][4] = 0b11111;
        walls[X_DIR][0] = 0b11111;
        walls[X_DIR][1] = 0b01110;
        walls[X_DIR][2] = 0b00100;
        walls[X_DIR][3] = 0b10010;
        walls[X_DIR][4] = 0b11111;

        /* Y DIR walls */
        explored_maze_walls[Y_DIR][0] = 0b1111;
        explored_maze_walls[Y_DIR][5] = 0b1111;
        walls[Y_DIR][5] = 0b1111;
        walls[Y_DIR][4] = 0b0010;
        walls[Y_DIR][3] = 0b0100;
        walls[Y_DIR][2] = 0b0010;
        walls[Y_DIR][1] = 0b0111;
        walls[Y_DIR][0] = 0b1111;
    }

    private void initDefaultMaze() {
        this.length_X = 5;
        this.length_Y = 4;
        this.start = new StartCell(0, 0);
        this.goal = new GoalCell(1, 0);
        this.current = new CurrentCell(0, 0);
        this.cells = newCellArray(start,goal);

        initDefaultMazeWalls();

        System.out.println("Maze with default parameters created");
        current.setCoordinatesToCell(start);
        System.out.println("current point set to start point");
    }

    private int[][] newWallArray(int cols, int rows) {
        int[][] walls = new int[DIR_SIZE][getWallsMaxSize(cols, rows)];
        for (int i = 0; i < cols; i++) {
            setWall(walls, i, 0, WallType.BOTTOM_WALL);
            setWall(walls, i, rows - 1, WallType.TOP_WALL);
        }

        for (int j = 0; j < rows; j++) {
            setWall(walls, 0, j, WallType.LEFT_WALL);
            setWall(walls, cols - 1, j, WallType.RIGHT_WALL);
        }
        return walls;
    }

    private int[][] newWallArrayWithAllWalls(int cols, int rows) {
        int[][] walls = new int[DIR_SIZE][getWallsMaxSize(cols, rows)];

        for (int x = 0; x <= cols; x++) {
            for (int y = 0; y <= rows; y++) {
                setWall(walls, x, y, WallType.BOTTOM_WALL);
                setWall(walls, x, y, WallType.LEFT_WALL);
            }
        }

        return walls;
    }

    private Cell[][] newCellArray(StartCell start, GoalCell goal) {
        Cell[][] grid = new Cell[length_X][length_Y];
        Cell c;
        for (int x = 0; x < length_X; x++) {
            for (int y = 0; y < length_Y; y++) {
                c = grid[x][y] = new Cell(x, y);
                if (goal != null) {
                    c.setDestination_distance(c.distanceTo(goal));
                }
            }
        }

        if (start != null) {
            grid[start.x][start.y].setCost(0);
        }

        return grid;
    }

    private Cell[][] newCellArray() {
        return newCellArray(null, null);
    }

    private boolean resetWall(int[][] walls, int x, int y, WallType wallType) {
        switch (wallType) {
            case LEFT_WALL: {
                walls[Y_DIR][x] ^= (~(0b1 << y));
            }
            break;
            case RIGHT_WALL: {
                walls[Y_DIR][x + 1] ^= (~(0b1 << y));

            }
            break;
            case BOTTOM_WALL: {
                walls[X_DIR][y] ^= (~(0b1 << x));
            }
            break;
            case TOP_WALL: {
                walls[X_DIR][y + 1] ^= (~(0b1 << x));
            }
            break;
            default:
                System.err.println("invalid wall type");
                return false;
        }
        return true;
    }

    public boolean resetWall(int[][] walls, Cell c, WallType wallType) {
        return resetWall(walls, c.x, c.y, wallType);
    }

    /*******ENUMS*******/

    public enum WallType {
        RIGHT_WALL(0),
        TOP_WALL(1),
        LEFT_WALL(2),
        BOTTOM_WALL(3);

        public final int value;

        WallType(int value) {
            this.value = value;
        }
    }
}
