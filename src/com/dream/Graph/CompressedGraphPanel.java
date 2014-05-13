package com.dream.Graph;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
public class CompressedGraphPanel extends GraphPanel {

    private ArrayList<SlotListener> slotListeners = new ArrayList<SlotListener>();
    private int slotPosition = 0;
    private int divider;
    private Color slotColor = Color.MAGENTA;

    public CompressedGraphPanel(int graphAmount, int frequency, int divider) {
        super(graphAmount, frequency);
        this.divider = divider;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                notifySlotListeners(e.getX() - X_INDENT - slotPosition);
            }
        });
    }

    public int checkSlotWorkspaceBounds() {
        int endIndex = startIndex + getPreferredSize().width - X_INDENT;
        if( slotPosition <= startIndex) {
            return -1;
        }

        if ((slotPosition + getSlotWidth()) >= endIndex) {
            return 1;
        }
        else {
            return 0;
        }

    }

    private int getSlotWidth() {
        int slotWidth = (getPreferredSize().width - X_INDENT)/divider;
        return slotWidth;
    }

    @Override
    public void setStartIndex(int startIndex) {
         if(slotPosition < startIndex) {
            notifySlotListeners(startIndex - slotPosition);
         }
         this.startIndex = startIndex;
    }

    public void moveSlot(int slotPositionChange) {
        slotPosition += slotPositionChange;
        if(slotPosition < 0) {
            slotPosition = 0;
        }
        if(slotPosition < startIndex) {
            startIndex = slotPosition;
        }
        repaint();
    }


    public void addSlotListener(SlotListener slotListener) {
          slotListeners.add(slotListener);
    }

    private void notifySlotListeners(int slotPosition) {
        for (SlotListener listener: slotListeners) {
            listener.moveSlot(slotPosition);

        }
    }

    private void paintSlot(Graphics g) {
        g.setColor(slotColor);
        g.drawRect(slotPosition - startIndex, 0, getSlotWidth(), g.getClipBounds().height-Y_INDENT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);    //To change body of overridden methods use File | Settings | File Templates.
        paintSlot(g);
    }
}
