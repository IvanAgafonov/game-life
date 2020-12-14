package com.ivanagafonov;

import com.ivanagafonov.compound.Component;
import com.ivanagafonov.compound.GraphicLogicMediator;
import com.ivanagafonov.compound.Mediator;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class MainFrame extends JFrame implements Component {
    private JButton startButton = new JButton("Start");
    private JButton stopButton = new JButton("Stop");
    private JButton clearButton = new JButton("Clear");
    private JPanel buttons = new JPanel();
    private PlayingPanel playingPanel;
    private ExecutorService controlThread;
    private Mediator mediator;


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
            PlayingPanel playingPanel = new PlayingPanel(countRowsField, countColumnsFiled);
            mainFrame.addPlayingPanel(playingPanel);
            Mediator mediator = new GraphicLogicMediator(mainFrame, game, playingPanel.getField());
            mainFrame.setMediator(mediator);
            game.setMediator(mediator);
            playingPanel.setMediator(mediator);
        });
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getClearButton() {
        return startButton;
    }

    public JPanel getButtons() {
        return buttons;
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    class StartHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            startButton.setText("Running");
            startButton.setEnabled(false);
            clearButton.setEnabled(false);
            buttons.setMaximumSize(buttons.getPreferredSize());

            controlThread = Executors.newSingleThreadExecutor();
//            controlThread.submit(() -> playingPanel.getGame().play());
            controlThread.shutdown();
            Executors.newSingleThreadExecutor().submit(this::waitEnd); // FIXME Thread Pool | pattern Observer
        }

        private void waitEnd() {
            try {
                controlThread.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
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
                controlThread.shutdownNow();
            }
        }
    }

    class ClearHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
//            playingPanel.getGame().clear();
        }
    }
}
