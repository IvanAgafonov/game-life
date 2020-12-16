package com.ivanagafonov;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Field {
    private int rows;
    private int columns;
    private List<List<Boolean>> cells;

    Field(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        initField();
    }

    private void initField() {
        cells = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            cells.add(i, Collections.synchronizedList(new ArrayList<>(columns)));
            for (int j = 0; j < columns; j++) {
                cells.get(i).add(j, false);
            }
        }
    }

    public boolean isEmptyField() {
        boolean isEmpty = true;
        for (List<Boolean> row : cells) {
            for (boolean cell : row) {
                if (cell) {
                    isEmpty = false;
                    break;
                }
            }
        }

        return isEmpty;
    }

    public void randomFill() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells.get(i).set(j, ThreadLocalRandom.current().nextBoolean());
            }
        }
    }

    public void clear () {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells.get(i).set(j, false);
            }
        }
    }

    public boolean getCell(int row, int column) {
        return cells.get(row).get(column);
    }

    public void changeCell(int row, int column) {
        cells.get(row).set(column,
                !cells.get(row).get(column));
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
