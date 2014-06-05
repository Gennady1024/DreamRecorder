package com.dream.Filters;

import com.dream.ApparatModel;
import com.dream.Data.StreamData;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 05.06.14
 * Time: 13:44
 * To change this template use File | Settings | File Templates.
 */

public abstract class CompressedStreamDataAdapter<Integer> implements StreamData<Integer> {
    private ApparatModel model;

    public  CompressedStreamDataAdapter(ApparatModel model) {
        this.model = model;
    }

    public ApparatModel getModel() {
        return model;
    }

    @Override
    public int size() {
        return model.getCompressedDataSize();
    }
}