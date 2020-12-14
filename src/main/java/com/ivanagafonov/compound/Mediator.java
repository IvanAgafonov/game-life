package com.ivanagafonov.compound;

import java.util.EventObject;

public interface Mediator {
    void notify(EventObject event);
}
