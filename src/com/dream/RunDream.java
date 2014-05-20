package com.dream;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 07.05.14
 * Time: 21:36
 * To change this template use File | Settings | File Templates.
 */
public class RunDream {
    public static void main(String[] args) {
      MainView mainWindow = new MainView(12);



        Controller controller = new Controller(mainWindow);
        controller.sendData();

    }
}
