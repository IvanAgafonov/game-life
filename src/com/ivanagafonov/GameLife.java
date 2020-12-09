package com.ivanagafonov;

import java.util.*;
import java.util.concurrent.*;

public class GameLife {
    private List<List<Cell>> cells;
    private List<List<Cell>> last_cells;
    private int countColumns;
    private int countRows;
    private int duration;

    <T extends List<? extends List<? extends Cell>> & Cloneable> GameLife(T initState, int duration) {
        cells = new ArrayList<>((ArrayList<ArrayList<Cell>>) initState);
        last_cells = new ArrayList<>((ArrayList<ArrayList<Cell>>) initState);
        countColumns = cells.get(0).size();
        countRows = cells.size();

        this.duration = duration;
    }

    public static void main(String[] argv) {
        ArrayList<ArrayList<Cell>> field = new ArrayList<>();
        ArrayList<Cell> row1 = new ArrayList<Cell>();
        ArrayList<Cell> row2 = new ArrayList<Cell>();
        row1.add(new Cell(true));
        row1.add(new Cell(false));
        row2.add(new Cell());
        row2.add(new Cell());
        field.add(row1);
        field.add(row2);
        GameLife gameLife = new GameLife(field, 5);
        gameLife.play();
    }

    public void play () {

        boolean isEmpty = true;
        for (int i = 0; i < cells.size(); i++) {
            cells.set(i, Collections.synchronizedList(cells.get(i)));
            for (int j = 0; j < cells.get(i).size(); j++) {
                if (cells.get(i).get(j).isCaptured()) {
                    isEmpty = false;
                }
            }
        }

        if (isEmpty)
            randomFill();

        while (duration > 0) {
            iteration();
            duration--;
        }

    }

    public void iteration () {
        ChangeCell callable;
        List<Future<CellInfo>> results = new ArrayList<>();
        for (int i = 0; i < last_cells.size(); i++) {
            for (int j = 0; j < last_cells.get(i).size(); j++) {
                if (last_cells.get(i).get(j).isCaptured()) {
                    callable = new DeathCallable(i, j);
                }
                else {
                    callable = new LifeCallable(i, j);
                }
                FutureTask<CellInfo> task = new FutureTask<>(callable);
                results.add(task);
                Thread thread = new Thread(task);
                thread.start();
            }
        }

        for (Future<CellInfo> result: results) {
            try {
                CellInfo cellInfo = result.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        last_cells = new ArrayList<>(cells);
    }

    private void randomFill() {}

    abstract class ChangeCell implements Callable<CellInfo> {
        private int row;
        private int column;

        ChangeCell(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        private int getCountNeighbors() {
            int countNeighbors = 0;

            if (getColumn() + 1 < countColumns &&
                    last_cells.get(getRow()).get(getColumn() + 1).isCaptured())
                countNeighbors++;
            if (getRow() + 1 < countRows &&
                    last_cells.get(getRow() + 1).get(getColumn()).isCaptured())
                countNeighbors++;
            if ( getRow() + 1 < countRows && getColumn() + 1 < countColumns &&
                    last_cells.get(getRow() + 1).get(getColumn() + 1).isCaptured())
                countNeighbors++;
            if (getRow() > 0 &&
                    last_cells.get(getRow() - 1).get(getColumn()).isCaptured())
                countNeighbors++;
            if (getColumn() > 0 &&
                    last_cells.get(getRow()).get(getColumn() - 1).isCaptured())
                countNeighbors++;
            if (getRow() > 0 && getColumn() > 0 &&
                    last_cells.get(getRow() - 1).get(getColumn() - 1).isCaptured())
                countNeighbors++;
            if (getRow() + 1 < countRows  && getColumn() > 0 &&
                    last_cells.get(getRow() + 1).get(getColumn() - 1).isCaptured())
                countNeighbors++;
            if (getRow() > 0  && getColumn() + 1 < countColumns &&
                    last_cells.get(getRow() - 1).get(getColumn() + 1).isCaptured())
                countNeighbors++;

            return countNeighbors;
        }
    }

    class LifeCallable extends ChangeCell {

        public static final int NEIGHBORS_FOR_NEW_LIFE = 3;

        LifeCallable(int row, int column) {
            super(row, column);
        }

        @Override
        public CellInfo call() throws Exception {
            boolean isLife = super.getCountNeighbors() == NEIGHBORS_FOR_NEW_LIFE;

            if (isLife)
                cells.get(getRow()).get(getColumn()).capture();

            return new CellInfo(getRow(), getColumn(), isLife);
        }
    }

    class DeathCallable extends ChangeCell {

        public static final int NEIGHBORS_FOR_ALONE_DEATH = 2;
        public static final int NEIGHBORS_FOR_FULLNESS_DEATH = 4;

        DeathCallable(int row, int column) {
            super(row, column);
        }

        @Override
        public CellInfo call() throws Exception {
            int countNeighbors = super.getCountNeighbors();

            boolean isDeath = countNeighbors < NEIGHBORS_FOR_ALONE_DEATH ||
                    countNeighbors > NEIGHBORS_FOR_FULLNESS_DEATH;

            if (isDeath)
                cells.get(getRow()).get(getColumn()).release();

            return new CellInfo(getRow(), getColumn(), isDeath);
        }
    }
}

class CellInfo {
    private int row;
    private int column;
    private boolean isChange;

    CellInfo(int row, int column, boolean isChange) {
        this.row = row;
        this.column = column;
        this.isChange = isChange;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isChange() {
        return isChange;
    }
}



