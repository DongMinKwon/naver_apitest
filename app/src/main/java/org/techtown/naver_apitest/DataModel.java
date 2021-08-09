package org.techtown.naver_apitest;

import java.util.ArrayList;
import java.util.List;

public class DataModel {
    String total;
    String display;
    ArrayList<Items> items;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
