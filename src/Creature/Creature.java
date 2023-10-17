package Creature;

public class Creature {
    private String name;
    private int col;
    private int row;
    private int attackValue;


    public Creature(String name) {
        this.name = name;
        System.out.println("Construct a creature.");
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
