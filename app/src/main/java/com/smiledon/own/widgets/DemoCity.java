package com.smiledon.own.widgets;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author East Chak
 * @date 2018/1/20 12:37
 */

public class DemoCity {

    public String tag;
    public String city;

    public DemoCity(String tag, String city) {
        this.tag = tag;
        this.city = city;

    }

    private static List<DemoCity> demoCityList;


    public static List<DemoCity> getDatas() {
        if (demoCityList == null) {
            demoCityList = new ArrayList<>();

            Arrays.sort(names,new ComparatorPinYin());

            for (int i = 0; i < names.length; i++) {
                demoCityList.add(new DemoCity(getFirstLetter(names[i]), names[i]));
            }
        }

        return demoCityList;
    }

    /**
     * 模拟姓名
     */
    public static String[] names = new String[] { "宋江", "卢俊义", "吴用",
            "公孙胜", "关胜", "林冲", "秦明", "呼延灼", "花荣", "柴进", "李应", "朱仝", "鲁智深",
            "武松", "董平", "张清", "杨志", "徐宁", "索超", "戴宗", "刘唐", "李逵", "史进", "穆弘",
            "雷横", "李俊", "阮小二", "张横", "阮小五", " 张顺", "阮小七", "杨雄", "石秀", "解珍",
            " 解宝", "燕青", "朱武", "黄信", "孙立", "宣赞", "郝思文", "韩滔", "彭玘", "单廷珪",
            "魏定国", "萧让", "裴宣", "欧鹏", "邓飞", " 燕顺", "杨林", "凌振", "蒋敬", "吕方",
            "郭盛", "安道全", "皇甫端", "王英", "扈三娘", "鲍旭", "樊瑞", "孔明", "孔亮", "项充",
            "李衮", "金大坚", "马麟", "童威", "童猛", "孟康", "侯健", "陈达", "杨春", "郑天寿",
            "陶宗旺", "宋清", "乐和", "龚旺", "丁得孙", "穆春", "曹正", "宋万", "杜迁", "薛永", "施恩",
            "周通", "李忠", "杜兴", "汤隆", "邹渊", "邹润", "朱富", "朱贵", "蔡福", "蔡庆", "李立",
            "李云", "焦挺", "石勇", "孙新", "顾大嫂", "张青", "孙二娘", " 王定六", "郁保四", "白胜",
            "时迁", "段景柱" };


    /**
     94      * 取第一个汉字的第一个字符
     95     * @Title: getFirstLetter
     96     * @Description: TODO
     97     * @return String
     98     * @throws
     99      */
    public static String getFirstLetter(String ChineseLanguage){
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);// 输出拼音全部大写
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
        try {
            String str = String.valueOf(cl_chars[0]);
            if (str.matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
                hanyupinyin = PinyinHelper.toHanyuPinyinStringArray(
                        cl_chars[0], defaultFormat)[0].substring(0, 1);;
            } else if (str.matches("[0-9]+")) {// 如果字符是数字,取数字
                hanyupinyin += cl_chars[0];
            } else if (str.matches("[a-zA-Z]+")) {// 如果字符是字母,取字母

                hanyupinyin += cl_chars[0];
            } else {// 否则不转换

            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println("字符不能转成汉语拼音");
        }
        return hanyupinyin;
    }

    /**
     * 功能：实现汉语拼音序比较
     *
     */
    static  class ComparatorPinYin implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return ToPinYinString(o1).compareTo(ToPinYinString(o2));
        }

        private String ToPinYinString(String str){

            StringBuilder sb=new StringBuilder();
            String[] arr=null;

            for(int i=0;i<str.length();i++){
                arr=PinyinHelper.toHanyuPinyinStringArray(str.charAt(i));
                if(arr!=null && arr.length>0){
                    for (String string : arr) {
                        sb.append(string);
                    }
                }
            }
            return sb.toString();
        }

    }


}
