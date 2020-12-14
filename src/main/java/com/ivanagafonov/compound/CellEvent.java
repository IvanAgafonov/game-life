package com.ivanagafonov.compound;

import java.util.EventObject;

public class CellEvent extends EventObject {
    private Cell cell;

    public CellEvent(Object source, Cell cell) {
        super(source);
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }
}

