package com.KG;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class MainFrame extends JFrame {


    public MainFrame(){
        super("My First Window"); //Заголовок окна

        setBounds(100, 100, 1000, 500); //Если не выставить
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //это нужно для того чтобы при
        //закрытии окна закрывалась и программа,
        //иначе она останется висеть в процессах
        add(new MyPanel());
        setVisible(true);
    }


}

class MyPanel extends JPanel {
    private ArrayList<Line2D> lines = new ArrayList<Line2D>();

    private Point start;
    private Point end;


    public MyPanel(){

        setBackground(Color.black);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                start = new Point(e.getX(), e.getY());
                end = start;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lines.add(makeLine(start.x, start.y, e.getX(), e.getY()));
                start = null;
                end = null;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                end = new Point(e.getX(), e.getY());
                repaint();
            }
        });
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.CYAN);
        for (Line2D l : lines) {
            g2.draw(l);
        }

        if (start != null && end != null) {
            Line2D l = makeLine(start.x, start.y, end.x, end.y);
            g2.draw(l);
        }
    }

    private Line2D.Double makeLine(int x1, int y1, int x2, int y2) {
        return new Line2D.Double(x1, y1, x2, y2);
    }
}
