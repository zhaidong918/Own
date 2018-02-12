package com.smiledon.own.app;

/**
 * ${DESC}
 *
 * @author zhaidong
 * @date 2018/2/12 12:47
 */

public class OwnConfig {


    public static class Plan{

        public static final int ALL = 0x00;

        public static class TabOne{

            public static final int COMPLETE = 0x01;

            public static final int NOT_COMPLETE = 0x02;

        }

        public static class TabTwo{

            public static final int NOW = 0x01;

            public static final int IMPORTANCE = 0x03;

            public static final int INSTANCY = 0x05;

            public static final int OTHER = 0x02;
        }


        public static class TabThree{

            public static final int TYPE_LIFT = 0x01;

            public static final int TYPE_WORK = 0x02;

        }
    }
}
