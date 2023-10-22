package Board;

enum SpellType {
    DETECT,
    HEAL,
    SHIELD,
    CONFUSE,
    CHARM
}
public class Spell {
    private SpellType type;

    public Spell(SpellType type){
        int n = 0;
        System.out.println("This is the " + n + "th spell in this game");
        this.type = type;
    }

    public SpellType getType(){
        return this.type;
    }

}
