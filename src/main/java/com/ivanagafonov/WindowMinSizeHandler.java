package com.ivanagafonov;

import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

class WindowMinSizeHandler implements WindowStateListener {

    public void windowStateChanged(WindowEvent e) {
        if (e.getSource() instanceof Component) {
            Component component = (Component) e.getSource();
            component.setMinimumSize(component.getPreferredSize());
        }
    }
}