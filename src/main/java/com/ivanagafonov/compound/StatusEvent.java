package com.ivanagafonov.compound;

import java.util.EventObject;

public class StatusEvent extends EventObject {
    private Status status;

    public StatusEvent(Object source, Status status) {
        super(source);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}

enum Status {RUNNING, STOPPED}
