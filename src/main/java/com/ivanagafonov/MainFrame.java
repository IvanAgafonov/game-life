package com.ivanagafonov;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private JButton startButton = new JButton("Start");
    private JButton stopButton = new JButton("Stop");
    private JButton clearButton = new JButton("Clear");
    private JPanel buttons;
    private PlayingPanel playingPanel;
    private Thread controlThread;


    MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Game life");
        this.setResizable(true);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setVisible(true);

        buttons = new JPanel();
        startButton.addActionListener(new StartHandler());
        stopButton.addActionListener(new StopHandler());
        clearButton.addActionListener(new ClearHandler());
        buttons.add(startButton);
        buttons.add(stopButton);
        buttons.add(clearButton);
        buttons.setMaximumSize(buttons.getPreferredSize());

        this.getContentPane().add(buttons);

        this.addWindowStateListener(new WindowMinSizeHandler());
    }

    public void addPlayingPanel(PlayingPanel panel) {
        this.playingPanel = panel;
        Box box = new Box(BoxLayout.Y_AXIS);
        box.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        box.add(Box.createVerticalGlue());
        box.add(panel);
        box.add(Box.createVerticalGlue());
        add(box, 0);
    }

    public static void main(String[] args) {
        final int countRowsField, countColumnsFiled, countIterationFiled;

        if (args.length != 3)
            throw new IllegalArgumentException("There should be 3 parameters M, N, T (width, height, duration)");

        try {
            countRowsField = Integer.parseUnsignedInt(args[0]);
            countColumnsFiled = Integer.parseUnsignedInt(args[1]);
            countIterationFiled = Integer.parseUnsignedInt(args[2]);
        } catch (NumberFormatException e) {
            NumberFormatException e2 = new NumberFormatException("The parameters should be positive integer");
            e2.initCause(e);
            throw e2;
        }

        if (countRowsField == 0 || countColumnsFiled == 0 || countIterationFiled == 0)
            throw new NumberFormatException("Zero parameter");

        EventQueue.invokeLater( () -> {
            MainFrame mainFrame = new MainFrame();
            GameLife game = new GameLife(countRowsField, countColumnsFiled, countIterationFiled);
            mainFrame.addPlayingPanel(new PlayingPanel(game));
        });

    }


    class StartHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            startButton.setText("Running");
            startButton.setEnabled(false);
            clearButton.setEnabled(false);
            buttons.setMaximumSize(buttons.getPreferredSize());

            controlThread = new Thread(() -> playingPanel.getGame().play());
            controlThread.start();
            new Thread(this::run).start();
        }

        private void run() {
            try {
                controlThread.join();
                startButton.setText("Start");
                startButton.setEnabled(true);
                clearButton.setEnabled(true);
                buttons.setMaximumSize(buttons.getPreferredSize());
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }

    class StopHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (controlThread != null) {
                controlThread.interrupt();
            }
        }
    }

    class ClearHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            playingPanel.getGame().clear();
        }
    }
}
