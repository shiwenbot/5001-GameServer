package Board;

import Animal.Animal;
import Animal.*;
import Creature.*;

import java.util.*;


public class Game {
    private static Game instance;
    final private int ROW = 20;
    final private int COL = 20;
    public Square[][] board = new Square[ROW][COL];
    public int turn = 0;
    public static List<Animal> animals = new ArrayList<>();
    //private List<Creature> creatures = new ArrayList<>();
    private String[] moveOrder = {"Rabbit", "Fox", "Deer", "Owl", "Badger"};
    private static List<Animal> shieldAnimal = new ArrayList<>();
    public static HashMap<Object, Coordinate> objectPosition = new HashMap<>();

    public Rabbit rabbit;
    public Fox fox;
    private Deer deer;
    private Owl owl;
    private Badger badger;

    public Unicorn unicorn;
    private Centaur centaur;
    private Dragon dragon;
    private Phoenix phoenix;
    private Sphinx sphinx;

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
        unicorn = new Unicorn("Unicorn");
        centaur = new Centaur("Centaur");
        dragon = new Dragon("Dragon");
        phoenix = new Phoenix("Phoenix");
        sphinx = new Sphinx("Sphinx");

        Random random = new Random(seed);
        int randomCol, randomRow;

        //place Animals
        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0 : {
                    randomCol = random.nextInt(20);
                    placeAnimal(randomCol, rabbit, board);
                    break;
                }
                case 1 : {
                    randomCol = random.nextInt(20);
                    placeAnimal(randomCol, fox, board);
                    break;
                }
                case 2 : {
                    randomCol = random.nextInt(20);
                    placeAnimal(randomCol, deer, board);
                    break;
                }
                case 3 : {
                    randomCol = random.nextInt(20);
                    placeAnimal(randomCol, owl, board);
                    break;
                }
                case 4 : {
                    randomCol = random.nextInt(20);
                    placeAnimal(randomCol, badger, board);
                    break;
                }
            }
        }

        //place Creatures
        for (int i = 0; i < 5; i++) {
            switch(i){
                case 0 : {
                    randomRow = random.nextInt(18) + 1;//exclude the first and the last row
                    randomCol = random.nextInt(20);
                    placeCreature(randomRow, randomCol, unicorn, board);
                    break;
                }
                case 1 : {
                    randomRow = random.nextInt(18) + 1;//exclude the first and the last row
                    randomCol = random.nextInt(20);
                    placeCreature(randomRow, randomCol, centaur, board);
                    break;
                }
                case 2 : {
                    randomRow = random.nextInt(18) + 1;//exclude the first and the last row
                    randomCol = random.nextInt(20);
                    placeCreature(randomRow, randomCol, dragon, board);
                    break;
                }
                case 3 : {
                    randomRow = random.nextInt(18) + 1;//exclude the first and the last row
                    randomCol = random.nextInt(20);
                    placeCreature(randomRow, randomCol, phoenix, board);
                    break;
                }
                case 4 : {
                    randomRow = random.nextInt(18) + 1;//exclude the first and the last row
                    randomCol = random.nextInt(20);
                    placeCreature(randomRow, randomCol, sphinx, board);
                    break;
                }
            }
        }
    }

    public void moveAnimal(Animal animal, int oldRow, int oldCol, int newRow, int newCol) throws Exception {
        try{
            if(animal.getName().equals(moveOrder[turn])){
                animal.move(oldRow, oldCol, newRow, newCol);
                Animal preAnimal = findPreviousAnimal(animal);
                if (shieldAnimal.contains(preAnimal)){
                    shieldAnimal.remove(animal);
                }
                turn++;
                if(turn == moveOrder.length) turn = 0;
            }else{
                throw new Exception("You should move animals in order.");
            }
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }

    public Square getSquare(int row, int col){
        return board[row][col];
    }

    public void castSpell(Animal animal, Spell spell) throws Exception {
        if(animal.getName().equals(moveOrder[turn])){
            SpellType spellType = spell.getType();

            switch (spellType) {
                case DETECT:
                    int curRow = animal.getSquare(animal).getRow();
                    int curCol = animal.getSquare(animal).getCol();

                    int[] dirRow = { -1, 0, 1, -1, 1, -1, 0, 1 };
                    int[] dirCol = { -1, -1, -1, 0, 0, 1, 1, 1 };

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
                    if(nearCreature(animal) != null){
                        nearCreature(animal).setConfused(true);
                    }
                    break;
                case CHARM:
                    break;
            }
            turn++;
            if(turn == moveOrder.length) turn = 0;
        }else{
            throw new Exception("This is not your turn.");
        }

    }

    public void attackAnimal(Animal animal, Creature creature){

    }

    /*The methods below are only used in Game class, so they should be private*/
    private void initializeBoard() {
        for (int row = 0; row < ROW; row++) {
            for (int col = 0; col < COL; col++) {
                this.board[row][col] = new Square(row, col);
            }
        }
    }

    public Animal findPreviousAnimal(Animal animal){
        if(animal.getName().equals("Rabbit")) return animals.get(animals.size() - 1);
        else return animals.get(animals.indexOf(animal) - 1);
    }

    //This method place animals randomly at row 0
    private void placeAnimal(int randomCol, Animal animal, Square[][] board){
        while(true){
            try {
                //System.out.println("The random num is " + randomCol);
                board[0][randomCol].setAnimal(animal);
                //System.out.println("有动物： " + board[0][randomCol].isHasAnimal());
            }catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            //System.out.println("This animal has been placed at row 0, col " + randomCol);
            break;
        }
    }

    //This method place creatures randomly excluding the first and the last row
    private void placeCreature(int randomCol, int randomRow, Creature creature, Square[][] board){
        while(true){
            try {
                //System.out.println("The randomCol is " + randomCol + " and randomRow is " + randomRow);
                board[randomRow][randomCol].setCreature(creature);
                //System.out.println("有怪物： " + board[randomRow][randomCol].isHasCreature());
            }catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            //System.out.println("This creature has been placed at row " + randomRow + " col " + randomCol);
            break;
        }
    }

    private void placeSpell(){

    }

    public Creature nearCreature(Animal animal){
        int curRow = animal.getSquare(animal).getRow();
        int curCol = animal.getSquare(animal).getCol();

        int[] dirRow = { -1, 0, 1, -1, 1, -1, 0, 1 };
        int[] dirCol = { -1, -1, -1, 0, 0, 1, 1, 1 };

        for (int i = 0; i < 8; i++) {
            int adjacentRow = curRow + dirRow[i];
            int adjacentCol = curCol + dirCol[i];
            if(isValidCoordinate(adjacentRow, adjacentRow)){
                boolean hasCreature = this.board[adjacentRow][adjacentCol].isHasCreature();
                if(hasCreature){
                    Coordinate coordinate = new Coordinate(adjacentRow, adjacentCol);
                    return (Creature) findAnimalByCoordinate(objectPosition, coordinate);
                }
            }
        }
        return null;
    }

    private boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < ROW && col >= 0 && col < COL;
    }

    private Object findAnimalByCoordinate(HashMap<Object, Coordinate> map, Coordinate coordinate) {
        for (Map.Entry<Object, Coordinate> entry : map.entrySet()) {
            if (entry.getValue().equals(coordinate)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
