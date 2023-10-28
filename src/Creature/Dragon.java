package Creature;

public class Dragon extends Creature{
    public String name;
    public String shortName;
    public int attack;
    public String description;
    public boolean confused;
    public boolean charmed;
    public String type = "Creature";
    public int confusedTurnLeft = 0;
    public int charmedTurnLeft = 0;

    @Override
    public boolean isCharmed() {
        return charmed;
    }

    @Override
    public void setCharmed(boolean charmed) {
        this.charmed = charmed;
    }

    @Override
    public int getConfusedTurnLeft() {
        return confusedTurnLeft;
    }

    @Override
    public void setConfusedTurnLeft(int confusedTurnLeft) {
        this.confusedTurnLeft = confusedTurnLeft;
    }

    @Override
    public int getCharmedTurnLeft() {
        return charmedTurnLeft;
    }

    @Override
    public void setCharmedTurnLeft(int charmedTurnLeft) {
        this.charmedTurnLeft = charmedTurnLeft;
    }


    public Dragon(String name) {
        super(name);
        shortName = "DD";
        attack = 10;
        description = "The DD is a dragon that practices social engineering. The dragon is very good at sending phishing emails pretending to be a prince.";
    }
    public boolean isConfused(){
        return this.confused;
    }
    public void setConfused(boolean confused) {
        this.confused = confused;
    }
}
