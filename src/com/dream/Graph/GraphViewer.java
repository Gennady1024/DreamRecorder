package com.dream.Graph;

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
public class GraphViewer extends JFrame implements  SlotListener {
    
    private int frequency;
    private int divider;
    private long startTime;
    private String title = "Dream Recorder";

    private ArrayList<GraphPanel> graphPanels = new ArrayList<GraphPanel>();
    private ArrayList<CompressedGraphPanel> compressedGraphPanels = new ArrayList<CompressedGraphPanel>();
    private ArrayList<Integer>  panelWeights = new ArrayList<Integer>();
    private JPanel mainPanel = new JPanel();
    private JPanel scrollablePanel = new JPanel();
    private JScrollPane scrollPanel = new JScrollPane(scrollablePanel,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    public GraphViewer(int frequency, int divider) {
       this.frequency = frequency;
       this.divider = divider;

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_RIGHT) {
                    moveSlot(1);
                }
                if(compressedGraphPanels.get(0).isSlotInWorkspace() == 1) {
                    Point viewPosition  = scrollPanel.getViewport().getViewPosition();
                    Point newViewPosition = new Point(viewPosition.x+1, viewPosition.y);
                    scrollPanel.getViewport().setViewPosition(newViewPosition);
                }

                if (key == KeyEvent.VK_LEFT) {
                    moveSlot(-1);
                    if(compressedGraphPanels.get(0).isSlotInWorkspace() == -1) {
                        Point viewPosition  = scrollPanel.getViewport().getViewPosition();
                        Point newViewPosition = new Point(viewPosition.x-1, viewPosition.y);
                        scrollPanel.getViewport().setViewPosition(newViewPosition);
                    }
                }
            }
        });

    }

    public void addGraphPanel(int graphsAmount, int weight) {
        panelWeights.add(weight);
        graphPanels.add(new  GraphPanel(graphsAmount, frequency));
    }

    public void addCompressedGraphPanel(int graphsAmount, int weight) {
        panelWeights.add(weight);
        CompressedGraphPanel panel =  new CompressedGraphPanel(graphsAmount, frequency/divider, divider);
        panel.addSlotListener(this);
        compressedGraphPanels.add(panel);
    }


    public void addData(int[] data) {
        int dataIndex = 0;
        for(GraphPanel panel: graphPanels) {
             int graphAmount = panel.getGraphAmount();
            for(int j = 0; j < graphAmount; j++) {
                if(dataIndex < data.length) {
                    panel.addData(j, data[dataIndex]);
                    dataIndex++;
                }
            }
        }
    }

    public void addCompressedData(int[] data) {
        int dataIndex = 0;
        for(CompressedGraphPanel panel: compressedGraphPanels) {
            int graphAmount = panel.getGraphAmount();
            for(int j = 0; j < graphAmount; j++) {
                if(dataIndex < data.length) {
                    panel.addData(j, data[dataIndex]);
                    dataIndex++;
                }
            }
        }
        Dimension oldDimension = scrollablePanel.getPreferredSize();
        scrollablePanel.setPreferredSize(new Dimension(oldDimension.width+1, oldDimension.height));
        //scrollPanel.getViewport().setViewPosition(new Point(50, 0));
        scrollablePanel.revalidate(); // we always have to call component.revalidate() after changing it "directly"(outside the GUI)
        scrollPanel.repaint();
    }


    public void start() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(title);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.GRAY);
        addMenu();
        add(mainPanel, BorderLayout.CENTER);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = dimension.width-20;
        int screenHeight = dimension.height-200;

        int sumWeight = 0;
        for(int i = 0; i < panelWeights.size(); i++) {
            sumWeight += panelWeights.get(i);
        }

        for(int i = 0; i < graphPanels.size(); i++) {
            GraphPanel panel =  graphPanels.get(i);
            int panelWeight = panelWeights.get(i);
            panel.setPreferredSize(new Dimension(screenWidth, screenHeight*panelWeight/sumWeight));
            mainPanel.add(panel);
        }

        for(int i = 0; i < compressedGraphPanels.size(); i++) {
            CompressedGraphPanel panel =  compressedGraphPanels.get(i);
            int panelWeight = panelWeights.get(i+graphPanels.size());
            panel.setPreferredSize(new Dimension(screenWidth, screenHeight*panelWeight/sumWeight));
            mainPanel.add(panel);
        }


        scrollablePanel.setPreferredSize(new Dimension(GraphPanel.X_INDENT,0));
        scrollablePanel.setBackground(Color.red);
        scrollPanel.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                moveScroll(e.getValue());
            }
        });
        add(scrollPanel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    private void addMenu() {
        JMenuBar mainMenu = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        mainMenu.add(fileMenu);

        JMenu recordMenu = new JMenu("Record");
        mainMenu.add(recordMenu);

        JMenu optionsMenu = new JMenu("Options");
        mainMenu.add(optionsMenu);
        add(mainMenu,BorderLayout.NORTH);
    }


    private void setGraphStartIndex(int startIndex) {
        for(GraphPanel panel: graphPanels) {
            panel.setStartIndex(startIndex);
            panel.repaint();
        }
    }

    private void setCompressedGraphStartIndex(int startIndex) {
        for(CompressedGraphPanel panel: compressedGraphPanels) {
            panel.setStartIndex(startIndex);
            panel.repaint();
        }
    }

    private void moveScroll(int scrollPosition) {
        setCompressedGraphStartIndex(scrollPosition);
    }

    private void setAutoZoom(boolean isAutoZoom) {
        for ( GraphPanel panel: graphPanels)  {
            panel.setAutoZoom(isAutoZoom);
        }

        for ( CompressedGraphPanel panel: compressedGraphPanels)  {
            panel.setAutoZoom(isAutoZoom);
        }
    }

    public void moveSlot(int slotPositionChange) {
        for(CompressedGraphPanel panel: compressedGraphPanels) {
            panel.moveSlot(slotPositionChange);
        }
    }
}
