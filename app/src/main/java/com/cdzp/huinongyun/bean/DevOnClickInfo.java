package com.cdzp.huinongyun.bean;

/**
 * Created by cdzp on 2018/3/29.
 */

public class DevOnClickInfo {
    private int label;
    private int position;

    public DevOnClickInfo(int label, int position) {
        this.label = label;
        this.position = position;
    }

    public int getLabel() {
        return label;
    }

    public int getPosition() {
        return position;
    }
}
