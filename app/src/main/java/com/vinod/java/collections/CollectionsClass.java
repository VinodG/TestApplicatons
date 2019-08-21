package com.vinod.java.collections;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class CollectionsClass {
    public static void main(String[] str)
    {
        //removing dublicate list
        ArrayList<Product> list =new ArrayList<Product>();
        list.add(new Product(1,"one"));
        list.add(new Product(1,"one"));
        list.add(new Product(2,"one"));
        list.add(new Product(3,"one"));
        Set<Product> set =new LinkedHashSet<Product>(list);
        list.clear();
        list.addAll(set);
        System.out.println("size "+list.size()); //print - > 3
    }
    public static class Product
    {
        int id = 0;
        String name ="";
        String sex ="";

        public Product(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            System.out.println(" equals is called");
            Product product = (Product)obj;
            return  this.id   == product.id &&
            this.name   .equals(product.name ) &&
            this.sex .equals(product.sex);
        }

        @Override
        public int hashCode() {
            System.out.println("hashcode is called ");
            return id+"".hashCode()+name.hashCode()+sex.hashCode();
        }
    }
}
