package mokylin.log.upload;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;


/**
 * Created by Admin on 2016/5/25.
 */
@Deprecated
public class ReadHDFS {
    public static void main(String[] args) {
        try {
            String dsf = "hdfs://mldc-hadoop/user/hive/warehouse/lqzg.db/recharge/plat=360/date=2016-05-01/lqzg_360_Recharge_2016-05-01.txt";
            Configuration conf = new Configuration();

            FileSystem fs = FileSystem.get(URI.create(dsf),conf);
            FSDataInputStream hdfsInStream = fs.open(new Path(dsf));

            byte[] ioBuffer = new byte[1024];
            int readLen = hdfsInStream.read(ioBuffer);
            while(readLen!=-1)
            {
                System.out.write(ioBuffer, 0, readLen);
                readLen = hdfsInStream.read(ioBuffer);
            }
            hdfsInStream.close();
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
