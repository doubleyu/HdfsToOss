package mokylin.log.upload.reader;

/**
 * Created by Admin on 2016/5/26.
 */
public interface IOssReader {
    public abstract boolean isFileSuccess(String file);

    public abstract void markFileSuccess(String ossFileList, String file);
}
