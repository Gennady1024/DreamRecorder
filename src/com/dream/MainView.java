package com.dream;

import com.dream.Filters.CompressedStreamDataAdapter;
import com.dream.Filters.StreamDataAdapter;
import com.dream.Graph.GraphsViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Main Window of our program...
 */
public class MainView extends JFrame {
    private String title = "Dream Recorder";
    private GraphsViewer graphsViewer;
    private  JMenuBar menu = new JMenuBar();
    private  ApparatModel model;
    private Controller controller;

    public MainView(ApparatModel apparatModel, Controller controller) {
        model = apparatModel;
        this.controller = controller;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(title);

        formMenu();

        // Key Listener to change MovementLimit in model
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                graphsViewer.dispatchEvent(e); // send KeyEvent to graphsViewer
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_UP) {
                    model.movementLimitUp();
                    graphsViewer.syncView();
                }

                if (key == KeyEvent.VK_DOWN) {
                    model.movementLimitDown();
                    graphsViewer.syncView();
                }
            }
        });

        graphsViewer = new GraphsViewer(model.COMPRESSION);
        graphsViewer.setPreferredSize(getWorkspaceDimention());
        graphsViewer.addGraphPanel(1, true);
        graphsViewer.addGraphPanel(1, true);
        graphsViewer.addGraphPanel(1, true);
        graphsViewer.addCompressedGraphPanel(1, false);
        graphsViewer.addCompressedGraphPanel(1, true);

        graphsViewer.addGraph(0, new StreamDataAdapter<Integer>(model) {
            @Override
            public Integer get(int index) {
                return getModel().getAccGraphData(index, false);
            }
        });

        graphsViewer.addGraph(0, new StreamDataAdapter<Integer>(model) {
            @Override
            public Integer get(int index) {
                return getModel().getAccGraphData(index, true);
            }
        });

      /*  graphsViewer.addGraph(0, new StreamDataAdapter<Integer>(model) {
            @Override
            public Integer get(int index) {
                return getModel().getAccPosition(index);
            }
        });  */



        graphsViewer.addGraph(1, new StreamDataAdapter<Integer>(model) {
            @Override
            public Integer get(int index) {
                return getModel().getHiPassedData(index, 600);
            }
        });

        graphsViewer.addGraph(2, new StreamDataAdapter<Integer>(model) {
            @Override
            public Integer get(int index) {
                return getModel().getLowPassData(index);
            }
        });




        graphsViewer.addCompressedGraph(0, new CompressedStreamDataAdapter<Integer>(model) {
            @Override
            public Integer get(int index) {
                return getModel().getCompressedDreamGraph(index);
            }
        });

        graphsViewer.addCompressedGraph(1, new CompressedStreamDataAdapter<Integer>(model) {
            @Override
            public Integer get(int index) {
                return getModel().getCompressedAccPosition(index);
            }
        });

        graphsViewer.addCompressedGraph(1, new CompressedStreamDataAdapter<Integer>(model) {
            @Override
            public Integer get(int index) {
                return getModel().getCompressedAccMovement(index);
            }
        });


        add(graphsViewer, BorderLayout.CENTER);

        pack();
        setFocusable(true);
        setVisible(true);
    }



    public void syncView() {
        graphsViewer.syncView();
    }

    public void showMessage(String s) {
        JOptionPane.showMessageDialog(this, s);
    }

    public void setStart(long starTime, int period_msec) {
        graphsViewer.setStart(starTime, period_msec);
    }

    private Dimension getWorkspaceDimention() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int width = dimension.width - 20;
        int height = dimension.height - 150;
        return new Dimension(width, height);
    }


    private void formMenu() {
        JMenu fileMenu = new JMenu("File");
        menu.add(fileMenu);
        JMenuItem open = new JMenuItem("Open");
        fileMenu.add(open);

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.readFromFile();
            }
        });


        JMenu recordMenu = new JMenu("Record");
        menu.add(recordMenu);
        JMenuItem start = new JMenuItem("Start");
        JMenuItem stop = new JMenuItem("Stop");
        recordMenu.add(start);
        recordMenu.add(stop);

        add(menu, BorderLayout.NORTH);
    }
}
