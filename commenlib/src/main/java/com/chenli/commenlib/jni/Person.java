package com.chenli.commenlib.jni;

import com.chenli.commenlib.util.mainutil.LogUtils;

/**
 * Created by Administrator on 2018/1/17.
 */

public class Person {
    private int age;
    private String name;

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void printPerson() {
        LogUtils.e("hangce", "age ======== " + age + "," + "name ======== " + name);
    }
}
