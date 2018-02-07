package com.smiledon.library.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by zhaidong on 2017/9/16.
 *
 * 原型设计模式
 */


public class PrototypeMode implements Cloneable,Serializable{


    /**
     * 浅复制：基本数据类型重新创建，引用类型不会依旧指向原对象指向的
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {

        PrototypeMode prototypeMode = (PrototypeMode) super.clone();
        return prototypeMode;
    }


    /**
     * 深复制：基本数据类型和引用类型都被重新创建。
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object deepClone() throws IOException, ClassNotFoundException {

        /* 写入当前对象的二进制流 */
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);

        /* 读出二进制流产生的新对象 */
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }

}
