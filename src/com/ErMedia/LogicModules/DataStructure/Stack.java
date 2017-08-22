package com.ErMedia.LogicModules.DataStructure;

/**
 * Created by root on 1/5/17.
 */
public class Stack {
    private int size;
    private Object[] arr;
    private int pointer;

    public Stack(int s) {
        size = s;
        arr = new Object[size];
        pointer = -1;
    }
    public void changeSize(){
        Object[] arr = new Object[size*2];
        for(int i = 0 ; i < size ; i++){
            arr[i] = this.arr[i];
        }
        this.arr = arr;
        size *= 2;
    }
    public void push(long j) {
        if (isFull()) {
            changeSize();
        }
        arr[pointer++] = j;
    }
    public Object pop() {
        if (this.isEmpty()) {
            return Integer.MIN_VALUE;
        }
        return arr[pointer--];
    }

    public Object top() {
        return arr[pointer];
    }
    public boolean isEmpty() {
        return (pointer == -1);
    }
    public boolean isFull() {
        return (pointer ==size - 1);
    }
}
