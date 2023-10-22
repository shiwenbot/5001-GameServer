package test;

import Animal.Animal;
import Board.Game;
import org.junit.jupiter.api.Test;

import static Board.Game.animals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFindPreviousAnimal {

    @Test
    public void testFindPreviousAnimal() {
        // ���� Game ����� Animal ����
        Game game = Game.getInstance(1);

        // ���� findPreviousAnimal ����
        Animal previousRabbit = game.findPreviousAnimal(animals.get(0));
        //Animal previousFox = game.findPreviousAnimal(fox);
        //Animal previousBadger = game.findPreviousAnimal(badger);

        // ʹ�ö�����֤��������Ϊ�Ƿ����Ԥ��
        assertEquals(animals.get(4), previousRabbit);
        //assertEquals(owl, previousFox);
        //assertEquals(fox, previousBadger);
    }
}
