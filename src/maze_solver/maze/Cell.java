package maze_solver.maze;

public class Cell {

    private boolean explored;
    private boolean visited;
    private int cost;
    private int destination_distance;
    private MyPoint2D coordinate;
    private Cell parent;

    public Cell() {
        this(0, 0);
    }

    public Cell(int x, int y){
        this(new MyPoint2D(x,y));
    }

    public Cell(MyPoint2D p) {
        this(p, false, Integer.MAX_VALUE, Integer.MAX_VALUE, null);
    }

    public Cell(MyPoint2D coordinate, boolean visited, int destination_distance, int cost, Cell parent) {
        this.coordinate = coordinate;
        this.visited = visited;
        this.destination_distance = destination_distance;
        this.cost = cost;
        this.parent = parent;
    }

    public boolean isVisited() {
        return visited;
    }

    public int getCost() {
        return cost;
    }

    public int getDestination_distance() {
        return destination_distance;
    }

    public MyPoint2D getCoordinate() {
        return coordinate;
    }

    public Cell getParent() {
        return parent;
    }

    public void setVisited() {
        this.visited = true;
    }

    public void setVisited(boolean state) {
        this.visited = state;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setDestination_distance(int destination_distance) {
        this.destination_distance = destination_distance;
    }

    public void setCoordinate(MyPoint2D coordinate) {
        this.coordinate = new MyPoint2D(coordinate);
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }
}
