package com.dream.Graph;

import com.dream.Data.Stock;

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
public class GraphPanel extends JPanel {
    protected Stock<Integer>[] graphs = new Stock[3];//panel can have a several graphs. Max 3 for simplicity

    protected double zoom = 1;
    protected final double dZoomPlus = Math.sqrt(2.0);// 2 clicks(rotations) up increase zoom twice
    protected final double dZoomMinus = 1 / dZoomPlus; // similarly 2 clicks(rotations) down reduces zoom twice
    protected boolean isAutoZoom;
    protected long startTime;
    protected final Color bgColor = Color.BLACK;
    protected final Color axisColor = Color.GREEN;
    protected int weight = 1;


    protected final Color graphColor = Color.YELLOW;
    protected int frequency = 0;
    protected int startIndex = 0;
    protected static final int X_INDENT = 30;
    protected static final int Y_INDENT = 30;


    public GraphPanel(int weight, int frequency) {
        setBackground(bgColor);
        setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.DARK_GRAY));
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                zooming(e.getWheelRotation());
            }
        });
    }


    protected int getWorkspaceWidth() {
        return (getPreferredSize().width - X_INDENT);
    }

    protected int getWorkspaceHeight() {
        return (getPreferredSize().height - Y_INDENT);
    }


    private void zooming(int zoomDirection) {
        if (zoomDirection > 0) {
            zoom = zoom * dZoomPlus;
        } else {
            zoom = zoom * dZoomMinus;
        }
        repaint();
    }

    public void addGraph(Stock<Integer> graphData) {
        int count = 0;
        while (graphs[count] != null) {
            count++;
        }
        if(count < graphs.length) {
            graphs[count] = graphData;
        }
        repaint();
    }


    protected int getGraphsLength() {
        if (graphs[0] == null) {
            return 0;
        }
        else {
            return graphs[0].size();
        }
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getStartIndex() {
        return startIndex;
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
        g2d.translate(X_INDENT, getWorkspaceHeight()); // move XY origin to the left bottom point
        g2d.transform(AffineTransform.getScaleInstance(1, -1 * zoom)); // flip Y-axis and zoom it
        paintAxisX(g);
        paintAxisY(g);
        g2d.setColor(graphColor);
        for(Stock<Integer> graph : graphs) {
            if(graph != null) {
                int endIndex = Math.min(getWorkspaceWidth(), (graph.size() - startIndex));
                for (int x = 0; x < endIndex; x++) {
                    int y = graph.get(x + startIndex);
                    g.drawLine(x, y, x, y);
                }
            }

        }
    }

    public int getWeight() {
        return weight;
    }
}
