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
        int screenWidth = dimension.width;
        int screenHeight = dimension.height;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        PaintingPanel paintingPane_l = new PaintingPanel();
        PaintingPanel paintingPane_2 = new PaintingPanel();



        paintingPane_l.setPreferredSize(new Dimension(screenWidth, screenHeight/6));
        paintingPane_2.setPreferredSize(new Dimension(screenWidth, screenHeight/3));
        paintingPane_l.isFast(false);
        for (int i = 0; i < 1000; i++) {
          int data = (int)(100*Math.sin(((double)i)/100));
        paintingPane_l.addData(data);

        }

        mainPanel.setBackground(Color.GRAY);
        mainPanel.add(paintingPane_l);
        mainPanel.add(Box.createVerticalStrut(2));
        mainPanel.add(paintingPane_2);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
