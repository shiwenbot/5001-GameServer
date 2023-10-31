package Creature;

import Animal.Animal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Centaur extends Creature{
    private String name;
    private String shortName;
    private int attack = 36;
    private String description;
    private boolean confused;
    private boolean charmed;
    private String type = "Creature";
    private int confusedTurnLeft = 0;
    private int charmedTurnLeft = 0;
    private HashMap<Animal, Integer> chamAnimal = new HashMap<>();
    public HashMap<Animal, Integer> getChamAnimal() {
        return chamAnimal;
    }
    public void updateChamAnimal(){
        Iterator<Map.Entry<Animal, Integer>> iterator = chamAnimal.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Animal, Integer> entry = iterator.next();
            int updatedValue = entry.getValue() - 1;

            if (updatedValue == 0) {
                iterator.remove();
            } else {
                chamAnimal.put(entry.getKey(), updatedValue);
            }
        }
    }
    public void addChamAnimal(Animal animal){
        chamAnimal.put(animal, 16);
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public int getAttack() {
        return attack;
    }

    @Override
    public String getDescription() {
        return description;
    }

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
        attack = 36;
        description = "The CC is a centaur that has mixed feeling about its love interest, a horse. The centaur is unsure whether they can love them fully.";
    }
    public boolean isConfused(){
        return this.confused;
    }
    public void setConfused(boolean confused) {
        this.confused = confused;
    }
}
