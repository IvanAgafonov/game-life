package com.ivanagafonov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestField {
    int rows = 5;
    int columns = 5;
    Field field;

    @BeforeEach
    public void initField() {
        field = new Field(rows, columns);
    }

    @Test
    public void testChangeOneCell() {
        int row = 1, column = 1;

        boolean value = field.getCell(row, column);
        field.changeCell(row, column);
        assertEquals(!value, field.getCell(row, column));
    }

    @Test
    public void testIsInitEmpty() {
        assertTrue(field.isEmptyField());
    }

    @Test
    public void testIsEmptyField_ifOneFilled() {
        field.clear();
        field.changeCell(1, 1);

        assertFalse(field.isEmptyField());
    }

    @Test
    public void testIsEmptyField_ifNoOneFilled() {
        field.clear();

        assertTrue(field.isEmptyField());
    }

    @Test
    public void testClearIfAllFilled() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                field.changeCell(i, j);
            }
        }

       List<List<Boolean>> clearCells = new ArrayList<>(rows);
        for(int i = 0; i < rows; i++) {
            clearCells.add(new ArrayList<>(columns));
            for(int j =0; j < columns; j++) {
                clearCells.get(i).add(false);
            }
        }

        field.clear();

        for(int i = 0; i < rows; i++) {
            for(int j =0; j < columns; j++) {
                assertFalse(field.getCell(i, j));
            }
        }
    }
}
