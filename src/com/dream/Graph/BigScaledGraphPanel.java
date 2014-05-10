package com.dream.Graph;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 10.05.14
 * Time: 21:32
 * To change this template use File | Settings | File Templates.
 */
public class BigScaledGraphPanel extends GraphPanel {
    public BigScaledGraphPanel(int graphAmount, int frequency) {
        super(graphAmount, frequency);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);    //To change body of overridden methods use File | Settings | File Templates.
        g.setColor(bgColor);
        g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        g.setColor(Color.yellow);
        g.drawRect(100, 100, 100, 100);
    }
}