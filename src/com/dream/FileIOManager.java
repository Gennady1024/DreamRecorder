package com.dream;

import com.github.dreamrec.ApplicationException;
import com.github.dreamrec.Model;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 24.05.14
 * Time: 23:43
 * To change this template use File | Settings | File Templates.
 */
public class FileIOManager {
    private static final Log log = LogFactory.getLog(FileIOManager.class);

    public void saveToFile(File file, ApparatModel model) throws ApplicationException {
        DataOutputStream outStream = null;
//        try {
        try {
            outStream = new DataOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            log.error(e);
            throw new ApplicationException("Error while saving file " + file.getName());
        }
        try {
            saveStateToStream(outStream, model);
        } catch (IOException e) {
            log.error(e);
            throw new ApplicationException("Error while saving file " + file.getName());
        } finally {
            try {
                outStream.close();
            } catch (IOException e) {
                log.error(e);
            }
        }
    }

    private void saveStateToStream(DataOutputStream outStream, ApparatModel model) throws IOException {
        outStream.writeLong(model.getStartTime());
        outStream.writeDouble(model.FREQUENCY);
        for (int i = 0; i < model.getDataSize(); i++) {
            outStream.writeInt(model.getCh1DataList().get(i));
            outStream.writeInt(model.getCh2DataList().get(i));
            outStream.writeInt(model.getAcc1DataList().get(i));
            outStream.writeInt(model.getAcc2DataList().get(i));
            outStream.writeInt(model.getAcc3DataList().get(i));
        }
    }


    public void readFromFile(File file, ApparatModel model) throws ApplicationException {
        DataInputStream dataInputStream = null;
        try {
            dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            loadStateFromInStream(dataInputStream, model);
        } catch (Exception e) {
            log.error(e);
            throw new ApplicationException("Error while reading from file " + file.getName());
        } finally {
            try {
                dataInputStream.close();
            } catch (IOException e) {
                log.error(e);
            }
        }
    }

    private void loadStateFromInStream(DataInputStream inputStream, ApparatModel model) throws IOException {
        try {
            long startTime = inputStream.readLong();
            double frequency = inputStream.readDouble();
            model.setStartTime(startTime);
            while (true) {
                model.addCh1Data(inputStream.readInt());
                model.addCh2Data(inputStream.readInt());
                model.addAcc1Data(inputStream.readInt());
                model.addAcc2Data(inputStream.readInt());
                model.addAcc3Data(inputStream.readInt());
            }
        } catch (EOFException e) {
            log.info("End of file");
        }
    }

}
