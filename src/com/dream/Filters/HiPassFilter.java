package com.dream.Filters;

import com.dream.Data.StreamData;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 25.05.14
 * Time: 0:29
 * To change this template use File | Settings | File Templates.
 */
public class HiPassFilter extends AbstractFilter<Integer> {
    private int bufferSize;

    public HiPassFilter(StreamData<Integer> inputData, int bufferSize) {
        super(inputData);
        this.bufferSize = bufferSize;
    }

    @Override
    protected Integer doFilter(int index) {
        if (index < bufferSize) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < bufferSize; i++) {
            sum+=inputData.get(index - i);
        }
        return inputData.get(index) - sum/bufferSize;
    }
}