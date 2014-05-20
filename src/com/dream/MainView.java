package com.dream;

import com.dream.Data.StreamData;
import com.dream.Graph.GraphsViewer;

import javax.swing.*;
import java.awt.*;

/**
 * Main Window of our program...
 */
public class MainView extends JFrame {
    private String title = "Dream Recorder";
    private GraphsViewer graphsViewer;
    private  JMenuBar menu = new JMenuBar();

    public MainView(int frequency, int divider) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(title);

        formMenu();

        graphsViewer = new GraphsViewer(frequency, divider);
        graphsViewer.setPreferredSize(getWorkspaceDimention());
        graphsViewer.addGraphPanel(1);
        graphsViewer.addGraphPanel(2);
        graphsViewer.addCompressedGraphPanel(1);
        graphsViewer.addCompressedGraphPanel(2);
        add(graphsViewer, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    /**
     *
     */
    public void addGraph(int panelNumber, StreamData<Integer> graphData) {
        graphsViewer.addGraph(panelNumber, graphData);
    }


    public void addCompressedGraph(int panelNumber, StreamData<Integer> graphData) {
        graphsViewer.addCompressedGraph(panelNumber, graphData);
    }

    public void syncView() {
        graphsViewer.syncView();
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

        JMenu recordMenu = new JMenu("Record");
        menu.add(recordMenu);

        JMenu optionsMenu = new JMenu("Options");
        menu.add(optionsMenu);
        add(menu,BorderLayout.NORTH);
    }
}
