package org.yaml.snakeyaml.generics;

import java.util.Map;

public class ObjectValuesWithParam<T, S> {

    private Object object;
    private Map<T, Map<S, Object>> values;
    private T[] possible;

    public Object getObject() {
        return object;
    }
    
    public void setObject(Object object) {
        this.object = object;
    }
    
    public void setValues(Map<T, Map<S, Object>> values) {
        this.values = values;
    }

    public Map<T, Map<S, Object>> getValues() {
        return values;
    }

    public void setPossible(T[] possible) {
        this.possible = possible;
    }
    
    public T[] getPossible() {
        return possible;
    }
    
}
