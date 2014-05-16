package com.dream;

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
    private GraphViewer mainPanel;

    public MainView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(title);
        addMenu();
        addGraphViewer();
        pack();
        setVisible(true);
    }

    private void addGraphViewer() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = dimension.width-20;
        int screenHeight = dimension.height-200;
        add(mainPanel, BorderLayout.CENTER);
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
}
