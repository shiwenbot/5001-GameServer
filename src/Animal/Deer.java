package Animal;

import Board.Spell;

import java.util.Map;

public class Deer extends Animal{
    public int lifePoints = 100;
    public String description = "Deer description";

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
    public Deer(String name) {
        super(name);
    }
}
