package com.ivanagafonov;

import java.util.*;
import java.util.concurrent.*;

public class GameLife {
    List<List<Cell>> cells;
    List<List<Cell>> last_cells;

    public static void main(String[] argv) {
        GameLife gameLife = new GameLife();
        ArrayList<ArrayList<Cell>> field = new ArrayList<>();
        ArrayList<Cell> row1 = new ArrayList<Cell>();
        ArrayList<Cell> row2 = new ArrayList<Cell>();
        row1.add(new Cell(true));
        row1.add(new Cell(false));
        row2.add(new Cell());
        row2.add(new Cell());
        field.add(row1);
        field.add(row2);
        gameLife.play(field, 5);
    }

    public  <T extends List<? extends List<? extends Cell>> & Cloneable> void play (T initState, int duration) {
        cells = new ArrayList<>((ArrayList<ArrayList<Cell>>) initState);

        boolean isEmpty = true;
        for (int i = 0; i < cells.size(); i++) {
            ArrayList<Cell> t =  new ArrayList<>(Collections.synchronizedList(cells.get(i)));
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
        for (List<Cell> row: cells) {
            for (Cell cell: row) {
                if (cell.isCaptured()) {
                    break;
                }
            }
        }
    }

    private void randomFill() {}
}




