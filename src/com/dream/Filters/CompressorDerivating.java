package com.dream.Filters;

import com.dream.ApparatModel;
import com.dream.Data.DataStream;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 16.06.14
 * Time: 23:16
 * To change this template use File | Settings | File Templates.
 */
public class CompressorDerivating extends Filter<Integer> {

    public CompressorDerivating(DataStream<Integer> inputData) {
        super(inputData, ApparatModel.COMPRESSION);
    }

    @Override
    protected Integer getData(int index) {
        if (index == 0) return 0;
        int bufferSize = 20;
        int t = 1200;
        int stepsNumber = t/bufferSize;
        int  inputBegin = index*compression - t/2;
        int  inputEnd = index*compression + t/2;

        if(inputBegin < 0) {
            return 0;
        }
        if(inputEnd > size()*compression) {
            return 0;
        }

        int sum = 0;
        for( int i = 0; i < stepsNumber/2; i++) {
            sum += (stepsNumber/2 -i)*(inputData.get(index * compression + bufferSize*i) + inputData.get(index * compression - bufferSize*i));

        }
        return sum - 983100;
    }
}