package Creature;

import Animal.Animal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Sphinx extends Creature{
    public String name = "Sassy Sphinx";
    public String shortName = "SS";
    public int attack = 21;
    public String description = "The SS is a sphinx that is very sassy. The sphinx is very good at giving sarcastic answers to questions.";
    public boolean confused;
    public boolean charmed;
    public String type = "Creature";
    public int confusedTurnLeft = 0;
    public int charmedTurnLeft = 0;
    public HashMap<Animal, Integer> chamAnimal = new HashMap<>();
    @Override
    public String getName() {
        return name;
    }
    public HashMap<Animal, Integer> getChamAnimal() {
        return chamAnimal;
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
    public void updateChamAnimal(){
        // ����һ���������Ա��� HashMap �ļ�
        Iterator<Map.Entry<Animal, Integer>> iterator = chamAnimal.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Animal, Integer> entry = iterator.next();
            int updatedValue = entry.getValue() - 1;

            if (updatedValue == 0) {
                // ֵΪ0���С���Ƴ���
                iterator.remove();
            } else {
                // ���¼���Ӧ��ֵ
                chamAnimal.put(entry.getKey(), updatedValue);
            }
        }
    }
    public void addChamAnimal(Animal animal){
        chamAnimal.put(animal, 16);
    }
    @Override
    public void setCharmedTurnLeft(int charmedTurnLeft) {
        this.charmedTurnLeft = charmedTurnLeft;
    }


    public Sphinx(String name) {
        super(name);
        shortName = "SS";
        attack = 21;
        description = "The SS is a sphinx that is very sassy. The sphinx is very good at giving sarcastic answers to questions.";
    }
    public boolean isConfused(){
        return this.confused;
    }
    public void setConfused(boolean confused) {
        this.confused = confused;
    }
}
