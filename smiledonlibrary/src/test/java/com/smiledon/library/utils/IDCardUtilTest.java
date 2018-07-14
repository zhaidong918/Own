package com.smiledon.library.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by 95867 on 2018/4/2.
 */
@RunWith(Parameterized.class)
public class IDCardUtilTest {
    @Parameterized.Parameter
    public String idCard;

    public IDCardUtilTest() {

    }



    @Parameterized.Parameters
    public static Collection idCard() {
        List data = new ArrayList();
//        data.add("511623199208095731");
        data.add("51162319920809573X");
        data.add("511623199208095731");
        data.add("511623199208095731");
        data.add("511623199208095731");
        return data;
    }

    @Test
    public void isIDCard() throws Exception {

        assertTrue(IDCardUtil.isIDCard(idCard));

    }

}