package com.ivanagafonov;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PlayingPanel extends JScrollPane {
    private PlayingField field;
    private final int rows, columns;

    PlayingPanel(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        setMaximumSize(new Dimension(rows * PlayingField.sideSize, columns * PlayingField.sideSize));

        field = new PlayingField();
        field.setPreferredSize(new Dimension(rows * PlayingField.sideSize, columns * PlayingField.sideSize));
        field.setBorder(BorderFactory.createLineBorder(Color.black));

        setViewportView(field);
    }

    class PlayingField extends JPanel {
        public static final int sideSize = 5;

        @Override
        public void paintComponent(Graphics g) {
            super.paint(g);
        }
    }

}
