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
     /*   int bufferSize = 2;
        if (index < bufferSize) {
            return 0;
        }
        if (index > size()-bufferSize) {
            return 0;
        }
        int sum1 = 0;
        int sum2 = 0;

        for (int i = 1; i < bufferSize; i++) {
            sum1 += inputData.get(index + i);
            sum2 += inputData.get(index - i);
        }
        return  (sum1 - sum2)/(2*bufferSize);  */
        if (index == 0) {
            return 0;
        }

        return  inputData.get(index) - inputData.get(index-1);
    }
}

