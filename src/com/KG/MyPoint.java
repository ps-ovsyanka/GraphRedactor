package com.KG;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import static com.KG.CoordinateSystem.myCoordToSystemCoord;
import static com.KG.CoordinateSystem.systemCoordToMyCoord;
import static java.lang.Math.*;

public class MyPoint extends MyComponent {
    Point point;

    public int getX() { return point.x; }
    public int getY() { return point.y; }

    public void setX(int x) { point.x = x; }
    public void setY(int y) { point.y = y; }

    public MyPoint(int x, int y) {
        point = new Point(x, y);
        color = new Color(0, 94, 77);
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Ellipse2D e = new Ellipse2D.Double(point.x, point.y, 5, 5);
        Graphics2D g_ = (Graphics2D) g;
        g_.setStroke(new BasicStroke(1));
        g_.draw(e);
        if (isFocus()) paintFocus(g_);
        else {
            g_.setColor(this.color);
            g_.fill(e);
        }
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
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
        g.setStroke(new BasicStroke(2));
        g.setColor(new Color(0, 94, 77));
        g.draw(e);
    }

    @Override
    public String showInfo() {
        Point p = CoordinateSystem.systemCoordToMyCoord(point);
        return "X: " + p.getX() + " Y: " + p.getY();
    }

    @Override
    public void move(int x, int y) {
        setX(getX() + x);
        setY(getY() + y);
    }

    @Override
    public void mirror(int coefX, int coefY) {
        point = systemCoordToMyCoord(point);
        point.x *= coefX;
        point.y *= coefY;
        point = myCoordToSystemCoord(point);
    }

    @Override
    public void rotateCentre(double angle) {
       // setX((int) (cos(angle)*point.x  - sin(angle)* point.y));
        //setY((int) (sin(angle)*point.x  + cos(angle)* point.y));
        Point newP = new Point();
        AffineTransform.getRotateInstance(angle,CoordinateSystem.x,CoordinateSystem.y).transform(point, newP);
        point = newP;
    }

    @Override
    public void rotate(int x, int y, int angle) {

    }

    public void scale (Point pointCentre, double scaleValue) {
        Point newP = systemCoordToMyCoord(point);
        Point point0 = new Point(0, 0);
        AffineTransform.getScaleInstance(scaleValue, scaleValue).transform(newP, newP);

        point = myCoordToSystemCoord(newP);
    }

    @Override
    public boolean contains(int x, int y) {
        return abs(point.x - x) < 5 & abs(point.y - y) < 5;
    }

    @Override
    public boolean contains(FocusRectangle focusRectangle) {
        return focusRectangle.contains(point.x, point.y);
    }
}
