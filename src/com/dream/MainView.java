package com.dream;

import com.dream.Data.DataStream;
import com.dream.Filters.*;
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

                if (key == KeyEvent.VK_1) {
                    model.remLimitUp();
                    graphsViewer.syncView();
                }

                if (key == KeyEvent.VK_2) {
                    model.remLimitDown();
                    graphsViewer.syncView();
                }
            }
        });

        graphsViewer = new GraphsViewer(model.COMPRESSION_120);
        graphsViewer.setPreferredSize(getWorkspaceDimention());

        graphsViewer.addGraphPanel(1, true);
        graphsViewer.addGraphPanel(1, true);
        graphsViewer.addGraphPanel(1, true);
        graphsViewer.addGraphPanel(1, true);

        graphsViewer.addCompressedGraphPanel(1, false);
        graphsViewer.addCompressedGraphPanel(1, true);

        /*
        DataStream<Integer> AccGraphData =  new FilterScaling(model.getAccMovementStream());
        DataStream<Integer> AccMovementLimitGraphData =  new FilterScaling(model.getAccMovementLimitStream());

        graphsViewer.addGraph(0, AccGraphData);
        graphsViewer.addGraph(0, AccMovementLimitGraphData);    */


//        DataStream<Integer> eyeHiPassedData =  new FilterHiPass(model.getCh1DataList(), 300);
//        DataStream<Integer> selectedEyeHiPassedData = new Multiplexer(eyeHiPassedData, model.getNotSleepEventsStream());
//        graphsViewer.addGraph(0, selectedEyeHiPassedData);

        graphsViewer.addGraph(0, new FilterHiPass(model.getCh1DataList(), 300));
        graphsViewer.addGraph(1, new FilterHiPass(model.getCh2DataList(), 300));
        graphsViewer.addGraph(2, new FilterDerivative(model.getCh1DataList()));
        graphsViewer.addGraph(3, new FilterDerivative(model.getCh2DataList()));

        //graphsViewer.addGraph(2, new FilterHiPassAdapt(model.getCh1DataList(), 15));
        //graphsViewer.addGraph(2,new FilterDerivativeAbs(model.getCh1DataList()));


        //graphsViewer.addGraph(2, model.getSleepPatternsStream());

        DataStream<Integer> compressedDreamGraph = new CompressorAveraging(new FilterDerivativeAbs(model.getCh1DataList()), model.getCompression());
        DataStream<Integer>  compressedNotSleepEvents = new CompressorMaximizing(model.getNotSleepEventsStream(), model.getCompression());
        DataStream<Integer>  selectedCompressedDreamGraph = new Multiplexer(compressedDreamGraph, compressedNotSleepEvents);
        graphsViewer.addCompressedGraph(0, selectedCompressedDreamGraph);


//        DataStream<Integer> eyeDerivativeAvg = new FilterDerivative1Plus2Abs(model.getCh1DataList(), 20);
//        DataStream<Integer> compressedSlowDreamGraph = new CompressorDerivating(eyeDerivativeAvg);
//        DataStream<Integer>  selectedCompressedSlowDreamGraph = new Multiplexer(compressedSlowDreamGraph, compressedNotSleepEvents);



//        DataStream<Integer> eyeDerivativeAbs =  new FilterDerivativeAbs(model.getCh1DataList());
//        DataStream<Integer> compressedREMGraph = new CompressorREM(eyeDerivativeAbs);
//        DataStream<Integer>  selectedCompressedREMGraph = new Multiplexer(compressedREMGraph, compressedNotSleepEvents);
//        graphsViewer.addCompressedGraph(1, selectedCompressedREMGraph);

        // graphsViewer.addCompressedGraph(2, selectedCompressedSlowDreamGraph);

        DataStream<Integer> compressedAccPosition = new CompressorAveraging(model.getAccPositionStream(), model.getCompression());
        graphsViewer.addCompressedGraph(1, compressedAccPosition);

        DataStream<Integer> compressedAccMovement = new CompressorMaximizing(model.getAccMovementStream(), model.getCompression());
        compressedAccMovement = new FilterScaling(compressedAccMovement); // scaling
        graphsViewer.addCompressedGraph(1, compressedAccMovement);

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
