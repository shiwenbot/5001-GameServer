package Animal;

import Board.Spell;

import java.util.Map;

public class Fox extends Animal{
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
    public Fox(String name) {
        super(name);
    }
}
