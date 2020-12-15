package com.ivanagafonov;

import com.ivanagafonov.GameLife.LifeCallable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;


class GameLifeTest {
    @Test
    public void getCountNeighborsTest() {
        GameLife gameLife = new GameLife(5, 5, 5);

        gameLife.getCells().get(0).set(1, true);
        gameLife.getCells().get(1).set(1, true);
        gameLife.getCells().get(1).set(0, true);

        GameLife.ChangeCell callable = gameLife.new LifeCallable(0, 0);
        int count = callable.getCountNeighbors();

        assertEquals(count, 3);
    }

    @Test
    public void clearTest() {
        int rows = 5, columns = 5;

        GameLife gameLife = new GameLife(rows, columns, 0);
        PlayingPanel panel = new PlayingPanel(gameLife);
        gameLife.getCells().get(0).set(1, true);
        gameLife.getCells().get(1).set(4, true);
        gameLife.getCells().get(1).set(0, true);

        gameLife.clear();

        List<List<Boolean>> clearCells = new ArrayList<>(rows);
        for(int i = 0; i < rows; i++) {
            clearCells.add(new ArrayList<>(columns));
            for(int j =0; j < columns; j++) {
                clearCells.get(i).add(false);
            }
        }

        assertEquals(gameLife.getCells(), clearCells);
    }

}