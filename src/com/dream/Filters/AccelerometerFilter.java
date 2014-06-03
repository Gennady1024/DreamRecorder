package com.dream.Filters;

import com.dream.Data.StreamData;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 03.06.14
 * Time: 14:34
 * To change this template use File | Settings | File Templates.
 */
public class AccelerometerFilter extends AbstractFilter<Integer>{
    protected final StreamData<Integer> yData;
    protected final StreamData<Integer> zData;

    private final int NOISE_LEVEL_MIN = 500;// минимально возможный уровень шума когда человек находится в глубоком сне и не шевелится
    private final int  INT_SCALE = 50;

    private final int MOVEMENT_MAX = 10; //

    private int data_X_previous = 0;
    private int data_Y_previous = 0;
    private int data_Z_previous = 0;


    public AccelerometerFilter(StreamData xData, StreamData yData, StreamData zData) {
        super(xData);
        this.yData = yData;
        this.zData = zData;
    }

    /**
     *  Определяем величину пропорциональную движению головы
     *  (дельта между текущим и предыдущим значением сигналов акселерометра).
     *  Суммируем амплитуды движений по трем осям.
     *  За ноль принят шумовой уровень.
     */
    protected Integer getAccMovement(int index) {
        int data_X =  inputData.get(index);
        int data_Z = zData.get(index);
        int data_Y = yData.get(index);
        int dX, dY, dZ;


        dX =  data_X - data_X_previous; data_X_previous = data_X; if (dX < 0)dX = -dX;
        dY =  data_Y - data_Y_previous; data_Y_previous = data_Y; if (dY < 0)dY = -dY;
        dZ =  data_Z - data_Z_previous; data_Z_previous = data_Z; if (dZ < 0)dZ = -dZ;

        return (dX + dY + dZ);
    }

    @Override
    protected Integer doFilter(int index) {
        int out;
        int accMovement = getAccMovement(index);
        int dXYZ = (INT_SCALE*(accMovement - NOISE_LEVEL_MIN) )/ NOISE_LEVEL_MIN;

        if (dXYZ <= 0) out = dXYZ / 2;
        else
        if (dXYZ >= 100) out = dXYZ / 10 + 100;
        else
            out = dXYZ;

        return out;
    }
}
