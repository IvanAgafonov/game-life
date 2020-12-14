package com.ivanagafonov.compound;

import com.ivanagafonov.GameLife;
import com.ivanagafonov.MainFrame;
import com.ivanagafonov.PlayingPanel;

import java.util.EventObject;


public class GraphicLogicMediator implements Mediator {
    private GameLife gameLife;
    private PlayingPanel.PlayingField playingField;
    private MainFrame mainFrame;

    public GraphicLogicMediator(MainFrame mainFrame, GameLife gameLife, PlayingPanel.PlayingField playingField) {
        this.mainFrame = mainFrame;
        this.gameLife = gameLife;
        this.playingField = playingField;
    }

    @Override
    public void notify(EventObject event) {
        if (event instanceof CellEvent) {
            CellEvent cellEvent = (CellEvent) event;
            if (cellEvent.getSource().equals(gameLife)) {
                playingField.setCurrentCell(cellEvent.getCell());
                playingField.repaint();
            }
            else if (cellEvent.getSource().equals(playingField)) {
                gameLife.changeCell(cellEvent.getCell());
            }
        }
        else if (event instanceof StatusEvent) {
            StatusEvent statusEvent = (StatusEvent) event;
            if (statusEvent.getSource().equals(gameLife)) {
                Status status = statusEvent.getStatus();
                if (status == Status.RUNNING) {
                    mainFrame.getStartButton().setText("Running");
                    mainFrame.getStartButton().setEnabled(false);
                    mainFrame.getClearButton().setEnabled(false);
                    mainFrame.getButtons().setMaximumSize(mainFrame.getButtons().getPreferredSize());
                }
                else if (status == Status.STOPPED) {
                    mainFrame.getStartButton().setText("Start");
                    mainFrame.getStartButton().setEnabled(true);
                    mainFrame.getClearButton().setEnabled(true);
                    mainFrame.getButtons().setMaximumSize(mainFrame.getButtons().getPreferredSize());
                }
            }
        }
    }
}