package com.dream;

import com.dream.Data.DataList;


/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 10.05.14
 * Time: 12:40
 * To change this template use File | Settings | File Templates.
 */
public class Controller {
    private MainView mainView;

    public Controller(MainView mainView) {
        this.mainView = mainView;
    }

    public void sendData() {
        DataList<Integer> data1 = new DataList<Integer>();
        DataList<Integer> data2 = new DataList<Integer>();
        DataList<Integer> data3 = new DataList<Integer>();
        mainView.addGraph(0, data1);
        mainView.addGraph(0, data2);
        mainView.addGraph(1, data2);

        mainView.addCompressedGraph(0, data3);
        mainView.addCompressedGraph(1, data3);

        int count = 0;
        for(int x = 0; x < 2000; x++) {


            data1.add(x%33);
            data2.add((int)(Math.sin(x/20.0)*100));

            if (count%120 ==0) {
                data3.add(50);
            }
            count++;
            mainView.syncView();

        }

        for(int x = 2000; x < 200000; x++) {
            try {
                Thread.sleep(1000);

            } catch (InterruptedException ex) {

            }

            data1.add(x%33);
            data2.add((int)(Math.sin(x/20.0)*100));

            if (count%120 ==0) {
                data3.add(50);
            }
            count++;
            mainView.syncView();

        }

        for(int x = 0; x < 10; x++) {
            data1.add(20);
            data2.add(20);
            data3.add(20);
            mainView.syncView();

        }

    }

}
