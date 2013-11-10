package com.github.dreamrec;

/**
 *
 */
public class AccelerometerPositionFilter extends AbstractFilter<Integer> {

    protected int divider = 1;
    protected final Filter<Integer> yData;
    protected final Filter<Integer> zData;

    public AccelerometerPositionFilter(Filter xData, Filter yData, Filter zData) {
        super(xData);
        this.yData = yData;
        this.zData = zData;
    }

    @Override
    protected Integer doFilter(int index) {
        return inputData.get(index) + yData.get(index) + zData.get(index);
    }
}
