package test;

import Animal.Animal;
import Board.Game;
import org.junit.jupiter.api.Test;

import static Board.Game.animals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFindPreviousAnimal {

    @Test
    public void testFindPreviousAnimal() {
        // 创建 Game 对象和 Animal 对象
        Game game = Game.getInstance(1);

        // 测试 findPreviousAnimal 方法
        Animal previousRabbit = game.findPreviousAnimal(animals.get(0));
        //Animal previousFox = game.findPreviousAnimal(fox);
        //Animal previousBadger = game.findPreviousAnimal(badger);

        // 使用断言验证方法的行为是否符合预期
        assertEquals(animals.get(4), previousRabbit);
        //assertEquals(owl, previousFox);
        //assertEquals(fox, previousBadger);
    }
}
