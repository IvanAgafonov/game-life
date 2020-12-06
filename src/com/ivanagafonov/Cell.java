package com.ivanagafonov;

public class Cell {
    private int column, row;
    private boolean isCaptured;

    Cell(int row, int column) {
        this.column = column;
        this.row = row;
    }

    public void capture(){
        isCaptured = true;
    }
    public void release(){
        isCaptured = false;
    }
}


