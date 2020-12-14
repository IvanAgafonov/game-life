package com.ivanagafonov;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class CellChangeFutureTask extends FutureTask<Boolean> {
    private final int row;
    private final int column;

    public CellChangeFutureTask(Callable<Boolean> callable, int row, int column) {
        super(callable);
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
