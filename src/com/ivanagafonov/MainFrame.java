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

        buttons = new JPanel();
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        clearButton = new JButton("Clear");
        buttons.add(startButton);
        buttons.add(stopButton);
        buttons.add(clearButton);
        this.getContentPane().add(BorderLayout.SOUTH, buttons);
    }



    public static void main(String[] args) {
            EventQueue.invokeLater( () -> {
                MainFrame mainFrame = new MainFrame();
                mainFrame.getContentPane().add(new PlayingPanel(1, 1), 0);
            });
    }
}
