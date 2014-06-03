package com.dream.Filters;

import com.dream.Data.StreamData;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 25.05.14
 * Time: 0:24
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractFilter<T> implements StreamData {
    protected final StreamData<T> inputData;
    protected int compression = 1;

    public AbstractFilter(StreamData<T> inputData) {
        this.inputData = inputData;
    }


    public final int size() {
        return inputData.size()/compression;
    }


    public final T get(int index) {
        checkIndexBounds(index);
        return doFilter(index);
    }

    private void checkIndexBounds(int index){
        if(index > size() || index < 0 ){
            throw  new IndexOutOfBoundsException("index:  "+index+",available:  "+size());
        }
    }

    protected abstract T doFilter(int index);
}
