package com.dream.Filters;

import com.dream.Data.DataStream;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 18.06.14
 * Time: 13:44
 * To change this template use File | Settings | File Templates.
 */


public class FilterDerivativeAvgAbs extends Filter<Integer> {
    private int bufferSize;

    public FilterDerivativeAvgAbs(DataStream<Integer> inputData, int bufferSize) {
        super(inputData);
        this.bufferSize = bufferSize;
    }

    @Override
    protected Integer getData(int index) {
        if (index < bufferSize) {
            return 0;
        }
        if (index > size()-bufferSize) {
            return 0;
        }
        int sum1 = 0;
        int sum2 = 0;

        for (int i = 0; i < bufferSize; i++) {
            sum1 += inputData.get(index + i);
            sum2 += inputData.get(index - i);
        }
        return  Math.abs(sum2 - sum1)/(2*bufferSize);
    }
}