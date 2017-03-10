package com.ErMedia.LogicModules.DataStructure;

import com.ErMedia.LogicModules.LogicComponents.Number;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by root on 1/5/17.
 */
public class ArrayListI {

    private Object[] list;
    private int size = 0;

    public ArrayListI() {
        list = new Object[10];
    }

    public Object get(int index) {
        if (index < size) {
            return list[index];
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static String[] toStringArray(ArrayList<String> arrayList){
        String strings[] = new String[arrayList.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = arrayList.get(i);
        }
        return strings;
    }

    public void add(Object obj) {
        if (list.length - size <= 5) {
            increaseListSize();
        }
        list[size++] = obj;
    }

    public Object remove(int index) {
        if (index < size) {
            Object obj = list[index];
            list[index] = null;
            int tmp = index;
            while (tmp < size) {
                list[tmp] = list[tmp + 1];
                list[tmp + 1] = null;
                tmp++;
            }
            size--;
            return obj;
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }

    }

    //TODO
    public boolean removeElement(Object object){
        int index = -1 ;
        for (int i = 0; i < list.length; i++) {
            if(object.equals(list[i])){
                index = i;
                break;
            }
        }
        if (index != -1) {
            list[index] = null;
            int tmp = index;
            while (tmp < size) {
                list[tmp] = list[tmp + 1];
                list[tmp + 1] = null;
                tmp++;
            }
            size--;
            return true;
        } else {
            return false;
        }

    }

    public static Long[] toIntegerArray(Object[] numbers){
        int counter = 0;
        for (int i = 0; i < numbers.length; i++) {
            if(numbers[i] == null)
                break;
            counter++;
        }
        Long[] nums = new Long[counter];
        for (int i = 0; i < counter; i++) {
            nums[i] = ((Number)numbers[i]).getNumber();
        }
        return nums;

    }

    //TODO
    public boolean contain(Object object){
        for (int i = 0; i < list.length; i++) {
            if(object.equals(list[i])){
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty(){
        return  size == 0;
    }
    public Object[] tonumbersLview(){
        return list;
    }

    public int size() {
        return size;
    }

    private void increaseListSize() {
        list = Arrays.copyOf(list, list.length * 2);
    }
}