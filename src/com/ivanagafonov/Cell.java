package com.ivanagafonov;

public class Cell implements Cloneable {
    private boolean isCaptured = false;

    Cell() {}

    Cell(boolean isCaptured) {
        this.isCaptured = isCaptured;
    }

    public void capture(){
        isCaptured = true;
    }
    public void release(){
        isCaptured = false;
    }

    public boolean isCaptured() {
        return isCaptured;
    }

    @Override
    public Cell clone() {
        Cell cell = new Cell();
        cell.isCaptured = this.isCaptured;
        return cell;
    }
}


