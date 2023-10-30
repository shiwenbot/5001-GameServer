package Creature;


import Animal.Animal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Creature {
    public String name;
    public String shortName;
    public int attack;
    public String description;
    public boolean confused;
    public boolean charmed;
    public String type = "Creature";
    public HashMap<Animal, Integer> chamAnimal = new HashMap<>();

    public HashMap<Animal, Integer> getChamAnimal() {
        return chamAnimal;
    }
    public void addChamAnimal(Animal animal){
        chamAnimal.put(animal, 16);
    }
    public void updateChamAnimal(){
        // 创建一个迭代器以遍历 HashMap 的键
        Iterator<Map.Entry<Animal, Integer>> iterator = chamAnimal.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Animal, Integer> entry = iterator.next();
            int updatedValue = entry.getValue() - 1;

            if (updatedValue == 0) {
                // 值为0或更小，移除键
                iterator.remove();
            } else {
                // 更新键对应的值
                chamAnimal.put(entry.getKey(), updatedValue);
            }
        }
    }


    public int confusedTurnLeft = 0;
    public int charmedTurnLeft = 0;

    public boolean isCharmed() {
        return charmed;
    }

    public void setCharmed(boolean charmed) {
        this.charmed = charmed;
    }

    public int getConfusedTurnLeft() {
        return confusedTurnLeft;
    }

    public void setConfusedTurnLeft(int confusedTurnLeft) {
        this.confusedTurnLeft = confusedTurnLeft;
    }

    public int getCharmedTurnLeft() {
        return charmedTurnLeft;
    }

    public void setCharmedTurnLeft(int charmedTurnLeft) {
        this.charmedTurnLeft = charmedTurnLeft;
    }
    public boolean isConfused(){
        return this.confused;
    }
    public void setConfused(boolean confused) {
        this.confused = confused;
    }
    public String getType() {
        return type;
    }

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




}
