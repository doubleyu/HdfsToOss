package com.dby.strings;

import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * Created by Admin on 2016/5/26.
 */
public class TestSubstring {
    @Test
    public void testSub(){
        String oriStr = "hdfs://mldc-hadoop/user/hive/warehouse/lqzg.db/recharge/plat=360/date=2016-05-04/lqzg_360_Recharge_2016-05-04.txt";
        System.out.println(oriStr.substring(39));
    }
}
