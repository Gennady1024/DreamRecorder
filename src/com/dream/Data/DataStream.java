package com.dream.Data;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 16.05.14
 * Time: 3:24
 * To change this template use File | Settings | File Templates.
 */

public interface DataStream<T> {
    public int size();
    public T get(int index);
}
