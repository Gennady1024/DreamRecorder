package com.dream;

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
public class PaintingPanel extends JPanel {
    private boolean isFast = true;
    private ArrayList<Integer> graphData = new ArrayList<Integer>();
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);    //To change body of overridden methods use File | Settings | File Templates.
        setBackground(Color.black);
        g.setColor(Color.green);

        if(isFast) {
            g.drawLine(0,0, 200, 100);
        } else {
            Graphics2D g2d = (Graphics2D)g;
            int width = g.getClipBounds().width;
            int height = g.getClipBounds().height;
            g2d.translate(0, height/2);
            AffineTransform flipTransform = AffineTransform.getScaleInstance(1, -1);
            g2d.transform(flipTransform);

            g.drawLine(0,0, width, 0);
            for(int i = 0; i < graphData.size(); i++ ) {
               g.drawLine(i, graphData.get(i), i, graphData.get(i));
            }
        }
    }

    public void isFast(boolean isFast) {
           this.isFast = isFast;
    }

    public void addData(int data) {
          graphData.add(data);
    }
}
