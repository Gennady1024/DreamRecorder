package com.dream.Graph;

import com.dream.Data.StreamData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 08.05.14
 * Time: 14:25
 * To change this template use File | Settings | File Templates.
 */
class GraphPanel extends JPanel {
    protected StreamData<Integer>[] graphs = new StreamData[3];//panel can have a several graphs. Max 3 for simplicity
    protected static final int X_INDENT = 50;
    protected static final int Y_INDENT = 20;
    protected static final double ZOOM_PLUS_CHANGE = Math.sqrt(2.0);// 2 clicks(rotations) up increase zoom twice
    protected static final double ZOOM_MINUS_CHANGE = 1 / ZOOM_PLUS_CHANGE; // similarly 2 clicks(rotations) down reduces zoom twice
    protected static final Color bgColor = Color.BLACK;
    protected static final Color axisColor = Color.GREEN;
    protected static final Color graphColor = Color.YELLOW;

    protected int startIndex = 0;
    protected double zoom = 0.5;
    protected boolean isAutoZoom;
    protected long startTime = System.currentTimeMillis();
    protected int weight = 1;
    protected double frequency = 1;


    GraphPanel(int weight) {
        this.weight = weight;
        setBackground(bgColor);
        // MouseListener to zoom Y_Axes
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                zooming(e.getWheelRotation());
            }
        });
    }


    protected void setStart(long starTime, double frequency) {
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

    protected void paintAxisY(Graphics g) {

        int minValueStep = 50;  //default value between two labels
        int minPointStep = 20; // distance between two labels in pixels
        int minValue = 0;

        g.setColor(axisColor);

        int valueStep = (int)(minPointStep/(zoom*minValueStep)+1)*minValueStep;
        int numberOfColumns = (int)(getWorkspaceHeight()/(zoom*valueStep));
        Graphics2D g2d = (Graphics2D) g;
        g2d.transform(AffineTransform.getScaleInstance(1.0, -1.0)); // flip transformation

        for (int i = 1; i < numberOfColumns+1; i++) {
            long gridValue = (minValue/valueStep)*valueStep + i*valueStep;
            int position = (int)Math.round(zoom*(gridValue - minValue));
            g.drawLine(12, -position, +18, -position);
            String valueText = String.valueOf(gridValue);
            g.drawString(valueText, -25, -position+5);
        }
        g2d.transform(AffineTransform.getScaleInstance(1.0, -1.0)); // flip transformation
    }

    protected void paintAxisX(Graphics g) {
        int HALF_SECOND_STEP = (int) frequency/2;  // graph points per half-second
        int SECOND_STEP = (int) frequency;  // graph points per second
        int TEN_SECONDS_STEP = (int) (10*frequency);
        int MINUTE_STEP = (int) (60*frequency);

        int MINUTE = 60*1000;//milliseconds

        g.setColor(axisColor);
        Graphics2D g2d = (Graphics2D) g;
        g2d.transform(AffineTransform.getScaleInstance(1.0, -1.0)); // flip transformation

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");

        for (int i = 0; i  < getWorkspaceWidth(); i++) {
            int position_half_second = i*HALF_SECOND_STEP;
            int position_second = i*SECOND_STEP;
            int position_ten_seconds = i*TEN_SECONDS_STEP;
            int position_minute = i*MINUTE_STEP;

            if(position_half_second < getWorkspaceWidth()){
                // Paint Point
                g.drawLine(position_half_second, 0, position_half_second, 0);
            }

            if(position_second < getWorkspaceWidth()){
                // Paint Stroke
                g.drawLine(position_second, -2, position_second, +2);
            }

            if(position_ten_seconds < getWorkspaceWidth()){
                // Paint Rectangle
                g.fillRect(position_ten_seconds-1, -4, 3, 9);
            }

            if(position_minute < getWorkspaceWidth()){
                String timeStamp = dateFormat.format(new Date(startTime + i*MINUTE)) ;
                // Paint Time Stamp
                g.drawString(timeStamp, position_minute - 15, +18);
            }

        }

        g2d.transform(AffineTransform.getScaleInstance(1.0, -1.0)); // flip transformation

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
        g.setColor(graphColor);
        transformCoordinate(g);

        paintAxisX(g);
        paintAxisY(g);

        g.setColor(graphColor);
        for (StreamData<Integer> graph : graphs) {
            if (graph != null) {
                int endIndex = Math.min(getWorkspaceWidth(), (graph.size() - startIndex));
                VerticalLine vLine = new VerticalLine();
                for (int x = 0; x < endIndex; x++) {
                    int y = (int) Math.round(zoom * graph.get(x + startIndex));
                    drawVerticalLine(g, x, y, vLine);
                }
            }

        }
        restoreCoordinate(g);
    }

    private void drawVerticalLine(Graphics g, int x, int y, VerticalLine vLine) {
        vLine.setNewBounds(y);
        g.drawLine(x, vLine.min, x, vLine.max);
    }

    class VerticalLine {
        int max = 0;
        int min = -1;


        void setNewBounds(int y) {
            if (y >= min && y <= max) {
                min = max = y;
            } else if (y > max) {
                min = max + 1;
                max = y;
            } else if (y < min) {
                max = min - 1;
                min = y;
            }
        }
    }
}



