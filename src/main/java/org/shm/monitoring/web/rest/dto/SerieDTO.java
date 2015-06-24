package org.shm.monitoring.web.rest.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanko on 20/6/15.
 */
public class SerieDTO {

    String name;
    List<Long> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getData() {
        return data;
    }

    public void setData(List<Long> data) {
        this.data = data;
    }


    public void addData(Long aData) {

        if (this.data ==null) {
            data = new ArrayList<>();
        }
        data.add(aData);
    }


    @Override
    public String toString() {
        return "SerieDTO{" +
            "name='" + name + '\'' +
            ", data=" + data +
            '}';
    }
}
