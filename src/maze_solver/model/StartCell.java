package maze_solver.model;

public class StartCell extends SpecialCell {
    public StartCell(int x, int y) {
        super(x, y, CellType.START);
    }
}
