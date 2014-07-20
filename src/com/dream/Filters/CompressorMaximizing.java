package com.dream.Filters;

import com.dream.ApparatModel;
import com.dream.Data.DataStream;

/**
 *
 */

public class CompressorMaximizing extends Filter<Integer> {
    public CompressorMaximizing(DataStream<Integer> inputData, int compression) {
        super(inputData, compression);
    }

    @Override
    protected Integer getData(int index) {
        if (index == 0) return 0;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < compression; i++) {
            max = Math.max(max, inputData.get(index * compression + i));
        }
        return max;
    }
}

