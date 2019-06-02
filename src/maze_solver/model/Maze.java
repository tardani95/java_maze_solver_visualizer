package maze_solver.model;

public class Maze {

    private static final int X_DIR = 0;
    private static final int Y_DIR = 1;
    private int length_X;
    private int length_Y;

    private StartCell start;
    private DestinationCell destination;
    private CurrentCell current;

    private int[][] walls;
    private int[][] explored_maze_walls;

    private Cell[][] cell;

    public Maze() {
        this(5, 4, new StartCell(0, 0), new DestinationCell(1, 0), new CurrentCell(0,0), new Cell[5][4]);
        initMazeWalls();
        System.out.println("initial maze created");
        current.setCoordinatesToCell(start);
        System.out.println("current point set to start point");
    }

    private Maze(int cols, int rows, StartCell start, DestinationCell destination, CurrentCell current, Cell[][] cell) {
        this.length_X = cols;
        this.length_Y = rows;
        this.start = start;
        this.destination = destination;
        this.current = current;
        this.cell = cell;
        initCells();
    }

    private void initCells() {
        Cell c;
        for (int x = 0; x < length_X; x++) {
            for (int y = 0; y < length_Y; y++) {
                c = cell[x][y] = new Cell(x, y);
                c.setDestination_distance(c.distanceTo(destination));
            }
        }
        cell[start.x][start.y].setCost(0);
    }

    private void initMazeWalls() {
        int max_size = Integer.max(length_X, length_Y) + 1;
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

    public Cell getDestination() {
        return destination;
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
        return cell[onCoordinate.x][onCoordinate.y];
    }

    public void pathFind(StartCell start, DestinationCell end, int type, Heuristic heuristic) {
        if (start != null) {
            this.start = start;
        }
        if (end != null) {
            this.destination = end;
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
                cell[i][j].setDestination_distance((int) p.distanceTo(i, j, heuristic));
            }
        }
        System.out.println("distances calculated");

    }

    public void initCost() {
        for (int i = 0; i < length_X; i++) {
            for (int j = 0; j < length_Y; j++) {
                cell[i][j].setCost(Integer.MAX_VALUE);
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
        cell[current.x][current.y].setVisited();

        // if destination point reached then terminate
        if (current.equals(destination)) {
            return true;
        }

        // discover nearby walls
        discoverWalls(current);

        // calculates the following best move
        Cell nextPosition = calcNextPosition(current);

        // set next move cell parent
        if (!cell[nextPosition.x][nextPosition.y].isVisited()) {
            cell[nextPosition.x][nextPosition.y].setParent(cell[current.x][current.y]);
        }

        /*try*/
        current.x = nextPosition.x;
        current.y = nextPosition.y;
        return false;

        /*destination of try*/
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
        if (current.equals(destination)) {
            return destination;
        }

        double currentCost = cell[current.x][current.y].getCost();
        double min_distance = 10000;

        Cell nextPosition = null;
        Cell p = null;

        for (WallType wallType : WallType.values()) {
            if (!getWall(explored_maze_walls, current, wallType)) {
                switch (wallType) {
                    //TODO - check equality operator with doubles
                    case RIGHT_WALL: {
                        p = new Cell(current.x + 1, current.y);
                        if (cell[p.x][p.y].isVisited()) {
                            p = null;
                        }
                    }
                    break;
                    case TOP_WALL: {
                        p = new Cell(current.x, current.y + 1);
                        if (cell[p.x][p.y].isVisited()) {
                            p = null;
                        }
                    }
                    break;
                    case LEFT_WALL: {
                        if (current.x > 0) {
                            p = new Cell(current.x - 1, current.y);
                            if (cell[p.x][p.y].isVisited()) {
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
                            if (cell[p.x][p.y].isVisited()) {
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

        if (nextPosition == null) {
            return cell[current.x][current.y].getParent();
        }


        return nextPosition;
    }

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
