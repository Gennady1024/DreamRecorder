package com.dream.Filters;

import com.dream.ApparatModel;
import com.dream.Data.DataStream;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 21.06.14
 * Time: 0:01
 * To change this template use File | Settings | File Templates.
 */

public class CompressorREM extends Filter<Integer> {
    public CompressorREM(DataStream<Integer> inputData, int compression ) {
        super(inputData, compression);
    }

    @Override
    protected Integer getData(int index) {
        if (index == 0) return 0;
        int peaksNumber = 0;
        boolean isUp = false;
        int COUNTER_MAX=3;
        int counter=0;

        for (int i = 0; i < compression; i++) {

            if(Math.abs(inputData.get(index * compression + i)) > ApparatModel.remLimit) {
                if(!isUp)
                isUp=true;
                counter=0;


            }
            if(Math.abs(inputData.get(index * compression + i)) < ApparatModel.remLimit/3) {
                if(isUp) {
                    if((++counter > COUNTER_MAX)) {
                        isUp=false;
                        peaksNumber++;
                        counter = 0;
                    }
                }
            }
        }
        if(peaksNumber > 2) {
            return 500;
        }


        for (int i = 0; i < compression; i++) {

            if(Math.abs(inputData.get(index * compression + i)) > (ApparatModel.remLimit - ApparatModel.remLimit/3) ){
                   isUp=true;
                   counter=0;
            }
            if(Math.abs(inputData.get(index * compression + i)) < ApparatModel.remLimit/3) {
                if(isUp) {
                    if((++counter > COUNTER_MAX)) {
                    isUp=false;
                    peaksNumber++;
                    counter = 0;
                    }
                }
            }
        }
        if(peaksNumber > 2) {
            return 250;
        }
        else {
            return 0;
        }
    }
}