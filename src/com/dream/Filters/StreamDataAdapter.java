package com.dream.Filters;

import com.dream.ApparatModel;
import com.dream.Data.StreamData;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 03.06.14
 * Time: 23:23
 * To change this template use File | Settings | File Templates.
 */
public abstract class StreamDataAdapter<Integer> implements StreamData<Integer> {
    private  ApparatModel model;

    public  StreamDataAdapter(ApparatModel model) {
        this.model = model;
    }

    public ApparatModel getModel() {
        return model;
    }

    @Override
    public int size() {
        return model.getDataSize();
    }
}