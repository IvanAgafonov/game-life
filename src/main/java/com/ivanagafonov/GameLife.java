package com.ivanagafonov;

import java.util.*;
import java.util.concurrent.*;

public class GameLife {

    private final ExecutorService threadPool;
    private final List<List<Boolean>> cells;
    private PlayingPanel.PlayingField playingField;
    private StatusEventManager statusEventManager;

    private final int countColumns;
    private final int countRows;
    private final int duration;

    GameLife(int countRows, int countColumns, int duration) {
        this.countRows = countRows;
        this.countColumns = countColumns;
        cells = new ArrayList<>(countRows);

        initField();

        statusEventManager = new StatusEventManager();
        threadPool = Executors.newFixedThreadPool(2);

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

    private void initField() {
        for (int i = 0; i < countRows; i++) {
            cells.add(i, new ArrayList<>(countColumns));
            for (int j = 0; j < countColumns; j++) {
                cells.get(i).add(j, false);
            }
        }
    }

    public void play () {
        int innerDuration = duration;
        boolean isFieldChanged;
        if (isEmptyField())
            randomFill();
        try {
            statusEventManager.notify(new StatusEvent(this, Status.RUNNING));
            while (innerDuration > 0) {
                isFieldChanged = iteration();
                if (!isFieldChanged)
                    break;
                innerDuration--;
            }
        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
        }
        finally {
            statusEventManager.notify(new StatusEvent(this, Status.STOPPED));
        }
    }

    public boolean iteration () throws ExecutionException, InterruptedException {
        ChangeCell callable;
        List<CellChangeFutureTask> results = new ArrayList<>();

        for (int i = 0; i < countRows; i++) {
            for (int j = 0; j < countColumns; j++) {
                if (cells.get(i).get(j)) {
                    callable = new DeathCallable(i, j);
                }
                else {
                    callable = new LifeCallable(i, j);
                }
                CellChangeFutureTask task = new CellChangeFutureTask(callable, i, j);
                results.add(task);
                threadPool.submit(task);
            }
        }

        boolean isFieldChanged = false;

        for (CellChangeFutureTask result: results) {
            Boolean isCellChange = null;

                isCellChange = result.get();

            if (isCellChange) {
                cells.get(result.getRow()).set(result.getColumn(),
                        !cells.get(result.getRow()).get(result.getColumn()));
                isFieldChanged = true;
            }
        }


        playingField.repaint();

        return isFieldChanged;
    }

    private void randomFill() {
        for (int i = 0; i < countRows; i++) {
            for (int j = 0; j < countColumns; j++) {
                cells.get(i).set(j, ThreadLocalRandom.current().nextBoolean());
            }
        }
    }

    public void clear () {
        for (int i = 0; i < countRows; i++) {
            for (int j = 0; j < countColumns; j++) {
                cells.get(i).set(j, false);
            }
        }

        playingField.repaint();
    }

    public void setPlayingField(PlayingPanel.PlayingField playingField) {
        this.playingField = playingField;
    }

    public int getCountColumns() {
        return countColumns;
    }

    public List<List<Boolean>> getCells() {
        return cells;
    }

    public int getCountRows() {
        return countRows;
    }

    public StatusEventManager getStatusEventManager() {
        return statusEventManager;
    }

    abstract class ChangeCell implements Callable<Boolean> {
        private final int row;
        private final int column;

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
                    cells.get(getRow()).get(getColumn() + 1))
                countNeighbors++;
            if (getRow() + 1 < countRows &&
                    cells.get(getRow() + 1).get(getColumn()))
                countNeighbors++;
            if ( getRow() + 1 < countRows && getColumn() + 1 < countColumns &&
                    cells.get(getRow() + 1).get(getColumn() + 1))
                countNeighbors++;
            if (getRow() > 0 &&
                    cells.get(getRow() - 1).get(getColumn()))
                countNeighbors++;
            if (getColumn() > 0 &&
                    cells.get(getRow()).get(getColumn() - 1))
                countNeighbors++;
            if (getRow() > 0 && getColumn() > 0 &&
                    cells.get(getRow() - 1).get(getColumn() - 1))
                countNeighbors++;
            if (getRow() + 1 < countRows  && getColumn() > 0 &&
                    cells.get(getRow() + 1).get(getColumn() - 1))
                countNeighbors++;
            if (getRow() > 0  && getColumn() + 1 < countColumns &&
                    cells.get(getRow() - 1).get(getColumn() + 1))
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
        public Boolean call() {
            return super.getCountNeighbors() == NEIGHBORS_FOR_NEW_LIFE;
        }
    }

    class DeathCallable extends ChangeCell {

        public static final int NEIGHBORS_FOR_ALONE_DEATH = 2;
        public static final int NEIGHBORS_FOR_FULLNESS_DEATH = 4;

        DeathCallable(int row, int column) {
            super(row, column);
        }

        @Override
        public Boolean call() {
            int countNeighbors = super.getCountNeighbors();

            return countNeighbors < NEIGHBORS_FOR_ALONE_DEATH ||
                    countNeighbors > NEIGHBORS_FOR_FULLNESS_DEATH;
        }
    }
}

