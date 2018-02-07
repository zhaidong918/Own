package com.smiledon.library.demo;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Created by zhaidong on 2017/9/16.
 *
 * 单例模式  Demo
 *
 */

public class SingleInstance implements Serializable{

    /**
     *  私有构造方法，防止被实例化
     */
    private SingleInstance() {

    }

    /**  单例--懒汉模式    **/

    volatile private static SingleInstance instance;

    public static SingleInstance getInstance() {

        if (instance == null) {
            //TODO... （多线程下 这里做耗时操作 导致非线程安全）
            synchronized (instance) {
                if(instance == null) // double check 必须用到关键字volatile
                    instance = new SingleInstance();
            }
        }

        return instance;
    }

    /**  单例--饿汉模式    **/

    private static SingleInstance singleInstance = new SingleInstance();

    public static SingleInstance getSingleInstance() {
        return singleInstance;
    }

    /**   单例--静态内部类 **/

    //JVM内部的机制能够保证当一个类被加载的时候，这个类的加载过程是线程互斥的
    private static class SingleInstanceFactory{
        private static SingleInstance singleInstance = new SingleInstance();
    }

    public static SingleInstance getFactorySingleInstance() {
        return SingleInstanceFactory.singleInstance;
    }


    /**
     * 该方法在反序列化时会被调用，该方法不是接口定义的方法，有点儿约定俗成的感觉
     */
    protected Object readResolve() throws ObjectStreamException {
        return SingleInstance.instance;
    }

}
