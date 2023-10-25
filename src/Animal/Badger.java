package Animal;

import Board.Spell;

import java.util.Map;

public class Badger extends Animal {
    public int lifePoints = 100;
    public String description = "Badger description";

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

    public Badger(String name) {
        super(name);
    }
}
