package com.dream.Filters;

import com.dream.Data.DataStream;


/**
 *
 */
public class FilterTest  extends Filter<Integer> {
    private int value = 0;
    private int timer = 0;
    int NOISE_LEVEL = 200;

    public FilterTest (DataStream<Integer> inputData) {
        super(inputData);
    }

    @Override
    protected Integer getData(int index) {
        int derivation = 0;
        if(index > 0) {
            derivation = inputData.get(index) - inputData.get(index - 1);
        }
        if(Math.abs(derivation) > NOISE_LEVEL) {
            if(value == 0) {
                value = inputData.get(index - 1);
            }
            timer = 200;
        }
        if(timer > 0) {
            timer--;
        }
        if(timer == 0) {
            value = 0;
            return 0;
        }
        return  inputData.get(index) - value;

    }
}