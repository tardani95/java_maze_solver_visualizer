package maze_solver.maze;

public class Cell {

    private boolean visited;
    private int cost;
    private int destination_distance;
    private Point2D coordinate;
    private Cell parent;

    public Cell() {
        this(new Point2D(0, 0), false, Integer.MAX_VALUE, Integer.MAX_VALUE, null);
    }

    public Cell(int x, int y) {
        this(new Point2D(x, y), false, Integer.MAX_VALUE, Integer.MAX_VALUE, null);
    }

    public Cell(Point2D coordinate, boolean visited, int destination_distance, int cost, Cell parent) {
        this.coordinate = new Point2D(coordinate.x, coordinate.y);
        this.visited = visited;
        this.destination_distance = destination_distance;
        this.cost = cost;
        this.parent = parent;
    }

    public boolean isVisited() {
        return visited;
    }

    public int getDestination_distance() {
        return destination_distance;
    }

    public int getCost() {
        return cost;
    }

    public Cell getParent() {
        return parent;
    }

    public void setDestination_distance(int destination_distance) {
        this.destination_distance = destination_distance;
    }

    public void setVisited() {
        this.visited = true;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }

    public Point2D getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Point2D coordinate) {
        this.coordinate = coordinate;
    }
}
