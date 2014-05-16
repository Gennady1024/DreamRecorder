package com.dream.Graph;

import com.dream.Data.Stock;

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

    private int frequency;
    private int divider;
    private long startTime;

    private ArrayList<GraphPanel> graphPanels = new ArrayList<GraphPanel>();
    private ArrayList<CompressedGraphPanel> compressedGraphPanels = new ArrayList<CompressedGraphPanel>();

    private JPanel mainPanel = new JPanel();
    private JPanel scrollablePanel = new JPanel();
    private JScrollPane scrollPanel = new JScrollPane(scrollablePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    public GraphViewer(int frequency, int divider) {
        this.frequency = frequency;
        this.divider = divider;
        setLayout(new BorderLayout());

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel, BorderLayout.CENTER);

        scrollPanel.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                moveScroll(e.getValue());
            }
        });
        add(scrollPanel, BorderLayout.SOUTH);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_RIGHT) {
                    moveSlot(1);
                }

                if (key == KeyEvent.VK_LEFT) {
                    moveSlot(-1);
                }
            }
        });
    }


    public void addGraphPanel(int weight) {
        GraphPanel panel = new GraphPanel(weight, frequency);
        graphPanels.add(panel);
        mainPanel.add(panel);
        //adjustSizes();
    }

    public void addCompressedGraphPanel(int weight) {
        CompressedGraphPanel panel = new CompressedGraphPanel(weight, frequency / divider, divider);
        panel.addSlotListener(this);
        compressedGraphPanels.add(panel);
        mainPanel.add(panel);
        //adjustSizes();
    }

    public void addGraph(int panelNumber, Stock<Integer> graphData) {
        if (panelNumber < graphPanels.size()) {
            graphPanels.get(panelNumber).addGraph(graphData);
        }
    }

    public void addCompressedGraph(int panelNumber, Stock<Integer> graphData) {
        if (panelNumber < compressedGraphPanels.size()) {
            compressedGraphPanels.get(panelNumber).addGraph(graphData);
        }
    }

    private void adjustScroll() {
        if(compressedGraphPanels.size() > 0) {
            CompressedGraphPanel panel = compressedGraphPanels.get(0);
            scrollablePanel.setPreferredSize(new Dimension(panel.getFullWidth(), 0));
            scrollPanel.getViewport().setViewPosition(panel.getViewPosition());
            scrollablePanel.revalidate(); // we always have to call component.revalidate() after changing it "directly"(outside the GUI)
            scrollPanel.repaint();
        }
    }

    private void adjustSizes(Dimension d) {
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
        //super.setPreferredSize(d);
        System.out.println("Panel setPreferredSize");
        adjustSizes(d);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
       // adjustSizes();
       // adjustScroll();
        //revalidate();
    }

    private void setGraphStartIndex(int startIndex) {
        for (GraphPanel panel : graphPanels) {
            panel.setStartIndex(startIndex);
            panel.repaint();
        }
    }

    private void setCompressedGraphStartIndex(int startIndex) {
        for (CompressedGraphPanel panel : compressedGraphPanels) {
            panel.setStartIndex(startIndex);
            panel.repaint();
        }
    }

    private void moveScroll(int scrollPosition) {
        setCompressedGraphStartIndex(scrollPosition);
        adjustScroll();
    }

    private void setAutoZoom(boolean isAutoZoom) {
        for (GraphPanel panel : graphPanels) {
            panel.setAutoZoom(isAutoZoom);
        }

        for (CompressedGraphPanel panel : compressedGraphPanels) {
            panel.setAutoZoom(isAutoZoom);
        }
    }

    public void moveSlot(int slotPositionChange) {
        for (CompressedGraphPanel panel : compressedGraphPanels) {
            panel.moveSlot(slotPositionChange);
        }
    }
}
