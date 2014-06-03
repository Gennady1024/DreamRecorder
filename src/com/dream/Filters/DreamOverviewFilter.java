package com.dream.Filters;

import com.dream.Data.StreamData;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 25.05.14
 * Time: 13:04
 * To change this template use File | Settings | File Templates.
 */
public class DreamOverviewFilter extends AbstractFilter<Integer> {

    public DreamOverviewFilter(StreamData<Integer> inputData, int compression) {
        super(inputData);
        this.compression = compression;
    }

    @Override
    protected Integer doFilter(int index) {
        if(index == 0) return 0;
        int sum = 0;

        for (int i = 0; i < compression; i++) {
            sum += Math.abs(Math.abs(inputData.get(index*compression + i) - inputData.get(index*compression + i - 1)));

        }
        return sum/compression;


    }
}
