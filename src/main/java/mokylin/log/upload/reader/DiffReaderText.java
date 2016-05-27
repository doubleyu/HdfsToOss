package mokylin.log.upload.reader;

import com.google.common.base.Stopwatch;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Admin on 2016/5/26.
 */
public class DiffReaderText implements IDiffReader {
    @Override
    public void createDiffFileList(String localFileList, String ossFileList, String diffFileList) {
        BufferedReader br1 = null;
        BufferedReader br2 = null;
        BufferedWriter bw = null;
        String sCurrentLine;
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        try {
            br1 = new BufferedReader(new FileReader(localFileList));
            br2 = new BufferedReader(new FileReader(ossFileList));
            bw = new BufferedWriter(new FileWriter(diffFileList));

            while ((sCurrentLine = br1.readLine()) != null) {
                list1.add(sCurrentLine);
            }
            while ((sCurrentLine = br2.readLine()) != null) {
                list2.add(sCurrentLine);
            }

            String[] array = list2.toArray(new String[list2.size()]);
            Arrays.sort(array);

            int count = 0;
            for (String str : list1) {
                if (Arrays.binarySearch(array, str) < 0) {
//                    System.out.println(str);
                    bw.write(str);
                    bw.newLine();
                    count++;
                }
            }
            bw.flush();
            System.out.println("Total Count : " + count);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br1 != null)
                    br1.close();
                if (br2 != null)
                    br2.close();
                if (bw != null) {
                    bw.flush();
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public List<String> getDiffFileListPart(String diffFileList, int lineNum) {
        List<String> fileToUploadlst = new ArrayList<String>();
        File file = new File(diffFileList);
        BufferedInputStream bis = null;
        BufferedReader in = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            //每次读取10M到缓冲区
            in = new BufferedReader(new InputStreamReader(bis, "UTF-8"),
                    10 * 1024 * 1024);
            int count = 0;
            while (in.ready()) {
                //读取固定行数，由lineNum指定
                if (count > lineNum) {
                    break;
                }
                String line = in.readLine();
                fileToUploadlst.add(line);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileToUploadlst;
    }
}
