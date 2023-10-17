package Animal;

public class Animal {
    private String name;
    private int col;
    private int row;

    public Animal(String name) {
        this.name = name;
        System.out.println("Construct Rabbit");
    }

    public boolean move() {
        return true;
    }


    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
