package com.ivanagafonov;

public class Cell implements Cloneable{
    private boolean isCaptured = false;

    Cell() {}

    Cell(boolean isCaptured) {
        this.isCaptured = isCaptured;
    }

    public void changeCaptured() {
        isCaptured = !isCaptured;
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

    public void setCaptured(boolean captured) {
        isCaptured = captured;
    }

    @Override
    public Cell clone() {
        Cell cell = new Cell();
        cell.isCaptured = this.isCaptured;
        return cell;
    }

    @Override
    public int hashCode() {
        return isCaptured() ? 0 : 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Cell cell = (Cell) obj;
        return this.isCaptured() == cell.isCaptured();
    }
}


