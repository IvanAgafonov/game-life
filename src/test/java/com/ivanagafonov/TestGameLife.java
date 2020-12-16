package com.ivanagafonov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestGameLife {
    Field field;
    GameLife gameLife;
    int rows = 5 ;
    int columns = 5;
    int duration = 5;

    @BeforeEach
    public void initGame() {
        field = new Field(rows, columns);
        gameLife = new GameLife(field, duration);
    }

    @Test
    public void testGamePlay_prematureTermination() throws ExecutionException, InterruptedException {
        field.changeCell(2, 2);
        GameLife spyGameLife = Mockito.spy(gameLife);
        spyGameLife.play();

        Mockito.verify(spyGameLife, Mockito.times(2)).iteration();
    }

    @Test
    public void testGamePlay_equalsDurationToCountIterationIfNormalField() throws ExecutionException, InterruptedException {
        field.changeCell(1, 2);
        field.changeCell(2, 2);
        field.changeCell(3, 2);

        GameLife spyGameLife = Mockito.spy(gameLife);
        spyGameLife.play();

        Mockito.verify(spyGameLife, Mockito.times(duration)).iteration();
    }

    @Test
    public void testRandomFilledWhenPlay_ifEmptyFieldGiven() {
        field.clear();
        gameLife = new GameLife(field, 0);
        gameLife.play();

        assertFalse(field.isEmptyField());
    }

}
