package maze_solver.model;

import static java.lang.Math.*;

public class Cell {
    protected int x;
    protected int y;

    private boolean explored;
    private boolean visited;
    private double cost;
    /**
     * can be removed and calculated during the path finding algorithms
     */
    private double destination_distance;
    protected CellType type;
    private Cell parent;

    /*******CONSTRUCTORS*******/

    private Cell(int x, int y, boolean explored, boolean visited,
                 double cost, double destination_distance,
                 CellType cellType, Cell parent) {
        this.x = x;
        this.y = y;
        this.explored = explored;
        this.visited = visited;
        this.cost = cost;
        this.destination_distance = destination_distance;
        this.type = cellType;
        this.parent = parent;
    }

    public Cell(int x, int y, CellType cellType) {
        this(x, y, false, false,
                Double.MAX_VALUE, Double.MAX_VALUE,
                cellType, null);
    }

    public Cell(int x, int y) {
        this(x, y, CellType.DEFAULT);
    }

    /*******GETTERS AND SETTERS*******/

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getCost() {
        return cost;
    }

    public double getDestination_distance() {
        return destination_distance;
    }

    public Cell getParent() {
        return parent;
    }

    public boolean isExplored() {
        return explored;
    }

    public boolean isVisited() {
        return visited;
    }

    private boolean isCellType(CellType cellType) {
        return type.equals(cellType);
    }

    // TODO: test result
    public boolean isStart() {
        return isCellType(CellType.START);
    }

    // TODO: test result
    public boolean isDestination() {
        return isCellType(CellType.DESTINATION);
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }

    public void setExplored() {
        setExplored(true);
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setVisited() {
        setVisited(true);
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setDestination_distance(double destination_distance) {
        this.destination_distance = destination_distance;
    }

    private void setType(CellType type) {
        this.type = type;
    }

    public void setTypeStart() {
        setType(CellType.START);
    }

    public void setTypeDestination() {
        setType(CellType.DESTINATION);
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }


    /*******OVERRIDDEN FUNCTIONS*******/

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }

        Cell c;

        if(obj instanceof Cell){
            c = (Cell) obj;
        }else{
            return false;
        }

        return (this.x == c.x && this.y == c.y);
    }

    /*******FUNCTIONS*******/


    public double distanceTo(int x, int y, Heuristic h) {
        switch (h) {
            case MANHATTAN:
                return abs(this.x - x) + abs(this.y - y);
            case EUCLIDEAN:
                return sqrt(pow(this.x - x, 2) + pow(this.y - y, 2));
            default:
                System.err.println(h.name() + " distance heuristic not implemented, returning -1.0");
                return -1.0;
        }
    }

    public double distanceTo(Cell c, Heuristic h){
        return distanceTo(c.x,c.y,h);
    }

    public double distanceTo(Cell c) {
        return distanceTo(c, Heuristic.EUCLIDEAN);
    }

    private boolean modifyX(int x) {
        boolean success = false;
        if (!isCellType(CellType.DEFAULT)) {
            this.x = x;
            success = true;
        }
        return success;
    }

    private boolean modifyY(int y) {
        boolean success = false;
        if (!isCellType(CellType.DEFAULT)) {
            this.y = y;
            success = true;
        }
        return success;
    }

    public void modifyXY(int x, int y) {
        if (modifyX(x) && modifyY(y)) {
            System.out.println(type.name() + " coordinates modified");
        }
    }

    public void modifyXY(Cell c){
        modifyXY(c.x,c.y);
    }



    /*******ENUMS*******/

    public enum CellType {
        DEFAULT,
        CURRENT,
        NEXT,
        START,
        DESTINATION
    }
}
