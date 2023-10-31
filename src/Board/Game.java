package Board;

import Animal.*;
import Creature.*;

import java.util.*;


public class Game {
    public static Game instance; // Singleton instance of the Game
    public final int ROW = 20;
    public final int COL = 20;

    public Square[][] board = new Square[ROW][COL];
    public int turn = 0;
    public static String[] moveOrder = {"Rabbit", "Fox", "Deer", "Owl", "Badger"};
    public static List<Animal> animals = new ArrayList<>(); // a list of 5 animals
    public static List<Creature> creatures = new ArrayList<>(); // a list of 5 creatures
    private static List<Animal> shieldAnimal = new ArrayList<>();

    // A mapping of objects (animals and creatures) to their positions on the game board.
    public static HashMap<Object, Coordinate> objectPosition = new HashMap<>();

    // A mapping of spells to their positions on the game board.
    public static HashMap<Spell, List<Coordinate>> spellPosition = new HashMap<>();
    public static String errorMessage = null;
    public static Spell[] spells = {Spell.Detect, Spell.Heal, Spell.Shield, Spell.Confuse, Spell.Charm};

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
            objectPosition.clear();
            spellPosition.clear();
            animals.clear();
            creatures.clear();
            errorMessage = null;
            instance = new Game(seed);
        }
        return instance;
    }

    // Constructs a new game with the specified random seed.
    private Game(long seed) {
        initializeBoard();

        // construct animal and creatures, then add them to the lists
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
        int randomIdx; // using this to decide which spell to construct

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
            Spell spell = spells[randomIdx];
            placeSpell(random, spell, board);
        }
    }

    /**
     * This method moves an animal to a new position and updates the game state accordingly
     * Game state includes:
     * 1. moveable and spellable of an animal
     * 2. the spells that an animal has
     * 3. visibility of the square
     * 4. update the life points and shield(if applicable) of previous animal
     * 5. uodate turn
     * 6. update confused and charmed of creatures
     * */
    public void moveAnimal(Animal animal, int oldRow, int oldCol, int newRow, int newCol) throws Exception {
        if (gameOver()){
            errorMessage = "Game is over!";
            throw new Exception(errorMessage);
        }
        else if (animal.isMoveable() == true) {
            // Move the animal and update its movability
            animal.move(oldRow, oldCol, newRow, newCol);
            animal.setMoveable(false);

            //pick up spell and remove the spell for the square
            int curRow = objectPosition.get(animal).getRow();
            int curCol = objectPosition.get(animal).getCol();
            if (board[curRow][curCol].isHasSpell()) {
                animal.setSpells(animal.getSpells(), findSpellByCoordinate(spellPosition, curRow, curCol));
                board[curRow][curCol].setHasSpell(false);
            }
            // visibility
            board[curRow][curCol].changeVisibile(true);

            // update the life points and shield(if applicable) of previous animal
            endPreviousTurn(animal);

            Animal nextAnimal = findNextAnimal(animal);
            nextAnimal.setMoveable(true);
            nextAnimal.setSpellable(true);
            // update turn
            turn = getIndexForAnimal(nextAnimal.getName());
            // update confused
            for (Creature creature : creatures){
                if (creature.getConfusedTurnLeft() != 0){
                    creature.setConfusedTurnLeft(creature.getConfusedTurnLeft() - 1);
                    if (creature.getConfusedTurnLeft() == 0){
                        creature.setConfused(false);
                    }
                }
            }
            // update charmed
            for (Creature creature : creatures){
                if (creature.getChamAnimal() != null){
                    creature.updateChamAnimal();
                }
            }
            errorMessage = null;
        } else {
            errorMessage = "It is not " + animal.getName() + "'s turn.";
            throw new Exception(errorMessage);
        }
    }

    // Returns the index of the animal in moveorder.
    public int getIndexForAnimal(String animalName) {
        for (int i = 0; i < moveOrder.length; i++) {
            if (moveOrder[i].equals(animalName)) {
                return i;
            }
        }
        return -1;
    }

    // decide a creature can attack or not, update shield of animal, update spellable of animal
    public void endPreviousTurn(Animal animal) {
        Animal preAnimal = findPreviousAnimal(animal);
        // If the current turn animal moved, then the previous turn animal cannot spell
        preAnimal.setSpellable(false);
        Creature creature = (Creature)findCreatureByCoordinate(objectPosition, objectPosition.get(preAnimal).getRow(), objectPosition.get(preAnimal).getCol());
        if (creature != null && !shieldAnimal.contains(preAnimal)) {
            if (creature.isConfused() == true) {
                System.out.println("Debug : " + creature.getName() + " is confused!");
            }else if (creature.getChamAnimal().containsKey(preAnimal)){
                System.out.println("Debug : " + creature.getName() + " is charmed!");
            }else{
                attackAnimal(creature, preAnimal);
                System.out.println("Debug : " + creature.getName() + " attack an animal successful!");
            }
        }
        else if (shieldAnimal.contains(preAnimal)) {
            shieldAnimal.remove(preAnimal);
        }
        System.out.println("Debug : Update the turn successful!");
    }

    // allow creature to attack and update the life points of animal
    public void attackAnimal(Creature creature, Animal animal) {
        int newLifePoints = animal.getLifePoints() - creature.getAttack();
        animal.setLifePoints(newLifePoints);
        if (animal.getLifePoints() < 0) animal.setLifePoints(0);
    }

    /**
     * Checks if the game is over by examining the life points of all animal characters and their positions.
     *
     * @return True if the game is over, false otherwise.
     */
    public boolean gameOver() {
        for (Animal animal : animals) {
            if (animal.getLifePoints() == 0) return true;
        }
        int count = 0;
        for (Animal animal : animals){
            if (animal.getSquare(animal).getRow() == 0) count++;
            if (count == 5) return true;
        }
        return false;
    }

    public Square getSquare(int row, int col) {
        return board[row][col];
    }

    /**
     * Casts a spell using an animal character and updates the game state accordingly.
     *
     * @param animal The animal character casting the spell.
     * @param spell  The spell to cast.
     */
    public void castSpell(Animal animal, Spell spell) throws Exception {
        int previousTurn = (turn - 1 + moveOrder.length) % moveOrder.length;
        //both the current turn and previous turn animal can cast spell
        if (animal.getName().equals(moveOrder[turn]) || animal.getName().equals(moveOrder[previousTurn])) {
            String spellType = spell.getType();
            Creature creature = nearCreature(animal);
            switch (spellType) {
                case "Detect":
                    int curRow = animal.getSquare(animal).getRow();
                    int curCol = animal.getSquare(animal).getCol();

                    int[] dirRow = {-1, 0, 1, -1, 1, -1, 0, 1};
                    int[] dirCol = {-1, -1, -1, 0, 0, 1, 1, 1};

                    for (int i = 0; i < 8; i++) {
                        int adjacentRow = curRow + dirRow[i];
                        int adjacentCol = curCol + dirCol[i];
                        this.board[adjacentRow][adjacentCol].changeVisibile(true);
                    }
                    animal.getSpells().put(spell, animal.getSpells().get(spell) - 1);
                    break;
                case "Heal":
                    animal.heal();
                    break;
                case "Shield":
                    shieldAnimal.add(animal);
                    break;
                case "Confuse":
                    if (creature!= null) {
                        creature.setConfused(true);
                        creature.setConfusedTurnLeft(6);
                    }
                    break;
                case "Charm":
                    if (creature != null) {
                        creature.setCharmed(true);
                        creature.addChamAnimal(animal);
                    }
                    break;
            }

            // update spell number that an animal holds
            animal.getSpells().put(spell, animal.getSpells().get(spell) - 1);
            Iterator<Map.Entry<Spell, Integer>> iterator = animal.spells.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Spell, Integer> entry = iterator.next();
                if (entry.getValue() == 0) {
                    iterator.remove();
                }
            }

            if (animal.getName().equals(moveOrder[turn])) turn++;
            if (turn == moveOrder.length) turn = 0;
            animal.setSpellable(false);
            animal.setMoveable(false);
            Animal nextAnimal = findNextAnimal(animal);
            nextAnimal.setMoveable(true);
            nextAnimal.setSpellable(true);
            errorMessage = null;
        } else {
            errorMessage = "It is not " + animal.getName() + "'s turn.";
            throw new Exception(errorMessage);
        }
    }

    // construct squares
    private void initializeBoard() {
        for (int row = 0; row < ROW; row++) {
            for (int col = 0; col < COL; col++) {
                this.board[row][col] = new Square(row, col);
            }
        }
    }

    public static Animal findPreviousAnimal(Animal animal) {
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
                int randomCol = random.nextInt(20);
                board[19][randomCol].setAnimal(animal);
                board[19][randomCol].changeVisibile(true);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            break;
        }
    }

    //This method place creatures randomly excluding the first and the last row
    private void placeCreature(Random random, Creature creature, Square[][] board) {
        while (true) {
            try {
                int randomRow = random.nextInt(18) + 1;
                int randomCol = random.nextInt(20);
                board[randomRow][randomCol].setCreature(creature);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            break;
        }
    }

    //This method place spells randomly excluding the first and the last row
    private void placeSpell(Random random, Spell spell, Square[][] board) {
        while (true) {
            try {
                int randomRow = random.nextInt(18) + 1;
                int randomCol = random.nextInt(20);
                board[randomRow][randomCol].setSpell(spell);
                System.out.println(spell.getType() + " is at row " + randomRow + " col " + randomCol);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            break;
        }
    }

    /**
     * This method check if there is a creature on a square adjacent to the animal
     *
     * @return if the creature exists it will be returned
     * */
    public Creature nearCreature(Animal animal) {
        int curRow = animal.getSquare(animal).getRow();
        int curCol = animal.getSquare(animal).getCol();

        int[] dirRow = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] dirCol = {-1, -1, -1, 0, 0, 1, 1, 1};

        for (int i = 0; i < 8; i++) {
            int adjacentRow = curRow + dirRow[i];
            int adjacentCol = curCol + dirCol[i];
            if (isValidCoordinate(adjacentRow, adjacentCol)) {
                boolean hasCreature = this.board[adjacentRow][adjacentCol].isHasCreature();
                if (hasCreature) {
                    return (Creature) findCreatureByCoordinate(objectPosition, adjacentRow, adjacentCol);
                }
            }
        }
        return null;
    }

    // make sure not out of bounds
    private boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < ROW && col >= 0 && col < COL;
    }

    // given location this method will return the animal
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

    // given location this method will return the creature
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

    // // given location this method will return the spell
    private Spell findSpellByCoordinate(HashMap<Spell, List<Coordinate>> map, int row, int col) {
        for (Map.Entry<Spell, List<Coordinate>> entry : map.entrySet()) {
            List<Coordinate> coordinates = entry.getValue();
            for (Coordinate coordinate : coordinates) {
                if (coordinate.getRow() == row && coordinate.getCol() == col) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    // This method would return the name of the next animal in moveorder
    public static String getNextAnimal(String currentAnimal) {
        for (int i = 0; i < moveOrder.length; i++) {
            if (moveOrder[i].equals(currentAnimal)) {
                if (i == moveOrder.length - 1) {
                    return moveOrder[0];
                } else {
                    return moveOrder[i + 1];
                }
            }
        }
        return "";
    }
}
