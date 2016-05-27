package mokylin.log.upload;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import mokylin.log.upload.reader.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Admin on 2016/5/26.
 */
public class FromHdfsToOss {
    private static final Logger logger = LoggerFactory
            .getLogger(FromHdfsToOss.class);
    private static final int threadCount = Runtime.getRuntime().availableProcessors();

    private static final ExecutorService exec = new ThreadPoolExecutor(
            threadCount, threadCount, 60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(threadCount * 3),
            new RejectedExecutionHandler() {

                @Override
                public void rejectedExecution(Runnable r,
                                              ThreadPoolExecutor executor) {
                    try {
                        // block until there's room
                        executor.getQueue().put(r);
                    } catch (InterruptedException e) {
                        throw new RejectedExecutionException(
                                "Unexpected InterruptedException", e);
                    }
                }
            });

    public static void main(String args[]) {
        String hdfsPath = "hdfs://mldc-hadoop/user/hive/warehouse/lqzg.db/recharge";
        String regex = ".*/plat=360/.*";
        String localFileList = "/data/hadoop/dev/mokylin_log/file_list/gjqt/localFileList.txt";
        String ossFileList = "/data/hadoop/dev/mokylin_log/file_list/gjqt/ossFileList.txt";
        String diffFileList = "/data/hadoop/dev/mokylin_log/file_list/gjqt/diffFileList.txt";
        String bucketName = "mokylin-hive";

        IHdfsReader iHdfsReader = new HdfsReaderText();

        IDiffReader iDiffReader = new DiffReaderText();



        iHdfsReader.createLocalFileList(hdfsPath, regex, localFileList);
        logger.info("finish create local file list...");
        iDiffReader.createDiffFileList(localFileList,ossFileList,diffFileList);
        logger.info("finish create diff file list...");
        //每次从diffFileList 中读取固定行数的数据
        List<String> diffFile = iDiffReader.getDiffFileListPart(diffFileList, 20);

        String ossPath = null;
        for(String fileToUpload : diffFile){
            if(fileToUpload.matches("hdfs://mldc-hadoop/user/hive/warehouse/.*")){
                ossPath = fileToUpload.substring(39);
            }
            exec.execute(new UploadRunner(bucketName, fileToUpload, ossPath, ossFileList));
        }
        exec.shutdown();

    }

}

class UploadRunner implements Runnable {
    private static final Logger logger = LoggerFactory
            .getLogger(UploadRunner.class);

    private final String bucketName;
    private String hdfsPath;
    private String ossPath;
    private final String ossFileList;
    private static IOssReader iOssReader = new OssReaderText();
    private OSSClient ossClient = OSSClientManager.getInstanceone().getOSSClient();
    private static Configuration conf = new Configuration();



    UploadRunner(String bucketName, String hdfsPath, String ossPath, String ossFileList) {
        this.bucketName = bucketName;
        this.hdfsPath = hdfsPath;
        this.ossPath = ossPath;
        this.ossFileList = ossFileList;
    }

    public void run() {
        logger.info(Thread.currentThread().getName() + "线程被调用了。");
        logger.info("OSSClient : "+ossClient);
        logger.info("IOssReader : "+iOssReader);

        try {
            FileSystem fs = FileSystem.get(URI.create(hdfsPath),conf);
            FSDataInputStream hdfsInStream = fs.open(new Path(hdfsPath));
            ossClient.putObject(new PutObjectRequest(bucketName, ossPath, hdfsInStream));
        } catch (OSSException e) {
            e.printStackTrace();
            return;
        } catch (ClientException e) {
            e.printStackTrace();
            return;
        } catch (Exception e){
            return;
        }

        iOssReader.markFileSuccess(ossFileList, hdfsPath);
        logger.info("FINISH " + hdfsPath);
    }

}
