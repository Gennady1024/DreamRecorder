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
public class CompressedGraphPanel extends GraphPanel {


    private ArrayList<SlotListener> slotListeners = new ArrayList<SlotListener>();
    private int slotIndex = 0;  // according to the beginning of Data arrays
    private int divider = 0;
    private Color slotColor = Color.MAGENTA;


    public CompressedGraphPanel(int graphAmount, int frequency, int divider) {
        super(graphAmount, frequency);
        this.divider = divider;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int newSlotIndex = e.getX() - X_INDENT + startIndex ;
                notifySlotListeners(newSlotIndex - slotIndex);
            }
        });
    }

    public int isSlotInWorkspace() {
        int slotWorkspacePosition = slotIndex - startIndex;
        if ( slotWorkspacePosition <= 0 ) {
            return -1;
        }
        if ( slotWorkspacePosition >= (getWorkspaceWidth() - getSlotWidth()) ) {
            return 1;
        }

        return 0;
    }

    private int getSlotWidth() {
        if (divider == 0) {
            return 0;
        }
        int slotWidth = getWorkspaceWidth()/divider;
        if(slotWidth > getGraphsLength()) {
            return 0;
        }
        else {
            return slotWidth;
        }
    }

    private int getSlotMaxIndex () {
        if(getSlotWidth() == 0) {
            return 0;
        }
        return getGraphsLength() - getSlotWidth();
    }

    public void moveSlot(int slotIndexChange) {
        int newSlotIndex = slotIndex + slotIndexChange;
        if (newSlotIndex < 0) {
            newSlotIndex = 0;
        }
        if (newSlotIndex > getSlotMaxIndex()) {
            newSlotIndex = getSlotMaxIndex();
        }

        slotIndex = newSlotIndex;

        if((isSlotInWorkspace() == -1) ) {
            startIndex = slotIndex;
        }
        if((isSlotInWorkspace() == 1) ) {
            startIndex = slotIndex + getSlotWidth() - getWorkspaceWidth();
        }
        repaint();
    }

    @Override
    public void setStartIndex(int startIndex) {
        int indexChange = startIndex - this.startIndex;
        this.startIndex = startIndex;
        if((isSlotInWorkspace() == -1) ) {
            notifySlotListeners(startIndex - slotIndex);
        }
         if(slotIndex < startIndex) {
            notifySlotListeners(startIndex - slotIndex);
         }
        if((isSlotInWorkspace() == 1) ) {
            notifySlotListeners(indexChange);
        }


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
        if(getSlotWidth() > 0) {
            g.setColor(slotColor);
            g.drawRect(slotIndex - startIndex, 0, getSlotWidth(), getWorkspaceHeight());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);    //To change body of overridden methods use File | Settings | File Templates.
        paintSlot(g);
    }
}
