package com.dream.Graph;

import com.dream.Data.StreamData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 08.05.14
 * Time: 14:25
 * To change this template use File | Settings | File Templates.
 */
class GraphPanel extends JPanel {
    protected StreamData<Integer>[] graphs = new StreamData[3];//panel can have a several graphs. Max 3 for simplicity
    protected static final int X_INDENT = 30;
    protected static final int Y_INDENT = 30;
    protected static final double ZOOM_PLUS_CHANGE = Math.sqrt(2.0);// 2 clicks(rotations) up increase zoom twice
    protected static final double ZOOM_MINUS_CHANGE = 1 / ZOOM_PLUS_CHANGE; // similarly 2 clicks(rotations) down reduces zoom twice
    protected static final Color bgColor = Color.BLACK;
    protected static final Color axisColor = Color.GREEN;
    protected static final Color graphColor = Color.YELLOW;

    protected int startIndex = 0;
    protected double zoom = 1;
    protected boolean isAutoZoom;
    protected long startTime = 0;
    protected int weight = 1;
    protected int frequency = 0;


    GraphPanel(int weight) {
        this.weight = weight;
        this.frequency = frequency;
        setBackground(bgColor);
        // MouseListener to zoom Y_Axes
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                zooming(e.getWheelRotation());
            }
        });
    }


    protected void setStart(long starTime, int frequency) {
            this.startTime = starTime;
            this.frequency = frequency;
    }


    protected int getStartIndex() {
        return startIndex;
    }

    protected void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    protected int getWeight() {
        return weight;
    }

    protected int getWorkspaceWidth() {
        return (getSize().width - X_INDENT);
    }

    protected int getWorkspaceHeight() {
        return (getSize().height - Y_INDENT);
    }

    protected int getFullWidth() {
        return X_INDENT + getGraphsSize();
    }

    protected void zooming(int zoomDirection) {
        if (zoomDirection > 0) {
            zoom = zoom * ZOOM_PLUS_CHANGE;
        } else {
            zoom = zoom * ZOOM_MINUS_CHANGE;
        }
        repaint();
    }

    protected void addGraph(StreamData<Integer> graphData) {
        int count = 0;
        while (graphs[count] != null) {
            count++;
        }
        if (count < graphs.length) {
            graphs[count] = graphData;
        }
        repaint();
    }


    protected int getGraphsSize() {
        if (graphs[0] == null) {
            return 0;
        } else {
            return graphs[0].size();
        }
    }

    protected void setAutoZoom(boolean isAutoZoom) {
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

    protected void transformCoordinate(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(X_INDENT, getWorkspaceHeight()); // move XY origin to the left bottom point
        g2d.transform(AffineTransform.getScaleInstance(1, -1)); // flip Y-axis
    }

    protected void restoreCoordinate(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(-X_INDENT, getWorkspaceHeight()); // move XY origin to the left top point
        g2d.transform(AffineTransform.getScaleInstance(1, -1)); // flip Y-axis and zoom it
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);    //To change body of overridden methods use File | Settings | File Templates.
        transformCoordinate(g);
        paintAxisX(g);
        paintAxisY(g);
        g.setColor(graphColor);
        for (StreamData<Integer> graph : graphs) {
            if (graph != null) {
                int endIndex = Math.min(getWorkspaceWidth(), (graph.size() - startIndex));
                for (int x = 0; x < endIndex; x++) {
                    int y = graph.get(x + startIndex);
                    g.drawLine(x, y, x, y);
                }
            }

        }
        restoreCoordinate(g);
    }
}
