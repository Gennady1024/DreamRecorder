package com.dream.Filters;

import com.dream.Data.DataStream;

/**
 *
 */

public class FilterDerivativeAbs extends Filter<Integer> {

    public FilterDerivativeAbs(DataStream<Integer> inputData) {
        super(inputData);
    }

    @Override
    protected Integer getData(int index) {
        if (index == 0) {
            return 0;
        }
        return Math.abs(inputData.get(index) - inputData.get(index - 1));
    }
}
