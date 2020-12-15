package com.ivanagafonov;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

public class GraphicEventManager {
    List<GraphicEventListener> listenerList = new ArrayList<>();

    public void notify(EventObject event) {
        listenerList.forEach((o) -> o.updateGraphic());
    }

    public boolean subscribe(GraphicEventListener listener) {
        return listenerList.add(listener);
    }

    public boolean unsubscribe(GraphicEventListener listener) {
        return listenerList.remove(listener);
    }
}

interface GraphicEventListener extends EventListener {
    void updateGraphic();
}

