package com.ErMedia.LogicModules.LogicComponents;

import java.io.Serializable;

/**
 * Created by root on 1/5/17.
 */
public class Message implements Serializable {
    private long src;
    private long dist;
    private String message;

    public Message(long src,long dist,String message){
        this.src = src;
        this.dist = dist;
        this.message = message;
    }


    public long getSrc() {
        return src;
    }

    public void setSrc(long src) {
        this.src = src;
    }

    public long getDist() {
        return dist;
    }

    public void setDist(long dist) {
        this.dist = dist;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
