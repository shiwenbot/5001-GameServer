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
    public static Spell createSpell(String spellName) {
        switch (spellName) {
            case "DETECT":
                return new Spell(SpellType.DETECT);
            case "HEAL":
                return new Spell(SpellType.HEAL);
            case "SHIELD":
                return new Spell(SpellType.SHIELD);
            case "CONFUSE":
                return new Spell(SpellType.CONFUSE);
            case "CHARM":
                return new Spell(SpellType.CHARM);
            default:
                // 如果传入的法术名称无效，可以返回 null 或抛出异常，具体取决于你的需求
                return null;
        }
    }

}
