package com.KG;

import javax.swing.*;
import java.awt.*;

public class CoordinateSystem extends JComponent {
    final static int x = 440, y = 300;
    final static int width = 200, height = 200;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g_ = (Graphics2D) g;
        g_.setColor(new Color(77, 137, 198));
        g_.setStroke(new BasicStroke(1));
        g_.drawLine(x - width, y, x + width, y);//ось х
        g_.drawLine(x, y - height, x, y + height);//ось у
     }

    public static Point systemCoordToMyCoord(Point systemPoint){
        return new Point(systemPoint.x - x, systemPoint.y - y);
    }
    public static Point myCoordToSystemCoord(Point myCoord){
        return new Point(myCoord.x + x, myCoord.y + y);
    }
}
