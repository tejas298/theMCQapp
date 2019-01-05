package com.tejas.root.finalapp.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by root on 27/12/18.
 */

@Entity(tableName = "paperinformation")
public class PaperInformation {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "order")
    private int order;

    @ColumnInfo(name = "price")
    private int price;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "testname")
    private String test_name;

    @ColumnInfo(name = "visited")
    private String visited;

    @ColumnInfo(name = "lock")
    private String lock;

    @ColumnInfo(name = "filename")
    private String filename;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getVisited() {
        return visited;
    }

    public void setVisited(String visited) {
        this.visited = visited;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
