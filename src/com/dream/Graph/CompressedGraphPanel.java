package com.dream.Graph;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 10.05.14
 * Time: 21:32
 * To change this template use File | Settings | File Templates.
 */
class CompressedGraphPanel extends GraphPanel {


    private ArrayList<SlotListener> slotListeners = new ArrayList<SlotListener>();
    private int slotIndex = 0;  // according to the beginning of Data arrays
    private int divider = 1;
    private Color slotColor = Color.MAGENTA;


    CompressedGraphPanel(int weight, int divider, boolean isXCentered ) {
        super(weight, isXCentered);
        this.divider = divider;

        //MouseListener to move Slot
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int newSlotIndex = e.getX() - X_INDENT + startIndex ;
                notifySlotListeners(newSlotIndex);
            }
        });
    }

    int getSlotIndex() {
        return slotIndex;
    }

    void setSlotIndex(int slotIndex) {
        this.slotIndex = slotIndex;
    }

    int getSlotWidth() {
        if (divider == 0) {
            return 0;
        }
        return  getWorkspaceWidth()/divider;
    }

    public void addSlotListener(SlotListener slotListener) {
          slotListeners.add(slotListener);
    }

    private void notifySlotListeners(int newSlotPosition) {
        for (SlotListener listener: slotListeners) {
            listener.moveSlot(newSlotPosition);

        }
    }

    private void paintSlot(Graphics g) {
        if(getSlotWidth() > 0) {
            g.setColor(slotColor);
            g.drawRect(slotIndex - startIndex, 0, getSlotWidth(), getMaxY());
        }
    }

    @Override
    protected void paintAxisX(Graphics g) {
        int MINUTE_STEP = (int)(60*frequency);  // graph points per minute
        int MINUTES_2_STEP = (int)(2 * 60 * frequency);
        int MINUTES_10_STEP = (int)(10 * 60 * frequency);
        int MINUTES_30_STEP = (int)(30 * 60*frequency);

        int MINUTES_30 = 30 * 60 * 1000;//milliseconds

        g.setColor(axisColor);
        Graphics2D g2d = (Graphics2D) g;
        g2d.transform(AffineTransform.getScaleInstance(1.0, -1.0)); // flip transformation

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");

        for (int i = 0; i  < getWorkspaceWidth(); i++) {
            if((i % MINUTES_10_STEP) == 0){
                // Paint Triangle
                g.fillPolygon(new int[]{i - 3, i + 3, i}, new int[]{0, 0, 6}, 3);
            }
            else if((i % MINUTES_2_STEP) == 0){
                //paint T
                g.drawLine(i - 1, 0, i + 1, 0);
                g.drawLine(i, 0, i, 5);
            }
            else if((i % MINUTE_STEP) == 0){
                // Paint Point
                g.drawLine(i, 0, i, 0);
            }

            if(((i % MINUTES_30_STEP) == 0)){
                String timeStamp = dateFormat.format(new Date(startTime + (startIndex+i)*MINUTES_30/MINUTES_30_STEP)) ;
                // Paint Time Stamp
                g.drawString(timeStamp, i - 15, +18);
            }
        }
        g2d.transform(AffineTransform.getScaleInstance(1.0, -1.0)); // flip transformation
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);    //To change body of overridden methods use File | Settings | File Templates.
        transformCoordinate(g);
        paintSlot(g);
        restoreCoordinate(g);
    }
}
