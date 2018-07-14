package lottie.smiledon.com.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class myClass {

    public static final String SPILT = ".";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


        while (true){

            String lin = scanner.nextLine();
            int length = Integer.parseInt(lin);
            String originContent = getOriginContent(length);

            long startTime = System.currentTimeMillis();
            StringTokenizer stringTokenizer = new StringTokenizer(originContent, SPILT);
            List<String> stringList = new ArrayList<>();
            while (stringTokenizer.hasMoreElements()){
                stringList.add(stringTokenizer.nextToken());
            }
            System.out.print("StringTokenizer 花费得时间：" + (System.currentTimeMillis() - startTime));

            startTime = System.currentTimeMillis();
            List<String> stringArrays = Arrays.asList(originContent.split(SPILT));
            System.out.print("String.split 花费得时间：" + (System.currentTimeMillis() - startTime));

        }

    }




    public static String getOriginContent(int length) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {

            for (int j = 0; j < 6; j++) {
                builder.append("a");
            }
            builder.append(SPILT);
        }

        return builder.toString();

    }



}
