import Animal.Animal;
import Animal.*;


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

        Rabbit rabbit = new Rabbit("Rabbit");
        Fox fox = new Fox("Fox");

        Badger badger = new Badger("Badger");

        Random random = new Random(seed);
        int randomCol, randomRow;
        for (int i = 0; i < 2; i++) {
            switch (i) {
                case 0 : {
                    randomCol = random.nextInt(20);
                    while(true){
                        try {
                            randomCol = random.nextInt(20);
                            System.out.println("The random num is " + randomCol);
                            board[0][randomCol].setAnimal(rabbit);
                            System.out.println("有动物： " + board[0][randomCol].hasAnimal());
                        }catch (CustomExceptions.AnimalExistsException e) {
                            e.printStackTrace();
                            continue;
                        }
                        System.out.println("This animal has been placed at row 0, col " + randomCol);
                        break;
                    }
                }
                case 1 : {
                    while(true){
                        try {
                            randomCol = random.nextInt(20);
                            System.out.println("The random num is " + randomCol);
                            board[0][randomCol].setAnimal(fox);
                            System.out.println("有动物： " + board[0][randomCol].hasAnimal());
                        }catch (CustomExceptions.AnimalExistsException e) {
                            e.printStackTrace();
                            continue;
                        }
                        System.out.println("This animal has been placed at row 0, col " + randomCol);
                        break;
                    }
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

    private void placeAnimal(int randomCol, Animal animal, Square[][] board){
        while(true){
            try {
                System.out.println("The random num is " + randomCol);
                board[0][randomCol].setAnimal(animal);
                System.out.println("有动物： " + board[0][randomCol].hasAnimal());
            }catch (CustomExceptions.AnimalExistsException e) {
                e.printStackTrace();
                continue;
            }
            System.out.println("This animal has been placed at row 0, col " + randomCol);
            break;
        }
    }
}
