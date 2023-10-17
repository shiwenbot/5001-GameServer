package Animal;

import Exceptions.ExceedMoveRangeException;
import Exceptions.NotStraightLineException;

public class Animal {
    private String name;
    private int col;
    private int row;

    public Animal(String name) {
        this.name = name;
        System.out.println("Construct an animal.");
    }

    public boolean move(int oldRow, int oldCol, int newRow, int newCol) throws Exception {
        return true;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
