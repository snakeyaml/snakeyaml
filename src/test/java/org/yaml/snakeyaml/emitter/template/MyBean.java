package org.yaml.snakeyaml.emitter.template;

import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.immutable.Point;

public class MyBean {
    private Point point;
    private List<String> list;
    private List<Integer> empty = new ArrayList<Integer>();
    private String id;

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<Integer> getEmpty() {
        return empty;
    }

    public void setEmpty(List<Integer> empty) {
        this.empty = empty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MyBean) {
            MyBean bean = (MyBean) obj;
            if (!id.equals(bean.id)) {
                return false;
            }
            if (!point.equals(bean.point)) {
                return false;
            }
            if (!list.equals(bean.list)) {
                return false;
            }
            if (!empty.equals(bean.empty)) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }

}
