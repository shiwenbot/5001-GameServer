package Board;

enum SpellType {
    DETECT,
    HEAL,
    SHIELD,
    CONFUSE,
    CHARM
}
public class Spell {
    public SpellType type;

    public Spell(SpellType type){
        this.type = type;
    }

    public SpellType getType(){
        return this.type;
    }

}
