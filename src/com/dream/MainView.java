package com.dream;

import com.dream.Data.StreamData;
import com.dream.Filters.DreamOverviewFilter;
import com.dream.Filters.HiPassFilter;
import com.dream.Graph.GraphsViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main Window of our program...
 */
public class MainView extends JFrame {
    private String title = "Dream Recorder";
    private GraphsViewer graphsViewer;
    private  JMenuBar menu = new JMenuBar();
    private ApparatModel model;
    private Controller controller;

    public MainView(ApparatModel model, Controller controller) {
        this.model = model;
        this.controller = controller;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(title);

        formMenu();

        graphsViewer = new GraphsViewer(model.DIVIDER);
        graphsViewer.setPreferredSize(getWorkspaceDimention());
        graphsViewer.addGraphPanel(1, true);
        graphsViewer.addGraphPanel(1, true);
        graphsViewer.addCompressedGraphPanel(1, false);
        graphsViewer.addCompressedGraphPanel(1, true);

        graphsViewer.addGraph(1, new HiPassFilter(model.getCh1DataList(), 50));
        graphsViewer.addCompressedGraph(0, new DreamOverviewFilter(model.getCh1DataList(), model.DIVIDER));

        add(graphsViewer, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }



    public void syncView() {
        graphsViewer.syncView();
    }

    public void showMessage(String s) {
        JOptionPane.showMessageDialog(this, s);
    }

    public void setStart(long starTime, double frequency) {
        graphsViewer.setStart(starTime, frequency);
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
