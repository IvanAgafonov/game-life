package com.ivanagafonov;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;


public class PlayingPanel extends JScrollPane implements GraphicEventListener{
    private final Field field;
    private final int rows, columns;
    private PlayingField innerFieldPanel;

    PlayingPanel(Field field) {
        this.rows = field.getRows();
        this.columns = field.getColumns();
        this.field = field;

        innerFieldPanel = new PlayingField();
        innerFieldPanel.setPreferredSize(new Dimension(columns * PlayingField.sideSize,
                rows * PlayingField.sideSize));
        innerFieldPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        setViewportView(innerFieldPanel);

        setMaximumSize(getPreferredSize());
    }

    @Override
    public void updateGraphic() {
        innerFieldPanel.repaint();
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
                    if (field.getCell(i, j))
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

            field.changeCell(row, column);
            if (e.getSource() instanceof JComponent)
                ((JComponent) e.getSource()).repaint();
        }
    }
}
