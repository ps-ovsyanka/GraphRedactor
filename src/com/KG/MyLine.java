package com.KG;

//import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

import static java.lang.Math.abs;


public class MyLine extends MyComponent {
    MyPoint p1, p2;
    //Line2D line = new Line2D.Double(1,4,5,6);

    public int A;
    public int B;
    public int C;

    public MyLine (MyPoint p1, MyPoint p2) {
        super();

        this.p1 = p1;
        this.p2 = p2;
        color = new Color(196, 105, 30);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                if (abs(e.getX() - getCentre().getX()) < 5 & abs(e.getY() - getCentre().getY()) < 5) {
                    color = Color.green;
                }
                repaint();
            }
        });

    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g_ = (Graphics2D) g;
        g_.setColor(this.color);
        g_.setStroke(new BasicStroke(2));
        g_.draw(new Line2D.Double(p1.getX() + 2.5, p1.getY()+ 2.5, p2.getX()+ 2.5, p2.getY()+ 2.5));
        p1.paintComponent(g);
        p2.paintComponent(g);
        //if (isFocus()) paintFocus(g_);
    }

    @Override
    public boolean isFocus() {
        return p1.isFocus() & p2.isFocus();
    }

    @Override
    public void setFocus(boolean focus) {
        this.focus = focus;
        p1.setFocus(focus);
        p2.setFocus(focus);
    }

    //public Dimension getPreferredSize() { return new Dimension(abs((int)(getX1() - getX2())), abs((int) (getY1() - getY2()))); }

    public double getX1() { return p1.getX(); }
    public double getX2() { return p2.getX(); }
    public double getY1() { return p1.getY(); }
    public double getY2() { return p2.getY(); }

    public Point getCentre() { return new Point((int) (getX1()+getX2())/2, (int) (getY1()+getY2())/2); }

    /*public void setX1 (double x1) { line = new Line2D.Double(x1, getY1(), getX2(), getY2()); }
    public void setX2 (double x2) { line = new Line2D.Double(getX1(), getY1(), x2, getY2()); }
    public void setY1 (double y1) { line = new Line2D.Double(getX1(), y1, getX2(), getY2()); }
    public void setY2 (double y2) { line = new Line2D.Double(getX1(), getY1(), getX2(), y2); }*/

    @Override
    public void move(int x, int y){
        return;
    }

    @Override
    public void mirror(int coefX, int coefY) {
        return;
    }

    //x` = x0 + (x-x0)COS - (y-y0)SIN
    //y` = y0 + (x-x0)SIN + (y-y0)COS
    @Override
    public void rotateCentre(double angle) {

    }

    @Override
    public void rotate(int x, int y, int angle) {

    }

    public void scale (Point pointCentre, double scaleValue){return;}

    @Override
    public boolean contains(int x, int y) {
        return !p1.contains(x, y) & !p2.contains(x, y) &
                (abs((x - getX1())*(getY2()-getY1())-(y - getY1())*(getX2()-getX1())) < 1000
                & (x <= getX1() & x >= getX2() | x <= getX2() & x >= getX1()));
    }

    @Override
    public boolean contains(FocusRectangle focusRectangle) {
        return focusRectangle.contains(p1.getX(), p1.getY()) & focusRectangle.contains(p2.getX(), p2.getY());
    }


    ///y1-y2,x2-x1,x1*y2-x2*y1
    public String showInfo() {
        int x1 = CoordinateSystem.systemCoordToMyCoord(new Point((int) getX1(), (int) getY1())).x;
        int x2 = CoordinateSystem.systemCoordToMyCoord(new Point((int) getX2(), (int) getY2())).x;
        int y1 = CoordinateSystem.systemCoordToMyCoord(new Point((int) getX1(), (int) getY1())).y;
        int y2 = CoordinateSystem.systemCoordToMyCoord(new Point((int) getX2(), (int) getY2())).y;

        A = (y1 - y2);
        B = (x2 - x1);
        C = (x1*y2 - x2*y1);
        return "   " + A + "X + " + B + "Y + " + C + " = 0";
    }


}


class myPopupMenu extends JPopupMenu {
    JMenuItem anItem;
    Point p;
    public myPopupMenu() {
        anItem = new JMenuItem("Click Me!");
        add(anItem);
    }

}

class PopClickListener extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger())
            doPop(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger())
            doPop(e);
    }

    private void doPop(MouseEvent e) {
        myPopupMenu menu = new myPopupMenu();
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}
