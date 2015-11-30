package com.csci4100.reviewr.helper;

/**
 * Created by mili on 2015-11-30.
 */
public class InputItem {
    private long ID;
    private String query;

    public InputItem(String query){
        this.ID = -1;
        this.query = query;

    }

    public void setID(long id) {
        this.ID = id;
    }
    public long ID() {
        return this.ID;
    }
    public void setQuery(String query){
        this.query = query;
    }
    public String query() {
        return this.query;
    }
}
