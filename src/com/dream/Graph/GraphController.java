package com.dream.Graph;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 10.05.14
 * Time: 12:40
 * To change this template use File | Settings | File Templates.
 */
public class GraphController {
    private GraphViewer graphViewer;

    public GraphController(GraphViewer graphViewer) {
        this.graphViewer = graphViewer;
    }

    public void sendData() {
        for(int x = 0; x < 1000; x++) {
            int[] data = new int[3];

            data[0] = x%25;
            data[1] = x%50;
            data[2] = x%100;
            graphViewer.addData(data);
        }
    }

}
