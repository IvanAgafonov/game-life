package com.ivanagafonov;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PlayingPanel extends JScrollPane {
    private PlayingField field;
    private final int rows, columns;
    private static final int MARGIN_WIDTH = 3;  // Magic number to not appear slider

    PlayingPanel(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        setMaximumSize(new Dimension(columns * PlayingField.sideSize + MARGIN_WIDTH
                , rows * PlayingField.sideSize));

        field = new PlayingField();
        field.setPreferredSize(new Dimension(columns * PlayingField.sideSize, rows * PlayingField.sideSize));
        field.setBorder(BorderFactory.createLineBorder(Color.black));

        setViewportView(field);
    }

    class PlayingField extends JPanel {
        public static final int sideSize = 10;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            float x = 0, y = 0;

            Graphics2D g2 = (Graphics2D) g;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    Rectangle2D rect = new Rectangle2D.Float(x, y, sideSize, sideSize);
                    g2.draw(rect);
                    x += sideSize;
                }
                x = 0;
                y += sideSize;
            }
        }
    }
}
