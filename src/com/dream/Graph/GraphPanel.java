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
    protected int startIndex = 0;

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

    public int getGraphAmount() {
        return graphsData.length;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);    //To change body of overridden methods use File | Settings | File Templates.
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(0, g.getClipBounds().height); // move XY origin to the left bottom point
        g2d.transform(AffineTransform.getScaleInstance(1, -1*zoom)); // flip Y-axis and zoom it
        g2d.setColor(Color.green);
        for(int i = 0; i < getGraphAmount(); i++) {
            int endIndex = Math.min(g2d.getClipBounds().width, (graphsData[i].size()-startIndex));
            System.out.println("end index = " + endIndex);
           for (int j = startIndex; j < startIndex+endIndex; j++) {
               int x = j-startIndex;
               int y = graphsData[i].get(j);
               g.drawLine(x, y, x, y);
           }
        }

    }

}
