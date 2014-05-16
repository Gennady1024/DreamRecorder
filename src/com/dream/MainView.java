package com.dream;

import com.dream.Data.Stock;
import com.dream.Graph.GraphViewer;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 16.05.14
 * Time: 13:05
 * To change this template use File | Settings | File Templates.
 */
public class MainView extends JFrame {
    private String title = "Dream Recorder";
    private GraphViewer graphViewer;

    public MainView(int frequency, int divider) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(title);

        addMenu();

        graphViewer = new GraphViewer(frequency, divider);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = dimension.width-20;
        int screenHeight = dimension.height-200;
        graphViewer.addGraphPanel(1);
        graphViewer.addGraphPanel(2);
        graphViewer.addCompressedGraphPanel(1);
        graphViewer.addCompressedGraphPanel(2);
        add(graphViewer, BorderLayout.CENTER);
        graphViewer.setPreferredSize(new Dimension(screenWidth, screenHeight));

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

    public void addGraph(int panelNumber, Stock<Integer> graphData) {
        graphViewer.addGraph(panelNumber, graphData);
    }

    public void addCompressedGraph(int panelNumber, Stock<Integer> graphData) {
        graphViewer.addCompressedGraph(panelNumber, graphData);
    }

}
