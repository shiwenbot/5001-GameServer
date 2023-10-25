package test;

import Animal.Animal;
import Animal.Rabbit;
import Board.Coordinate;
import Board.Game;
import org.junit.jupiter.api.Test;

import static Board.Game.animals;
import static Board.Game.objectPosition;
import static Server.GameServerMain.seed;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestFindPreviousAnimal {

    @Test
    public void testFindPreviousAnimal() {
        // ���� Game ����� Animal.Animal ����
        Game game = Game.getInstance(seed);

        // ���� findPreviousAnimal ����
        Animal previousRabbit = game.findPreviousAnimal(animals.get(0));
        //Animal.Animal previousFox = game.findPreviousAnimal(fox);
        //Animal.Animal previousBadger = game.findPreviousAnimal(badger);

        // ʹ�ö�����֤��������Ϊ�Ƿ����Ԥ��
        assertEquals(animals.get(4), previousRabbit);
        //assertEquals(owl, previousFox);
        //assertEquals(fox, previousBadger);
    }

//    @Test
//    public void testGameMove(){
//        Game game = Game.getInstance(seed);
//        Rabbit rabbit = game.rabbit;
//
//        System.out.println("Rabbit is at row " + objectPosition.get(rabbit).getRow() + " and col " + objectPosition.get(rabbit).getCol());
//        //game.moveAnimal(rabbit, objectPosition.get(rabbit).getRow(), objectPosition.get(rabbit).getCol(), );
//        for (Object key : objectPosition.keySet()) {
//            Coordinate value = objectPosition.get(key);
//            System.out.println("Key: " + key + ", Value: " + value.getRow() + " " + value.getCol());
//        }
//    }



}
