package com.dream.Filters;

import com.dream.Data.DataStream;

/**
 *
 */
public class FilterHiPassAdapt extends Filter<Integer> {
    private int bufferSize=0;
    private int NOISE_LEVEL = 200;
    private int BUFFER_SCALE = 30;

    public FilterHiPassAdapt(DataStream<Integer> inputData) {
        super(inputData);
    }

    @Override
    protected Integer getData(int index) {
        int derivation = 0;
        if(index > 0) {
            derivation = inputData.get(index) - inputData.get(index - 1);
        }
        if(Math.abs(derivation) > NOISE_LEVEL) {
            int newBufferSize = ((Math.abs(derivation) - NOISE_LEVEL)*BUFFER_SCALE)/NOISE_LEVEL;
            bufferSize = Math.max(bufferSize, newBufferSize);
            bufferSize = Math.min(bufferSize, 200);
        }
        if(bufferSize == 0) {
            return 0;
        }
        if (index < bufferSize) {
            return 0;
        }
        if (index > size()-bufferSize) {
            return 0;
        }
        int sum = 0;
        for (int i = 1; i <= bufferSize; i++) {
            sum += (inputData.get(index - i) + inputData.get(index + i));
        }
        int result = inputData.get(index) - sum/(2*bufferSize);
        System.out.println(index+"   buffer: "+bufferSize);
        bufferSize -= 2;
        bufferSize = Math.max(bufferSize, 0);
        return  result;
    }
}