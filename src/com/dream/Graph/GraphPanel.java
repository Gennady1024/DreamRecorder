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
            Dimension currentDimention = getPreferredSize();
            setPreferredSize(new Dimension(graphsData[graphNumber].size(), currentDimention.height));
        }
    }

    public int getGraphAmount() {
        return graphsData.length;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);    //To change body of overridden methods use File | Settings | File Templates.
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(0, g.getClipBounds().height); // move XY origin to the left bottom point
        g2d.transform(AffineTransform.getScaleInstance(1, -1*zoom)); // flip Y-axis and zoom it
        g.setColor(Color.green);
        for(int i = 0; i < getGraphAmount(); i++) {
           for (int j = 0; j < graphsData[i].size(); j++) {
               int x = j;
               int y = graphsData[i].get(j);
               g.drawLine(x, y, x, y);
           }
        }

    }

}
