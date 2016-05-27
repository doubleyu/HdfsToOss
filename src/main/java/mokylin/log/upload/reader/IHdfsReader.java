package mokylin.log.upload.reader;

import java.util.List;

/**
 * Created by Admin on 2016/5/26.
 */
public interface IHdfsReader {
    public abstract List<String> getLocalFileList(String hdfsPath, String regex);

    public abstract void createLocalFileList(String hdfsPath, String regex, String localFileList);
}
