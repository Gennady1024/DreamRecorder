package com.dream.Graph;

import com.dream.Data.StreamData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: galafit
 * Date: 07/05/14
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
public class GraphsViewer extends JPanel implements SlotListener {

    private int frequency;
    private int divider;
    private long startTime;

    private ArrayList<GraphPanel> graphPanels = new ArrayList<GraphPanel>();
    private ArrayList<CompressedGraphPanel> compressedGraphPanels = new ArrayList<CompressedGraphPanel>();

    private JPanel PaintingPanel = new JPanel();
    private JPanel scrollablePanel = new JPanel();
    private JScrollPane scrollPanel = new JScrollPane(scrollablePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    public GraphsViewer(int frequency, int divider) {
        this.frequency = frequency;
        this.divider = divider;
        setLayout(new BorderLayout());

        PaintingPanel.setLayout(new BoxLayout(PaintingPanel, BoxLayout.Y_AXIS));
        add(PaintingPanel, BorderLayout.CENTER);

        scrollPanel.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                moveScroll(e.getValue());
            }
        });
        add(scrollPanel, BorderLayout.SOUTH);

        setFocusable(true); //only that way KeyListeners work

        // Key Listener to move Slot
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_RIGHT) {
                    moveSlot(getSlotIndex() + 1);
                }

                if (key == KeyEvent.VK_LEFT) {
                    moveSlot(getSlotIndex() - 1);
                }
            }
        });
    }


    public void addGraphPanel(int weight) {
        GraphPanel panel = new GraphPanel(weight, frequency);
        graphPanels.add(panel);
        PaintingPanel.add(panel);
        setPanelsPreferredSizes();
    }

    public void addCompressedGraphPanel(int weight) {
        CompressedGraphPanel panel = new CompressedGraphPanel(weight, frequency / divider, divider);
        panel.addSlotListener(this);
        compressedGraphPanels.add(panel);
        PaintingPanel.add(panel);
        setPanelsPreferredSizes();
    }

    public void addGraph(int panelNumber, StreamData<Integer> graphData) {
        if (panelNumber < graphPanels.size()) {
            graphPanels.get(panelNumber).addGraph(graphData);
        }
    }

    public void addCompressedGraph(int panelNumber, StreamData<Integer> graphData) {
        if (panelNumber < compressedGraphPanels.size()) {
            compressedGraphPanels.get(panelNumber).addGraph(graphData);
        }
    }

    @Override
    public void setPreferredSize(Dimension d) {
        super.setPreferredSize(d);
        setPanelsPreferredSizes();
    }

    public void syncView(){
        autoScroll();
        repaint();
    }


    public void moveSlot(int newSlotIndex) {
        int slotMaxIndex;
        if (getSlotWidth() == 0) {
            slotMaxIndex = 0;
        }
        else {
            slotMaxIndex = getCompressedGraphsSize() - getSlotWidth();;
        }

        if (newSlotIndex < 0) {
            newSlotIndex = 0;
        }
        if (newSlotIndex > slotMaxIndex) {
            newSlotIndex = slotMaxIndex;
        }

        int compressedGraphsNewStartIndex = getCompressedGraphsStartIndex();

        if((isSlotInWorkspace(newSlotIndex, compressedGraphsNewStartIndex) == -1) ) {
            compressedGraphsNewStartIndex = newSlotIndex;
        }
        if((isSlotInWorkspace(newSlotIndex, compressedGraphsNewStartIndex) == 1) ) {
            compressedGraphsNewStartIndex = newSlotIndex + getSlotWidth() - getWorkspaceWidth();
        }

        for (CompressedGraphPanel panel : compressedGraphPanels) {
            panel.setSlotIndex(newSlotIndex);
            panel.setStartIndex(compressedGraphsNewStartIndex);
            panel.repaint();
        }

        int GraphsNewStartIndex = newSlotIndex * divider;
        for (GraphPanel panel : graphPanels) {
            panel.setStartIndex(GraphsNewStartIndex);
            panel.repaint();
        }

        syncScroll();
    }



    private void autoScroll () {
        int graphsMaxStartIndex = getGraphsSize() - getWorkspaceWidth();
        if(graphsMaxStartIndex < 0) {
            graphsMaxStartIndex = 0;
        }

        int slotMaxIndex;
        if (getSlotWidth() == 0) {
            slotMaxIndex = 0;
        }
        else {
            slotMaxIndex = graphsMaxStartIndex/divider;
        }

        int slotIndex = getSlotIndex();
        if (slotMaxIndex == slotIndex) {
            for (GraphPanel panel : graphPanels) {
                panel.setStartIndex(graphsMaxStartIndex);
                panel.repaint();
            }
        }
        if (slotMaxIndex == (slotIndex + 1)) {
            moveSlot(slotMaxIndex);
        }
        syncScroll();
    }


    private void syncScroll() {
        if (compressedGraphPanels != null) {
            if (compressedGraphPanels.size() > 0) {
                CompressedGraphPanel panel = compressedGraphPanels.get(0);
                scrollablePanel.setPreferredSize(new Dimension(panel.getFullWidth(), 0));
                scrollPanel.getViewport().setViewPosition(new Point(panel.getStartIndex(), 0));
                scrollablePanel.revalidate(); // we always have to call component.revalidate() after changing it "directly"(outside the GUI)
                scrollPanel.repaint();
            }
        }
    }

    private void setPanelsPreferredSizes() {
        Dimension d = getPreferredSize();
        int width = d.width;
        int height = d.height - scrollPanel.getPreferredSize().height;
        int sumWeight = 0;
        for (GraphPanel panel : graphPanels) {
            sumWeight += panel.getWeight();
        }
        for (CompressedGraphPanel panel : compressedGraphPanels) {
            sumWeight += panel.getWeight();
        }

        for (GraphPanel panel : graphPanels) {
            panel.setPreferredSize(new Dimension(width, height * panel.getWeight() / sumWeight));
        }
        for (CompressedGraphPanel panel : compressedGraphPanels) {
            panel.setPreferredSize(new Dimension(width, height * panel.getWeight() / sumWeight));
        }
    }

    private void moveCompressedGraphs(int newStartIndex) {
        int newSlotIndex = getSlotIndex();
        if((isSlotInWorkspace(newSlotIndex, newStartIndex) == -1) ) {
            newSlotIndex = newStartIndex;
        }
        if((isSlotInWorkspace(newSlotIndex, newStartIndex) == 1) ) {
            newSlotIndex = newStartIndex  + getWorkspaceWidth() -  getSlotWidth();
        }

        for (CompressedGraphPanel panel : compressedGraphPanels) {
            panel.setSlotIndex(newSlotIndex);
            panel.setStartIndex(newStartIndex);
            panel.repaint();
        }

        int GraphsNewStartIndex = newSlotIndex * divider;
        for (GraphPanel panel : graphPanels) {
            panel.setStartIndex(GraphsNewStartIndex);
            panel.repaint();
        }
    }

    private void moveScroll(int newScrollPosition) {
        moveCompressedGraphs(newScrollPosition);
    }

    int isSlotInWorkspace(int slotIndex, int startIndex) {
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
        if (compressedGraphPanels == null){
            return 0;
        }
        if (compressedGraphPanels.size() == 0) {
            return 0;
        }
        return compressedGraphPanels.get(0).getSlotWidth();
    }


    private int getSlotIndex() {
        if (compressedGraphPanels == null){
            return 0;
        }
        if (compressedGraphPanels.size() == 0) {
            return 0;
        }
        return compressedGraphPanels.get(0).getSlotIndex();
    }

    private int getGraphsSize() {
        if (graphPanels == null){
            return 0;
        }
        if (graphPanels.size() == 0) {
            return 0;
        }
        return graphPanels.get(0).getGraphsSize();
    }

    private int getCompressedGraphsSize() {
        if (compressedGraphPanels == null){
            return 0;
        }
        if (compressedGraphPanels.size() == 0) {
            return 0;
        }
        return compressedGraphPanels.get(0).getGraphsSize();
    }

    private int getGraphsStartIndex() {
        if (graphPanels == null){
            return 0;
        }
        if (graphPanels.size() == 0) {
            return 0;
        }
        return graphPanels.get(0).getStartIndex();
    }

    private int getCompressedGraphsStartIndex() {
        if (compressedGraphPanels == null){
            return 0;
        }
        if (compressedGraphPanels.size() == 0) {
            return 0;
        }
        return compressedGraphPanels.get(0).getStartIndex();
    }


    protected int getWorkspaceWidth() {
        return (getSize().width - GraphPanel.X_INDENT);
    }


}