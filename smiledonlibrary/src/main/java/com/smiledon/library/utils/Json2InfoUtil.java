package com.smiledon.library.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 根据java的反射机制
 */
public class Json2InfoUtil {


	/**
	 * 根据属性名获取属性值
	 * */
	public static Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据属性名设置属性值
	 * */
	public static void setFieldValueByName(String fieldName, Object fieldValue, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String setter = "set" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(setter,new Class[]{String.class});
			method.invoke(o, new Object[] {fieldValue});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * 获取属性名数组
	 * */
	private String[] getFiledName(Object o){
		Field[] fields=o.getClass().getDeclaredFields();
		String[] fieldNames=new String[fields.length];
		for(int i=0;i<fields.length;i++){
			System.out.println(fields[i].getType());
			fieldNames[i]=fields[i].getName();
		}
		return fieldNames;
	}

	/**
	 * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
	 * */
	private List getFiledsInfo(Object o){
		Field[] fields=o.getClass().getDeclaredFields();
		String[] fieldNames=new String[fields.length];
		List list = new ArrayList();
		Map infoMap=null;
		for(int i=0;i<fields.length;i++){
			infoMap = new HashMap();
			infoMap.put("type", fields[i].getType().toString());
			infoMap.put("name", fields[i].getName());
			infoMap.put("value", getFieldValueByName(fields[i].getName(), o));
			list.add(infoMap);
		}
		return list;
	}

	/**
	 * 获取对象的所有属性值，返回一个对象数组
	 * */
	public Object[] getFiledValues(Object o){
		String[] fieldNames=this.getFiledName(o);
		Object[] value=new Object[fieldNames.length];
		for(int i=0;i<fieldNames.length;i++){
			value[i]=this.getFieldValueByName(fieldNames[i], o);
		}
		return value;
	}

}
