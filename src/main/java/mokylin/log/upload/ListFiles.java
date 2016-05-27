package mokylin.log.upload;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.s3.S3Credentials;

import java.net.URI;

/**
 * Created by Admin on 2016/5/25.
 */
@Deprecated
public class ListFiles {
    private static final String game = "";
    public static void main(String[] args) throws Exception {
//        if(args.length != 1){
//            System.out.println("Usage : FilesList <target>");
//            System.exit(1);
//        }
        String path = "/user/hive/warehouse/lqzg.db/recharge";


        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(URI.create(path), conf);
//        FileStatus[] fs = hdfs.listStatus(new Path(path));

        RemoteIterator<LocatedFileStatus> fileItr = hdfs.listFiles(new Path(path), true);
        String regex = ".*/plat=360/.*";
        while(fileItr.hasNext()){
            Path p = fileItr.next().getPath();
            String absPath = p.toString();
            if(absPath.matches(regex)){
                System.out.println(p);
            }
        }


//        FileStatus[] fs = hdfs.listStatus(new Path(path), new PathFilter() {
//            @Override
//            public boolean accept(Path path) {
//
//                System.out.println("FILE PATH : "+path.toString());
//
//                String regex = "date=2016-05-0.*";
//                return (path.getName().matches(regex));
//            }
//        });
//        Path[] listPath = FileUtil.stat2Paths(fs);
//        for (Path p : listPath)
//            System.out.println("path ======> " + p);
    }
}
