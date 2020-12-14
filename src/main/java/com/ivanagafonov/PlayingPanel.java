package com.ivanagafonov;

import com.ivanagafonov.compound.Cell;
import com.ivanagafonov.compound.CellEvent;
import com.ivanagafonov.compound.Component;
import com.ivanagafonov.compound.Mediator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;


public class PlayingPanel extends JScrollPane implements Component {
    private final int rows, columns;
    private PlayingField field = new PlayingField();
    private Mediator mediator;
    private boolean initPaint = false;

    PlayingPanel(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        field.setPreferredSize(new Dimension(columns * PlayingField.sideSize, rows * PlayingField.sideSize));
        field.setBorder(BorderFactory.createLineBorder(Color.black));

        setViewportView(field);

        setMaximumSize(getPreferredSize());
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public class PlayingField extends JPanel {
        public static final int sideSize = 10;

        private Cell currentCell;

        PlayingField() {
            addMouseListener(new MouseHandler());
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            float x = 0, y = 0;

            Graphics2D g2 = (Graphics2D) g;
            if (!initPaint) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        Rectangle2D rect = new Rectangle2D.Float(x, y, sideSize, sideSize);
//                    if (game.getCells().get(i).get(j))
//                    {
//                        g2.setPaint(Color.GREEN);
//                        g2.fill(rect);
//                        g2.setPaint(Color.black);
//                    }
                        g2.draw(rect);
                        x += sideSize;
                    }
                    x = 0;
                    y += sideSize;
                }
                initPaint = true;
            }
            else {
                if (currentCell != null) {
                    y = sideSize * currentCell.getRow();
                    x = sideSize * currentCell.getColumn();
                    Rectangle2D rect = new Rectangle2D.Float(x, y, sideSize, sideSize);
                    if (currentCell.isFilled()) {
                        g2.setPaint(Color.GREEN);
                    }
                    else {
                        g2.setPaint(Color.WHITE);
                    }
                    g2.fill(rect);
                    g2.setPaint(Color.black);
                    g2.draw(rect);
                }
            }
        }

        public void setCurrentCell(Cell currentCell) {
            this.currentCell = currentCell;
        }
    }

    public PlayingPanel.PlayingField getField() {
        return field;
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

            Cell cell = new Cell(row, column);
            cell.setChanged(true);
            CellEvent cellEvent = new CellEvent(e.getSource(), cell);
            mediator.notify(cellEvent);
//            game.getCells().get(row).set(column, !game.getCells().get(row).get(column)); // FIXME OBSERVER | Mediator?
//            if (e.getSource() instanceof JComponent)
//                ((JComponent) e.getSource()).repaint();
        }
    }
}
