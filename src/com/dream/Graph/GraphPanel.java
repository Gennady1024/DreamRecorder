package com.dream.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 08.05.14
 * Time: 14:25
 * To change this template use File | Settings | File Templates.
 */
public class GraphPanel extends JPanel {
    protected ArrayList<Integer>[] graphsData; // panel can have a several graphs
    protected double zoom = 1;
    protected long startTime;
    protected Color bgColor = Color.BLACK;
    protected int frequency;

    public GraphPanel(int graphAmount, int frequency) {
        graphsData = new ArrayList[graphAmount];
        this.frequency = frequency;
        for(int i = 0; i < graphAmount; i++){
            graphsData[i] = new ArrayList<Integer>();
        }
        setBackground(bgColor);
    }

    public void addData(int graphNumber, int data) {
        if (graphNumber < graphsData.length) {
            graphsData[graphNumber].add(data);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);    //To change body of overridden methods use File | Settings | File Templates.
        g.setColor(Color.green);
        g.drawOval(100,100,100,100);
    }

}
