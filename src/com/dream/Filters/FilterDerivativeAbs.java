package com.dream.Filters;

import com.dream.Data.DataStream;

/**
 *
 */

public class FilterDerivativeAbs extends Filter<Integer> {
    private int NOISE_LEVEL = 0;
    private int derivative = 0;

    public FilterDerivativeAbs(DataStream<Integer> inputData) {
        super(inputData);
    }

    @Override
    protected Integer getData(int index) {
        if (index == 0) {
            return 0;
        }

        derivative = Math.abs(inputData.get(index) - inputData.get(index - 1));

        if(derivative > NOISE_LEVEL) {
            return derivative;
        }
        else return 0;
    }
}
