package com.smiledon.library.utils;


public class PatternUtil {

	/**
	 *
	 *  匹配特定符号加上转义符  '\'
	 *  /d 表示0以上的数字
	 *
	 *  例子 ：^[\u4e00-\u9fa5a-zA-Z0-9]*$
	 *
	 *  前后加上  ^ 和  $ (^匹配字符的开始，$匹配字符的结束)
	 *  '[]'            匹配区间
	 *  '\u4e00-\u9fa5' 匹配汉字
	 *  '0-9'           匹配数字
	 *  'a-zA-z'        匹配大小写字母
	 *  '*'、{2，9}      匹配限定符 （不限定位数，2-9位）
	 *
	 */

	public static final String PATTERN_HANZI = "^[\u4e00-\u9fa5]*$";

	public static final String PATTERN_ZIMU = "^[A-Za-z]*$";

	public static final String PATTERN_SHUZI = "^[0-9]*$";

}
