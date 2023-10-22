package Creature;


public class Creature {
    private String name;
    private int attackValue;
    private boolean confused;

    public Creature(String name) {
        this.name = name;
        //System.out.println("Construct a creature.");
    }

    public void setConfused(boolean confused) {
        this.confused = confused;
    }


}
