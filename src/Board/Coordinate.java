package Board;

public class Coordinate {
    private int row;
    private int col;

    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setX(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setY(int col) {
        this.col = col;
    }
}
