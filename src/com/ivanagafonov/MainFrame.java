package com.ivanagafonov;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Game life");
        this.setResizable(true);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater( () -> {
            MainFrame mainFrame = new MainFrame();
        });
    }
}
