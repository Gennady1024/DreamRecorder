package com.dream.Filters;

import com.dream.Data.DataStream;

/**
 *
 */
public abstract class Filter<T> implements DataStream {
    protected final DataStream<T> inputData;
    protected int compression = 1;

    public Filter(DataStream<T> inputData, int compression) {
        this.inputData = inputData;
        this.compression = compression;
    }

    public Filter(DataStream<T> inputData) {
        this(inputData, 1);
    }

    protected abstract T getData(int index);


    public final int size() {
        return inputData.size()/compression;
    }


    public final T get(int index) {
        checkIndexBounds(index);
        return getData(index);
    }

    private void checkIndexBounds(int index){
        if(index > size() || index < 0 ){
            throw  new IndexOutOfBoundsException("index:  "+index+", available:  "+size());
        }
    }

}
