package maze_solver.model;

public class SpecialCell extends Cell {

    protected SpecialCell(int x, int y, CellType cellType) {
        super(x, y, cellType);
    }

    protected void setCoordinates(int x, int y) {
        modifyXY(x, y);
    }

    protected void setCoordinatesToCell(Cell c) {
        setCoordinates(c.x,c.y);
    }
}
