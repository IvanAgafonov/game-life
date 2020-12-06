package com.ivanagafonov;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.ConcurrentHashMap;

public class PlayingPanel extends JScrollPane {
    private PlayingField field;
    private final int rows, columns;
    private ConcurrentHashMap<Cell, Square> cellSquareConcurrentHashMap;
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

    class Square {
        private float x1, x2, y1, y2;

        Square (float x1, float x2, float y1, float y2) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
        }
    }
}
