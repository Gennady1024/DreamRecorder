package com.dream.Filters;

import com.dream.Data.DataStream;

/**
 *
 */

public class FilterScaling extends Filter<Integer> {

    public FilterScaling(DataStream<Integer> inputData) {
        super(inputData);
    }

    @Override
    protected Integer getData(int index) {
        int SCALE = 4;
        return (int) Math.sqrt(inputData.get(index)  * SCALE) - 50;
    }
}
