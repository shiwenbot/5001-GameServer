package Board;

import Animal.*;
import Creature.*;

import java.util.*;


public class Game {
    private static Game instance;
    public final int ROW = 20;
    public final int COL = 20;
    public Square[][] board = new Square[ROW][COL];
    public int turn = 0;
    public static List<Animal> animals = new ArrayList<>();
    public static List<Creature> creatures = new ArrayList<>();
    public static String[] moveOrder = {"Rabbit", "Fox", "Deer", "Owl", "Badger"};
    private static List<Animal> shieldAnimal = new ArrayList<>();
    public static HashMap<Object, Coordinate> objectPosition = new HashMap<>();
    public static HashMap<Spell, Coordinate> spellPosition = new HashMap<>();
    public static String errorMessage = null;
    public static SpellType[] spells = {SpellType.DETECT, SpellType.HEAL, SpellType.SHIELD, SpellType.CONFUSE, SpellType.CHARM};

    public Rabbit rabbit;
    public Fox fox;
    public Deer deer;
    public Owl owl;
    public Badger badger;

    public Unicorn unicorn;
    public Centaur centaur;
    public Dragon dragon;
    public Phoenix phoenix;
    public Sphinx sphinx;

    public static Game getInstance(long seed) {
        if (instance == null) {
            instance = new Game(seed);
        }
        return instance;
    }

    /*
     *
     * */
    private Game(long seed) {
        initializeBoard();

        //initialize animal and creatures
        rabbit = new Rabbit("Rabbit");
        fox = new Fox("Fox");
        deer = new Deer("Deer");
        owl = new Owl("Owl");
        badger = new Badger("Badger");
        animals.add(rabbit);
        animals.add(fox);
        animals.add(deer);
        animals.add(owl);
        animals.add(badger);

        //initialize Creatures
        unicorn = new Unicorn("Under-appreciated Unicorn");
        centaur = new Centaur("Complicated Centaur");
        dragon = new Dragon("Deceptive Dragon");
        phoenix = new Phoenix("Precocious Phoenix");
        sphinx = new Sphinx("Sassy Sphinx");
        creatures.add(unicorn);
        creatures.add(centaur);
        creatures.add(dragon);
        creatures.add(phoenix);
        creatures.add(sphinx);

        Random random = new Random(seed);
        int randomIdx;

        //place Animals
        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0: {
                    placeAnimal(random, rabbit, board);
                    break;
                }
                case 1: {
                    placeAnimal(random, fox, board);
                    break;
                }
                case 2: {
                    placeAnimal(random, deer, board);
                    break;
                }
                case 3: {
                    placeAnimal(random, owl, board);
                    break;
                }
                case 4: {
                    placeAnimal(random, badger, board);
                    break;
                }
            }
        }

        //place Creatures
        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0: {
                    placeCreature(random, unicorn, board);
                    break;
                }
                case 1: {
                    placeCreature(random, centaur, board);
                    break;
                }
                case 2: {
                    placeCreature(random, dragon, board);
                    break;
                }
                case 3: {
                    placeCreature(random, phoenix, board);
                    break;
                }
                case 4: {
                    placeCreature(random, sphinx, board);
                    break;
                }
            }
        }

        //place spell
        for (int i = 0; i < 10; i++) {
            randomIdx = random.nextInt(5);
            Spell spell = new Spell(spells[randomIdx]);
            placeSpell(random, spell, board);
        }
    }

    public void moveAnimal(Animal animal, int oldRow, int oldCol, int newRow, int newCol) throws Exception {
        if (animal.isMoveable() == true) {
            //移动并更新moveable
            animal.move(oldRow, oldCol, newRow, newCol);
            animal.setMoveable(false);

            //如果新格子有spell的话就捡起来
            int curRow = objectPosition.get(animal).getRow();
            int curCol = objectPosition.get(animal).getCol();
            if(board[curRow][curCol].isHasSpell()){
                animal.setSpells(animal.getSpells(), findSpellByCoordinate(spellPosition, curRow, curCol));
                //把这个格子的spell移除
                board[curCol][curCol].setHasSpell(false);
            }

            endPreviousTurn(animal);

            Animal nextAnimal = findNextAnimal(animal);
            nextAnimal.setMoveable(true);
            nextAnimal.setSpellable(true);

            Animal preAnimal = findPreviousAnimal(animal);
//            if (shieldAnimal.contains(preAnimal)) {
//                shieldAnimal.remove(animal);
//            }
            turn++;
            if (turn == moveOrder.length) turn = 0;
        } else {
            errorMessage = "It is not " + animal.getName() + "'s turn.";
            throw new Exception(errorMessage);
        }
    }

    public void endPreviousTurn(Animal animal) {
        Animal preAnimal = findPreviousAnimal(animal);
        preAnimal.setSpellable(false);
        if (preAnimal.withCreature(objectPosition.get(preAnimal).getRow(), objectPosition.get(preAnimal).getCol())) {
            Object creature = findCreatureByCoordinate(objectPosition, objectPosition.get(preAnimal).getRow(), objectPosition.get(preAnimal).getCol());
            attackAnimal((Creature) creature, preAnimal);
            System.out.println("Attack an animal successful!");
        }
        System.out.println("Update the turn successful!");
    }

    public void attackAnimal(Creature creature, Animal animal) {
        int newLifePoints = animal.getLifePoints() - creature.getAttack();
        animal.setLifePoints(newLifePoints);
        if (animal.getLifePoints() < 0) animal.setLifePoints(0);
    }

    public boolean gameOver() {
        for (Animal animal : animals) {
            if (animal.getLifePoints() == 0) return true;
        }
        return false;
    }

    public Square getSquare(int row, int col) {
        return board[row][col];
    }

    public void castSpell(Animal animal, Spell spell) throws Exception {
        if (animal.getName().equals(moveOrder[turn])) {
            SpellType spellType = spell.getType();

            switch (spellType) {
                case DETECT:
                    int curRow = animal.getSquare(animal).getRow();
                    int curCol = animal.getSquare(animal).getCol();

                    int[] dirRow = {-1, 0, 1, -1, 1, -1, 0, 1};
                    int[] dirCol = {-1, -1, -1, 0, 0, 1, 1, 1};

                    for (int i = 0; i < 8; i++) {
                        int adjacentRow = curRow + dirRow[i];
                        int adjacentCol = curCol + dirCol[i];
                        this.board[adjacentRow][adjacentCol].changeVisibile(true);
                    }
                    break;
                case HEAL:
                    animal.heal();
                    break;
                case SHIELD:
                    shieldAnimal.add(animal);
                case CONFUSE:
                    //如果有的话，先返回这个怪物，让它中招一个回合
                    if (nearCreature(animal) != null) {
                        nearCreature(animal).setConfused(true);
                    }
                    break;
                case CHARM:
                    break;
            }
            turn++;
            if (turn == moveOrder.length) turn = 0;
        } else {
            throw new Exception("This is not your turn.");
        }

    }


    /*The methods below are only used in Game class, so they should be private*/
    private void initializeBoard() {
        for (int row = 0; row < ROW; row++) {
            for (int col = 0; col < COL; col++) {
                this.board[row][col] = new Square(row, col);
            }
        }
    }

    public Animal findPreviousAnimal(Animal animal) {
        if (animal.getName().equals("Rabbit")) return animals.get(animals.size() - 1);
        return animals.get(animals.indexOf(animal) - 1);
    }

    private Animal findNextAnimal(Animal animal) {
        if (animal.getName().equals("Badger")) return animals.get(0);
        return animals.get(animals.indexOf(animal) + 1);
    }

    //This method place animals randomly at row 0
    private void placeAnimal(Random random, Animal animal, Square[][] board) {
        while (true) {
            try {
                //System.out.println("The random num is " + randomCol);
                int randomCol = random.nextInt(20);
                board[19][randomCol].setAnimal(animal);
                System.out.println(animal.getName() + "is at col： " + randomCol);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            //System.out.println("This animal has been placed at row 0, col " + randomCol);
            break;
        }
    }

    //This method place creatures randomly excluding the first and the last row
    private void placeCreature(Random random, Creature creature, Square[][] board) {
        while (true) {
            try {
                int randomRow = random.nextInt(18) + 1;//exclude the first and the last row
                int randomCol = random.nextInt(20);
                board[randomRow][randomCol].setCreature(creature);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            break;
        }
    }

    private void placeSpell(Random random, Spell spell, Square[][] board) {
        while (true) {
            try {
                int randomRow = random.nextInt(18) + 1;//exclude the first and the last row
                int randomCol = random.nextInt(20);
                board[randomRow][randomCol].setSpell(spell);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            break;
        }
    }

    public Creature nearCreature(Animal animal) {
        int curRow = animal.getSquare(animal).getRow();
        int curCol = animal.getSquare(animal).getCol();

        int[] dirRow = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] dirCol = {-1, -1, -1, 0, 0, 1, 1, 1};

        for (int i = 0; i < 8; i++) {
            int adjacentRow = curRow + dirRow[i];
            int adjacentCol = curCol + dirCol[i];
            if (isValidCoordinate(adjacentRow, adjacentRow)) {
                boolean hasCreature = this.board[adjacentRow][adjacentCol].isHasCreature();
                if (hasCreature) {
                    return (Creature) findCreatureByCoordinate(objectPosition, adjacentRow, adjacentCol);
                }
            }
        }
        return null;
    }

    private boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < ROW && col >= 0 && col < COL;
    }

    private Object findAnimalByCoordinate(HashMap<Object, Coordinate> map, int row, int col) {
        for (Map.Entry<Object, Coordinate> entry : map.entrySet()) {
            if (entry.getValue().getRow() == row && entry.getValue().getCol() == col) {
                Object key = entry.getKey();
                if (key instanceof Animal) {
                    return key;
                }
            }
        }
        return null;
    }

    private Object findCreatureByCoordinate(HashMap<Object, Coordinate> map, int row, int col) {
        for (Map.Entry<Object, Coordinate> entry : map.entrySet()) {
            if (entry.getValue().getRow() == row && entry.getValue().getCol() == col) {
                Object key = entry.getKey();
                if (key instanceof Creature) {
                    return key;
                }
            }
        }
        return null;
    }
    private Spell findSpellByCoordinate(HashMap<Spell, Coordinate> map, int row, int col) {
        for (Map.Entry<Spell, Coordinate> entry : map.entrySet()) {
            if (entry.getValue().getRow() == row && entry.getValue().getCol() == col) {
                Spell key = entry.getKey();
                if (key instanceof Spell) {
                    return key;
                }
            }
        }
        return null;
    }
}
