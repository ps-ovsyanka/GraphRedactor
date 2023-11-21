package com.KG;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import static java.lang.Math.abs;

public class FocusRectangle extends JComponent {
    int x1 = 0, x2 = 0, y1 = 0, y2 = 0;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g_ = (Graphics2D) g;
        g_.setColor(Color.BLUE);
        g_.setStroke(new BasicStroke(1));
        g_.drawRect(Math.min(x1, x2), Math.min(y1, y2), abs(x2-x1), abs(y2-y1));
    }

    public boolean contains(int x, int y) {
        Rectangle2D r = new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2), abs(x2-x1), abs(y2-y1));

        return r.contains(x, y);
    }
}

//x1-x + x2-x <= Math.abs(x2-x1) & y1-y + y2-y <= Math.abs(y2-y1)