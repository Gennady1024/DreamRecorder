package com.github.dreamrec.gcomponent;

import com.github.dreamrec.filters.Filter;

/**
 *
 */
public interface IGraphPainterModel {

    public Filter<Integer>[] getDataView();

    public int getStartIndex();

    public double getYZoom();

    public int getXSize();

}
