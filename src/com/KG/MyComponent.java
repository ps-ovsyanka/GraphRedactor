package com.KG;

import javax.swing.*;
import java.awt.*;

public abstract class MyComponent extends JComponent {
    boolean focus;
    Color color;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public abstract boolean isFocus();
    public abstract void setFocus(boolean focus);
    public abstract String showInfo();

    public abstract void move(int x, int y);
    public abstract void mirror(int x, int y);
    public abstract void rotateCentre(double corn);
    public abstract void rotate(int x, int y, int angle);
    public abstract void scale (Point pointCentre, double scaleValue);

    public abstract boolean contains(int coefX, int coefY);
    public abstract boolean contains(FocusRectangle focusRectangle);



}
