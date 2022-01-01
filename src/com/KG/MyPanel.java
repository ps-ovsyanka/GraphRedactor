package com.KG;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class MyPanel extends JPanel {
    CoordinateSystem coordinateSystem = new CoordinateSystem();
    ArrayList<MyLine> lines = new ArrayList<>();
    ArrayList<MyPoint> points = new ArrayList<>();
    public MyLine currentLine = null;
    public Point currentPoint = null;
    public ArrayList<MyComponent> focus = new ArrayList<>();
    public ArrayList<MyComponent> components = new ArrayList<>();

    public MainFrame mainFrame;
    FocusRectangle focusRectangle;

    public MyPanel(){
        setBackground(Color.black);
        initPopupMenu();
        addListeners();
        setSize(1000,1000);
    }

    private void addListeners() {
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                currentPoint = new Point(e.getX(), e.getY());
                if (e.getButton() == MouseEvent.BUTTON1 & getCursor().getType() != Cursor.MOVE_CURSOR){
                    focusRectangle = new FocusRectangle();
                    focusRectangle.x1 = currentPoint.x;
                    focusRectangle.y1 = currentPoint.y;
                    focusRectangle.x2 = currentPoint.x;
                    focusRectangle.y2 = currentPoint.y;
                    /*if (getCursor().getType() != Cursor.DEFAULT_CURSOR) {
                        if (focusLine!=null) {
                            focusLine.setBackground(Color.white);
                            focusLine.editPoint = new Point(e.getX(), e.getY());
                        }

                        for (MyLine l: lines) {
                            if (l.BelongToLine(e.getX(), e.getY())) {
                                setFocusLine(l);
                            }
                        }
                    } else {
                        currentLine = new MyLine(e.getX(), e.getY(), e.getX(), e.getY());
                        setFocusLine(currentLine);
                    }*/

                } else if (e.getButton() == MouseEvent.BUTTON1 & getCursor().getType() == Cursor.MOVE_CURSOR) {
                    for (MyComponent c: focus) {
                        c.editPoint.x = e.getX();
                        c.editPoint.y = e.getY();
                    }
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (focusRectangle != null) {
                    focusRectangle.x2 = e.getX();
                    focusRectangle.y2 = e.getY();
                    clearFocus();
                    for (MyComponent c: components) {
                        if (c.inhere(focusRectangle))
                            addToFocus(c);
                    }
                    focusRectangle = null;
                }
                if (currentPoint.equals(e.getPoint())) {//проверка клика
                    boolean f = false;//флаг попадания объектов в фокус
                    for (MyComponent c: components) {
                        if (c.inhere(e.getX(), e.getY())) {
                            setFocus(c);
                            f = true;
                        }
                    }
                    if (!f)
                        clearFocus();
                }
                repaint();
            }

            @Override
            public void mouseClicked (MouseEvent e) {
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                CheckCursor(e.getX(), e.getY());
                CheckEquationLine();
                if (focusRectangle != null) {
                    focusRectangle.x2 = e.getX();
                    focusRectangle.y2 = e.getY();
                } else {
                    if (getCursor().getType() == Cursor.MOVE_CURSOR) {
                        for (MyComponent c: focus) {
                            c.moveTo(e.getX(), e.getY());
                        }
                    }
                }
                /*if (getCursor().getType() != Cursor.DEFAULT_CURSOR & focusLine != null) {
                    focusLine.MoveTo(e.getX(), e.getY());
                } else if (currentLine != null) {
                    currentLine.setX2(e.getX());
                    currentLine.setY2(e.getY());
                }*/
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                CheckCursor(e.getX(), e.getY());
                for (MyComponent component: components) {
                    if (component.inhere(e.getX(), e.getY())) {
                        if (component.isFocus())
                            setCursor(new Cursor(Cursor.MOVE_CURSOR));
                        else
                            setCursor(new Cursor(Cursor.HAND_CURSOR));
                        break;
                    } else
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
    }

    public void clearFocus() {
        for (MyComponent c: focus) {
            c.setFocus(false);
        }
        focus.clear();
    }

    public void CheckCursor(int x, int y){
        mainFrame.cursor.setText("Cursor  [" + x + " : " + y + "]");
        mainFrame.toolbarMode.repaint();
    }

    public void CheckEquationLine(){
        if (focus.size() == 1) {
            mainFrame.equationLine.setText("   Info:  " + focus.get(0).showInfo());
        } else
            mainFrame.equationLine.setText("");
        mainFrame.toolbarMode.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (MyComponent component: components) {
            component.paintComponent(g);
        }
        if (focusRectangle != null) {
            focusRectangle.paintComponent(g);
        }
        coordinateSystem.paintComponent(g);
    }


    public void setFocus(MyComponent component){
        for (MyComponent c: focus) {
            c.setFocus(false);
        }
        focus.clear();
        component.setFocus(true);
        focus.add(component);
        CheckEquationLine();
        repaint();
    }

    public void addToFocus(MyComponent component){
        component.setFocus(true);
        focus.add(component);
        repaint();
    }

    public void addPoint(int x, int y) {
        MyPoint p = new MyPoint(x, y);
        components.add(p);
        setFocus(p);
    }

    public void addLine(MyComponent p1, MyComponent p2) {
        MyLine l = new MyLine((MyPoint) p1, (MyPoint) p2);
        components.add(l);
        setFocus(l);
    }

    public void initPopupMenu(){
        JPopupMenu menu = new JPopupMenu();
        Action menuActionAddPoint = new AbstractAction("Add point") {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPoint(currentPoint.x, currentPoint.y);
                repaint();
            }
        };
        Action menuActionCreateLine = new AbstractAction("Create line") {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLine(focus.get(0), focus.get(1));
                repaint();
            }
        };
        Action menuActionMirrorX = new AbstractAction("Mirror X") {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (MyComponent c: focus) {
                    c.mirror(-1, 1);
                }
                repaint();
            }
        };
        Action menuActionMirrorY = new AbstractAction("Mirror Y") {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (MyComponent c: focus) {
                    c.mirror(1, -1);
                }
                repaint();
            }
        };
        Action menuActionDelete = new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (MyComponent c: focus) {
                    components.remove(c);
                }
                repaint();
            }
        };
        menu.add(menuActionAddPoint);
        menu.add(menuActionCreateLine);
        menu.add(menuActionMirrorX);
        menu.add(menuActionMirrorY);
        menu.add(menuActionDelete);
        setComponentPopupMenu(menu);
    }
}
