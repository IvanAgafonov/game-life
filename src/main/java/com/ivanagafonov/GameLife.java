package com.ivanagafonov;

import java.util.*;
import java.util.concurrent.*;

public class GameLife {

    private List<List<Boolean>> cells;
    private List<List<Boolean>> last_cells;
    private PlayingPanel.PlayingField playingField;

    public int getCountColumns() {
        return countColumns;
    }

    public List<List<Boolean>> getCells() {
        return cells;
    }

    public int getCountRows() {
        return countRows;
    }

    private int countColumns;
    private int countRows;
    private int duration;

    GameLife(int countRows, int countColumns, int duration) {
        this.countRows = countRows;
        this.countColumns = countColumns;
        cells = new ArrayList<>(countRows);

        for (int i = 0; i < countRows; i++) {
            cells.add(i, Collections.synchronizedList(new ArrayList<>(countColumns)));
            for (int j = 0; j < countColumns; j++) {
                cells.get(i).add(j, false);
            }
        }

        last_cells = ListHelper.deepCopy2D(cells);
        this.duration = duration;
    }

    private boolean isEmptyField() {
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

    public void play () {
        int innerDuration = duration;
        try {
            if (isEmptyField())
                randomFill();

            while (innerDuration > 0) {
                iteration();
                if (ListHelper.deepEquals2D(cells, last_cells))
                    break;
                last_cells = ListHelper.deepCopy2D(cells);
                innerDuration--;
                Thread.sleep(1);
            }
        } catch (InterruptedException | ExecutionException e) {
            //e.printStackTrace();
            //TODO Handle
        }

    }

    public void iteration () throws InterruptedException, ExecutionException {
        ChangeCell callable;
        List<Future<CellInfo>> results = new ArrayList<>();
        for (int i = 0; i < countRows; i++) {
            for (int j = 0; j < countColumns; j++) {
                if (last_cells.get(i).get(j)) {
                    callable = new DeathCallable(i, j);
                }
                else {
                    callable = new LifeCallable(i, j);
                }
                FutureTask<CellInfo> task = new FutureTask<>(callable);
                results.add(task);
                Thread thread = new Thread(task);  // FIXME Сделать пул потоков
                thread.start();
            }
        }

        for (Future<CellInfo> result: results) {
                CellInfo cellInfo = result.get();  // FIXME Принимать результаты и применять к массиву. Убрать 2-ой массив
        }
        playingField.repaint();  // FIXME применить паттерн Observer
    }

    private void randomFill() {
        for (List<Boolean> row : last_cells) {
            for (boolean cell : row) {
                cell = ThreadLocalRandom.current().nextBoolean();
            }
        }
    }

    public void clear () {
        for (int i = 0; i < countRows; i++) {
            for (int j = 0; j < countColumns; j++) {
                cells.get(i).set(j, false);
            }
        }

        for (int i = 0; i < countRows; i++) {
            for (int j = 0; j < countColumns; j++) {
                last_cells.get(i).set(j, false);
            }
        }

        playingField.repaint();
    }

    public void setPlayingField(PlayingPanel.PlayingField playingField) {
        this.playingField = playingField;
    }

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
                    last_cells.get(getRow()).get(getColumn() + 1))
                countNeighbors++;
            if (getRow() + 1 < countRows &&
                    last_cells.get(getRow() + 1).get(getColumn()))
                countNeighbors++;
            if ( getRow() + 1 < countRows && getColumn() + 1 < countColumns &&
                    last_cells.get(getRow() + 1).get(getColumn() + 1))
                countNeighbors++;
            if (getRow() > 0 &&
                    last_cells.get(getRow() - 1).get(getColumn()))
                countNeighbors++;
            if (getColumn() > 0 &&
                    last_cells.get(getRow()).get(getColumn() - 1))
                countNeighbors++;
            if (getRow() > 0 && getColumn() > 0 &&
                    last_cells.get(getRow() - 1).get(getColumn() - 1))
                countNeighbors++;
            if (getRow() + 1 < countRows  && getColumn() > 0 &&
                    last_cells.get(getRow() + 1).get(getColumn() - 1))
                countNeighbors++;
            if (getRow() > 0  && getColumn() + 1 < countColumns &&
                    last_cells.get(getRow() - 1).get(getColumn() + 1))
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
        public CellInfo call() {
            boolean isLife = super.getCountNeighbors() == NEIGHBORS_FOR_NEW_LIFE;

            if (isLife)
                cells.get(getRow()).set(getColumn(), true);

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
        public CellInfo call() {
            int countNeighbors = super.getCountNeighbors();

            boolean isDeath = countNeighbors < NEIGHBORS_FOR_ALONE_DEATH ||
                    countNeighbors > NEIGHBORS_FOR_FULLNESS_DEATH;

            if (isDeath)
                cells.get(getRow()).set(getColumn(), false);

            return new CellInfo(getRow(), getColumn(), isDeath);
        }
    }
}

class CellInfo {  // FIXME Зачем?
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



