package com.ivanagafonov;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class MainFrame extends JFrame implements StatusEventListener {
    private JButton startButton = new JButton("Start");
    private JButton stopButton = new JButton("Stop");
    private JButton clearButton = new JButton("Clear");
    private JPanel buttons = new JPanel();
    private PlayingPanel playingPanel;
    private ExecutorService controlThread;
    private GameLife gameLife;


    MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Game life");
        this.setResizable(true);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setVisible(true);

        startButton.addActionListener(new StartHandler());
        stopButton.addActionListener(new StopHandler());
        clearButton.addActionListener(new ClearHandler());
        buttons.add(startButton);
        buttons.add(stopButton);
        buttons.add(clearButton);
        buttons.setMaximumSize(buttons.getPreferredSize());

        this.getContentPane().add(buttons);
    }

    public void setMinSizePreferred() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth()/2;
        int screenHeight = gd.getDisplayMode().getHeight()/2;

        int mainFrameMinHeight = Math.min(getPreferredSize().height, screenHeight);
        int mainFrameMinWidth = Math.min(getPreferredSize().width, screenWidth);

        setMinimumSize(new Dimension(mainFrameMinWidth, mainFrameMinHeight));
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
            Field field = new Field(countRowsField, countColumnsFiled);
            GameLife game = new GameLife(field, countIterationFiled);
            mainFrame.setGameLife(game);
            PlayingPanel playingPanel = new PlayingPanel(field);
            mainFrame.addPlayingPanel(playingPanel);
            game.getStatusEventManager().subscribe(mainFrame);
            game.getGraphicEventManager().subscribe(playingPanel);
            mainFrame.setMinSizePreferred();
        });

    }

    @Override
    public void update(Status status) {
        if (status == Status.RUNNING) {
            startButton.setText("Running");
            startButton.setEnabled(false);
            clearButton.setEnabled(false);
            buttons.setMaximumSize(buttons.getPreferredSize());
        }
        else if (status == Status.STOPPED) {
            startButton.setText("Start");
            startButton.setEnabled(true);
            clearButton.setEnabled(true);
            buttons.setMaximumSize(buttons.getPreferredSize());
        }
    }

    public void setGameLife(GameLife gameLife) {
        this.gameLife = gameLife;
    }


    class StartHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            controlThread = Executors.newSingleThreadExecutor();
            controlThread.submit(() -> gameLife.play());
        }
    }

    class StopHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (controlThread != null) {
                controlThread.shutdownNow();
            }
        }
    }

    class ClearHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            gameLife.clear();
        }
    }
}
