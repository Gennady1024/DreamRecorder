package com.dream.Filters;

import com.dream.Data.DataStream;

/**
 *
 */

public class FilterLowPass extends Filter<Integer> {
    private int bufferSize;

    public FilterLowPass(DataStream<Integer> inputData, int bufferSize) {
        super(inputData);
        this.bufferSize = bufferSize;
    }

    @Override
    protected Integer getData(int index) {
        if (index < bufferSize) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < bufferSize; i++) {
            sum += inputData.get(index - i);
        }
        return  sum / bufferSize;
    }
}