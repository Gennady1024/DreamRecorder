package com.dream.Filters;

import com.dream.Data.DataStream;

/**
 *
 */

public class FilterDerivativeTest extends Filter<Integer> {

    public FilterDerivativeTest(DataStream<Integer> inputData) {
        super(inputData);
    }

    @Override
    protected Integer getData(int index) {
        return getDerivative1(index);
    }

    protected int getDerivative1(int index) {
        int bufferSize =  4;
        if (index <= bufferSize) {
            return 0;
        }
        return (inputData.get(index) - inputData.get(index-bufferSize));
    }

    protected int getDerivative(int index) {
        int bufferSize =  2;
        if (index <= bufferSize) {
            return 0;
        }
        if (index >= (size() - bufferSize)) {
            return 0;
        }
        int sum1 = 0;
        int sum2 = 0;
        for (int i = 0; i <= bufferSize; i++) {
            sum1 = inputData.get(index+i);
            sum2 = inputData.get(index - 1 - i);
        }

        return (sum1 - sum2)/ (bufferSize + 1);
    }
}