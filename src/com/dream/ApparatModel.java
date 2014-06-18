package com.dream;

import com.dream.Data.DataList;
import com.dream.Data.DataStream;
import com.dream.Filters.FilterHiPass;
import com.dream.Filters.FilterLowPass;

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
    private DataList<Integer> acc_2_data = new DataList<Integer>();   //list with accelerometer 2 chanel data
    private DataList<Integer> acc_3_data = new DataList<Integer>();   //list with accelerometer 3 chanel data
    private DataList<Integer> sleep_data = new DataList<Integer>();   // 0 - sleep, 1 - not sleep

    private long startTime; //time when data recording was started

    private int movementLimit = 2000;
    private final double MOVEMENT_LIMIT_CHANGE = 1.05;
    private final int FALLING_ASLEEP_TIME = 60; // seconds
    private int sleepTimer = 0;

    private final int SIN_90 = 1800 / 4;  // if (F(X,Y) = 4) arc_F(X,Y) = 180 Grad
    private final int ACC_X_NULL = -1088;
    private final int ACC_Y_NULL = 1630;
    private final int ACC_Z_NULL = 4500;
    int Z_mod = 90;

    public void movementLimitUp() {
        movementLimit *= MOVEMENT_LIMIT_CHANGE;
        sleepTimer = 0;
        for (int i = 0; i < getDataSize(); i++) {
            setSleepData(i);
        }
    }

    public void movementLimitDown() {
        movementLimit /= MOVEMENT_LIMIT_CHANGE;
        sleepTimer = 0;
        for (int i = 0; i < getDataSize(); i++) {
            setSleepData(i);
        }
    }
    private void setSleepData(int index) {
        if (isMoved(index)) {
            sleepTimer = FALLING_ASLEEP_TIME * 1000 / PERIOD_MSEC;
        }

        int isSleep = 0;

        if ((sleepTimer > 0)) {
            isSleep = 1;
            sleepTimer--;
        }

        if (index < sleep_data.size()) {
            sleep_data.set(index, isSleep);
        } else {
            sleep_data.add(isSleep);
        }
    }


    /**
     * Определяем величину пропорциональную движению головы
     * (дельта между текущим и предыдущим значением сигналов акселерометра).
     * Суммируем амплитуды движений по трем осям.
     * За ноль принят шумовой уровень.
     */

    private int getAccMovement(int index) {
        int step = 2;
        int dX, dY, dZ;
        int maxX = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxZ = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE;
        if (index > step) {
            for (int i = 0; i <= step; i++) {
                maxX = Math.max(maxX, getNormalizedDataAcc1(index - i));
                minX = Math.min(minX, getNormalizedDataAcc1(index - i));
                maxY = Math.max(maxY, getNormalizedDataAcc2(index - i));
                minY = Math.min(minY, getNormalizedDataAcc2(index - i));
                maxZ = Math.max(maxZ, getNormalizedDataAcc3(index - i));
                minZ = Math.min(minZ, getNormalizedDataAcc3(index - i));
            }
            dX = maxX - minX;
            dY = maxY - minY;
            dZ = maxZ - minZ;
        } else {
            dX = 0;
            dY = 0;
            dZ = 0;
        }

        int dXYZ = Math.abs(dX) + Math.abs(dY) + Math.abs(dZ);
        return dXYZ;
    }

    public DataStream<Integer> getAccMovementStream() {
        return new DataStreamAdapter<Integer>() {
            @Override
            protected Integer getData(int index) {
                return getAccMovement(index);
            }
        };
    }


    public DataStream<Integer> getAccMovementLimitStream() {
        return new DataStreamAdapter<Integer>() {
            @Override
            protected Integer getData(int index) {
                return movementLimit;
            }
        };
    }


    public DataStream<Integer> getAccPositionStream() {
        return new DataStreamAdapter<Integer>() {
            @Override
            protected Integer getData(int index) {
                return getAccPosition(index);
            }
        };
    }

    public DataStream<Integer> getNotSleepEventsStream() {
        return new DataStreamAdapter<Integer>() {
            @Override
            protected Integer getData(int index) {
                return sleep_data.get(index);
            }
        };
    }


    private int getAccPosition(int index) {
        final int DATA_SIN_90 = 16000;

        final int DATA_SIN_45 = DATA_SIN_90 * 3363 / 4756; // sin(45) = sqrt(2)/2 ~= 3363/4756

        final int X_data_mod = DATA_SIN_90, Y_data_mod = DATA_SIN_90, Z_data_mod = DATA_SIN_90;
        final int X_mod = SIN_90, Y_mod = SIN_90;


        int XY_angle;
        int data_X = getNormalizedDataAcc1(index);
        int data_Y = getNormalizedDataAcc2(index);
        int data_Z = getNormalizedDataAcc3(index);

        if (data_Z > DATA_SIN_45) {   // Если человек не лежит
            Z_mod *= -1;
            return Z_mod;
        }

        double Z = (double) data_Z / Z_data_mod;

        double ZZ = Z * Z;
        double sec_Z = 1 + ZZ * 0.43 + ZZ * ZZ * 0.77;

        double double_X = ((double) data_X / X_data_mod) * sec_Z;
        double double_Y = ((double) data_Y / Y_data_mod) * sec_Z;
        int X = (int) (double_X * X_mod);
        int Y = (int) (double_Y * Y_mod);

        XY_angle = angle(X, Y);

        return XY_angle;
    }

    private int angle(int X, int Y) {
        int XY_angle = 0;
        // XY_angle =  1 + sin(x) - cos(x); if (X >= 0 && Y >=0)
        // XY_angle =  3 - sin(x) - cos(x); if (X >= 0 && Y < 0)
        // XY_angle = -1 + sin(x) + cos(x); if (X <  0 && Y >=0)
        // XY_angle = -3 - sin(x) + cos(x); if (X <  0 && Y < 0)

        if (X >= 0 && Y >= 0) {
            XY_angle = SIN_90 + X - Y;
        } else if (X >= 0 && Y < 0) {
            XY_angle = 3 * SIN_90 - X - Y;
        } else if (X < 0 && Y >= 0) {
            XY_angle = -SIN_90 + X + Y;
        } else if (X < 0 && Y < 0) {
            XY_angle = -3 * SIN_90 - X + Y;
        }

        return XY_angle / 10;
    }


    private boolean isMoved(int index) {
        if (getAccMovement(index) > movementLimit) {
            return true;
        }
        return false;
    }


    private int getNormalizedDataAcc1(int index) {
        return (acc_1_data.get(index) - ACC_X_NULL);
    }


    private int getNormalizedDataAcc2(int index) {
        return -(acc_2_data.get(index) - ACC_Y_NULL);
    }


    private int getNormalizedDataAcc3(int index) {
        return -(acc_3_data.get(index) + ACC_Z_NULL);
    }


    public int getDataSize() {
        int size = chanel_1_data.size();
        size = Math.min(size, chanel_2_data.size());
        size = Math.min(size, acc_1_data.size());
        size = Math.min(size, acc_2_data.size());
        size = Math.min(size, acc_3_data.size());

        return size;
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

    private void addData(int data, DataList<Integer> dataStore) {
        int size = getDataSize();
        dataStore.add(data);
        if (getDataSize() > size) {
            setSleepData(getDataSize() - 1);     // add SleepData
        }
    }

    public void addCh1Data(int data) {
        addData(data, chanel_1_data);
    }

    public void addCh2Data(int data) {
        addData(data, chanel_2_data);
    }

    public void addAcc1Data(int data) {
        addData(data, acc_1_data);
    }


    public void addAcc2Data(int data) {
        addData(data, acc_2_data);

    }

    public void addAcc3Data(int data) {
        addData(data, acc_3_data);
    }


    abstract class DataStreamAdapter<Integer> implements DataStream<Integer> {
        protected abstract Integer getData(int index);

        public final Integer get(int index) {
            checkIndexBounds(index);
            return getData(index);
        }


        private void checkIndexBounds(int index){
            if(index > size() || index < 0 ){
                throw  new IndexOutOfBoundsException("index:  "+index+", available:  "+size());
            }
        }

        @Override
        public int size() {
            return getDataSize();
        }
    }
}
