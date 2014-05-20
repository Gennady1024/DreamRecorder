package com.dream.Graph;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
    private int divider = 0;
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);    //To change body of overridden methods use File | Settings | File Templates.
        transformCoordinate(g);
        paintSlot(g);
        restoreCoordinate(g);
    }
}
