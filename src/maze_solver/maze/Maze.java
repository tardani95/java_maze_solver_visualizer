package maze_solver.maze;


public class Maze {

    private static Maze instance = new Maze();

    public static final int RIGHT_WALL = 0;
    public static final int TOP_WALL = 1;
    public static final int LEFT_WALL = 2;
    public static final int BOTTOM_WALL = 3;
    private static final int X_DIR = 0;
    private static final int Y_DIR = 1;
    private int cols;
    private int rows;
    Point2D start;
    Point2D end;
    Point2D current;

    int[][] walls;
    int[][] explored_maze_walls;

    public Cell[][] cell;

    public static Maze getInstance() {
        return instance;
    }

    private Maze() {
        this(5, 4, new Point2D(0, 0), new Point2D(1, 0), null, new Cell[5][4]);
        initMazeWalls();
        System.out.println("initial maze created");
        current = new Point2D(start);
        System.out.println("current point set to start point");
    }

    public Maze(int cols, int rows, Point2D start, Point2D end, Point2D current, Cell[][] cell) {
        this.cols = cols;
        this.rows = rows;
        this.start = start;
        this.end = end;
        this.current = current;
        this.cell = cell;
        initCells();
    }

    private void initCells(){
        Cell c = null;
        for(int x = 0; x<cols; x++){
            for(int y = 0; y < rows; y++){
                c = cell[x][y] = new Cell(x,y);
                c.setDestination_distance( (int) (new Point2D(x,y)).distanceTo(end,0));
            }
        }
        cell[start.x][start.y].setCost(0);


    }

    public void initMazeWalls() {
        int max_size = Integer.max(cols, rows) + 1;
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

    public boolean getWall(int[][] walls, Point2D point, int wall_type) {
        return getWall(walls, point.x, point.y, wall_type);
    }

    public boolean getWall(int[][] walls, int x, int y, int wall_type) {
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

    public boolean setWall(int[][] walls, Point2D point, int wall_type) {
        return setWall(walls, point.x, point.y, wall_type);
    }

    public boolean setWall(int[][] walls, int x, int y, int wall_type) {
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

    public Point2D getStart() {
        return start;
    }

    public Point2D getEnd() {
        return end;
    }

    public Point2D getCurrent() {
        return current;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public int[][] getWalls() {
        return walls;
    }

    public int[][] getExploredWalls() {
        return explored_maze_walls;
    }

    public void pathFind(Point2D start, Point2D end, int type, int heuristic) {
        if (start != null) {
            this.start = start;
        }
        if (end != null) {
            this.end = end;
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

    public void calcDistances(Point2D p, int heuristic) {
        switch (heuristic) {
            case 0: {
                System.out.println("Euler heuristic distance calculation");


            }
            break;
            case 1: {
                System.out.println("Manhattam heuristic distance calculation");
            }
            break;
            default:
                System.err.println("not implemented heuristic");
                return;
        }
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                cell[i][j].setDestination_distance((int) p.distanceTo(i, j, heuristic));
            }
        }
        System.out.println("distances calculated");

    }

    public void initCost() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                cell[i][j].setCost(Integer.MAX_VALUE);
            }
        }
        System.out.println("initial cost set to integer max value");
    }

    public boolean bestDepthFirstSearch(Point2D point2D) {
        return bestDepthFirstSearch(point2D.x, point2D.y);
    }

    public boolean bestDepthFirstSearch(int x, int y) {
        current.x = x;
        current.y = y;

        // set actual node as visited
        cell[current.x][current.y].setVisited();

        // if end point reached then terminate
        if (current.equals(end)) {
            return true;
        }

        // discover nearby walls
        discoverWalls(current);

        // calculates the following best move
        Point2D nextPosition = calcNextPosition(current);

        // set next move cell parent
        if(!cell[nextPosition.x][nextPosition.y].isVisited()){
            cell[nextPosition.x][nextPosition.y].setParent(cell[current.x][current.y]);
        }

        /*try*/
        current.x = nextPosition.x;
        current.y = nextPosition.y;
        return  false;

        /*end of try*/
//        return bestDepthFirstSearch(nextPosition);
    }


    public void discoverWalls(Point2D current) {
        for (int wallType = 0; wallType < 4; ++wallType) {
            if (getWall(walls, current, wallType)) {
                setWall(explored_maze_walls, current, wallType);
            }
        }
    }

    private Point2D calcNextPosition(Point2D current) {
        if (current == null) {
            return null;
        }
        if (current.equals(end)) {
            return end;
        }

        int currentCost = cell[current.x][current.y].getCost();
        int min_distance = 10000;

        Point2D nextPosition = null;
        Point2D p = null;

        for (int wallType = 0; wallType < 4; ++wallType) {
            if (!getWall(explored_maze_walls, current, wallType)) {
                switch (wallType) {
                    //TODO - check equality operator with doubles
                    case 0: {
                        p = new Point2D(current.x + 1, current.y);
                        if(cell[p.x][p.y].isVisited()){
                            p = null;
                        }
                    }
                    break;
                    case 1: {
                        p = new Point2D(current.x, current.y + 1);
                        if(cell[p.x][p.y].isVisited()){
                            p = null;
                        }
                    }
                    break;
                    case 2: {
                        if (current.x > 0) {
                            p = new Point2D(current.x - 1, current.y);
                            if(cell[p.x][p.y].isVisited()){
                                p = null;
                            }
                        } else {
                            continue;
                        }
                    }
                    break;
                    case 3: {
                        if (current.y > 0) {
                            p = new Point2D(current.x, current.y - 1);
                            if(cell[p.x][p.y].isVisited()){
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
                    if (cell[p.x][p.y].getCost() > (currentCost + 1)) {
                        cell[p.x][p.y].setCost(currentCost + 1);
                        cell[p.x][p.y].setParent(cell[current.x][current.y]);
                    }

                    if (cell[p.x][p.y].getDestination_distance() < min_distance) {
                        nextPosition = p;
                        min_distance = cell[p.x][p.y].getDestination_distance();
                    }
                }
            }
        }

        if(nextPosition == null){
            Cell parent = cell[current.x][current.y].getParent();
            return parent.getCoordinate();
        }


        return nextPosition;
    }
}
