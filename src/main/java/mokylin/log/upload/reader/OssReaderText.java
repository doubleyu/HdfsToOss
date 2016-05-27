package mokylin.log.upload.reader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by Admin on 2016/5/26.
 */
public class OssReaderText implements IOssReader{


    @Override
    public boolean isFileSuccess(String file) {
        return false;
    }

    @Override
    public synchronized void markFileSuccess(String ossFileList, String file) {
        Writer output;
        try {
            output = new BufferedWriter(new FileWriter(ossFileList, true));
            output.append(file + "\n");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
