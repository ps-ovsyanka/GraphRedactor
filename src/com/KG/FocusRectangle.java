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
        g_.setColor(Color.lightGray);
        g_.setStroke(new BasicStroke(1));
        g_.drawRect(x1 < x2 ? x1 : x2, y1 < y2 ? y1 : y2, abs(x2-x1), abs(y2-y1));
    }

    public boolean inhere(int x, int y) {
        Rectangle2D r = new Rectangle2D.Double(x1 < x2 ? x1 : x2, y1 < y2 ? y1 : y2, abs(x2-x1), abs(y2-y1));

        return r.contains(x, y);
    }
}

//x1-x + x2-x <= Math.abs(x2-x1) & y1-y + y2-y <= Math.abs(y2-y1)