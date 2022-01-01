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
        g_.setColor(Color.blue);
        g_.setStroke(new BasicStroke(1));
        g_.drawLine(x - width, y, x + width, y);//ось х

        g_.drawLine(x, y - height, x, y + height);//ось у
    }

    public static Point systemCoordToMyCoord(Point systemPoint){
        Point myCoord = new Point(systemPoint.x - x, systemPoint.y - y);
        return myCoord;
    }
    public static Point myCoordToSystemCoord(Point myCoord){
        Point systemPoint = new Point(myCoord.x + x, myCoord.y + y);
        return systemPoint;
    }
}
