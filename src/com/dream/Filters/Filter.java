package com.dream.Filters;

import com.dream.Data.DataStream;

/**
 *
 */
public abstract class Filter<T> implements DataStream {
    protected final DataStream<T> inputData;

    public Filter(DataStream<T> inputData) {
        this.inputData = inputData;
    }

    protected abstract T getData(int index);


    public int size() {
        return inputData.size();
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
