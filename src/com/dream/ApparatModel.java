package com.dream;

import com.dream.Data.DataList;
import com.dream.Data.StreamData;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 10.05.14
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
public class ApparatModel {
    public static final int COMPRESSION = 120; //compression for big-scaled graphs
    public static final int PERIOD_MSEC = 100;  // milliseconds!!!!  period of the incoming data (for fast graphics)
    private DataList<Integer> chanel_1_data = new DataList<Integer>();   //list with prefiltered incoming data of eye movements
    private DataList<Integer> chanel_2_data = new DataList<Integer>();   //list with prefiltered incoming chanel2 data
    private DataList<Integer> acc_1_data = new DataList<Integer>();   //list with accelerometer 1 chanel data
    private DataList<Integer> acc_2_data = new DataList<Integer>();   //list with accelerometer 1 chanel data
    private DataList<Integer> acc_3_data = new DataList<Integer>();   //list with accelerometer 1 chanel data

    private final int ACC_X_NULL = -1088;
    private final int ACC_Y_NULL =  1630;
    private final int ACC_Z_NULL =  4500;
    private int MOVEMENT_MAX = 3;

    private long startTime; //time when data recording was started


    /**
     *  Определяем величину пропорциональную движению головы
     *  (дельта между текущим и предыдущим значением сигналов акселерометра).
     *  Суммируем амплитуды движений по трем осям.
     *  За ноль принят шумовой уровень.
     */
    private double getAccMovement(int index) {
        int NOISE_LEVEL_MIN = 500;// минимально возможный уровень шума когда человек находится в глубоком сне и не шевелится

        int dX, dY, dZ;
        if(index  > 0) {
            dX =  getNormalizedDataAcc1(index) - getNormalizedDataAcc1(index - 1);
            dY =  getNormalizedDataAcc2(index) - getNormalizedDataAcc2(index - 1);
            dZ =  getNormalizedDataAcc3(index) - getNormalizedDataAcc3(index - 1);
        }
        else {
            dX = 0;
            dY = 0;
            dZ = 0;
        }
        double dXYZ = (double) (Math.abs(dX) + Math.abs(dY) + Math.abs(dZ));
        return (dXYZ - NOISE_LEVEL_MIN)/ NOISE_LEVEL_MIN;
    }


    private boolean isSleep(int index) {
        return (getAccMovement(index) < MOVEMENT_MAX);
    }

    public int getHiPassData(int index) {
        int bufferSize = 50;
        if(! isSleep(index)){
            return Integer.MAX_VALUE;
        }

        if (index < bufferSize) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < bufferSize; i++) {
            sum+=chanel_1_data.get(index - i);
        }
        return chanel_1_data.get(index) - sum/bufferSize;
    }

    public int getAccGraphData(int index) {
                int  INT_SCALE = 50;
                int out;
                int dXYZ = (int)(getAccMovement(index)*INT_SCALE);

                if (dXYZ <= 0) out = dXYZ / 2;
                else
                if (dXYZ >= 100) out = dXYZ / 10 + 100;
                else
                    out = dXYZ;

                return out;
    }


    private int getNormalizedDataAcc1(int index) {
        return (acc_1_data.get(index) - ACC_X_NULL);
    }


    private int getNormalizedDataAcc2(int index) {
        return -(acc_2_data.get(index) - ACC_Y_NULL);
    }


    private int getNormalizedDataAcc3(int index) {
        return -(acc_1_data.get(index) + ACC_Z_NULL);
    }



    public int getDataSize() {
        return chanel_1_data.size();
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public DataList<Integer> getCh1DataList() {
        return chanel_1_data;
    }

    public DataList<Integer> getCh2DataList() {
        return chanel_2_data;
    }

    public DataList<Integer> getAcc1DataList() {
        return acc_1_data;
    }

    public DataList<Integer> getAcc2DataList() {
        return acc_2_data;
    }

    public DataList<Integer> getAcc3DataList() {
        return acc_3_data;
    }

    public void addCh1Data(int data) {
        chanel_1_data.add(data);
    }

    public void addCh2Data(int data) {
        chanel_2_data.add(data);
    }

    public void addAcc1Data(int data) {
        acc_1_data.add(data);
    }

    public void addAcc2Data(int data) {
        acc_2_data.add(data);
    }

    public void addAcc3Data(int data) {
        acc_3_data.add(data);
    }
}
