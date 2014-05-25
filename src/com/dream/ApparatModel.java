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
    public static final int FREQUENCY = 10; //frequency Hz of the incoming data (for fast graphics)
    public static final int DIVIDER = 120; //frequency divider for big-scaled graphs
    public final int PERIOD = 100;  // milliseconds!!!!  period of the incoming data (for fast graphics)

    private DataList<Integer> chanel_1_data = new DataList<Integer>();   //list with prefiltered incoming data of eye movements
    private DataList<Integer> chanel_2_data = new DataList<Integer>();   //list with prefiltered incoming chanel2 data
    private DataList<Integer> acc_1_data = new DataList<Integer>();   //list with accelerometer 1 chanel data
    private DataList<Integer> acc_2_data = new DataList<Integer>();   //list with accelerometer 1 chanel data
    private DataList<Integer> acc_3_data = new DataList<Integer>();   //list with accelerometer 1 chanel data

    private long startTime; //time when data recording was started


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
