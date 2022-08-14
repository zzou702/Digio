package com.example.team7_project_1;

import com.example.team7_project_1.models.Phone;

import java.util.ArrayList;
import java.util.Random;

public class DataProvider {
    static String[] names = {"Iphone", "Iphone", "Iphone", "Iphone", "Iphone", "Iphone", "Iphone"};

    public static ArrayList<Phone> generateData() {
        ArrayList<Phone> phones = new ArrayList<>();

        Random random = new Random();
        for (String name : names) {
            int rnd = random.nextInt(1200);
            Phone phone = new Phone(name, rnd);
            phones.add(phone);
        }
        return phones;
    }
}
