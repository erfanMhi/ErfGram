package com.ErMedia.Test;

import java.util.ArrayList;

/**
 * Created by root on 1/17/17.
 */
public class Node {
    private ArrayList<Node> neighbors ;
    private int id ;
    public Node(int id){ this.id = id;
    neighbors = new ArrayList<>();}


    public ArrayList<Node> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ArrayList<Node> neighbors) {
        this.neighbors = neighbors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
