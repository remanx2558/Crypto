package com.paytm.pgplus.crypto.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GeneralUrils<K,V> {

    public static <V> V[] appendToArray(V[] array, V item){
        V[] arr=(V[])new Object[array.length+1];
        return arr;
    }
    public static <K,V>ArrayList<V> mapToListValue(HashMap<K, V> hm){
        ArrayList<V> al=new ArrayList<V>();
        for (Map.Entry<K,V> entry : hm.entrySet()){
            al.add(entry.getValue());
        }
        return al;
    }
    //public E[] appendToArray(E[] array, E item){}
}
