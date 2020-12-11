package com.ivanagafonov;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

class WindowMinSizeHandler implements WindowStateListener {

    public void windowStateChanged(WindowEvent e) {
        if (e.getSource() instanceof JFrame) {
            JFrame mainFrame = (JFrame) e.getSource();
            mainFrame.setMinimumSize(mainFrame.getPreferredSize());
        }
    }
}