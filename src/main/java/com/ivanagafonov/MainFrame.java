package com.ivanagafonov;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private JButton startButton;
    private JButton stopButton;
    private JButton clearButton;
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
        startButton = new JButton("Start");
        startButton.addActionListener(new StartHandler(this));
        stopButton = new JButton("Stop");
        stopButton.addActionListener(new StopHandler(this));
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ClearHandler(this));
        buttons.add(startButton);
        buttons.add(stopButton);
        buttons.add(clearButton);
        buttons.setMaximumSize(buttons.getPreferredSize());

        this.getContentPane().add(buttons);

        this.addWindowStateListener(new WindowHandler());
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
        final int M, N, T;

        if (args.length != 3) {
            System.err.println("There should be 3 parameters M, N, T (width, height, duration)");
            System.exit(-1);
        }
        try {
            M = Integer.parse   UnsignedInt(args[0]);
            N = Integer.parseUnsignedInt(args[1]);
            T = Integer.parseUnsignedInt(args[2]);
            if (M == 0 || N == 0 || T == 0)
                throw new NumberFormatException("Zero parameter");
            EventQueue.invokeLater( () -> {
                MainFrame mainFrame = new MainFrame();
                GameLife game = new GameLife(M, N, T);
                mainFrame.addPlayingPanel(new PlayingPanel(game));
            });
        } catch (NumberFormatException e) {  // FIXME too wide try catch
            System.err.println("The parameters should be positive integer");
            System.exit(-2);
        }
    }

    public PlayingPanel getPlayingPanel() {
        return playingPanel;
    }

    public JPanel getButtons() {
        return buttons;
    }

    public Thread getControlThread() {
        return controlThread;
    }

    public void setControlThread(Thread controlThread) {
        this.controlThread = controlThread;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    class StartHandler implements ActionListener {
        MainFrame outer;
        public StartHandler (MainFrame outer) {
            this.outer = outer;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            outer.getStartButton().setText("Running");
            outer.getStartButton().setEnabled(false);
            outer.getClearButton().setEnabled(false);
            outer.getButtons().setMaximumSize(buttons.getPreferredSize());

            outer.setControlThread(new Thread(() -> {outer.getPlayingPanel().getGame().play(); }));
            outer.getControlThread().start();
            new Thread(() -> {
                try {
                    outer.getControlThread().join();
                    outer.getStartButton().setText("Start");
                    outer.getStartButton().setEnabled(true);
                    outer.getClearButton().setEnabled(true);
                    outer.getButtons().setMaximumSize(buttons.getPreferredSize());
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }).start();
        }
    }

    class StopHandler implements ActionListener {
        MainFrame outer;
        public StopHandler (MainFrame outer) {
            this.outer = outer;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (outer.getControlThread() != null) {
                outer.getControlThread().interrupt();
            }
        }
    }

    class ClearHandler implements ActionListener {
        MainFrame outer;
        public ClearHandler (MainFrame outer) {
            this.outer = outer;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            outer.getPlayingPanel().getGame().clear();
        }
    }

    class WindowHandler implements WindowStateListener {

        public void windowStateChanged(WindowEvent e) {
            if (e.getSource() instanceof JFrame) {
                JFrame mainFrame = (JFrame) e.getSource();
                mainFrame.setMinimumSize(mainFrame.getPreferredSize());
            }
        }
    }
}
