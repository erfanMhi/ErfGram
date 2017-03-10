package com.ErMedia.LogicModules.LogicComponents;

import java.io.Serializable;

/**
 * Created by root on 1/5/17.
 */
public class Request implements Serializable {
    private String src;
    private String dist;

    public Request(String src ,String dist){
        this.src = src;
        this.dist = dist;
    }
    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

}
