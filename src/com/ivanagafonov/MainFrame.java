package com.ivanagafonov;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private JButton startButton;
    private JButton stopButton;
    private JButton clearButton;
    private JPanel buttons;

    MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Game life");
        this.setResizable(true);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setVisible(true);
    }

    public static void main(String[] args) {
            EventQueue.invokeLater( () -> {
                MainFrame mainFrame = new MainFrame();
                mainFrame.getContentPane().add(BorderLayout.NORTH, new PlayingPanel(1, 1));
            });
    }
}
