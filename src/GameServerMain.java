import Animal.Fox;
import Animal.Rabbit;
import Board.Coordinate;
import Board.Game;
import Creature.Creature;

import java.util.Map;

import static Board.Game.objectPosition;

public class GameServerMain {
    public static void main(String[] args) throws Exception {
        Game game = Game.getInstance(1);
        printCoordinates();
        Rabbit rabbit = game.rabbit;
        Fox fox = game.fox;
        Creature unicorn = game.unicorn;
        game.board[0][6].setCreature(unicorn);
        //System.out.println("The position of unicorn ");
        System.out.println(game.nearCreature(rabbit));
        //game.moveAnimal(rabbit, rabbit.getSquare(rabbit).getRow(), rabbit.getSquare(rabbit).getCol(), 1, 7);
        //System.out.println("Value of turn: " + game.turn);
    }


    public static void printCoordinates() {
        for (Map.Entry<Object, Coordinate> entry : objectPosition.entrySet()) {
            Coordinate coordinate = entry.getValue();
            System.out.println("Object: " + entry.getKey() + " - Row: " + coordinate.getRow() + ", Col: " + coordinate.getCol());
        }
    }
}
