package test;

import Animal.Rabbit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class straightLine {

    @Test
    public void testIsStraightLineWithSameRow() {
        assertTrue(new Rabbit("").isStraightLine(0, 0, 0, 3));
    }

    @Test
    public void testIsStraightLineWithSameColumn() {
        assertTrue(new Rabbit("").isStraightLine(0, 1, 2, 1));
    }

    @Test
    public void testIsNotStraightLineWithInvalidMovement() {
        assertFalse(new Rabbit("").isStraightLine(0, 0, 1, 2));
    }

    @Test
    public void testIsNotStraightLineWithValidMovement() {
        assertTrue(new Rabbit("").isStraightLine(0, 0, 2, 2));
    }
}