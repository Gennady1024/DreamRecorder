package com.dream.Filters;

import com.dream.ApparatModel;
import com.dream.Data.DataStream;
import com.dream.Graph.GraphsViewer;

/**
 *
 */

public class CompressorMaximizing extends Compressor {
    public CompressorMaximizing(DataStream<Integer> inputData, GraphsViewer viewer) {
        super(inputData, viewer);
    }

    @Override
    protected Integer getData(int index) {
        int compression = viewer.getCompression();
        if (index == 0) return 0;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < compression; i++) {
            max = Math.max(max, inputData.get(index * compression + i));
        }
        return max;
    }
}

