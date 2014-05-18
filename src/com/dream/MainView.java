package com.dream;

import com.dream.Data.StreamData;
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
    private  JMenuBar menu = new JMenuBar();

    public MainView(int frequency, int divider) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(title);

        formMenu();

        graphViewer = new GraphViewer(frequency, divider);
        graphViewer.setPreferredSize(getWorkspaceDimention());
        graphViewer.addGraphPanel(1);
        graphViewer.addGraphPanel(2);
        graphViewer.addCompressedGraphPanel(1);
        graphViewer.addCompressedGraphPanel(2);
        add(graphViewer, BorderLayout.CENTER);


        pack();
        setVisible(true);
    }

    @Override
    public void repaint() {
        graphViewer.repaint();
        super.repaint();
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

    public void addGraph(int panelNumber, StreamData<Integer> graphData) {
        graphViewer.addGraph(panelNumber, graphData);
    }



    public void addCompressedGraph(int panelNumber, StreamData<Integer> graphData) {
        graphViewer.addCompressedGraph(panelNumber, graphData);
    }

}
