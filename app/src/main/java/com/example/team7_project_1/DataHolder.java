package com.example.team7_project_1;

import android.provider.ContactsContract;

import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DataHolder {
    // Fields used in the CartActivity in order to keep track of what phones the user has put
    // in their shopping cart
    static ArrayList<Phone> shopping_cart_phones = new ArrayList<>();
    static ArrayList<Product> shopping_cart_products = new ArrayList<>();

    // Field used in the ComparisonFilterActivity to keep track of the ID of the first product we
    // decided to compare
    static int first_product_id = -1;

    public static boolean addToShoppingCart(int phone_id) {
        Phone phone = DataProvider.getPhoneById(phone_id);
        Product equivalent_product = DataProvider.getProductByPhoneId(phone_id);
        if (!shopping_cart_phones.contains(phone) && !shopping_cart_products.contains(equivalent_product)) {
            shopping_cart_phones.add(phone);
            shopping_cart_products.add(equivalent_product);
            return false;
        } else {
            return true;
        }
    }

    public static void removeFromShoppingCart(int phone_id) {
        Phone phone = DataProvider.getPhoneById(phone_id);
        Product equivalent_product = DataProvider.getProductByPhoneId(phone_id);
        shopping_cart_phones.remove(phone);
        shopping_cart_products.remove(equivalent_product);
    }

}
