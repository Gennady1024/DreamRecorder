package com.dream;

import com.dream.Data.DataList;
import com.dream.Data.DataStream;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 10.05.14
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
public class ApparatModel {


    public static final int COMPRESSION_120 = 120; //compression for big-scaled graphs
    public static final int COMPRESSION_10 = 10; //compression for sleep patterns
    public static final int PERIOD_MSEC = 100;  // milliseconds!!!!  period of the incoming data (for fast graphics)
    private DataList<Integer> chanel_1_data = new DataList<Integer>();   //list with prefiltered incoming data of eye movements
    private DataList<Integer> chanel_2_data = new DataList<Integer>();   //list with prefiltered incoming chanel2 data
    private DataList<Integer> acc_1_data = new DataList<Integer>();   //list with accelerometer 1 chanel data
    private DataList<Integer> acc_2_data = new DataList<Integer>();   //list with accelerometer 2 chanel data
    private DataList<Integer> acc_3_data = new DataList<Integer>();   //list with accelerometer 3 chanel data
    private DataList<Integer> sleep_data = new DataList<Integer>();   // 0 - sleep, 1 - not sleep
    
    private DataList<Integer>  peaks = new DataList<Integer>();

    private DataList<Integer> sleep_patterns = new DataList<Integer>();

    public static final int UNKNOWN = Integer.MAX_VALUE - 400;
    public static final int REM = Integer.MAX_VALUE - 300;
    public static final int SLOW = Integer.MAX_VALUE - 200;
    public static final int STAND = Integer.MAX_VALUE;
    public static final int MOVE = Integer.MAX_VALUE - 100;
    public static final int GAP = 99;
    
    private int derivativeMax = 0;
    private int indexDerivativeMax=0;


    private long startTime; //time when data recording was started

    private int movementLimit = 2000;
    private final double MOVEMENT_LIMIT_CHANGE = 1.05;

    public static int remLimit = 1000;
    public static final int REM_LIMIT_MIN = 700;
    boolean isUp1 = false;
    
    private boolean isRem = false;
    private int firstRemPeak = 0;
    private int lastRemPeak = 0;

    boolean isUp = false;
    int peaksCounter =0;
    int timer = 0;

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

    public void remLimitUp() {
        remLimit *= MOVEMENT_LIMIT_CHANGE;
    }

    public void remLimitDown() {
        remLimit /= MOVEMENT_LIMIT_CHANGE;
    }

    private boolean isStand(int index){
        if (getAccPosition(index) == STAND) {
            return true;
        }
        return false;
    }

    private boolean isSleep(int index) {
        if(index < 18000) {   //выкидиваем первые полчаса от начала записи
            return false;
        }
        if (isStand(index)) {
            sleepTimer = (FALLING_ASLEEP_TIME * 1000 *5) / PERIOD_MSEC;
        }
        if (isMoved(index)) {
            sleepTimer = Math.max(sleepTimer, (FALLING_ASLEEP_TIME  * 1000) / PERIOD_MSEC);
        }

        boolean isSleep = true;

        if ((sleepTimer > 0)) {
            isSleep = false;
            sleepTimer--;
        }

        return isSleep;
    }

    private void setSleepData(int index) {
        int sleep = 1;
        if(isSleep(index)) {
            sleep = 0;
        }

        if (index < sleep_data.size()) {
            sleep_data.set(index, sleep);
        } else {
            sleep_data.add(sleep);
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

    public DataStream<Integer> getSleepPatternsStream() {
        return new DataStreamAdapter<Integer>() {
            @Override
            protected Integer getData(int index) {
                return sleep_patterns.get(index);
            }
        };
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
            return STAND;
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

    private int getDerivativeAbs(int index) {
        if (index == 0) {
            return 0;
        }
        return Math.abs(chanel_1_data.get(index) - chanel_1_data.get(index - 1));
    }

    private int getDerivativeEvg(int index, int ticks) {
       if(index < ticks) {
           return 0;
       }
       int sum = 0;
       int number = 1;
       for (int i = 0; i < ticks; i++) {
           if(getDerivativeAbs(index - i) < remLimit/2) {
               sum = sum + getDerivativeAbs(index - i);
               number++;
           }
       }
        return sum/number;
    }


    private int getDerivativeMax(int index, int ticks) {
        if(index < ticks) {
            return 0;
        }
        int max = 0;
        for (int i = 0; i < ticks; i++) {
            if(getDerivativeAbs(index - i) < remLimit/3)
                max = Math.max(max, getDerivativeAbs(index - i));
        }
        return max;
    }


    private boolean isRemBegin(int index){
        int time_3_peaks = 90; // 90 ticks or 9 sec
        if(!isSleep(index)) {
            peaksCounter = 0;
            return false;
        }

        if(getDerivativeAbs(index) > remLimit) {
            if(!isUp) {
                peaksCounter++;
                isUp = true;
                lastRemPeak = index;
                if(peaksCounter == 1) {
                    firstRemPeak = index;
                }
                if((index - firstRemPeak) > time_3_peaks) {
                    firstRemPeak = index;
                    peaksCounter = 1;
                }
                if(peaksCounter > 2) {
                    for (int i = 0; i < (index-firstRemPeak); i++) {
                        sleep_patterns.set(firstRemPeak+i, REM);
                    }
                    peaksCounter = 0;
                    return true;
                }
            }
        }
        if(getDerivativeAbs(index) < remLimit/3) {
            if(isUp){
                isUp = false;
            }
        }
        return false;
    }

    private boolean isRemEnd(int index) {
        int time_repose = 40; // 40 ticks or 4 sec

        if(!isSleep(index)) {
            return true;
        }

        if (getDerivativeAbs(index) > remLimit/3) {
            lastRemPeak = index;
        }

        if((index-lastRemPeak) > time_repose) {
            for (int i = 0; i < (index-lastRemPeak); i++) {
                sleep_patterns.set(lastRemPeak+i, UNKNOWN);
            }
            return true;
        }
        return false;
    }


    private boolean isRem(int index){
        if(isRem) {
            isRem = !isRemEnd(index);
            long time = startTime + (long)((indexDerivativeMax) * PERIOD_MSEC);
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String timeStamp = dateFormat.format(new Date(time)) ;
           // System.out.println("Max/2: " + derivativeMax / 2);
           // System.out.println("index: "+indexDerivativeMax);
           // System.out.println("time: "+timeStamp);
            //System.out.println("peaks number: "+peaks.size());

        }
        else {
            isRem = isRemBegin(index);
        }
        return isRem;
    }

    
    private void calculatePeak(int index) {
        if(isSleep(index)) {
            int derivative = getDerivativeAbs(index);
            if ( derivative > REM_LIMIT_MIN) {
                if(!isUp1) {
                    peaks.add( derivative);
                    isUp1 = true;
                }
            }
            if(derivative < REM_LIMIT_MIN/2) {
                if(isUp1){
                    isUp1 = false;
                }
            }
        }


      /*  if(isSleep(index)) {
            int derivative = getDerivativeAbs(index);
            if ( derivative > REM_LIMIT_MIN) {
                peaks.add(derivative);
            }
        } */
    }
    
    public int calculateRemMax() {
        Integer peaks_arr[] =  new Integer[peaks.size()];
        peaks.toArray(peaks_arr);
        Arrays.sort(peaks_arr);
        for(int i=0; i<peaks_arr.length; i++) {
            System.out.println(i+" peak: "+peaks_arr[i]);
        }
        int indexMax = (int) (0.9 * peaks_arr.length);
        int rem = peaks_arr[indexMax]/2;
        System.out.println("peaks number: " + peaks.size());
        System.out.println("RemLevel: " + rem);
        return rem;
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
        int sizeNew = getDataSize();
        if (sizeNew > size) {
            setSleepData(sizeNew - 1);     // add SleepData
           if(getAccPosition(sizeNew-1) == STAND) {  // person is standing
                 sleep_patterns.add(STAND);
            }
            else if (isMoved(sizeNew-1)) { // person is moving
                sleep_patterns.add(MOVE);
            }
           else if (isRem(sizeNew-1)) { // person in REM
               sleep_patterns.add(REM);
           }
            else {
                sleep_patterns.add(UNKNOWN);
            }

            calculatePeak(sizeNew-1);
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
