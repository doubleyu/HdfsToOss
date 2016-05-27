package mokylin.log.upload.reader;

import java.util.List;

/**
 * Created by Admin on 2016/5/26.
 */
public interface IDiffReader {
    public abstract void createDiffFileList(String localFileList, String ossFileList, String diffFileList);

    public abstract List<String> getDiffFileListPart(String diffFileList, int lineNum);
}
