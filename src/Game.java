import Animal.Animal;
import Animal.*;
import Creature.*;


import java.util.Random;

public class Game {
    final private int ROW = 20;
    final private int COL = 20;
    Square[][] board = new Square[ROW][COL];
    //int turn;

    /*
     *
     * */
    Game(long seed) {
        initializeBoard();

        //initialize animal and creatures
        Rabbit rabbit = new Rabbit("Rabbit");
        Fox fox = new Fox("Fox");
        Deer deer = new Deer("Deer");
        Owl owl = new Owl("Owl");
        Badger badger = new Badger("Badger");

        //initialize Creatures
        Unicorn unicorn = new Unicorn("Unicorn");
        Centaur centaur = new Centaur("Centaur");
        Dragon dragon = new Dragon("Dragon");
        Phoenix phoenix = new Phoenix("Phoenix");
        Sphinx sphinx = new Sphinx("Sphinx");

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

    private void initializeBoard() {
        for (int row = 0; row < ROW; row++) {
            for (int col = 0; col < COL; col++) {
                this.board[row][col] = new Square(row, col);
            }
        }
    }

    //This method place animals randomly at row 0
    private void placeAnimal(int randomCol, Animal animal, Square[][] board){
        while(true){
            try {
                System.out.println("The random num is " + randomCol);
                board[0][randomCol].setAnimal(animal);
                System.out.println("有动物： " + board[0][randomCol].isHasAnimal());
            }catch (CustomExceptions.AnimalExistsException e) {
                e.printStackTrace();
                continue;
            }
            System.out.println("This animal has been placed at row 0, col " + randomCol);
            break;
        }
    }

    //This method place creatures randomly excluding the first and the last row
    private void placeCreature(int randomCol, int randomRow, Creature creature, Square[][] board){
        while(true){
            try {
                System.out.println("The randomCol is " + randomCol + " and randomRow is " + randomRow);
                board[randomRow][randomCol].setCreature(creature);
                System.out.println("有怪物： " + board[randomRow][randomCol].isHasCreature());
            }catch (CustomExceptions.CreatureExistsException e) {
                e.printStackTrace();
                continue;
            }
            System.out.println("This creature has been placed at row " + randomRow + " col " + randomCol);
            break;
        }
    }
}
