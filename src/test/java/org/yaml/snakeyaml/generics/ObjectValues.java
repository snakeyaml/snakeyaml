package org.yaml.snakeyaml.generics;

import java.util.Map;

public class ObjectValues {

    private Object object;
    private Map<String, Map<Integer, Object>> values;
    private String[] possible;

    public Object getObject() {
        return object;
    }
    
    public void setObject(Object object) {
        this.object = object;
    }
    
    public void setValues(Map<String, Map<Integer, Object>> values) {
        this.values = values;
    }

    public Map<String, Map<Integer, Object>> getValues() {
        return values;
    }

    public void setPossible(String[] possible) {
        this.possible = possible;
    }
    
    public String[] getPossible() {
        return possible;
    }
    
}
