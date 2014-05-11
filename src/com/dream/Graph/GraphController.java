package com.dream.Graph;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 12.05.14
 * Time: 0:49
 * To change this template use File | Settings | File Templates.
 */
public class GraphController {
    private GraphViewer graphViewer;

    public GraphController(GraphViewer graphViewer) {
        this.graphViewer = graphViewer;
    }

    public void  moveScroll(int scrollPosition) {
        graphViewer.setBigScaledStartIndex(scrollPosition);
    }


}
