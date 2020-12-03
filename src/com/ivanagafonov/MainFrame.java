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
        final int M, N, T;

        if (args.length != 3) {
            System.err.println("There should be 3 parameters M, N, T (width, height, duration)");
            System.exit(-1);
        }
        try {
            M = Integer.parseUnsignedInt(args[0]);
            N = Integer.parseUnsignedInt(args[1]);
            T = Integer.parseUnsignedInt(args[2]);
            if (M == 0 || N == 0 || T == 0)
                throw new NumberFormatException("Zero parameter");
            EventQueue.invokeLater( () -> {
                MainFrame mainFrame = new MainFrame();
                mainFrame.getContentPane().add(new PlayingPanel(M, N), 0);
            });
        } catch (NumberFormatException e) {  // FIXME too wide try catch
            System.err.println("The parameters should be positive integer");
            System.exit(-2);
        }
    }
}
