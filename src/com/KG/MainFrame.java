package com.KG;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    public MyPanel panel = new MyPanel();

    public JToolBar toolbarMode = new JToolBar();

    public JLabel cursor = new JLabel();
    public JLabel equationLine = new JLabel();

    public MainFrame(){
        super("My First Window"); //Заголовок окна
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //это нужно для того чтобы при
        //закрытии окна закрывалась и программа,
        //иначе она останется висеть в процессах

        add(panel);
        panel.mainFrame = this;
        add(toolbarMode, BorderLayout.NORTH);
        toolbarMode.setFloatable(false);
        toolbarMode.add(cursor);
        toolbarMode.add(equationLine);
        toolbarMode.setMargin(new Insets(0, 0, 0, -20));


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                cursor.setText("X: " + e.getX() + "Y: " + e.getY());
                toolbarMode.repaint();
            }
        });

        //pack();
        setBounds(100, 100, 900, 700);
        setVisible(true);
        setResizable(false);
    }

}