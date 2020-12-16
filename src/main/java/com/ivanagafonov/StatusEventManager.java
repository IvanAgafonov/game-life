package com.ivanagafonov;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

public class StatusEventManager {
    List<StatusEventListener> listenerList = new ArrayList<>();

    public void notify(StatusEvent event) {
        listenerList.forEach((o) -> o.update(event.getStatus()));
    }

    public boolean subscribe(StatusEventListener listener) {
        return listenerList.add(listener);
    }

    public boolean unsubscribe(StatusEventListener listener) {
        return listenerList.remove(listener);
    }
}

enum Status {RUNNING, STOPPED}

interface StatusEventListener extends EventListener {
    void update(Status status);
}

class StatusEvent extends EventObject {
    private Status status;

    public StatusEvent(Object source, Status status) {
        super(source);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}