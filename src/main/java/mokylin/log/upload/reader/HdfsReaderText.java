package mokylin.log.upload.reader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2016/5/26.
 */
public class HdfsReaderText implements IHdfsReader {
    @Override
    public List<String> getLocalFileList(String path, String regex) {
        return null;
    }

    @Override
    public void createLocalFileList(String hdfsPath, String regex, String localFileList) {
        //Example Path:
        //String path = "/user/hive/warehouse/lqzg.db/recharge";
        //Example Regex:
        //String regex = ".*/plat=360/.*";

        Writer fw = null;

        Configuration conf = new Configuration();
        try {
            fw = new BufferedWriter(new FileWriter(localFileList));
            FileSystem hdfs = FileSystem.get(URI.create(hdfsPath), conf);
            RemoteIterator<LocatedFileStatus> fileItr = hdfs.listFiles(new Path(hdfsPath), true);
            while (fileItr.hasNext()) {
                Path p = fileItr.next().getPath();
                String absPath = p.toString();
                if (absPath.matches(regex)) {
                    fw.write(absPath+"\n");
                }
            }
            fw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fw!=null){
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
