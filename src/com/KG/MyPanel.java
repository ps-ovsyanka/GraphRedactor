package com.KG;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.CubicCurve2D;
import java.util.HashSet;
import static com.KG.CoordinateSystem.*;
import static java.lang.Math.*;

public class MyPanel extends JPanel {
    CoordinateSystem coordinateSystem = new CoordinateSystem();
    public Point editPoint = null;
    public HashSet<MyComponent> focus = new HashSet<>();
    public HashSet<MyComponent> components = new HashSet<>();
    public HashSet<CubicCurve2D> splines = new HashSet<>();

    public MainFrame mainFrame;
    FocusRectangle focusRectangle;

    public MyPanel(){
        setBackground(Color.lightGray);
        initPopupMenu();
        addListeners();

        setSize(1000,1000);
        add(new MyPoint(100, 150));

    }

    private void addListeners() {
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                editPoint = new Point(e.getX(), e.getY());
                if (e.getButton() == MouseEvent.BUTTON1 & getCursor().getType() != Cursor.MOVE_CURSOR){
                    focusRectangle = new FocusRectangle();
                    focusRectangle.x1 = editPoint.x;
                    focusRectangle.y1 = editPoint.y;
                    focusRectangle.x2 = editPoint.x;
                    focusRectangle.y2 = editPoint.y;

                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
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
                        if (c.contains(focusRectangle))
                            addToFocus(c);
                    }
                    focusRectangle = null;
                }
                if (editPoint.equals(e.getPoint()) & getCursor().getType() != Cursor.MOVE_CURSOR) {//проверка клика
                    boolean f = false;//флаг попадания объектов в фокус
                    for (MyComponent c: components) {
                        if (c.contains(e.getX(), e.getY())) {
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
                            c.move( e.getX() - editPoint.x, e.getY() - editPoint.y);
                        }
                    } else if (getCursor().getType() == Cursor.CROSSHAIR_CURSOR) {
                        Point localEditPoint = systemCoordToMyCoord(editPoint);
                        Point localPoint = systemCoordToMyCoord(e.getPoint());
                        double angle = atan2(localPoint.y, localPoint.x) - atan2(localEditPoint.y, localEditPoint.x);

                        for (MyComponent c: focus) {
                            c.rotateCentre(angle);
                        }
                    }
                    editPoint = new Point(e.getX(), e.getY());
                }

                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                CheckCursor(e.getX(), e.getY());
                for (MyComponent component: components) {
                    if (component.contains(e.getX(), e.getY())) {
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

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                System.out.println(e.getScrollType());

                if (e.getWheelRotation() > 0) {
                    for (MyComponent c: focus) {
                        c.scale(new Point(), 0.9);
                    }
                } else {
                    for (MyComponent c: focus) {
                        c.scale(new Point(), 1.1);
                    }
                }

                CheckEquationLine();
                repaint();
            }
        });
    }

    public void clearFocus() {
        for (MyComponent c: focus) {
            c.setFocus(false);
        }
        CheckEquationLine();
        focus.clear();
    }

    public void setFocus(MyComponent component){
        for (MyComponent c: focus) {
            c.setFocus(false);
        }
        focus.clear();
        component.setFocus(true);
        addToFocus(component);
        CheckEquationLine();
        repaint();
    }

    public void addToFocus(MyComponent component){
        component.setFocus(true);
        if (component.getClass().equals(MyLine.class)) {
            MyLine l = (MyLine) component;
            focus.add(l.p1);
            focus.add(l.p2);
        } else {
            focus.add(component);
        }
        CheckEquationLine();
        repaint();
    }

    public void CheckCursor(int x, int y){
        mainFrame.cursor.setText("Cursor  [" + x + " : " + y + "]");
        mainFrame.toolbarMode.repaint();
    }

    public void CheckEquationLine(){
        if (focus.size() == 1) {
            MyComponent c = (MyComponent) (focus.toArray()[0]);
            mainFrame.equationLine.setText("   Info:  " + c.showInfo());
        } else if (focus.size() == 2){

            for (MyComponent c: components)
                if (c.getClass().equals(MyLine.class))
                {
                    Object[] arr = focus.toArray();
                    if (((MyLine) c).p1.equals(arr[0]) & ((MyLine) c).p2.equals(arr[1]) | ((MyLine) c).p1.equals(arr[1]) & ((MyLine) c).p2.equals(arr[0]))
                        mainFrame.equationLine.setText("   Info:  " + c.showInfo());
                }
        } else
            mainFrame.equationLine.setText("");
        mainFrame.toolbarMode.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        coordinateSystem.paintComponent(g);
        for (MyComponent component: components) {
            component.paintComponent(g);
        }
        if (focusRectangle != null) {
            focusRectangle.paintComponent(g);
        }
        for (CubicCurve2D component: splines) {
            ((Graphics2D) g).draw(component);
        }

    }

    public void addPoint(int x, int y) {
        MyPoint p = new MyPoint(x, y);
        components.add(p);
        setFocus(p);
    }

    public void addLine(MyPoint p1, MyPoint p2) {
        MyLine l = new MyLine( p1, p2);
        components.add(l);
        setFocus(l);
    }

    public void addSpline(HashSet<MyComponent> arr){
        for(MyComponent c: arr)
            if (c.getClass().equals(MyLine.class)) return;



        CubicCurve2D curve = new CubicCurve2D.Double(((MyPoint)(focus.toArray()[0])).getX(), ((MyPoint)(focus.toArray()[0])).getY(),
                ((MyPoint)(focus.toArray()[1])).getX(), ((MyPoint)(focus.toArray()[1])).getY(),
                ((MyPoint)(focus.toArray()[2])).getX(), ((MyPoint)(focus.toArray()[2])).getY(),
                ((MyPoint)(focus.toArray()[3])).getX(), ((MyPoint)(focus.toArray()[3])).getY());
        splines.add(curve);
    }

    public  void delete(HashSet<MyComponent> arrComponent) {

        for (MyComponent c: arrComponent)
            if (c.getClass().equals(MyPoint.class)) {
                for(MyComponent l: components)
                    if (l.getClass().equals(MyLine.class)) {
                        MyLine newL = (MyLine) l;
                        if (newL.p1.equals(c) | newL.p2.equals(c)) {
                            components.remove(l);
                            break;
                        }
                    }
                components.remove(c);
            }
    }


    public void initPopupMenu(){
        JPopupMenu menu = new JPopupMenu();
        Action menuActionAddPoint = new AbstractAction("Add point") {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPoint(editPoint.x, editPoint.y);
                repaint();
            }
        };
        Action menuActionCreateLine = new AbstractAction("Create line") {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLine((MyPoint) (focus.toArray()[0]), (MyPoint) (focus.toArray()[1]));
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
                delete(focus);
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
