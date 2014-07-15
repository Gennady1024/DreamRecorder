package com.dream.Filters;

import com.dream.Data.DataStream;

/**
 *
 */

public class FilterTest_3 extends Filter<Integer> {

    public FilterTest_3(DataStream<Integer> inputData) {
        super(inputData);
    }

    @Override
    protected Integer getData(int index) {
        int REM_LEVEL = 1500;
        int derivative = getDerivative(index);
        if(derivative > REM_LEVEL){
            return derivative;
        }
        return 0;
    }

    protected int getDerivative(int index) {
        int pointDistance =  4;
        if (index <= pointDistance) {
            return 0;
        }
        return (inputData.get(index) - inputData.get(index - pointDistance));
    }
}