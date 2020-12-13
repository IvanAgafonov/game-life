package com.ivanagafonov;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;


public class PlayingPanel extends JScrollPane {  // FIXME Not need to be specific class
    private final GameLife game;
    private final int rows, columns;
    private static final int MARGIN_WIDTH = 3;  // FIXME Get rid of Magic number to not appear slider

    PlayingPanel(GameLife game) {
        this.rows = game.getCountRows();
        this.columns = game.getCountColumns();
        this.game = game;

        setMaximumSize(new Dimension(columns * PlayingField.sideSize + MARGIN_WIDTH
                , rows * PlayingField.sideSize));

        PlayingField field = new PlayingField();
        field.setPreferredSize(new Dimension(columns * PlayingField.sideSize, rows * PlayingField.sideSize));
        field.setBorder(BorderFactory.createLineBorder(Color.black));

        game.setPlayingField(field);

        setViewportView(field);
    }

    class PlayingField extends JPanel {
        public static final int sideSize = 10;

        PlayingField() {
            addMouseListener(new MouseHandler());
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            float x = 0, y = 0;

            Graphics2D g2 = (Graphics2D) g;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    Rectangle2D rect = new Rectangle2D.Float(x, y, sideSize, sideSize);
                    if (game.getCells().get(i).get(j))
                    {
                        g2.setPaint(Color.GREEN);
                        g2.fill(rect);
                        g2.setPaint(Color.black);
                    }

                    g2.draw(rect);
                    x += sideSize;
                }
                x = 0;
                y += sideSize;
            }
        }
    }

    public GameLife getGame() {
        return game;
    }

    class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            int x = e.getX();
            int y = e.getY();
            int row = y/PlayingField.sideSize;
            if (row > rows-1)
                row = rows-1;
            int column = x/PlayingField.sideSize;
            if (column > columns-1)
                column = column-1;

            game.getCells().get(row).set(column, !game.getCells().get(row).get(column)); // FIXME OBSERVER
            if (e.getSource() instanceof JComponent)
                ((JComponent) e.getSource()).repaint();
        }
    }
}
