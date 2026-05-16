package com.example.myapplication;

import java.io.Serializable;

public class UserSerializable implements Serializable {
    private final String name;
    private final int age;

    public UserSerializable(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
