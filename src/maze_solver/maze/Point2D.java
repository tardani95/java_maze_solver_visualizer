package maze_solver.maze;

public class Point2D {
    public int x;
    public int y;

    public Point2D() {
        this(0, 0);
    }

    public Point2D(Point2D p){
        this(p.x,p.y);
    }

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(obj instanceof Point2D)) {
            return false;
        }

        Point2D compared = (Point2D) obj;
        return (this.x == compared.x) && (this.y == compared.y);
    }

    public double distanceTo(Point2D p2, int heuristic) {
        return distanceTo(p2.x,p2.y,heuristic);
    }

    public double distanceTo(int x, int y, int heuristic) {
        switch (heuristic) {
            case 0: { /*euklides*/
                return Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
            }
            case 1: { /*manhattam*/
                return Math.abs(this.x - x) + Math.abs(this.y - y);
            }

            default:
                System.err.println("no such heuristic");
                return -1.0;
        }

    }
}
