package com.vinod.java.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/*
Write a function called solution(data, n) that takes in a list of less than 100 integers and a
number n, and returns that same list but with all of the numbers that occur more than n times
 removed entirely. The returned list should retain the same ordering as the original list - you don't
  want to mix up those carefully-planned shift rotations! For instance, if data was [5, 10, 15, 10, 7]
  and n was 1, solution(data, n) would return the list [5, 15, 7] because 10 occurs twice, and thus
   was removed from the list entirely.
* */

public class FooChal {
    public static void main(String args[]) {
        // Integer[] arr = new Integer[]{1, 4, 7, 3, 2, 4, 4, 3, 7, 1, 2, 1, 2, 2, 3, 2, 4, 4};
        Integer[] arr = new Integer[]{3,1,2,4,3,1,2,3,5,6,6};
        int n = 2; 
        List<Integer> alUpdated = solution(Arrays.asList(arr), n);
        System.out.println("after removing elements(updated list)");
        for (Integer val : alUpdated)
            System.out.println(val);
    }

    private static List<Integer> solution(List<Integer> al, int k) {
        LinkedHashMap<Integer, Integer> hm = new LinkedHashMap<>();
        for (int i = 0; i < al.size(); i++) {
            Integer key = al.get(i);
            Integer value = hm.get(key);
            hm.put(key, value == null ? 1 : value + 1);
        } 
        List<Integer> keys = new ArrayList(hm.keySet());
        List<Integer> listUpdated = new ArrayList<>();
        for (Integer key : al) {
            if(hm.get(key) <= k)
            listUpdated.add(key);
        }
        return listUpdated;
    }
}
