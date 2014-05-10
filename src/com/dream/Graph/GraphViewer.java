package com.dream.Graph;

import com.dream.Graph.GraphPanel;
import com.github.dreamrec.GUIActions;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: galafit
 * Date: 07/05/14
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
public class GraphViewer extends JFrame{
    
    private int frequency;
    private int divider;
    private long startTime;
    private boolean isZoomAutomatic;
    private String title = "Dream Recorder";

    private ArrayList<GraphPanel> graphPanels = new ArrayList<GraphPanel>();
    private ArrayList<BigScaledGraphPanel> bigScaledGraphPanels = new ArrayList<BigScaledGraphPanel>();
    private ArrayList<Integer>  panelWeights = new ArrayList<Integer>();
    private JPanel mainPanel = new JPanel();
    private int strut = 2; // strut between panels


    public GraphViewer(int frequency, int divider) {
       this.frequency = frequency;
       this.divider = divider;
        isZoomAutomatic = true;
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.GRAY);
        addMenu();
        add(mainPanel, BorderLayout.CENTER);
        addScrollBar();
    }

    public void setZoomType(boolean isZoomAutomatic) {
        isZoomAutomatic = isZoomAutomatic;
    }

    public void addGraphPanel(int graphsAmount, int weight) {
        panelWeights.add(weight);
        graphPanels.add(new  GraphPanel(graphsAmount, frequency));
    }

    public void addBigScaledGraphPanel(int graphsAmount, int weight) {
        panelWeights.add(weight);
        bigScaledGraphPanels.add(new  BigScaledGraphPanel(graphsAmount, frequency/divider));
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

    private void addScrollBar() {
        JScrollBar scrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
        add(scrollBar, BorderLayout.SOUTH);
    }

    public void addData() {

    }
    public void start() {
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
            if(i > 0) {
                mainPanel.add(Box.createVerticalStrut(strut));
            }
            mainPanel.add(panel);
        }

        for(int i = 0; i < bigScaledGraphPanels.size(); i++) {
            BigScaledGraphPanel panel =  bigScaledGraphPanels.get(i);
            int panelWeight = panelWeights.get(i+graphPanels.size());
            panel.setPreferredSize(new Dimension(screenWidth, screenHeight*panelWeight/sumWeight));

            mainPanel.add(Box.createVerticalStrut(strut));

            mainPanel.add(panel);
        }

        pack();
        setVisible(true);
    }
}
