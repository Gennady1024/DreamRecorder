package com.github.dreamrec.filters;

/**
 *
 */
public class AccelerometerDynamicFilter extends AbstractFilter<Integer> {

    protected final Filter<Integer> yData;
    protected final Filter<Integer> zData;

    private final int DATA_SIN_90 = 16000;
    private final int NOISE_LEVEL = 500;
    private final int DATA_SIN_45 = DATA_SIN_90 *3363/4756; // sin(45) = sqrt(2)/2 ~= 3363/4756

    private int data_X_previous = 0;
    private int data_Y_previous = 0;
    private int data_Z_previous = 0;

    public AccelerometerDynamicFilter(Filter xData, Filter yData, Filter zData) {
        super(xData);
        this.yData = yData;
        this.zData = zData;
    }

    @Override
    protected Integer doFilter(int index) {

        int data_Z = zData.get(index);
        int data_X =  inputData.get(index);
        int data_Y = yData.get(index);
        int dX, dY, dZ, dXYZ;
        double ddX, ddY, ddZ, ddXYZ, ddOUT;



        if (data_Z > DATA_SIN_45){ return 0; }

        dX =  data_X - data_X_previous; data_X_previous = data_X; if (dX < 0)dX = -dX;
        dY =  data_Y - data_Y_previous; data_Y_previous = data_Y; if (dY < 0)dY = -dY;
        dZ =  data_Z - data_Z_previous; data_Z_previous = data_Z; if (dZ < 0)dZ = -dZ;

        dXYZ = (dX + dY + dZ) - NOISE_LEVEL;

        ddXYZ = ((double)dXYZ)/ NOISE_LEVEL;

        if (ddXYZ <= 0)ddOUT = ddXYZ / 2.0;
        else
        if (ddXYZ >= 2)ddOUT = ddXYZ / 10.0 + 2.0;
        else
            ddOUT = ddXYZ;

        ddOUT *= 50;

        // return dXYZ;

        return (int)ddOUT;
    }


}
