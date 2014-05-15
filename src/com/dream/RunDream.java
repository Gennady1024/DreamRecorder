package com.dream;

import com.dream.Graph.GraphViewer;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 07.05.14
 * Time: 21:36
 * To change this template use File | Settings | File Templates.
 */
public class RunDream {
    public static void main(String[] args) {
      GraphViewer graphWindow = new GraphViewer(10, 120);
        graphWindow.addGraphPanel(1,1);
        graphWindow.addGraphPanel(2,2);
        graphWindow.addCompressedGraphPanel(1, 1);
        graphWindow.addCompressedGraphPanel(2, 2);
        graphWindow.start();

        Controller controller = new Controller(graphWindow);
        controller.sendData();

    }
}
