package com.github.dreamrec;

/**
 *
 */
public class AccelerometerDynamicFilter extends AbstractFilter<Integer>  {

    protected final Filter<Integer> yData;
    protected final Filter<Integer> zData;

    public AccelerometerDynamicFilter(Filter xData, Filter yData, Filter zData) {
        super(xData);
        this.yData = yData;
        this.zData = zData;
    }

    @Override
    protected Integer doFilter(int index) {
        return inputData.get(index) + yData.get(index) + zData.get(index);
    }
}
