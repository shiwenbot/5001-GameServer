package Animal;

import Board.Spell;

import java.util.Map;

public class Owl extends Animal{
    public int lifePoints = 100;
    public String description = "Fox description";

    @Override
    public String getDescription() {
        return this.description;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public Map<Spell, Integer> getSpells() {
        return spells;
    }
    @Override
    public int getLifePoints() {
        return lifePoints;
    }
    public Owl(String name) {
        super(name);
    }
}
