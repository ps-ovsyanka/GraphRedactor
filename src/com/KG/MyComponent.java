package com.KG;

import javax.swing.*;
import java.awt.*;

public abstract class MyComponent extends JComponent {
    boolean focus;
    Color color;
    public Point editPoint = new Point();
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public abstract boolean isFocus();
    public abstract void setFocus(boolean focus);
    public abstract String showInfo();

    public abstract void moveTo(int x, int y);
    public abstract void mirror(int x, int y);

    public abstract boolean inhere(int coefX, int coefY);
    public abstract boolean inhere(FocusRectangle focusRectangle);


}
