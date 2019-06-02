package maze_solver.model;

public class CurrentCell extends SpecialCell {
    public CurrentCell(int x, int y) {
        super(x, y, CellType.CURRENT);
    }
}

