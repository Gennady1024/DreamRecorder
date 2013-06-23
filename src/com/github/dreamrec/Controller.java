package com.github.dreamrec;

import com.crostec.ads.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class Controller {

    private Timer repaintTimer;
    private Model model;
    private MainWindow mainWindow;
    private ApplicationProperties applicationProperties;
    private static final Log log = LogFactory.getLog(Controller.class);
    public static int CURSOR_SCROLL_STEP = 1; //in points
    private boolean isAutoScroll = false;
    private boolean isRecording = false;
    private Ads ads = new Ads();
    private BdfWriter bdfWriter;
    private IncomingDataBuffer incomingDataBuffer;

    public Controller(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        model = Factory.getModel(applicationProperties);
        repaintTimer = new Timer(applicationProperties.getRepaintDelay(), new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateModel();
                mainWindow.repaint();
            }
        });
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public boolean isRecording() {
        return isRecording;
    }

    protected void updateModel() {
        while (incomingDataBuffer.available()) {
            int[] frame = incomingDataBuffer.poll();
            model.addEyeData(ch1PreFilter.getFilteredValue(frame[0]));
            model.addCh2Data(ch2PreFilter.getFilteredValue(frame[1]));
            model.addAcc1Data(acc1PreFilter.getFilteredValue(frame[2]));
            model.addAcc2Data(acc2PreFilter.getFilteredValue(frame[3]));
            model.addAcc3Data(acc3PreFilter.getFilteredValue(frame[4]));
        }
        if (isAutoScroll) {
            model.setFastGraphIndexMaximum();
        }
    }

    public void startRecording() {
        isRecording = true;
        AdsConfiguration adsConfiguration = new AdsConfigUtil().readConfiguration();
        BdfHeaderData bdfHeaderData = new BdfHeaderData(adsConfiguration);
        if (bdfWriter != null) {
            ads.removeAdsDataListener(bdfWriter);
        }
        bdfWriter = new BdfWriter(bdfHeaderData);
        ads.addAdsDataListener(bdfWriter);
        incomingDataBuffer = new IncomingDataBuffer();
        ads.addAdsDataListener(incomingDataBuffer);
        try {
            ads.startRecording(bdfHeaderData.getAdsConfiguration());
        } catch (AdsException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(0);
        }
        model.clear();
        model.setFrequency(10);
        model.setStartTime(System.currentTimeMillis());  //todo remove
        repaintTimer.start();
        isAutoScroll = true;
    }

    public void stopRecording() {
        if (!isRecording) return;
        isRecording = false;
        ads.stopRecording();
        bdfWriter.stopRecording();
        repaintTimer.stop();
        isAutoScroll = false;
    }

    public void changeXSize(int xSize) {
        model.setXSize(xSize);
        applicationProperties.setXSize(xSize);
        mainWindow.repaint();
    }

    public void scrollCursorForward() {
        model.moveFastGraph(model.getFastGraphIndex() + CURSOR_SCROLL_STEP * Model.DIVIDER);
        if (model.isFastGraphIndexMaximum()) {
            isAutoScroll = true;
        }
        mainWindow.repaint();
    }

    public void scrollCursorBackward() {
        model.moveFastGraph(model.getFastGraphIndex() - CURSOR_SCROLL_STEP * Model.DIVIDER);
        isAutoScroll = false;
        mainWindow.repaint();
    }

    public void moveCursor(int newPosition) {
        model.moveCursor(newPosition);
        mainWindow.repaint();
    }

    public void scrollSlowGraph(int scrollPosition) {
        model.moveSlowGraph(scrollPosition);
        isAutoScroll = false;
        mainWindow.repaint();
    }

    public void closeApplication() {
        throw new UnsupportedOperationException("todo");
    }
}
