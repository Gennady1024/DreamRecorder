package com.github.dreamrec;

/**
 *
 */
public class AccelerometerPositionFilter extends AbstractFilter<Integer> {

    protected int divider = 1;
    protected final Filter<Integer> yData;
    protected final Filter<Integer> zData;
    private final int data_mod = 16000;
    private       int X_data_mod  = data_mod, Y_data_mod = data_mod, Z_data_mod = data_mod;
    private final int data_Sin_45 = data_mod *3363/4756; // sin(45) = sqrt(2)/2 ~= 3363/4756

    private final int Sin_90 = 1800/4;  // if (F(X,Y) = 4) arc_F(X,Y) = 180 Grad
    private int X_mod = Sin_90, Y_mod = Sin_90, Z_mod = 90;

    private int data_X_Max = 0, data_X_Min = 0, data_X_Nul = -1088, data_X_1 = 0;
    private int data_Y_Max = 0, data_Y_Min = 0, data_Y_Nul =  1630, data_Y_1 = 0;
    private int data_Z_Max = 0, data_Z_Min = 0, data_Z_Nul =  4500;
    private int data_X_mod = data_mod, data_Y_mod = data_mod, data_Z_mod = data_mod;

    public AccelerometerPositionFilter(Filter xData, Filter yData, Filter zData) {
        super(xData);
        this.yData = yData;
        this.zData = zData;
    }


    @Override
    protected Integer doFilter(int index) {
        int XY_angle;
        int data_Z = -(zData.get(index)    + data_Z_Nul);
        int data_X =  (inputData.get(index)- data_X_Nul);
        int data_Y = -(yData.get(index)    - data_Y_Nul);
       // data_Max_Min(X, Y, Z);

       if (data_Z > data_Sin_45){Z_mod *= -1; return Z_mod; }

       calibrXY(data_X, data_Y);

       int X = data_X * X_mod / X_data_mod;
       int Y = data_Y * Y_mod / Y_data_mod;

       XY_angle = angle(X, Y);

        return
                XY_angle
         ;
    }

    protected int angle(int X, int Y) {
        // double X_Sin, Y_Sin, Z_Sin;
        int XY_angle = 0;
        // XY_angle =  1 + sin(x) - cos(x); if (X >= 0 && Y >=0)
        // XY_angle =  3 - sin(x) - cos(x); if (X >= 0 && Y < 0)
        // XY_angle = -1 + sin(x) + cos(x); if (X <  0 && Y >=0)
        // XY_angle = -3 - sin(x) + cos(x); if (X <  0 && Y < 0)

             if (X >= 0 && Y >=0) { XY_angle =     Sin_90 + X - Y; }
        else if (X >= 0 && Y < 0) { XY_angle =  3* Sin_90 - X - Y; }

        else if (X <  0 && Y >=0) { XY_angle =    -Sin_90 + X + Y; }
        else if (X <  0 && Y < 0) { XY_angle = -3* Sin_90 - X + Y; }

        return XY_angle/10;
    }

    protected boolean zeroTrigger(int data_0, int data_1) {
       if (data_1 >= 0 && data_0 <= 0 || data_1 <= 0 && data_0 >= 0)
            return true;
       else return  false;
    }

    protected void calibrXY(int data_X, int data_Y) {

        boolean zero_X;
        zero_X = zeroTrigger(data_X_1, data_X );
        data_X_1 = data_X;
        boolean zero_Y;
        zero_Y = zeroTrigger(data_Y_1, data_Y );
        data_Y_1 = data_Y;
        if(zero_X) Y_data_mod = data_Y;
        if (Y_data_mod < 0)Y_data_mod = -Y_data_mod;
        if(zero_Y) X_data_mod = data_X;
        if (X_data_mod < 0)X_data_mod = -X_data_mod;
    }
    
    protected void data_Max_Min(int X, int Y, int Z) {

        if (data_X_Max < X) data_X_Max = X;  if (data_X_Min > X) data_X_Min = X;
        if (data_Y_Max < Y) data_Y_Max = Y;  if (data_Y_Min > Y) data_Y_Min = Y;
        if (data_Z_Max < Z) data_Z_Max = Z;  if (data_Z_Min > Z) data_Z_Min = Z;
    }
}
