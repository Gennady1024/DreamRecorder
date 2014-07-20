package com.dream.Filters;

import com.dream.ApparatModel;
import com.dream.Data.DataStream;

/**
 *
 */
public class CompressorAveraging extends Filter<Integer> {
    public CompressorAveraging(DataStream<Integer> inputData, int compression) {
        super(inputData, compression);
    }

    @Override
    protected Integer getData(int index) {
        if (index == 0) return 0;
        int sum = 0;
        for (int i = 0; i < compression; i++) {
            sum += inputData.get(index * compression + i);
        }
        return sum / compression;
    }
}
