package com.dream.Graph;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
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


    CompressedGraphPanel(int weight, int divider) {
        super(weight);
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
            g.drawRect(slotIndex - startIndex, 0, getSlotWidth(), g.getClipBounds().height - Y_INDENT);
        }
    }

    @Override
    protected void paintAxisX(Graphics g) {
        int MINUTE_STEP = (int)(60*frequency);  // graph points per minute
        int TWO_MINUTES_STEP = (int)(2 * 60 * frequency);
        int TEN_MINUTES_STEP = (int)(10 * 60 * frequency);
        int THIRTY_MINUTES_STEP = (int)(30 * 60*frequency);

        int THIRTY_MINUTES = 30 * 60 * 1000;//milliseconds

        g.setColor(axisColor);
        Graphics2D g2d = (Graphics2D) g;
        g2d.transform(AffineTransform.getScaleInstance(1.0, -1.0)); // flip transformation

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");

        for (int i = 0; i  < getWorkspaceWidth(); i++) {
            int position_minute = i*MINUTE_STEP;
            int position_two_minutes = i*TWO_MINUTES_STEP;
            int position_ten_minutes = i*TEN_MINUTES_STEP;
            int position_thirty_minutes = i*THIRTY_MINUTES_STEP;

            if(position_minute < getWorkspaceWidth()){
                //paintPoint
                g.drawLine(position_minute, 0, position_minute, 0);
            }

            if(position_two_minutes < getWorkspaceWidth()){
                //paint T
                g.drawLine(position_two_minutes - 1, 0, position_two_minutes + 1, 0);
                g.drawLine(position_two_minutes, 0, position_two_minutes, 5);
            }

            if(position_ten_minutes < getWorkspaceWidth()){
                // Paint Triangle
                GeneralPath triangle = new GeneralPath();
                triangle.moveTo(position_ten_minutes - 3, 0);
                triangle.lineTo(position_ten_minutes + 3, 0);
                triangle.lineTo(position_ten_minutes, 6);
                triangle.lineTo(position_ten_minutes - 3, 0);
                g2d.fill(triangle);
            }

            if(position_thirty_minutes < getWorkspaceWidth()){
                String timeStamp = dateFormat.format(new Date(startTime + i*THIRTY_MINUTES)) ;
                // Paint Time Stamp
                g.drawString(timeStamp, position_thirty_minutes - 15, +18);
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
