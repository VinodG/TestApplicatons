package com.vinod.lib.collections;




import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CollectionsClass {
    public static void main(String[] str)
    {
        //removing dublicate list
        ArrayList<Product> list =new ArrayList<Product>();
        list.add(new Product(1,"one"));
        list.add(new Product(1,"one"));
        list.add(new Product(2,"one"));
        list.add(new Product(3,"one"));
        testSet(list);
//        testTree(list);
//        TreeSet<Product>    ts = new TreeSet<Product>(list);
    }

    private static void testSet(ArrayList<Product> list) {
        Set<Product> set =new LinkedHashSet<Product>(list);
        list.clear();
        list.addAll(set);
        System.out.println("size "+list.size()); //print - > 3
    }

    private static void testTree(ArrayList<Product> list) {
        List<String> li = new ArrayList<String>();
        li.add("one");
        li.add("two");
        li.add("three");
        li.add("four");
        System.out.println("List: "+li);
        //create a treeset with the list
        TreeSet<Product> myset = new TreeSet<Product>(/*new Comparator<Product>() {
            @Override
            public int compare(Product product, Product t1) {
                return product.id - t1.id;
            }
        }*/);
        myset.addAll(list);
        for (Product p:myset)
            System.out.println("Set: "+p.id);
    }

    public static class Product implements Comparable<Product>
    {
        int id = 0;
        String name ="";
        String sex ="";

        public Product(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public boolean equals( Object obj) {
            System.out.println(" equals is called");
            Product product = (Product)obj;
            return  this.id   == product.id &&
                    this.name.equals(product.name) &&
                    this.sex.equals(product.sex);
        }

        @Override
        public int hashCode() {
            System.out.println("hashcode is called ");
            return id+"".hashCode()+name.hashCode()+sex.hashCode();
        }

        @Override
        public int compareTo(Product product) {
            return   (this.id - product.id) ;
        }
    }

    private static void removingDuplicateElements() {
        Integer [] arr =  new Integer[] {1,4,7,4,7,1,2,1,2,2,3,2,4,4 };
        List list = Arrays.asList(arr);
        printList(list);
        LinkedHashSet set = new LinkedHashSet(list);
        System.out.println("after removing duplicate elements");
        printList(new ArrayList<>(set));
    }
    private static void printList(List list) {
        for (int i=0;i<list.size();i++ ){
            System.out.println(list.get(i)+"");
        }
    }
}
