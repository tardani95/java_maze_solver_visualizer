package maze_solver.model;

public class GoalCell extends SpecialCell{
    public GoalCell(int x, int y) {
        super(x, y, CellType.DESTINATION);
    }
}
