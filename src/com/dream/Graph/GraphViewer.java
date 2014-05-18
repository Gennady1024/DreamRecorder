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
public class GraphViewer extends JPanel implements SlotListener {

    private boolean isAutoScroll = true;
    private int frequency;
    private int divider;
    private long startTime;

    private ArrayList<GraphPanel> graphPanels = new ArrayList<GraphPanel>();
    private ArrayList<CompressedGraphPanel> compressedGraphPanels = new ArrayList<CompressedGraphPanel>();

    private JPanel PaintingPanel = new JPanel();
    private JPanel scrollablePanel = new JPanel();
    private JScrollPane scrollPanel = new JScrollPane(scrollablePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    public GraphViewer(int frequency, int divider) {
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

    private void adjustScroll() {
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

    @Override
    public void setPreferredSize(Dimension d) {
        super.setPreferredSize(d);
        setPanelsPreferredSizes();
    }

    @Override
    public void repaint() {
        adjustScroll();
        super.repaint();
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

    private void moveGraphs(int newStartIndex) {
        for (GraphPanel panel : graphPanels) {
            panel.moveGraphs(newStartIndex);
            panel.repaint();
        }
    }

    private void moveCompressedGraphs(int newStartIndex) {
        for (CompressedGraphPanel panel : compressedGraphPanels) {
            panel.moveGraphs(newStartIndex);
            panel.repaint();
        }
    }

    private void moveScroll(int newScrollPosition) {
        moveCompressedGraphs(newScrollPosition);
    }

    private void setAutoZoom(boolean isAutoZoom) {
        for (GraphPanel panel : graphPanels) {
            panel.setAutoZoom(isAutoZoom);
        }

        for (CompressedGraphPanel panel : compressedGraphPanels) {
            panel.setAutoZoom(isAutoZoom);
        }
    }

    public void moveSlot(int newSlotIndex) {

        for (CompressedGraphPanel panel : compressedGraphPanels) {
            panel.moveSlot(newSlotIndex);
        }
        adjustScroll();
    }
}
