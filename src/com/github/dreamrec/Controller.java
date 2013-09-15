package com.github.dreamrec;

import com.crostec.ads.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private FrequencyDividingPreFilter channel1FrequencyDividingPreFilter;
    private HiPassPreFilter channel1HiPassPreFilter = new HiPassPreFilter(10, 0.05);
//    private HiPassPreFilter mioHiPassPreFilter = new HiPassPreFilter(250, 10);
//    private FrequencyDividingPreFilter mioFrequencyDividingPreFilter;

    public Controller(final Model model, ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        this.model = model;
        repaintTimer = new Timer(applicationProperties.getRepaintDelay(), new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateModel();
                mainWindow.repaint();
            }
        });
        channel1FrequencyDividingPreFilter = new FrequencyDividingPreFilter(25) {
            @Override
            public void notifyListeners(int value) {
                model.addEyeData(channel1HiPassPreFilter.getFilteredValue(value));
            }
        };
        /*mioFrequencyDividingPreFilter = new FrequencyDividingPreFilter(25){
            @Override
            public void notifyListeners(int value) {
                model.addCh2Data(value);
            }
        };*/
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
            for (int i = 0; i < 50; i++) {
               channel1FrequencyDividingPreFilter.add(frame[i]);
            }
            /*for (int i = 0; i < 50; i++) {
               mioFrequencyDividingPreFilter.add(Math.abs(mioHiPassPreFilter.getFilteredValue(50 + frame[i])));
            }*/
            for (int i = 0; i < 2; i++) {
               model.addAcc1Data(frame[50 + i]);
            }
            for (int i = 0; i < 2; i++) {
                model.addAcc2Data(frame[52 + i]);
            }
            for (int i = 0; i < 2; i++) {
                model.addAcc3Data(frame[54 + i]);
            }
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
        bdfHeaderData.setFileNameToSave(new SimpleDateFormat("dd-MM-yyyy_HH-mm").format(new Date(System.currentTimeMillis())) + ".bdf");
        bdfWriter = new BdfWriter(bdfHeaderData);
        ads.addAdsDataListener(bdfWriter);
        model.clear();
        model.setFrequency(10);
        model.setStartTime(System.currentTimeMillis());  //todo remove
        repaintTimer.start();
        incomingDataBuffer = new IncomingDataBuffer();
        ads.addAdsDataListener(incomingDataBuffer);
        try {
            ads.startRecording(bdfHeaderData.getAdsConfiguration());
        } catch (AdsException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(0);
        }

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
        stopRecording();
        System.exit(0);
    }

    public void readBdfFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(mainWindow);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            log.info("Opening: " + file.getName() + ".");
            BdfReader bdfReader = new BdfReader();
            incomingDataBuffer = new IncomingDataBuffer();
            model.clear();
            bdfReader.addDataListener(incomingDataBuffer);
            bdfReader.read(file);
            updateModel();
            mainWindow.repaint();
        } else {
            log.info("Open command cancelled by user.");
        }
    }
}
