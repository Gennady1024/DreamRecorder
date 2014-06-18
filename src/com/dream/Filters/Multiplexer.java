package com.dream.Filters;

import com.dream.Data.DataStream;

/**
 *
 */
public class Multiplexer extends Filter<Integer> {
    protected final DataStream<Integer> selectorData;

    public Multiplexer(DataStream<Integer> inputData, DataStream<Integer> selectorData) {
        super(inputData);
        this.selectorData = selectorData;
    }

    @Override
    protected Integer getData(int index) {
        if (selectorData.get(index) == 0) {
            return inputData.get(index);
        }
        else {
          return Integer.MAX_VALUE;
        }
    }
}
