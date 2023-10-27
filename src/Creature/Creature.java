package Creature;


public class Creature {
    public String name;
    public String shortName;
    public int attack;
    public String description;

    public String getType() {
        return type;
    }

    public String type = "Creature";
    private boolean confused;
    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public int getAttack() {
        return attack;
    }

    public String getDescription() {
        return description;
    }




    public Creature(String name) {
        this.name = name;
        //System.out.println("Construct a creature.");
    }

    public void setConfused(boolean confused) {
        this.confused = confused;
    }


}
