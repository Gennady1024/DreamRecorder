package com.dream;

import com.dream.Graph.GraphViewer;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 10.05.14
 * Time: 12:40
 * To change this template use File | Settings | File Templates.
 */
public class Controller {
    private GraphViewer graphViewer;

    public Controller(GraphViewer graphViewer) {
        this.graphViewer = graphViewer;
    }

    public void sendData() {
        for(int x = 0; x < 2000; x++) {
            int[] data = new int[3];

            data[0] = x%25;
            data[1] = x%50;
            data[2] = x%100;
            graphViewer.addData(data);
            graphViewer.addCompressedData(data);
        }

        for(int x = 0; x < 10; x++) {
            int[] data = new int[3];

            data[0] = 20;
            data[1] = 20;
            data[2] = 20;
            graphViewer.addData(data);
            graphViewer.addCompressedData(data);
        }

    }

}
