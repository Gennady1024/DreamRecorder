package com.dream;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: galafit
 * Date: 07/05/14
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
public class Graph {
    
    private int frequency;
    private long startTime;
    private int zoom;
    private int startIndex;
    private boolean isZoomAutomatic;
    private ArrayList<Integer> inputData = new ArrayList<Integer>();


    public Graph(int frequency, long startTime) {
       this.frequency = frequency;
       this.startTime = startTime;
       zoom = 1;
       startIndex = 0;
        isZoomAutomatic = true;
    }

    public void setStartIndex(int index) {
        startIndex = index;

    }

    public void setZoom (int zoom) {
        this.zoom = zoom;

    }

    public void addDataElement(int dataElement) {
        inputData.add(dataElement);

    }

    public void setZoomType(boolean isZoomAutomatic) {
        isZoomAutomatic = isZoomAutomatic;

    }

    public void paint() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize ();
        int screenWorkingWidth = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
        int screenWorkingHeight = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;

        frame.setSize(screenWorkingWidth, screenWorkingHeight);

        frame.setVisible(true);

    }

}
