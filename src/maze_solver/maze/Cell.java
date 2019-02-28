package maze_solver.maze;

public class Cell {
    public boolean visited;
    public int destination_distance;
    public int cost;
    public Cell parent;

    public Cell() {
        this(false,Integer.MAX_VALUE,Integer.MAX_VALUE,null);
    }

    public Cell(boolean visited, int destination_distance, int cost, Cell parent) {
        this.visited = visited;
        this.destination_distance = destination_distance;
        this.cost = cost;
        this.parent = parent;
    }

    public boolean isVisited(){
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

    public void setVisited() {
        this.visited = true;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }
}
