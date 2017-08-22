package com.ErMedia.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by root on 1/17/17.
 */
public class Test {
    public static void main(String[] args) {
        boolean visited[] = new boolean[100];
        Node a = new Node(1);
        Node b = new Node(2);
        Node c = new Node(3);
        Node d = new Node(4);
        Node e = new Node(5);
        Node f = new Node(6);
        Node g = new Node(7);
        Node h = new Node(8);
        a.getNeighbors().add(b);
        a.getNeighbors().add(c);
        a.getNeighbors().add(d);
        b.getNeighbors().add(c);
        ArrayList<String> paths = new ArrayList<>();
        paths = showAllPath(a,e,visited,paths,a.getId() + "");
        if(paths.isEmpty()){
            System.out.println("empty");
        }
        for (int i = 0; i < paths.size(); i++) {
            System.out.println(paths.get(i));

        }
    }
    public static ArrayList<String> showAllPath(Node src,Node dist,boolean visited[],ArrayList<String> paths,String s){
        visited[src.getId()] = true;
        if(src.getId() == dist.getId()) {
            paths.add(s);
            return paths;
        }

        for (int i = 0; i < src.getNeighbors().size(); i++) {
            if (!visited[src.getNeighbors().get(i).getId()])
                showAllPath(src.getNeighbors().get(i),dist, Arrays.copyOf(visited,100),paths,s + " -> " + src.getNeighbors().get(i).getId());

        }

        return paths;
    }
}
