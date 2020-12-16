package com.ivanagafonov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.ParameterizedType;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestChangeCell {
    private int rows = 5;
    private int columns = 5;
    private Field field;
    private GameLife gameLife;

    private static Stream<Arguments> provideEdgeCells() {
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(0, 4),
                Arguments.of(4, 0),
                Arguments.of(4, 4)
        );
    }

    @BeforeEach
    public void initGame() {
        field = new Field(5, 5);
        gameLife = new GameLife(field, 5);

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                field.changeCell(i, j);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("provideEdgeCells")
    public void testGetCountNeighborsTest_atTheEdges(int row, int column) {
        GameLife.ChangeCell callable = gameLife.new DeathCallable(row, column);

        int count = callable.getCountNeighbors();

        assertEquals(count, 3);
    }

    @Test
    public void testGetCountNeighborsTest_atTheCenter() {
        GameLife.ChangeCell callable = gameLife.new DeathCallable(2, 2);

        int count = callable.getCountNeighbors();

        assertEquals(count, 8);
    }

}
