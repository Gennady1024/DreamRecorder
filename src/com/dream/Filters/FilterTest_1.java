package com.dream.Filters;

import com.dream.Data.DataStream;


/**
 *
 */
public class FilterTest_1 extends Filter<Integer> {
    private int timer = 0;
    private int NOISE_LEVEL = 400;
    private int sum = 0;

    public FilterTest_1(DataStream<Integer> inputData) {
        super(inputData);
    }

    @Override
    protected Integer getData(int index) {
        if(index > 1) {
            if(Math.abs(inputData.get(index) - inputData.get(index - 1)) > NOISE_LEVEL) {
                timer = 50;
            }
                sum += (inputData.get(index) - inputData.get(index - 1));
                timer--;
        }

        if(timer <= 0) {
            sum = 0;
        }
        return sum;
    }
}