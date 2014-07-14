package com.dream.Filters;

import com.dream.Data.DataStream;

/**
 *
 */

public class FilterDerivative extends Filter<Integer> {

    public FilterDerivative(DataStream<Integer> inputData) {
        super(inputData);
    }

    @Override
    protected Integer getData(int index) {
        if (index == 0) {
            return 0;
        }
        return inputData.get(index) - inputData.get(index - 1);
    }
}
