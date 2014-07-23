package com.dream.Filters;

import com.dream.Data.DataStream;
import com.dream.Graph.GraphsViewer;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 24.07.14
 * Time: 0:12
 * To change this template use File | Settings | File Templates.
 */
public abstract class Compressor extends Filter<Integer> {
    protected GraphsViewer viewer;

    public Compressor(DataStream<Integer> inputData, GraphsViewer viewer) {
        super(inputData);
        this.viewer = viewer;
    }

    @Override
    public int size() {
        return inputData.size()/ viewer.getCompression();
    }
}
