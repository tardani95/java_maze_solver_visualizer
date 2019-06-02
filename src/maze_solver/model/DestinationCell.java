package maze_solver.model;

public class DestinationCell extends SpecialCell{
    public DestinationCell(int x, int y) {
        super(x, y, CellType.DESTINATION);
    }
}
