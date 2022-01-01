package com.KG;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import static com.KG.CoordinateSystem.myCoordToSystemCoord;
import static com.KG.CoordinateSystem.systemCoordToMyCoord;
import static java.lang.Math.abs;

public class MyPoint extends MyComponent {
    public Point point;

    public int getX() { return point.x; }
    public int getY() { return point.y; }

    public void setX(int x) { point.x = x; }
    public void setY(int y) { point.y = y; }

    public MyPoint(int x, int y) {
        point = new Point(x, y);
        color = Color.green;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Ellipse2D e =new Ellipse2D.Double(point.x, point.y, 5, 5);
        Graphics2D g_ = (Graphics2D) g;
        g_.setColor(this.color);
        g_.setStroke(new BasicStroke(1));
        g_.draw(e);
        g_.fill(e);
        if (isFocus()) paintFocus(g_);
    }

    @Override
    public boolean isFocus() {
        return focus;
    }

    @Override
    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    private void paintFocus(Graphics2D g){
        Ellipse2D e =new Ellipse2D.Double(point.x-0.5, point.y-0.5, 7, 7);
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.red);
        g.draw(e);
    }

    @Override
    public String showInfo() {
        return "X: " + getX() + " Y: " + getY();
    }

    @Override
    public void moveTo(int x, int y) {
        int moveX = x - editPoint.x;
        int moveY = y - editPoint.y;
        setX(getX() + moveX);
        setY(getY() + moveY);
        editPoint = new Point((int)x, (int)y);
    }

    @Override
    public void mirror(int coefX, int coefY) {
        point = systemCoordToMyCoord(point);
        point.x *= coefX;
        point.y *= coefY;
        point = myCoordToSystemCoord(point);
    }

    @Override
    public boolean inhere(int x, int y) {
        return abs(point.x - x) < 5 & abs(point.y - y) < 5;
    }

    @Override
    public boolean inhere(FocusRectangle focusRectangle) {
        return focusRectangle.inhere(point.x, point.y);
    }
}
