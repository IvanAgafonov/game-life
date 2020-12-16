package com.ivanagafonov;

import java.util.*;
import java.util.concurrent.*;

public class GameLife {

    private final ExecutorService threadPool;
    private Field field;
    private PlayingPanel.PlayingField playingField;
    private StatusEventManager statusEventManager;
    private GraphicEventManager graphicEventManager;

    private final int countColumns;
    private final int countRows;
    private final int duration;

    GameLife(Field field, int duration) {
        this.countRows = field.getRows();
        this.countColumns = field.getColumns();
        this.field = field;

        statusEventManager = new StatusEventManager();
        graphicEventManager = new GraphicEventManager();
        threadPool = Executors.newFixedThreadPool(2);

        this.duration = duration;
    }

    public void play (){
        int innerDuration = duration;
        boolean isFieldChanged;
        if (field.isEmptyField())
            field.randomFill();
        try {
            statusEventManager.notify(new StatusEvent(this, Status.RUNNING));
            while (innerDuration > 0) {
                isFieldChanged = iteration();
                if (!isFieldChanged)
                    break;
                innerDuration--;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
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
                if (field.getCell(i, j)) {
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

        for (CellChangeFutureTask result: results) {
            result.get();
        }

        boolean isFieldChanged = false;

        for (CellChangeFutureTask result: results) {
            Boolean isCellChange;

                isCellChange = result.get();

            if (isCellChange) {
                field.changeCell(result.getRow(), result.getColumn());
                isFieldChanged = true;
            }
        }

        graphicEventManager.notify(new EventObject(this));

        return isFieldChanged;
    }

    public void clear () {
        field.clear();

        graphicEventManager.notify(new EventObject(this));
    }

    public StatusEventManager getStatusEventManager() {
        return statusEventManager;
    }

    public GraphicEventManager getGraphicEventManager() {
        return graphicEventManager;
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

        public int getCountNeighbors() {
            int countNeighbors = 0;

            if (getColumn() + 1 < countColumns &&
                    field.getCell(getRow(), getColumn() + 1))
                countNeighbors++;
            if (getRow() + 1 < countRows &&
                    field.getCell(getRow() + 1, getColumn()))
                countNeighbors++;
            if ( getRow() + 1 < countRows && getColumn() + 1 < countColumns &&
                    field.getCell(getRow() + 1, getColumn() + 1))
                countNeighbors++;
            if (getRow() > 0 &&
                    field.getCell(getRow() - 1, getColumn()))
                countNeighbors++;
            if (getColumn() > 0 &&
                    field.getCell(getRow(), getColumn() - 1))
                countNeighbors++;
            if (getRow() > 0 && getColumn() > 0 &&
                    field.getCell(getRow() - 1, getColumn() - 1))
                countNeighbors++;
            if (getRow() + 1 < countRows  && getColumn() > 0 &&
                    field.getCell(getRow() + 1, getColumn() - 1))
                countNeighbors++;
            if (getRow() > 0  && getColumn() + 1 < countColumns &&
                    field.getCell(getRow() - 1, getColumn() + 1))
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

