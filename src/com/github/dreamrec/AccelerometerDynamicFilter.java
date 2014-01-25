package com.github.dreamrec;

/**
 *
 */
public class AccelerometerDynamicFilter extends AbstractFilter<Integer>  {

    protected final Filter<Integer> yData;
    protected final Filter<Integer> zData;

    private final int data_mod = 16000;
    private final int noise_level = 500;
    private       int Z_data_mod = data_mod;
    private final int data_Sin_45 = data_mod *3363/4756; // sin(45) = sqrt(2)/2 ~= 3363/4756

    private int data_X_Nul = -1088, data_X_1 = 0;
    private int data_Y_Nul =  1630, data_Y_1 = 0;
    private int data_Z_Nul =  4500, data_Z_1 = 0;

    public AccelerometerDynamicFilter(Filter xData, Filter yData, Filter zData) {
        super(xData);
        this.yData = yData;
        this.zData = zData;
    }

    @Override
    protected Integer doFilter(int index) {

        int data_Z = -(zData.get(index)    + data_Z_Nul);
        int data_X =  (inputData.get(index)- data_X_Nul);
        int data_Y = -(yData.get(index)    - data_Y_Nul);
        int dX, dY, dZ, dXYZ;
        double ddX, ddY, ddZ, ddXYZ, ddOUT;

        if (data_Z > data_Sin_45){ return 0; }

        dX = data_X_1 - data_X; data_X_1 = data_X; if (dX < 0)dX = -dX;
        dY = data_Y_1 - data_Y; data_Y_1 = data_Y; if (dY < 0)dY = -dY;
        dZ = data_Z_1 - data_Z; data_Z_1 = data_Z; if (dZ < 0)dZ = -dZ;

        dXYZ = (dX + dY + dZ) - noise_level;

        ddXYZ = ((double)dXYZ)/noise_level;

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
