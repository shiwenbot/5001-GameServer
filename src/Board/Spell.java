package Board;

public enum Spell {
    Detect("Detect", "The detect spell allows the animal to detect the mythical creatures on the adjacent squares."),
    Heal("Heal", "The heal spell allows the animal to heal 10 life points."),
    Shield("Shield", "The shield spell allows the animal to block a mythical creature attack for that turn."),
    Confuse("Confuse", "The confuse spell allows the animal to confuse a mythical creature on a square adjacent to the animal but not the square the animal is occupying. The mythical creature will not attack any animal for the next turn."),
    Charm("Charm", "The charm spell allows the animal to charm a mythical creature on a square adjacent to the animal but not the square the animal is occupying. The mythical creature will not attack the charming animal for the next three turns.");
    public String type;
    public String description;

    Spell(String type, String description){
        this.type = type;
        this.description = description;
    }

    public String getType(){
        return this.type;
    }
    public String getDescription() {return this.description;}
}
