package com.dream.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
    protected final double dZoomPlus  = Math.sqrt(2.0);// 2 clicks(rotations) up increase zoom twice
    protected final double dZoomMinus = 1/dZoomPlus; // similarly 2 clicks(rotations) down reduces zoom twice
    protected boolean isAutoZoom;
    protected long startTime;
    protected final Color bgColor = Color.BLACK;
    protected final Color axisColor = Color.GREEN;
    protected final Color graphColor = Color.YELLOW;
    protected int frequency;
    protected int startIndex = 0;
    protected static final int X_INDENT = 30;
    protected static final int Y_INDENT = 30;

    public GraphPanel(int graphAmount, int frequency) {
        graphsData = new ArrayList[graphAmount];
        this.frequency = frequency;
        for(int i = 0; i < graphAmount; i++){
            graphsData[i] = new ArrayList<Integer>();
        }
        setBackground(bgColor);
        setBorder(BorderFactory.createMatteBorder(1, 0, 1,0, Color.DARK_GRAY));

        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                zooming(e.getWheelRotation());
            }
        });
    }

    private void zooming(int zoomDirection) {
        if(zoomDirection > 0) {
            zoom = zoom*dZoomPlus;
        }
        else {
            zoom = zoom*dZoomMinus;
        }
        repaint();
    }

    public void addData(int graphNumber, int data) {
        if (graphNumber < graphsData.length) {
            graphsData[graphNumber].add(data);
        }
        repaint();
    }

    public int getGraphAmount() {
        return graphsData.length;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
        repaint();
    }

    public void setAutoZoom(boolean isAutoZoom) {
        this.isAutoZoom = isAutoZoom;
        repaint();
    }

    protected void paintAxisX(Graphics g) {
        g.setColor(axisColor);
         g.drawLine(0, 0, g.getClipBounds().width, 0);
    }

    protected void paintAxisY(Graphics g) {
        g.setColor(axisColor);
        g.drawLine(0, 0, 0, g.getClipBounds().height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);    //To change body of overridden methods use File | Settings | File Templates.
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(X_INDENT, g.getClipBounds().height - Y_INDENT); // move XY origin to the left bottom point
        g2d.transform(AffineTransform.getScaleInstance(1, -1*zoom)); // flip Y-axis and zoom it

        paintAxisX(g);
        paintAxisY(g);
        g2d.setColor(graphColor);
        for(int i = 0; i < getGraphAmount(); i++) {
            int endIndex = Math.min(g2d.getClipBounds().width- X_INDENT, (graphsData[i].size()-startIndex));
           for (int j = 0; j < endIndex; j++) {
               int x = j;
               int y = graphsData[i].get(j + startIndex);
               g.drawLine(x, y, x, y);
           }
        }
    }
}
