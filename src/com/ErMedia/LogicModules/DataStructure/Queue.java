package com.ErMedia.LogicModules.DataStructure;


import java.util.Arrays;

/**
 * Created by root on 1/9/17.
 */
public class Queue {
    private int front ;
    private Object arr[] ;
    private int back;
    private int size;
    public Queue(){
        size = 0 ;
        front = 0 ;
        arr = new Object[1000] ;
        back = 0 ;
    }
    public void enqueue(Object obj){
        if(this.size() == arr.length){
            return;
        }
        size++;
        arr[back] = obj;
        back = (back+1)% arr.length;
    }

    public Object dequeue(){
        if(this.isEmpty()){
            return null;
        }
        size--;
        Object tmp = arr[front];
        front = (front +1)%arr.length;
        return tmp;
    }

    public Object front(){
        return arr[front];
    }
    public void increaseListSize() {

        arr = Arrays.copyOf(arr, arr.length * 2);
    }

    public boolean isEmpty(){
        return size == 0;
    }
    public int size(){
        return size;
    }


}
