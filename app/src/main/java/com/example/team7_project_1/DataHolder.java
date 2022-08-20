package com.example.team7_project_1;

import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DataHolder {
    // Fields used in the CartActivity in order to keep track of what phones the user has put
    // in their shopping cart
    static ArrayList<Phone> shopping_cart_phones = new ArrayList<>();
    static ArrayList<Product> getShopping_cart_products = new ArrayList<>();

    // Field used in the ComparisonFilterActivity to keep track of the ID of the first phone we
    // decided to compare
    static int first_phone_id = -1;

}
