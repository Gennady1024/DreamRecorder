package com.dream.Filters;

import com.dream.ApparatModel;
import com.dream.Data.DataStream;
import com.dream.Graph.GraphsViewer;
import com.dream.MainView;

/**
 *
 */
public class CompressorAveraging extends Compressor {
    public CompressorAveraging(DataStream<Integer> inputData, GraphsViewer viewer) {
        super(inputData, viewer);
    }

    @Override
    protected Integer getData(int index) {
        int compression = viewer.getCompression();
        if (index == 0) return 0;
        int sum = 0;
        for (int i = 0; i < compression; i++) {
            sum += inputData.get(index * compression + i);
        }
        return sum / compression;
    }


}
