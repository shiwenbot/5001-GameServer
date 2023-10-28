package Creature;

public class Centaur extends Creature{
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

    public Centaur(String name) {
        super(name);
        shortName = "CC";
        attack = 10;
        description = "The CC is a centaur that has mixed feeling about its love interest, a horse. The centaur is unsure whether they can love them fully.";
    }
    public boolean isConfused(){
        return this.confused;
    }
    public void setConfused(boolean confused) {
        this.confused = confused;
    }
}
